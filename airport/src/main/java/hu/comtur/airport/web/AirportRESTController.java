package hu.comtur.airport.web;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@Autowired
	AirportService airportService;
	
	@Autowired
	AirportMapper airportMapper;
	
	@GetMapping
	public List<AirportDto> getAll() {
		return airportMapper.airportToDtos(airportService.findAll());
	}
	
	@GetMapping("/{id}")
	public AirportDto getById(@PathVariable long id) {
		Airport airport = airportService.findById(id).orElseThrow(() -> 
				new ResponseStatusException(HttpStatus.NOT_FOUND));
		/*if (airport != null) {
			return airportMapper.airportToDto(airport);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}*/ // Using Optional's orElseThrow instead.
		return airportMapper.airportToDto(airport);
	}
	
	@PostMapping
	public AirportDto createAirport(@RequestBody @Valid AirportDto airportDto) {
		Airport airport = airportService.save(airportMapper.dtoToAirport(airportDto));
		return airportMapper.airportToDto(airport);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id, @RequestBody @Valid AirportDto airportDto) {
		Airport airport = airportMapper.dtoToAirport(airportDto);
		airport.setId(id);
		try {
			AirportDto savedAirportDta = airportMapper.airportToDto(airportService.update(airport));
			return ResponseEntity.ok(savedAirportDta);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public void deleteAirport(@PathVariable long id) {
		airportService.delete(id);
	}
}
