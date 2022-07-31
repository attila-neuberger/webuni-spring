package hu.webuni.logistics.comtur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.logistics.comtur.model.TransportPlan;

/**
 * Transport plan JPA repository.
 * 
 * @author comtur
 */
public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long> {

	@Query(
			"SELECT DISTINCT tp " +
			"FROM TransportPlan tp " + 
				"JOIN Section s ON s.transportPlan = tp.id")
	@EntityGraph(attributePaths = {"sections"}, type = EntityGraphType.LOAD)
	List<TransportPlan> getTransportPlansDetailed();
	
	@Query(
			"SELECT DISTINCT tp " +
			"FROM TransportPlan tp " + 
				"JOIN Section s ON s.transportPlan = tp.id " + 
			"WHERE tp.id = :id")
	@EntityGraph(attributePaths = {"sections"}, type = EntityGraphType.LOAD)
	Optional<TransportPlan> getTransportPlanDetailedById(long id);
}
