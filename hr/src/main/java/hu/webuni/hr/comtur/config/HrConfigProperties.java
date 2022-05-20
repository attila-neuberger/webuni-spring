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
		
		private double[] limit;
		private int[] percent;
		
		public double[] getLimit() {
			return limit;
		}
		public void setLimit(double[] limit) {
			this.limit = limit;
		}
		public int[] getPercent() {
			return percent;
		}
		public void setPercent(int[] percent) {
			this.percent = percent;
		}
	}
}
