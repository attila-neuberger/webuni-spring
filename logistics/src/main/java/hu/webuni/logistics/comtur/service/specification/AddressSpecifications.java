package hu.webuni.logistics.comtur.service.specification;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.logistics.comtur.model.Address;
import hu.webuni.logistics.comtur.model.Address_;

/**
 * {@link Address} specifications object.
 * 
 * @author comtur
 */
public class AddressSpecifications {

	public static Specification<Address> hasCountry(String countryIso) {
		return (root, cq, cb) -> cb.equal(root.get(Address_.countryIso), countryIso);
	}

	public static Specification<Address> hasZipCode(String zipCode) {
		return (root, cq, cb) -> cb.equal(root.get(Address_.zipCode), zipCode);
	}

	public static Specification<Address> hasCity(String city) {
		return (root, cq, cb) -> cb.like(cb.upper(root.get(Address_.city)), city.toUpperCase() + "%");
	}

	public static Specification<Address> hasStreet(String street) {
		return (root, cq, cb) -> cb.like(cb.upper(root.get(Address_.street)), street.toUpperCase() + "%");
	}
}
