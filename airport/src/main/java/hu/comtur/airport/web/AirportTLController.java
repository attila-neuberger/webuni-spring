package hu.comtur.airport.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.comtur.airport.dto.AirportDto;

@Controller
public class AirportTLController {
	
	private List<AirportDto> allAirports = new ArrayList<>();
	
	{
		allAirports.add(new AirportDto(1L, "Ferenc Liszt Airport", "BUD"));
	}
	
	@GetMapping("/")
	public String home() {
		System.out.println("Test output.");
		return "index";
	}
	
	@GetMapping("/airports")
	public String listAirports(Map<String, Object> model) {
		model.put("airports", allAirports);
		model.put("newAirport", new AirportDto());
		return "airports";
	}
	
	@PostMapping("/airports")
	public String addAirport(AirportDto airport) {
		allAirports.add(airport);
		return "redirect:airports";
	}
}
