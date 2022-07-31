package hu.webuni.logistics.comtur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.logistics.comtur.model.Milestone;

/**
 * Milestone JPA repository.
 * 
 * @author comtur
 */
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

}
