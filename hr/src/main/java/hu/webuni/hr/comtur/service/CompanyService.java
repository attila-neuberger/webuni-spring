package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
public class CompanyService extends BaseService<Company> {
	
	{
		List<Employee> employeesOfCompany1 = new ArrayList<>(2);
		employeesOfCompany1.add(new Employee(8L, "Bea", "Szoftverfejlesztő", 1100, LocalDateTime.of(2018, 10, 20, 0, 0)));
		employeesOfCompany1.add(new Employee(9L, "Ákos", "Szoftverfejlesztő", 1010, LocalDateTime.of(2021, 12, 1, 0, 0)));
		entities.put(1L, new Company(1L, "Cég 1", "1111 Bp, Fő u. 1.", employeesOfCompany1));
		entities.put(2L, new Company(2L, "Cég 2", "2112 Veresegyház, Mellék u. 2.", null));
	}
}
