package hu.comtur.airport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.comtur.airport.service.AirportService;
import hu.comtur.airport.service.InitDbService;
import hu.comtur.airport.service.PriceService;

@SpringBootApplication
public class AirportApplication implements CommandLineRunner {

	@Autowired
	private PriceService priceService;
	
	@SuppressWarnings("unused")
	@Autowired
	private AirportService airportService;
	
	@Autowired
	private InitDbService initDbService;
	
	public static void main(String[] args) {
		SpringApplication.run(AirportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(priceService.getFinalPrice(200));
		System.out.println(priceService.getFinalPrice(20_000));
		// airportService.createFlight();
		initDbService.createUsersIfNeeded();
	}
}
