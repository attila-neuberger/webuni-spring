package hu.comtur.airport.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.comtur.airport.dto.AirportDto;

@RestController
@RequestMapping("/api/airports")
public class AirportRESTController {

	private Map<Long, AirportDto> airports = new HashMap<>();
	
	{
		airports.put(1L, new AirportDto(1L, "Lisz Ferenc", "BUD"));
		airports.put(2L, new AirportDto(2L, "Chania Airport", "CHA"));
	}
	
	@GetMapping
	public List<AirportDto> getAll() {
		return new ArrayList<>(airports.values());
	}
	
	/*@GetMapping("/{id}")
	public AirportDto getById(@PathVariable long id) {
		return airports.get(id);
	}*/
	
	@GetMapping("/{id}")
	public ResponseEntity<AirportDto> getById(@PathVariable long id) {
		AirportDto airport = airports.get(id);
		if (airport != null) {
			return ResponseEntity.ok(airport);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public AirportDto createAirport(@RequestBody AirportDto airport) {
		airports.put(airport.getId(), airport);
		return airport;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id, @RequestBody AirportDto airport) {
		if (!airports.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}
		airport.setId(id);
		airports.put(id, airport);
		return ResponseEntity.ok(airport);
	}
	
	@DeleteMapping("/{id}")
	public void deleteAirport(@PathVariable long id) {
		airports.remove(id);
	}
}
