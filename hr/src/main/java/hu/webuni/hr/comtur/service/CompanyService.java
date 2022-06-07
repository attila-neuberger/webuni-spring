package hu.webuni.hr.comtur.service;

import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.repository.CompanyRepository;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class CompanyService extends BaseService<Company> {

	public CompanyService(CompanyRepository companyRepository) {
		super();
		this.repository = companyRepository;
	}
	
	@Override
	public Company save(Company company) {
		for (Employee employee : company.getEmployees()) {
			employee.setCompany(company);
		}
		return repository.save(company);
	}
}
