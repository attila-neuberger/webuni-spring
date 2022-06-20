package hu.comtur.airport.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import hu.comtur.airport.model.Airport_;
import hu.comtur.airport.model.Flight;
import hu.comtur.airport.model.Flight_;

public class FlightSpecifications {

	public static Specification<Flight> hasId(long id) {
		// return (root, cq, cb) -> cb.equal(root.get("id"), id);
		
		Specification<Flight> flight = new Specification<Flight>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Flight> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				return cb.equal(root.get(Flight_.id), id);
			}
		};
		return flight;
	}
	
	public static Specification<Flight> hasFlightNumber(String flightNumber) {
		return (root, cq, cb) -> cb.like(root.get(Flight_.flightNumber), flightNumber + "%");
	}
	
	public static Specification<Flight> hasTakeOffTime(LocalDateTime takeOffTime) {
		LocalDateTime startOfDay = LocalDateTime.of(takeOffTime.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Flight_.takeOffTime), startOfDay, startOfDay.plusDays(1));
	}
	
	public static Specification<Flight> hasTakeOffIata(String iata) {
		return (root, cq, cb) -> cb.like(root.get(Flight_.takeOff).get(Airport_.iata), iata + "%");
	}
}
