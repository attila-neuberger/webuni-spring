package hu.webuni.logistics.comtur.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.logistics.comtur.model.Address;
import hu.webuni.logistics.comtur.model.Milestone;

/**
 * Address JPA repository.
 * 
 * @author comtur
 */
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

	@Override
	Page<Address> findAll(Pageable pageable);
	
	@Query(
			"SELECT m " +
			"FROM Milestone m " + 
			"WHERE m.address.id = :id")
	Optional<Milestone> getMilestoneOfAddress(long id);
}
