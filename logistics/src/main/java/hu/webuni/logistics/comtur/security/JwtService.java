package hu.webuni.logistics.comtur.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.logistics.comtur.config.LogisticsConfigProperties;

/**
 * Service class for Java web token handling.
 * 
 * @author comtur
 */
@Service
public class JwtService {

	@Autowired
	LogisticsConfigProperties logisticsConfigProperties;

	private static final String AUTH = "auth";

	/**
	 * Creates a Java web token based on the configuration parameters and the
	 * current user principal.
	 * 
	 * @param principal Current user principal.
	 * @return Created Java web token.
	 */
	public String createJwtToken(UserDetails principal) {
		String algorithmName = logisticsConfigProperties.getJwtdata().getAlgorithm();
		Algorithm alg = getAlgorithm(algorithmName);
		return JWT.create().withSubject(principal.getUsername())
				.withIssuer(logisticsConfigProperties.getJwtdata().getIssuer())
				.withArrayClaim(AUTH,
						principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
				.withExpiresAt(new Date(System.currentTimeMillis()
						+ TimeUnit.SECONDS.toMillis(logisticsConfigProperties.getJwtdata().getExpiration())))
				.sign(alg);
	}

	/**
	 * Creates algorithm based on configuration value. Supports methods with 1
	 * String parameter only.
	 * 
	 * @param algorithmName Algorithm name in the configuration file.
	 * @return Algorithm instance.
	 */
	private Algorithm getAlgorithm(String algorithmName) {
		if (algorithmName.startsWith("HMAC")) {
			try {
				Method method = Algorithm.class.getMethod(algorithmName, String.class);
				return (Algorithm) method.invoke(null, logisticsConfigProperties.getJwtdata().getSecret());
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				return Algorithm.none();
			}
		} else {
			return Algorithm.none();
		}
	}

	/**
	 * Parses a Java web token to get the current {@link User}.
	 * 
	 * @param jwtToken Java web token.
	 * @return Current user.
	 */
	public UserDetails parseJwt(String jwtToken) {
		String algorithmName = logisticsConfigProperties.getJwtdata().getAlgorithm();
		Algorithm alg = getAlgorithm(algorithmName);
		DecodedJWT decodedJWT = JWT.require(alg)
				.withIssuer(logisticsConfigProperties.getJwtdata().getIssuer())
				.build()
				.verify(jwtToken);
		return new User(decodedJWT.getSubject(), "dummy",
				decodedJWT.getClaim(AUTH).asList(String.class).stream().map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList()));
	}
}
