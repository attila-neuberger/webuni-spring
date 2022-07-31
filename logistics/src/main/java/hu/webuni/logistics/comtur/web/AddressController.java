package hu.webuni.logistics.comtur.web;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.logistics.comtur.dto.AddressDto;
import hu.webuni.logistics.comtur.dto.Views;
import hu.webuni.logistics.comtur.mapper.AddressMapper;
import hu.webuni.logistics.comtur.model.Address;
import hu.webuni.logistics.comtur.service.AddressService;
import hu.webuni.logistics.comtur.web.exception.IdViolationException;

/**
 * Address REST controller. Methods use ResponseEntity.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

	@Autowired
	AddressService addressService;

	@Autowired
	AddressMapper addressMapper;

	/**
	 * Creates an address entity. DTO is mandatory and validated, and must not
	 * contain an ID.
	 * 
	 * @param addressDto Address DTO in the request body.
	 * @return Created entity as DTO.
	 */
	@PostMapping
	public ResponseEntity<AddressDto> create(@RequestBody @Valid AddressDto addressDto) {
		if (addressDto.getId() != null) {
			// return ResponseEntity.badRequest().build();
			throw new IdViolationException("Address DTO has an ID at creation request.");
			// Exception handler sends BAD_REQUEST response.
		}
		try {
			Address address = addressService.create(addressMapper.dtoToAddress(addressDto));
			return ResponseEntity.ok(addressMapper.addressToDto(address));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Gets all addresses.
	 * 
	 * @return List of addresses.
	 */
	@GetMapping
	@JsonView(Views.VisibleData.class)
	public Collection<AddressDto> getAll() {
		return addressMapper.addressesToDtos(addressService.findAll());
	}

	/**
	 * Gets address by its ID.
	 * 
	 * @param id ID of the address.
	 * @return Address DTO, or NOT FOUND response, if not exists.
	 */
	@GetMapping("/{id}")
	@JsonView(Views.VisibleData.class)
	public ResponseEntity<AddressDto> getById(@PathVariable long id) {
		Optional<Address> addressOptional = addressService.findById(id);
		if (addressOptional.isPresent()) {
			return ResponseEntity.ok(addressMapper.addressToDto(addressOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Deletes address by its ID.
	 * 
	 * @param id ID of the address.
	 * @return OK response.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<AddressDto> remove(@PathVariable long id) {
		try {
			addressService.remove(id);
			return ResponseEntity.ok().build();
		} catch (NoSuchElementException e) {
			return ResponseEntity.ok().build();
		} catch (IllegalStateException e) {
			// Address cannot be deleted because a milestone refers it.
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Modifies an address. DTO is mandatory and validated, and if it contains an
	 * ID, that ID and the path variable ID must match.
	 * 
	 * @param id         ID of the address.
	 * @param addressDto Address DTO in the request body.
	 * @return Modified entity as DTO.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<AddressDto> modify(@PathVariable long id, @RequestBody @Valid AddressDto addressDto) {
		if (addressDto.getId() != null && addressDto.getId().longValue() != id) {
			// return ResponseEntity.badRequest().build();
			throw new IdViolationException("Path variable ID and address DTO ID do not match.");
			// Exception handler sends BAD_REQUEST response.
		}
		try {
			Address address = addressService.modify(id, addressMapper.dtoToAddress(addressDto));
			return ResponseEntity.ok(addressMapper.addressToDto(address));
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Searches for an address entity.
	 * 
	 * @param addressDto Address DTO in the request body.
	 * @param pageable   Pageable object.
	 * @return List of found addresses.
	 */
	@PostMapping("/search")
	public Collection<AddressDto> search(@RequestBody AddressDto addressDto,
			@PageableDefault(sort = { "id" }) Pageable pageable,
			@RequestParam(required = false, value = "size") Integer size, ServletResponse response) {

		List<Address> addresses = null;
		if (size == null) {
			// Size is missing - returning page 0.
			// pageable = PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort());
			addresses = addressService.getAllByExample(addressMapper.dtoToAddress(addressDto), pageable.getSort());
			((HttpServletResponse) response).addHeader("X-Total-Count", String.valueOf(addresses.size()));
		} else {
			Page<Address> addressPage = addressService.pageAllByExample(addressMapper.dtoToAddress(addressDto), pageable);
			addresses = addressPage.getContent();
			((HttpServletResponse) response).addHeader("X-Total-Count", String.valueOf(addressPage.getTotalElements()));
		}
		return addressMapper.addressesToDtos(addresses);
	}
}
