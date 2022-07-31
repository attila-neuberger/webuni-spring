package hu.webuni.logistics.comtur.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class of configuration properties.
 * 
 * @author comtur
 */
@ConfigurationProperties(prefix = "logistics")
@Component
public class LogisticsConfigProperties {

	/**
	 * Transport plan API configurations
	 */
	private Transportplan transportplan;

	public Transportplan getTransportplan() {
		return transportplan;
	}

	public void setTransportplan(Transportplan transportplan) {
		this.transportplan = transportplan;
	}

	public static class Transportplan {

		private int[] delayminutes;

		private double[] delaypenalty;

		public int[] getDelayminutes() {
			return delayminutes;
		}

		public void setDelayminutes(int[] delayminutes) {
			this.delayminutes = delayminutes;
		}

		public double[] getDelaypenalty() {
			return delaypenalty;
		}

		public void setDelaypenalty(double[] delaypenalty) {
			this.delaypenalty = delaypenalty;
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
