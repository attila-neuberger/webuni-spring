package hu.comtur.airport.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.comtur.airport.dto.AirportDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AirportControllerIt {
	
	private static final String BASE_URI = "/api/airports";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Test
	void testThatCreatedAirportIsListed() throws Exception {
		List<AirportDto> airportsBefore = getAllAirports();
		AirportDto airportDto = new AirportDto(8L, "Gatwick Airport", "GAT");
		createAirport(airportDto);
		List<AirportDto> airportsAfter = getAllAirports();
		
		assertThat(airportsAfter.subList(0, airportsBefore.size()))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(airportsBefore);
		assertThat(airportsAfter.get(airportsAfter.size() - 1))
				.usingRecursiveComparison()
				.ignoringFields("id")
				.isEqualTo(airportDto);
	}

	private List<AirportDto> getAllAirports() {
		List<AirportDto> responseList = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(AirportDto.class)
				.returnResult()
				.getResponseBody();
		Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));
		return responseList;
	}
	
	private void createAirport(AirportDto airportDto) {
		webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(airportDto)
				.exchange()
				.expectStatus()
				.isOk();
	}
}
