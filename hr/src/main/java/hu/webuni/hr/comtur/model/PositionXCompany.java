package hu.webuni.hr.comtur.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "position_x_company")
public class PositionXCompany {

	@Id
	@GeneratedValue
	private long id;
	
	private int minSalary;
	
	@ManyToOne
	private Position position;
	
	@ManyToOne
	private Company company;
	
	public PositionXCompany() {}

	public PositionXCompany(int minSalary, Position position, Company company) {
		super();
		this.minSalary = minSalary;
		this.position = position;
		this.company = company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
		PositionXCompany other = (PositionXCompany) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "PositionXCompany [id=" + id + ", minSalary=" + minSalary + ", position=" + position + ", company="
				+ company + "]";
	}
}
