package hu.comtur.airport.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import hu.comtur.airport.model.Airport;
import hu.comtur.airport.model.Flight;
import hu.comtur.airport.repository.AirportRepository;
import hu.comtur.airport.repository.FlightRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class AirportServiceIt {

	@Autowired
	private AirportService airportService;
	
	@Autowired
	private AirportRepository airportRepository;
	
	@Autowired
	private FlightRepository flightRepository;
	
	@BeforeEach
	public void init() {
		flightRepository.deleteAll();
		airportRepository.deleteAll();
	}
	
	@Test
	void testCreateFlight() throws Exception {
		String flightNumber = "F1234";
		long takeoff = createAirport("Liszt Ferenc Repülőtér", "BUD");
		long landing = createAirport("Hurghada Egypt", "HUR");
		LocalDateTime takeoffTime = LocalDateTime.of(2022, 5, 8, 8, 45, 0);
		Flight flight = airportService.createFlight(flightNumber, takeoff, landing, takeoffTime);
		
		Optional<Flight> savedFlightOptional = flightRepository.findById(flight.getId());
		assertThat(savedFlightOptional).isNotEmpty();
		assertThat(savedFlightOptional.get().getFlightNumber()).isEqualTo(flightNumber);
		assertThat(savedFlightOptional.get().getTakeOff().getId()).isEqualTo(takeoff);
		assertThat(savedFlightOptional.get().getLanding().getId()).isEqualTo(landing);
		assertThat(savedFlightOptional.get().getTakeOffTime()).isCloseTo(takeoffTime, new TemporalUnitWithinOffset(1, ChronoUnit.MICROS));
		assertThat(savedFlightOptional.get().getTakeOffTime()).isCloseTo(takeoffTime, within(1, ChronoUnit.MICROS)); // Assertions.within
	}

	private long createAirport(String name, String iata) {
		return airportRepository.save(new Airport(name, iata)).getId();
	}
	
	@Test
	@SuppressWarnings("unused")
	void testFindFlightsByExample() throws Exception {
		long airport1 = createAirport("Dorthmund", "DOR");
		long airport2 = createAirport("Düsseldorf", "DUS");
		long airport3 = createAirport("Chania", "CHA");
		long airport4 = createAirport("Heraklion", "HER");
		LocalDateTime takeOffTime = LocalDateTime.of(2022, 06, 17, 10, 15, 0);
		long flight1 = createFlight("FN-42", airport1, airport3, takeOffTime);
		long flight2 = createFlight("FN-43", airport2, airport3, takeOffTime.plusHours(2));
		long flight3 = createFlight("XFN-5", airport1, airport3, takeOffTime.plusHours(8));
		long flight4 = createFlight("FN-44", airport1, airport3, takeOffTime.plusDays(2));
		long flight5 = createFlight("FN-48", airport3, airport4, takeOffTime);
		
		Flight example = new Flight();
		example.setFlightNumber("FN-");
		example.setTakeOff(new Airport("Example", "D"));
		example.setTakeOffTime(takeOffTime);
		List<Flight> flightsFound = airportService.findFlightsByExample(example);
		assertThat(flightsFound.stream().map(Flight::getId).collect(Collectors.toList())).containsExactly(flight1, flight2);
	}
	
	private long createFlight(String flightNumber, long takeOffId, long landingId, LocalDateTime takeOffTime) {
		return airportService.createFlight(flightNumber, takeOffId, landingId, takeOffTime).getId();
	}
}
