package hu.comtur.airport.model;

import javax.persistence.Entity;

@Entity
public class D extends C {

	protected String d1;

	public String getD1() {
		return d1;
	}

	public void setD1(String d1) {
		this.d1 = d1;
	}
}
