package hu.comtur.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.comtur.airport.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {

}
