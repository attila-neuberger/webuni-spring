package hu.comtur.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.comtur.airport.model.Airport;

public interface AirportRepository extends JpaRepository<Airport, Long> {

	Long countByIata(String iata);
	
	Long countByIataAndIdNot(String iata, long id);
}
