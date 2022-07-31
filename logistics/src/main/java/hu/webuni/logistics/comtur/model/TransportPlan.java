package hu.webuni.logistics.comtur.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Transport plan JPA entity.
 * 
 * @author comtur
 */
@Entity
@Table(name = "transport_plan")
public class TransportPlan extends Id {

	@Column(name = "expected_income", nullable = false)
	private double expectedIncome;

	@OneToMany(mappedBy = "transportPlan")
	private List<Section> sections;

	public TransportPlan() {
		super();
	}

	public TransportPlan(double expectedIncome) {
		super();
		this.expectedIncome = expectedIncome;
	}

	public double getExpectedIncome() {
		return expectedIncome;
	}

	public void setExpectedIncome(double expectedIncome) {
		this.expectedIncome = expectedIncome;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
}
