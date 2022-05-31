package hu.comtur.airport.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.comtur.airport.dto.AirportDto;
import hu.comtur.airport.mapper.AirportMapper;
import hu.comtur.airport.model.Airport;
import hu.comtur.airport.service.AirportService;

@RestController
@RequestMapping("/api/airports")
public class AirportRESTController {

	/*private Map<Long, AirportDto> airports = new HashMap<>();
	
	{
		airports.put(1L, new AirportDto(1L, "Lisz Ferenc", "BUD"));
		airports.put(2L, new AirportDto(2L, "Chania Airport", "CHA"));
	}*/ // Moved to AirportService.
	
	@Autowired
	AirportService airportService;
	
	@Autowired
	AirportMapper airportMapper;
	
	@GetMapping
	public List<AirportDto> getAll() {
		// return new ArrayList<>(airports.values());
		return airportMapper.airportToDtos(airportService.findAll());
	}
	
	// @GetMapping("/{id}")
	// public AirportDto getById(@PathVariable long id) {
	// 	return airports.get(id);
	// }
	
	// @GetMapping("/{id}")
	// public ResponseEntity<AirportDto> getById(@PathVariable long id) {
	// 	AirportDto airport = airports.get(id);
	// 	if (airport != null) {
	// 		return ResponseEntity.ok(airport);
	// 	} else {
	// 		return ResponseEntity.notFound().build();
	// 	}
	// }
	
	@GetMapping("/{id}")
	public AirportDto getById(@PathVariable long id) {
		// AirportDto airport = airports.get(id);
		// if (airport != null) {
		// 	return airport;
		// } else {
		//	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		// }
		Airport airport = airportService.findById(id);
		if (airport != null) {
			return airportMapper.airportToDto(airport);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public AirportDto createAirport(@RequestBody @Valid AirportDto airportDto) {
		// checkUniqueIata(airportDto.getIata()); // Moved to AirportService.
		// airports.put(airportDto.getId(), airport);
		Airport airport = airportService.save(airportMapper.dtoToAirport(airportDto));
		// return airportDto;
		return airportMapper.airportToDto(airport);
	}
	
	/*private void checkUniqueIata(String iata) {
		Optional<AirportDto> airportWithSameIata = airports
			.values()
			.stream()
			.filter(a -> a.getIata().equals(iata))
			.findAny();
		if (airportWithSameIata.isPresent()) {
			throw new NonUniqueIataException(iata);
		}
	}*/ // Moved to AirportService.

	/*@PutMapping("/{id}")
	public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id, @RequestBody @Valid AirportDto airport) {
		if (!airports.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}
		checkUniqueIata(airport.getIata());
		airport.setId(id);
		airports.put(id, airport);
		return ResponseEntity.ok(airport);
	}
	
	@DeleteMapping("/{id}")
	public void deleteAirport(@PathVariable long id) {
		airports.remove(id);
	}*/
}
