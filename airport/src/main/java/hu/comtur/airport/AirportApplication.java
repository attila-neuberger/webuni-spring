package hu.comtur.airport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.comtur.airport.service.PriceService;

@SpringBootApplication
public class AirportApplication implements CommandLineRunner {

	@Autowired
	private PriceService priceService;
	
	public static void main(String[] args) {
		SpringApplication.run(AirportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(priceService.getFinalPrice(200));
		System.out.println(priceService.getFinalPrice(20_000));
	}

	/*@Bean
	public DiscountService discountService() {
		// return new DefaultDiscountService();
		return new SpecialDiscountService();
	}*/
}
