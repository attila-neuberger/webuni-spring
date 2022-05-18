package hu.comtur.airport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.comtur.airport.service.DiscountService;
import hu.comtur.airport.service.SpecialDiscountService;

@Configuration
@Profile("prod")
public class ProdDiscountConfiguration {

	@Bean
	public DiscountService discountService() {
		return new SpecialDiscountService();
	}
}
