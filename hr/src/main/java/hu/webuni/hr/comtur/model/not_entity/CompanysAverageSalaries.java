package hu.webuni.hr.comtur.model.not_entity;

import java.util.Objects;

import hu.webuni.hr.comtur.repository.CompanyRepository;

/**
 * Helper class for {@link CompanyRepository}'s getCompanysAverageSalaries query.
 * @author comtur
 */
public class CompanysAverageSalaries {
	
	private String position;
	
	private double averageSalary;

	public CompanysAverageSalaries(String position, double averageSalary) {
		this.position = position;
		this.averageSalary = averageSalary;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getAverageSalary() {
		return averageSalary;
	}

	public void setAverageSalary(double averageSalary) {
		this.averageSalary = averageSalary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(position);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanysAverageSalaries other = (CompanysAverageSalaries) obj;
		return Objects.equals(position, other.position);
	}

	@Override
	public String toString() {
		return "CompanysAverageSalaries [position=" + position + ", averageSalary=" + averageSalary + "]";
	}
}
