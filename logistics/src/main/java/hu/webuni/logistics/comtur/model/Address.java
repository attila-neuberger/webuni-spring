package hu.webuni.logistics.comtur.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Address JPA entity.
 * 
 * @author comtur
 */
@Entity
public class Address extends Id {

	@Column(name = "country_iso", length = 2, nullable = false)
	private String countryIso;

	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private String number;

	@Column
	private double lat;

	@Column
	private double lng;

	public Address() {
		super();
	}

	public Address(String countryIso, String zipCode, String city, String street, String number, double lat,
			double lng) {
		super();
		this.countryIso = countryIso;
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;
		this.number = number;
		this.lat = lat;
		this.lng = lng;
	}

	public String getCountryIso() {
		return countryIso;
	}

	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
}
