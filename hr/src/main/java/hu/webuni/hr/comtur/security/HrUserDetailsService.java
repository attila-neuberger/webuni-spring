package hu.webuni.hr.comtur.security;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.repository.EmployeeRepository;

@Service
public class HrUserDetailsService implements UserDetailsService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRepository
				.findByUserName(username)
				.orElseThrow(new Supplier<UsernameNotFoundException>() {

					@Override
					public UsernameNotFoundException get() {
						return new UsernameNotFoundException(username);
					}
				});
		return new ExtendedUserPrincipal(employee, new ArrayList<GrantedAuthority>());
	}
}
