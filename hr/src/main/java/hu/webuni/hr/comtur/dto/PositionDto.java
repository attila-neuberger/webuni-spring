package hu.webuni.hr.comtur.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.Views.VisibleData;
import hu.webuni.hr.comtur.model.Education;

public class PositionDto {

	@JsonView(VisibleData.class)
	private long id;
	
	@JsonView(VisibleData.class)
	@NotEmpty
	private String name;
	
	@JsonView(VisibleData.class)
	private Education minEducation;
	
	public PositionDto() {}

	public PositionDto(String name, Education minEducation) {
		this.name = name;
		this.minEducation = minEducation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Education getMinEducation() {
		return minEducation;
	}

	public void setMinEducation(Education minEducation) {
		this.minEducation = minEducation;
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
		PositionDto other = (PositionDto) obj;
		return id == other.id;
	}
}
