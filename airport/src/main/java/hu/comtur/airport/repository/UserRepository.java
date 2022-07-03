package hu.comtur.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.comtur.airport.model.AirportUser;

public interface UserRepository extends JpaRepository<AirportUser, String> {

}
