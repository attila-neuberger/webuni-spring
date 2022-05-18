package hu.comtur.airport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.comtur.airport.config.AirportConfigProperties;

/**
 * Discount service implementing class (default class).
 * 
 * @author comtur
 */
@Service // Using as Spring bean.
public class DefaultDiscountService implements DiscountService {

	@Autowired
	AirportConfigProperties config;
	
	@Override
	public int getDiscountPercent(int totalPrice) {
		// return 10;
		return config.getDiscount().getDef().getPercent();
	}

}
