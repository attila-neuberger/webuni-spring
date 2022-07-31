package hu.webuni.logistics.comtur.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hu.webuni.logistics.comtur.security.JwtAuthFilter;

/**
 * Web security configuration class.
 * 
 * @author comtur
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtAuthFilter jwtAuthFilter;

	/**
	 * Granted authorities.
	 */
	private final static String ADDRESS_ROLE = "AddressManager", TRANSPORT_ROLE = "TransportManager";

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("address_user")
				.authorities(ADDRESS_ROLE).password(passwordEncoder().encode("pass")).and().withUser("transport_user")
				.authorities(TRANSPORT_ROLE).password(passwordEncoder().encode("pass")).and().withUser("admin_user")
				.authorities(ADDRESS_ROLE, TRANSPORT_ROLE).password(passwordEncoder().encode("pass"));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers(HttpMethod.POST, "/api/login/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/addresses/search").authenticated()
				.antMatchers(HttpMethod.POST, "/api/addresses/**").hasAuthority(ADDRESS_ROLE)
				.antMatchers(HttpMethod.PUT, "/api/addresses/**").hasAuthority(ADDRESS_ROLE)
				.antMatchers(HttpMethod.DELETE, "/api/addresses/**").hasAuthority(ADDRESS_ROLE)
				.antMatchers(HttpMethod.POST, "/api/transportPlans/*/delay").hasAuthority(TRANSPORT_ROLE)
				.anyRequest().authenticated();

		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
