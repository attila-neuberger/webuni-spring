package hu.webuni.logistics.comtur.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.logistics.comtur.model.Address;
import hu.webuni.logistics.comtur.model.Milestone;
import hu.webuni.logistics.comtur.repository.AddressRepository;
import hu.webuni.logistics.comtur.service.specification.AddressSpecifications;

/**
 * Service class for {@link Address} entities. Methods are inherited from
 * {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class AddressService extends BaseService<Address> {

	/**
	 * Creates an address service.
	 * 
	 * @param addressRepository Address repository.
	 */
	public AddressService(AddressRepository addressRepository) {
		super();
		this.repository = addressRepository;
	}

	/**
	 * Creates an address.
	 * 
	 * @param address Entity to save.
	 * @return Created entity.
	 * @throws IllegalArgumentException If entity already exists.
	 */
	@Transactional
	public Address create(Address address) throws IllegalArgumentException {
		if (exists(address.getId())) {
			throw new IllegalArgumentException(String.format("Entity with ID %d already exists.", address.getId()));
		}
		return save(address);
	}

	/**
	 * Modifies an address.
	 * 
	 * @param id      ID of the entity.
	 * @param address Entity to save.
	 * @return Modified entity.
	 * @throws IllegalArgumentException If entity does not exist.
	 */
	@Transactional
	public Address modify(long id, Address address) throws NoSuchElementException {
		if (!exists(id)) {
			throw new NoSuchElementException(String.format("Entity with ID %d does not exist.", id));
		}
		address.setId(id);
		return save(address);
	}

	/**
	 * Removes an address.
	 * 
	 * @param id ID of the entity.
	 * @throws NoSuchElementException If entity does not exist.
	 * @throws IllegalStateException  If entity cannot be deleted.
	 */
	@Transactional
	public void remove(long id) throws NoSuchElementException, IllegalStateException {
		if (exists(id)) {
			Optional<Milestone> milestoneOptional = getMilestoneOfAddress(id);
			if (milestoneOptional.isPresent()) {
				throw new IllegalStateException(String.format(
						"Address with ID %d cannot be deleted, because a milestone exists (ID: %d) that refers it.", id,
						milestoneOptional.get().getId()));
			}
			delete(id);
		} else {
			throw new NoSuchElementException(String.format("Entity with ID %d does not exist.", id));
		}
	}

	/**
	 * Gets milestone (optional) of address ID.
	 * 
	 * @return Milestone as {@link Optional}.
	 */
	public Optional<Milestone> getMilestoneOfAddress(long addressId) {
		return ((AddressRepository) repository).getMilestoneOfAddress(addressId);
	}

	/**
	 * Gets page of the Address entities.
	 * 
	 * @param example  Example address object.
	 * @param pageable Pageable object.
	 * @return Page of Addresses.
	 */
	public Page<Address> pageAllByExample(Address example, Pageable pageable) {
		Specification<Address> specification = getSpecification(example);
		return ((AddressRepository) repository).findAll(specification, pageable);
	}

	/**
	 * Gets specification of an address.
	 * 
	 * @param example Example address object.
	 * @return Specification of the address.
	 */
	private Specification<Address> getSpecification(Address example) {
		Specification<Address> specification = Specification.where(null);
		if (example.getCountryIso() != null) {
			specification = specification.and(AddressSpecifications.hasCountry(example.getCountryIso()));
		}
		if (example.getZipCode() != null) {
			specification = specification.and(AddressSpecifications.hasZipCode(example.getZipCode()));
		}
		if (example.getCity() != null) {
			specification = specification.and(AddressSpecifications.hasCity(example.getCity()));
		}
		if (example.getStreet() != null) {
			specification = specification.and(AddressSpecifications.hasStreet(example.getStreet()));
		}
		return specification;
	}

	/**
	 * Gets list of the Address entities.
	 * 
	 * @param example Example address object.
	 * @param sort    Sort object.
	 * @return List of Addresses.
	 */
	public List<Address> getAllByExample(Address example, Sort sort) {
		Specification<Address> specification = getSpecification(example);
		return ((AddressRepository) repository).findAll(specification, sort);
	}
}
