package hu.comtur.airport.model;

import javax.persistence.Entity;

@Entity
public abstract class C extends A {

	protected String c1;

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}
}
