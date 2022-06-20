package hu.comtur.airport.model;

import javax.persistence.Entity;

@Entity
public class B extends A {

	protected String b1;

	public String getB1() {
		return b1;
	}

	public void setB1(String b1) {
		this.b1 = b1;
	}
}
