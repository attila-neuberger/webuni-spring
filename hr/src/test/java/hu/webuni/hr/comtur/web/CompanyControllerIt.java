package hu.webuni.hr.comtur.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.comtur.dto.CompanyDto;
import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.model.Education;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CompanyControllerIt {

	@Autowired
	WebTestClient webTestClient;
	
	@BeforeEach
	public void init() {
		// No cleanup needed.
	}
	
	private final static String BASE_URI = "/api/companies";
	
	@Test
	void testCreateEmployeeForCompany() throws Exception {
		List<CompanyDto> companiesBefore = getAllCompanies();
		if (!companiesBefore.isEmpty()) {
			CompanyDto company = companiesBefore.get(0);
			EmployeeDto employee = new EmployeeDto(55L, "Name 55", new PositionDto("Position 55", Education.PHD), 
					555, LocalDateTime.of(2015, 5, 5, 5, 5, 5));
			createEmployeeForCompany(employee, company.getId());
			List<CompanyDto> companiesAfter = getAllCompanies();
			
			assertThat(companiesAfter.get(0).getEmployees().size()).isEqualTo(companiesBefore.get(0).getEmployees().size() + 1);
			assertThat(companiesAfter.get(0).getEmployees().contains(employee));
		}
	}
	
	/**
	 * Gets sorted company list (GET to base URI, body is list of {@link CompanyDto}s).
	 * @return List of companies.
	 */
	private List<CompanyDto> getAllCompanies() {
		List<CompanyDto> responseList = webTestClient
				.get()
				.uri(String.format("%s?full=true", BASE_URI))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(CompanyDto.class)
				.returnResult()
				.getResponseBody();
		Collections.sort(responseList, new Comparator<CompanyDto>() {

			@Override
			public int compare(CompanyDto c1, CompanyDto c2) {
				return (int)(c1.getId() - c2.getId());
			}
		});
		return responseList;
	}
	
	/**
	 * Creates an employee (POST to base URI, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to create.
	 * @param id          ID of the company.
	 */
	private void createEmployeeForCompany(EmployeeDto employeeDto, long id) {
		webTestClient
				.post()
				.uri(String.format("%s/%d/add", BASE_URI, id))
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	@Test
	void testDeleteEmployeeFromCompany() throws Exception {
		List<CompanyDto> companiesBefore = getAllCompanies();
		if (!companiesBefore.isEmpty()) {
			CompanyDto company = companiesBefore.get(0);
			String employeeName = "Name 56";
			EmployeeDto employee = new EmployeeDto(56L, employeeName, new PositionDto("Position 56", Education.PHD), 
					556, LocalDateTime.of(2015, 6, 5, 6, 5, 6));
			createEmployeeForCompany(employee, company.getId());
			List<CompanyDto> companiesAfterInsert = getAllCompanies();
			long employeeId = employee.getId();
			for (EmployeeDto emp : companiesAfterInsert.get(0).getEmployees()) {
				if (emp.getName().equals(employeeName)) {
					employeeId = emp.getId();
				}
			}
			
			assertThat(companiesAfterInsert.get(0).getEmployees().size()).isEqualTo(companiesBefore.get(0).getEmployees().size() + 1);
			assertThat(companiesAfterInsert.get(0).getEmployees().contains(employee));
			
			deleteEmployeeFromCompany(company.getId(), employeeId);
			List<CompanyDto> companiesAfterDelete = getAllCompanies();
			
			assertThat(companiesAfterDelete.get(0).getEmployees().size()).isEqualTo(companiesAfterInsert.get(0).getEmployees().size() - 1);
			assertThat(!companiesAfterDelete.get(0).getEmployees().contains(employee));
			assertThat(companiesAfterDelete)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(companiesBefore);
			assertThat(employee.getCompany()).isNull();
		}
	}
	
	/**
	 * Deletes an employee from a company (DELETE to base URI + /companyID/delete/employeeID).
	 * @param companyId  ID of the company.
	 * @param employeeId ID of the employee.
	 */
	private void deleteEmployeeFromCompany(long companyId, long employeeId) {
		webTestClient
				.delete()
				.uri(String.format("%s/%d/delete/%d", BASE_URI, companyId, employeeId))
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	@Test
	void testSwapEmployeesOfCompany() throws Exception {
		List<CompanyDto> companiesBefore = getAllCompanies();
		if (!companiesBefore.isEmpty()) {
			CompanyDto company = companiesBefore.get(0);
			// List<EmployeeDto> employeesOriginal = company.getEmployees();
			EmployeeDto employee = new EmployeeDto(57L, "Name 57", new PositionDto("Position 57", Education.PHD), 
					557, LocalDateTime.of(2015, 7, 5, 7, 5, 7));
			List<EmployeeDto> employeesNew = new ArrayList<>(1);
			employeesNew.add(employee);
			swapEmployeesOfCompany(employeesNew, company.getId());
			List<CompanyDto> companiesAfterSwap = getAllCompanies();
			
			assertThat(companiesAfterSwap.get(0).getEmployees().size()).isEqualTo(employeesNew.size());
			assertThat(companiesAfterSwap.get(0).getEmployees().get(0))
				.usingRecursiveComparison()
				.ignoringFields("id")
				.ignoringFields("title.id")
				.isEqualTo(employee);
		}
	}
	
	/**
	 * Swaps list of employees of a company (PUT to base URI + /companyID/swap).
	 * @param employeeDtos List of new employees.
	 * @param id           ID of the company.
	 */
	private void swapEmployeesOfCompany(List<EmployeeDto> employeeDtos, long id) {
		webTestClient
				.put()
				.uri(String.format("%s/%d/swap", BASE_URI, id))
				.bodyValue(employeeDtos)
				.exchange()
				.expectStatus()
				.isOk();
	}
}
