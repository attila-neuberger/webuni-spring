package hu.webuni.logistics.comtur.dto;

import java.util.Objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.logistics.comtur.dto.Views.VisibleData;

/**
 * Section entity as data transfer object.
 * 
 * @author comtur
 */
public class SectionDto implements Comparable<SectionDto> {

	@JsonView(VisibleData.class)
	private long id;

	@JsonView(VisibleData.class)
	@NotEmpty
	private int number;

	@ManyToOne(optional = false)
	@JoinColumn(name = "transport_plan_id")
	@JsonView(VisibleData.class)
	@NotEmpty
	private TransportPlanDto transportPlan;

	@ManyToOne(optional = false)
	@JoinColumn(name = "from_milestone_id")
	@JsonView(VisibleData.class)
	@NotEmpty
	private MilestoneDto fromMilestone;

	@ManyToOne(optional = false)
	@JoinColumn(name = "to_milestone_id")
	@JsonView(VisibleData.class)
	@NotEmpty
	private MilestoneDto toMilestone;

	public SectionDto() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public TransportPlanDto getTransportPlan() {
		return transportPlan;
	}

	public void setTransportPlan(TransportPlanDto transportPlan) {
		this.transportPlan = transportPlan;
	}

	public MilestoneDto getFromMilestone() {
		return fromMilestone;
	}

	public void setFromMilestone(MilestoneDto fromMilestone) {
		this.fromMilestone = fromMilestone;
	}

	public MilestoneDto getToMilestone() {
		return toMilestone;
	}

	public void setToMilestone(MilestoneDto toMilestone) {
		this.toMilestone = toMilestone;
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
		SectionDto other = (SectionDto) obj;
		return id == other.id;
	}

	@Override
	public int compareTo(SectionDto otherSection) {
		return number - otherSection.number;
	}

	@Override
	public String toString() {
		return "SectionDto [id=" + id + ", number=" + number + ", transportPlan=" + transportPlan + ", fromMilestone="
				+ fromMilestone + ", toMilestone=" + toMilestone + "]";
	}
}
