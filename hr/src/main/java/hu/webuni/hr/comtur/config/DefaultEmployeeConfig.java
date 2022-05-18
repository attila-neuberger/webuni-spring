package hu.webuni.hr.comtur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.comtur.service.DefaultEmployeeService;
import hu.webuni.hr.comtur.service.EmployeeService;

@Configuration
@Profile("!smart")
public class DefaultEmployeeConfig {

	@Bean
	public EmployeeService employeeService() {
		return new DefaultEmployeeService();
	}
}
