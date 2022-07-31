package hu.webuni.logistics.comtur.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.logistics.comtur.dto.Views.VisibleData;

/**
 * Milestone entity as data transfer object.
 * 
 * @author comtur
 */
public class MilestoneDto {

	@JsonView(VisibleData.class)
	private long id;

	@JsonView(VisibleData.class)
	@NotEmpty
	private LocalDateTime plannedTime;

	@OneToOne(optional = false)
	@JoinColumn(name = "address_id")
	@JsonView(VisibleData.class)
	@NotEmpty
	private AddressDto address;

	public MilestoneDto() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(LocalDateTime plannedTime) {
		this.plannedTime = plannedTime;
	}

	public AddressDto getAddress() {
		return address;
	}

	public void setAddress(AddressDto address) {
		this.address = address;
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
		MilestoneDto other = (MilestoneDto) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "MilestoneDto [id=" + id + ", plannedTime=" + plannedTime + ", address=" + address + "]";
	}
}
