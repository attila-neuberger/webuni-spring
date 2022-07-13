package hu.webuni.hr.comtur.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {
	
	/**
	 * Employee salary configuration.
	 */
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
	
	/**
	 * JWT data configuration.
	 */
	private Jwtdata jwtdata;
	
	public Jwtdata getJwtdata() {
		return jwtdata;
	}

	public void setJwtdata(Jwtdata jwtdata) {
		this.jwtdata = jwtdata;
	}

	public static class Jwtdata {
		
		private String secret;
		private String algorithm;
		private long expiration;
		private String issuer;
		
		public String getSecret() {
			return secret;
		}
		
		public void setSecret(String secret) {
			this.secret = secret;
		}
		
		public String getAlgorithm() {
			return algorithm;
		}
		
		public void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}
		
		public long getExpiration() {
			return expiration;
		}

		public void setExpiration(long expiration) {
			this.expiration = expiration;
		}

		public String getIssuer() {
			return issuer;
		}
		
		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}
	}
}
