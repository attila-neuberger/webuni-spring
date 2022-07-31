package hu.webuni.logistics.comtur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.logistics.comtur.model.Address;
import hu.webuni.logistics.comtur.model.Milestone;
import hu.webuni.logistics.comtur.repository.MilestoneRepository;

/**
 * Service class for {@link Milestone} entities. Methods are inherited from
 * {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class MilestoneService extends BaseService<Milestone> {

	@Autowired
	private AddressService addressService;

	/**
	 * Creates a milestone service.
	 * 
	 * @param milestoneRepository Milestone repository.
	 */
	public MilestoneService(MilestoneRepository milestoneRepository) {
		super();
		this.repository = milestoneRepository;
	}

	/**
	 * Creates a milestone.
	 * 
	 * @param milestone Entity to save.
	 * @return Saved entity.
	 * @throws IllegalArgumentException If entity already exists.
	 */
	@Transactional
	public Milestone create(Milestone milestone) throws IllegalArgumentException {
		if (exists(milestone.getId())) {
			throw new IllegalArgumentException(String.format("Entity with ID %d already exists.", milestone.getId()));
		}
		Address address = addressService.create(milestone.getAddress());
		milestone.setAddress(address);
		return save(milestone);
	}
}
