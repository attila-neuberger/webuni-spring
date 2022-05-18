package hu.comtur.airport.service;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides final price.
 * 
 * @author comtur
 */
@Service // Using as Spring bean.
public class PriceService {
	
	// @Autowired // Inject DiscountService.
	private DiscountService discountService;

	public PriceService(DiscountService discountService) {
		this.discountService = discountService;
	}

	public int getFinalPrice(int price) {
		return (int)(price / 100.0 * (100 - discountService.getDiscountPercent(price)));
	}
}
