package hu.webuni.logistics.comtur.dto;

import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.logistics.comtur.dto.Views.VisibleData;

/**
 * Address entity as data transfer object.
 * 
 * @author comtur
 */
public class AddressDto {

	@JsonView(VisibleData.class)
	private Long id;

	@JsonView(VisibleData.class)
	@NotEmpty
	@Size(min = 2, max = 2)
	private String country;

	@JsonView(VisibleData.class)
	@NotEmpty
	private String zipCode;

	@JsonView(VisibleData.class)
	@NotEmpty
	private String city;

	@JsonView(VisibleData.class)
	@NotEmpty
	private String street;

	@JsonView(VisibleData.class)
	@NotEmpty
	private String number;

	@JsonView(VisibleData.class)
	@Min(-180)
	@Max(180)
	private double lat;

	@JsonView(VisibleData.class)
	@Min(-180)
	@Max(180)
	private double lng;

	public AddressDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressDto other = (AddressDto) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "AddressDto [id=" + id + ", country=" + country + ", zipCode=" + zipCode + ", city=" + city + ", street="
				+ street + ", number=" + number + ", lat=" + lat + ", lng=" + lng + "]";
	}
}
