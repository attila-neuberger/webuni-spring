package hu.comtur.airport.service;

public class NonUniqueIataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NonUniqueIataException(String iata) {
		super("Existing IATA: " + iata);
	}
}
