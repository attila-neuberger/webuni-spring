package hu.webuni.hr.comtur.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "position_x_company")
public class PositionXCompany extends Id {

	private int minSalary;
	
	@ManyToOne
	private Position position;
	
	@ManyToOne
	private Company company;
	
	public PositionXCompany() {
		super();
	}

	public PositionXCompany(int minSalary, Position position, Company company) {
		super();
		this.minSalary = minSalary;
		this.position = position;
		this.company = company;
	}

	public int getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "PositionXCompany [id=" + getId() + ", minSalary=" + minSalary + ", position=" + position + ", company="
				+ company + "]";
	}
}
