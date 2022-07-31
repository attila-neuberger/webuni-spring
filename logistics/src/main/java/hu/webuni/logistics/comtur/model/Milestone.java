package hu.webuni.logistics.comtur.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Milestone JPA entity.
 * 
 * @author comtur
 */
@Entity
public class Milestone extends Id {

	@Column(name = "planned_time", nullable = false)
	private LocalDateTime plannedTime;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
	private Address address;

	public Milestone() {
		this(null, null);
	}

	public Milestone(LocalDateTime plannedTime) {
		this(plannedTime, null);
	}

	public Milestone(LocalDateTime plannedTime, Address address) {
		super();
		this.plannedTime = plannedTime;
		this.address = address;
	}

	public LocalDateTime getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(LocalDateTime plannedTime) {
		this.plannedTime = plannedTime;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
