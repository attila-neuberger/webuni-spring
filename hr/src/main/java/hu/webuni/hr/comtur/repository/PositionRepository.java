package hu.webuni.hr.comtur.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.comtur.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
	
	Optional<Position> findByName(String name);
}
