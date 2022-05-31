package hu.comtur.airport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

	@Test
	void testGetFinalPrice() throws Exception {
		int newPrice = new PriceService(p -> 5).getFinalPrice(100);
		assertEquals(95, newPrice);
	}
	
	@Test
	void testGetFinalPrice2() throws Exception {
		int newPrice = new PriceService(p -> 5).getFinalPrice(100);
		assertThat(newPrice).isEqualTo(95);
	}
	
	@InjectMocks
	PriceService priceService;
	
	@Mock
	DiscountService discountService;
	
	@Test // Mockito
	void testGetFinalPrice3() throws Exception {
		/*import static -> Mockito.*/when(discountService.getDiscountPercent(100)).thenReturn(5);
		int newPrice = priceService.getFinalPrice(100);
		assertThat(newPrice).isEqualTo(95);
	}
}
