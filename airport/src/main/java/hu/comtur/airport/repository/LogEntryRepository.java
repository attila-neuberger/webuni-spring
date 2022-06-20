package hu.comtur.airport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.comtur.airport.model.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}
