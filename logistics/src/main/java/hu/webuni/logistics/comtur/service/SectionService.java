package hu.webuni.logistics.comtur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.logistics.comtur.model.Milestone;
import hu.webuni.logistics.comtur.model.Section;
import hu.webuni.logistics.comtur.repository.SectionRepository;

/**
 * Service class for {@link Section} entities. Methods are inherited from
 * {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class SectionService extends BaseService<Section> {

	@Autowired
	private MilestoneService milestoneService;

	/**
	 * Creates a section service.
	 * 
	 * @param sectionRepository Section repository.
	 */
	public SectionService(SectionRepository sectionRepository) {
		super();
		this.repository = sectionRepository;
	}

	/**
	 * Creates a section
	 * 
	 * @param section Entity to save.
	 * @return Saved entity.
	 * @throws IllegalArgumentException If entity already exists.
	 */
	@Transactional
	public Section create(Section section) throws IllegalArgumentException {
		if (exists(section.getId())) {
			throw new IllegalArgumentException(String.format("Entity with ID %d already exists.", section.getId()));
		}
		Milestone fromMilestone = milestoneService.create(section.getFromMilestone());
		Milestone toMilestone = milestoneService.create(section.getToMilestone());
		section.setFromMilestone(fromMilestone);
		section.setToMilestone(toMilestone);
		return save(section);
	}
}
