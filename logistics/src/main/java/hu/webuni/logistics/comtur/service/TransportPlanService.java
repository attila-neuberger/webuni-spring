package hu.webuni.logistics.comtur.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.logistics.comtur.config.LogisticsConfigProperties;
import hu.webuni.logistics.comtur.dto.DelayDto;
import hu.webuni.logistics.comtur.model.Milestone;
import hu.webuni.logistics.comtur.model.Section;
import hu.webuni.logistics.comtur.model.TransportPlan;
import hu.webuni.logistics.comtur.repository.TransportPlanRepository;

/**
 * Service class for {@link TransportPlan} entities. Methods are inherited from
 * {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class TransportPlanService extends BaseService<TransportPlan> {

	@Autowired
	private SectionService sectionService;

	@Autowired
	private MilestoneService milestoneService;

	@Autowired
	private LogisticsConfigProperties configProperties;

	/**
	 * Creates a transport plan service.
	 * 
	 * @param transportPlanRepository Transport plan repository.
	 */
	public TransportPlanService(TransportPlanRepository transportPlanRepository) {
		super();
		this.repository = transportPlanRepository;
	}

	/**
	 * Gets list of all transport plans.
	 * 
	 * @return List of transport plans.
	 */
	public List<TransportPlan> getAll() {
		return ((TransportPlanRepository) repository).getTransportPlansDetailed();
	}

	/**
	 * Gets a transport plan (optional) based on its ID.
	 * 
	 * @param id ID of the transport plan.
	 * @return Found transport plan as {@link Optional}.
	 */
	public Optional<TransportPlan> getById(long id) {
		return ((TransportPlanRepository) repository).getTransportPlanDetailedById(id);
	}

	/**
	 * Creates a transport plan.
	 * 
	 * @param transportPlan Entity to save.
	 * @return Saved entity.
	 * @throws IllegalArgumentException If entity already exists.
	 */
	@Transactional
	public TransportPlan create(TransportPlan transportPlan) throws IllegalArgumentException {
		if (exists(transportPlan.getId())) {
			throw new IllegalArgumentException(
					String.format("Entity with ID %d already exists.", transportPlan.getId()));
		}
		TransportPlan result = save(transportPlan);
		for (Section section : transportPlan.getSections()) {
			section.setTransportPlan(result);
			sectionService.create(section);
		}
		return result;
	}

	/**
	 * Increases delay of a transport plan.
	 * 
	 * @param transportPlan Entity to modify.
	 * @param delayDto      Delay object.
	 * @return Saved entity.
	 * @throws IllegalArgumentException If milestone specified in the delay DTO does
	 *                                  not belong to the transport plan.
	 */
	@Transactional
	public TransportPlan increaseDelay(TransportPlan transportPlan, DelayDto delayDto) throws IllegalArgumentException {
		List<Section> sections = transportPlan.getSections();
		Collections.sort(sections);
		Milestone milestone = null;
		int sectionIndex = 0;
		boolean isFromMilestone = false;
		for (int i = 0; milestone == null && i < sections.size(); ++i) {
			if (sections.get(i).getFromMilestone().getId() == delayDto.getMilestoneId()) {
				milestone = sections.get(i).getFromMilestone();
				sectionIndex = i;
				isFromMilestone = true;
			}
			if (milestone == null && sections.get(i).getToMilestone().getId() == delayDto.getMilestoneId()) {
				milestone = sections.get(i).getToMilestone();
				sectionIndex = i;
			}
		}
		if (milestone == null) {
			throw new IllegalArgumentException("Milestone does not belong to the transport plan.");
		}

		milestone.setPlannedTime(milestone.getPlannedTime().plusMinutes(delayDto.getDelay()));
		milestoneService.save(milestone);
		if (isFromMilestone) {
			sections.get(sectionIndex).getToMilestone().setPlannedTime(
					sections.get(sectionIndex).getToMilestone().getPlannedTime().plusMinutes(delayDto.getDelay()));
			milestoneService.save(sections.get(sectionIndex).getToMilestone());
		} else if (sectionIndex + 1 < sections.size()) {
			sections.get(sectionIndex + 1).getFromMilestone().setPlannedTime(sections.get(sectionIndex + 1)
					.getFromMilestone().getPlannedTime().plusMinutes(delayDto.getDelay()));
			milestoneService.save(sections.get(sectionIndex + 1).getFromMilestone());
		}

		int[] delayMinutes = configProperties.getTransportplan().getDelayminutes();
		boolean penaltyExecuted = false;
		for (int i = delayMinutes.length - 1; !penaltyExecuted && i >= 0; --i) {
			if (delayDto.getDelay() >= delayMinutes[i]) {
				transportPlan.setExpectedIncome(transportPlan.getExpectedIncome()
						* (1 - 0.01 * configProperties.getTransportplan().getDelaypenalty()[i]));
				penaltyExecuted = true;
			}
		}
		if (penaltyExecuted) {
			((TransportPlanRepository) repository).save(transportPlan);
		}
		return transportPlan;
	}
	
	/**
	 * Deletes all transport plans.
	 */
	@Transactional
	public void deleteAll() {
		for (TransportPlan transportPlan : getAll()) {
			for (Section section : transportPlan.getSections()) {
				section.setTransportPlan(null);
				sectionService.delete(section.getId());
			}
			delete(transportPlan.getId());
		}
	}
}
