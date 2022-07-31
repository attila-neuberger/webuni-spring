package hu.webuni.logistics.comtur.dto;

import java.util.List;
import java.util.Objects;

import javax.persistence.OneToMany;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.logistics.comtur.dto.Views.VisibleData;

/**
 * Transport plan entity as data transfer object.
 * 
 * @author comtur
 */
public class TransportPlanDto {

	@JsonView(VisibleData.class)
	private Long id;

	@JsonView(VisibleData.class)
	@Positive
	private double expectedIncome;

	@JsonView(VisibleData.class)
	@OneToMany(mappedBy = "transportPlan")
	private List<SectionDto> sections;

	public TransportPlanDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getExpectedIncome() {
		return expectedIncome;
	}

	public void setExpectedIncome(double expectedIncome) {
		this.expectedIncome = expectedIncome;
	}

	public List<SectionDto> getSections() {
		return sections;
	}

	public void setSections(List<SectionDto> sections) {
		this.sections = sections;
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
		TransportPlanDto other = (TransportPlanDto) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "TransportPlanDto [id=" + id + ", expectedIncome=" + expectedIncome + ", sections=" + sections + "]";
	}
}
