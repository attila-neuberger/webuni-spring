package hu.webuni.hr.comtur.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {
	
	private Employeesalary employeesalary;

	public Employeesalary getEmployeesalary() {
		return employeesalary;
	}

	public void setEmployeesalary(Employeesalary employeesalary) {
		this.employeesalary = employeesalary;
	}

	public static class Employeesalary {
		
		private Def def = new Def();
		private Smart smart = new Smart();
		
		public Def getDef() {
			return def;
		}
		public void setDef(Def def) {
			this.def = def;
		}
		public Smart getSmart() {
			return smart;
		}
		public void setSmart(Smart smart) {
			this.smart = smart;
		}
	}
	
	public static class Def {
		
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}
	}
	
	public static class Smart {
		
		private Limit limit = new Limit();
		private Percent percent = new Percent();
		
		public Limit getLimit() {
			return limit;
		}
		public void setLimit(Limit limit) {
			this.limit = limit;
		}
		public Percent getPercent() {
			return percent;
		}
		public void setPercent(Percent percent) {
			this.percent = percent;
		}
	}
	
	public static class Limit {
		
		private double top;
		private double medium;
		private double bottom;
		
		public double getTop() {
			return top;
		}
		public void setTop(double top) {
			this.top = top;
		}
		public double getMedium() {
			return medium;
		}
		public void setMedium(double medium) {
			this.medium = medium;
		}
		public double getBottom() {
			return bottom;
		}
		public void setBottom(double bottom) {
			this.bottom = bottom;
		}
	}
	
	public static class Percent {
		
		private int top;
		private int medium;
		private int bottom;
		private int none;
		
		public int getTop() {
			return top;
		}
		public void setTop(int top) {
			this.top = top;
		}
		public int getMedium() {
			return medium;
		}
		public void setMedium(int medium) {
			this.medium = medium;
		}
		public int getBottom() {
			return bottom;
		}
		public void setBottom(int bottom) {
			this.bottom = bottom;
		}
		public int getNone() {
			return none;
		}
		public void setNone(int none) {
			this.none = none;
		}
	}
}
