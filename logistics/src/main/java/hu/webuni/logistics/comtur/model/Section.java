package hu.webuni.logistics.comtur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Section JPA entity.
 * 
 * @author comtur
 */
@Entity
public class Section extends Id implements Comparable<Section> {

	@Column(nullable = false)
	private int number;

	@ManyToOne(optional = false)
	@JoinColumn(name = "transport_plan_id", referencedColumnName = "id", nullable = false)
	private TransportPlan transportPlan;

	@ManyToOne(optional = false)
	@JoinColumn(name = "from_milestone_id", referencedColumnName = "id", nullable = false)
	private Milestone fromMilestone;

	@ManyToOne(optional = false)
	@JoinColumn(name = "to_milestone_id", referencedColumnName = "id", nullable = false)
	private Milestone toMilestone;

	public Section() {
		super();
	}

	public Section(int number, TransportPlan transportPlan, Milestone fromMilestone, Milestone toMilestone) {
		super();
		this.number = number;
		this.transportPlan = transportPlan;
		this.fromMilestone = fromMilestone;
		this.toMilestone = toMilestone;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public TransportPlan getTransportPlan() {
		return transportPlan;
	}

	public void setTransportPlan(TransportPlan transportPlan) {
		this.transportPlan = transportPlan;
	}

	public Milestone getFromMilestone() {
		return fromMilestone;
	}

	public void setFromMilestone(Milestone fromMilestone) {
		this.fromMilestone = fromMilestone;
	}

	public Milestone getToMilestone() {
		return toMilestone;
	}

	public void setToMilestone(Milestone toMilestone) {
		this.toMilestone = toMilestone;
	}

	@Override
	public int compareTo(Section otherSection) {
		return number - otherSection.number;
	}
}
