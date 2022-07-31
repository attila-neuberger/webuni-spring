package hu.webuni.logistics.comtur.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.logistics.comtur.dto.LoginDto;

/**
 * Login REST controller.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/login")
public class JwtLoginController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtService jwtService;

	/**
	 * Login request.
	 * 
	 * @param loginDto Login DTO.
	 * @return Java web token.
	 */
	@PostMapping
	public String login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
		return jwtService.createJwtToken((UserDetails) authentication.getPrincipal());
	}
}
