package hu.comtur.airport.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.comtur.airport.model.LogEntry;
import hu.comtur.airport.repository.LogEntryRepository;

@Service
public class LogEntryService {

	@Autowired
	LogEntryRepository logEntryRepository;
	
	public void createLog(String description) {
		callBackendSystem();
		logEntryRepository.save(new LogEntry(description));
	}
	
	private void callBackendSystem() {
		if (new Random().nextInt(4) == 1) {
			System.err.println("Backend system save failure.");
			throw new RuntimeException(); // Simulating backend error.
		}
	}
}
