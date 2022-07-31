package hu.webuni.logistics.comtur.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.logistics.comtur.dto.DelayDto;
import hu.webuni.logistics.comtur.dto.TransportPlanDto;
import hu.webuni.logistics.comtur.dto.Views;
import hu.webuni.logistics.comtur.mapper.TransportPlanMapper;
import hu.webuni.logistics.comtur.model.Milestone;
import hu.webuni.logistics.comtur.model.TransportPlan;
import hu.webuni.logistics.comtur.service.MilestoneService;
import hu.webuni.logistics.comtur.service.TransportPlanService;
import hu.webuni.logistics.comtur.web.exception.IdViolationException;

/**
 * TransportPlan REST controller. Methods use ResponseEntity.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/transportPlans")
public class TransportPlanController {

	@Autowired
	TransportPlanService transportPlanService;

	@Autowired
	TransportPlanMapper transportPlanMapper;

	@Autowired
	MilestoneService milestoneService;

	/**
	 * Creates a transport plan entity. DTO is mandatory and validated, and must not
	 * contain an ID.
	 * 
	 * @param transportPlanDto Transport plan DTO in the request body.
	 * @return Created entity as DTO.
	 */
	@PostMapping
	public ResponseEntity<TransportPlanDto> create(@RequestBody @Valid TransportPlanDto transportPlanDto) {
		if (transportPlanDto.getId() != null) {
			throw new IdViolationException("Transport plan DTO has an ID at creation request.");
			// Exception handler sends BAD_REQUEST response.
		}
		try {
			TransportPlan transportPlan = transportPlanService
					.create(transportPlanMapper.dtoToTransportPlan(transportPlanDto));
			return ResponseEntity.ok(transportPlanMapper.transportPlanToDto(transportPlan));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Gets all transport plans.
	 * 
	 * @return List of transport plans.
	 */
	@GetMapping
	@JsonView(Views.VisibleData.class)
	public Collection<TransportPlanDto> getAll() {
		return transportPlanMapper.transportPlansToDtos(transportPlanService.getAll());
	}

	/**
	 * Gets transport plan by its ID.
	 * 
	 * @param id ID of the transport plan.
	 * @return Transport plan DTO, or NOT FOUND response, if not exists.
	 */
	@GetMapping("/{id}")
	@JsonView(Views.VisibleData.class)
	public ResponseEntity<TransportPlanDto> getById(@PathVariable long id) {
		Optional<TransportPlan> transportPlanOptional = transportPlanService.findById(id);
		if (transportPlanOptional.isPresent()) {
			return ResponseEntity.ok(transportPlanMapper.transportPlanToDto(transportPlanOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Sets delay of a transport plan.
	 * 
	 * @param id       ID of the transport plan.
	 * @param delayDto Delay object.
	 * @return Transport plan DTO, or NOT FOUND response, if transport plan or delay
	 *         specified milestone do not exist.
	 */
	@PostMapping("/{id}/delay")
	public ResponseEntity<TransportPlanDto> delay(@PathVariable long id, @RequestBody @Valid DelayDto delayDto) {
		Optional<TransportPlan> optionalTransportPlan = transportPlanService.getById(id);
		if (optionalTransportPlan.isEmpty()) {
			return ResponseEntity.notFound().build(); // Transport plan not found.
		}
		TransportPlan transportPlan = optionalTransportPlan.get();

		Optional<Milestone> optionalMilestone = milestoneService.findById(delayDto.getMilestoneId());
		if (optionalMilestone.isEmpty()) {
			return ResponseEntity.notFound().build(); // Milestone not found.
		}

		try {
			return ResponseEntity.ok(transportPlanMapper
					.transportPlanToDto(transportPlanService.increaseDelay(transportPlan, delayDto)));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
