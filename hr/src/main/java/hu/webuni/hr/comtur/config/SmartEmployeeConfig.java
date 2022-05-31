package hu.webuni.hr.comtur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.comtur.service.IEmployeeService;
import hu.webuni.hr.comtur.service.SmartEmployeeService;

@Configuration
@Profile("smart")
public class SmartEmployeeConfig {

	@Bean
	@Primary
	public IEmployeeService iEmployeeService() {
		return new SmartEmployeeService();
	}
}
