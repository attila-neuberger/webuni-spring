package hu.comtur.airport.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/*import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;*/

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.comtur.airport.model.Airport;
import hu.comtur.airport.model.Flight;
import hu.comtur.airport.repository.AirportRepository;
import hu.comtur.airport.repository.FlightRepository;

@Service
public class AirportService {

	/*private Map<Long, Airport> airports = new HashMap<>();
	
	{
		airports.put(1L, new Airport(1L, "Lisz Ferenc", "BUD"));
		airports.put(2L, new Airport(2L, "Chania Airport", "CHA"));
	}*/ // Using database instead of memory.
	
	// @PersistenceContext
	// EntityManager em; // Using  Spring Data instead of Entity manager.
	
	private AirportRepository airportRepository;
	private FlightRepository flightRepository;
	private LogEntryService logEntryService;
	
	// Injecting via constructor.
	public AirportService(AirportRepository airportRepository, FlightRepository flightRepository,
			LogEntryService logEntryService) {
		this.airportRepository = airportRepository;
		this.flightRepository = flightRepository;
		this.logEntryService = logEntryService;
	}

	@Transactional
	public Airport save(Airport airport) {
		checkUniqueIata(airport.getIata(), null);
		// airports.put(airport.getId(), airport); // Using database instead of memory.
		// em.persist(airport); // Using Spring Data instead of Entity manager.
		// return airport; // Using Spring Data instead of Entity manager.
		return airportRepository.save(airport);
	}
	
	public List<Airport> findAll() {
		// return new ArrayList<>(airports.values()); // Using database instead of memory.
		// return em.createQuery("SELECT a FROM Airport a", Airport.class)
		// 		.getResultList(); // Using Spring Data instead of Entity manager.
		return airportRepository.findAll();
	}
	
	public Optional<Airport> findById(long id) {
		// return airports.get(id); // Using database instead of memory.
		// return em.find(Airport.class, id); // Using Spring Data instead of Entity manager.
		return airportRepository.findById(id);
	}
	
	@Transactional
	public void delete(long id) {
		// airports.remove(id); // Using database instead of memory.
		// em.remove(findById(id)); // Using Spring Data instead of Entity manager.
		airportRepository.deleteById(id);
	}
	
	private void checkUniqueIata(String iata, Long id) {
		/*Optional<Airport> airportWithSameIata = airports
			.values()
			.stream()
			.filter(a -> a.getIata().equals(iata))
			.findAny();
		if (airportWithSameIata.isPresent()) {
			throw new NonUniqueIataException(iata);
		}*/ // Using EM instead of memory map.
		
		boolean forUpdate = id != null;
		/*TypedQuery<Long> query = em.createNamedQuery(forUpdate ? "Airport.countByIataAndIdNotIn" : "Airport.countByIata", Long.class)
				.setParameter("iata", iata);
		if (forUpdate) {
			query.setParameter("id", id);
		}
		long count = query
				.getSingleResult();*/ // Using AirportRepository's own methods instead of Airport's named query.
		
		Long count = forUpdate
				? airportRepository.countByIataAndIdNot(iata, id)
				: airportRepository.countByIata(iata);
		
		if (count > 0) {
			throw new NonUniqueIataException(iata);
		}
	}
	
	@Transactional
	public Airport update(Airport airport) {
		checkUniqueIata(airport.getIata(), airport.getId());
		// return em.merge(airport); // Using Spring Data instead of Entity manager.
		if (airportRepository.existsById(airport.getId())) {
			Airport savedAirport = airportRepository.save(airport);
			logEntryService.createLog(String.format("Airport modified with ID %d, new name: %s.", 
					airport.getId(), airport.getName()));
			return savedAirport;
		} else {
			throw new NoSuchElementException();
		}
	}
	
	@Transactional
	public Flight createFlight(String flightNumber, long takeOffAirportId, long landingAirportId, LocalDateTime takeOffTime) {
		Flight flight = new Flight();
		flight.setFlightNumber(flightNumber);
		flight.setTakeOff(airportRepository.findById(takeOffAirportId).get());
		flight.setLanding(airportRepository.findById(landingAirportId).get());
		flight.setTakeOffTime(takeOffTime);
		return flightRepository.save(flight);
	}
	
	public List<Flight> findFlightsByExample(Flight example) {
		long id = example.getId();
		String flightNumber = example.getFlightNumber();
		String takeOffIata = null;
		Airport takeOff = example.getTakeOff();
		if (takeOff != null) {
			takeOffIata = takeOff.getIata();
		}
		LocalDateTime takeOffTime = example.getTakeOffTime();
		
		Specification<Flight> specification = Specification.where(null);
		if (id > 0) {
			specification = specification.and(FlightSpecifications.hasId(id));
		}
		if (StringUtils.hasText(flightNumber)) {
			specification = specification.and(FlightSpecifications.hasFlightNumber(flightNumber));
		}
		if (StringUtils.hasText(takeOffIata)) {
			specification = specification.and(FlightSpecifications.hasTakeOffIata(takeOffIata));
		}
		if (takeOffTime != null) {
			specification = specification.and(FlightSpecifications.hasTakeOffTime(takeOffTime));
		}
		return flightRepository.findAll(specification, Sort.by("id"));
	}
}
