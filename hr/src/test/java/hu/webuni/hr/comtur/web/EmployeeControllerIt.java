package hu.webuni.hr.comtur.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.comtur.dto.EmployeeDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIt {

	@Autowired
	WebTestClient webTestClient;
	
	private final static String BASE_URI = "/api/employees";
	
	@Test
	void testThatCreatedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto employeeDto = new EmployeeDto(9L, "Name 9", "Position 9", 999, LocalDateTime.now());
		createEmployee(employeeDto);
		List<EmployeeDto> employeesAfter = getAllEmployees();
		
		assertThat(employeesAfter.subList(0, employeesBefore.size()))
				// .usingRecursiveFieldByFieldElementComparator() // Not needed because equals and hashCode are overridden.
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1))
				// .usingRecursiveComparison() // Not needed because equals and hashCode are overridden.
				.isEqualTo(employeeDto);
	}
	
	/**
	 * Gets sorted employee list (GET to base URI, body is list of {@link EmployeeDto}s).
	 * @return List of employees.
	 */
	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> responseList = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
		Collections.sort(responseList); // List is Comparable based on ID.
		return responseList;
	}
	
	/**
	 * Creates an employee (POST to base URI, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to create.
	 */
	private void createEmployee(EmployeeDto employeeDto) {
		webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	@Test
	void testThatCreatedEmployeeValidationFails() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto employeeWithEmptyName = new EmployeeDto(11L, "", "Position 11", 999, LocalDateTime.now());
		EmployeeDto employeeWithEmptyPosition = new EmployeeDto(12L, "Name 12", "", 999, LocalDateTime.now());
		EmployeeDto employeeWithNegativeSalary = new EmployeeDto(13L, "Name 13", "Position 13", -999, LocalDateTime.now());
		EmployeeDto employeeWithFutureStartDate = new EmployeeDto(14L, "Name 14", "Position 14", 999, LocalDateTime.now().plusDays(1));
		createInvalidEmployee(employeeWithEmptyName);
		createInvalidEmployee(employeeWithEmptyPosition);
		createInvalidEmployee(employeeWithNegativeSalary);
		createInvalidEmployee(employeeWithFutureStartDate);
		List<EmployeeDto> employeesAfter = getAllEmployees();
		
		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
		assertThat(employeesAfter)
				.containsExactlyElementsOf(employeesBefore);
	}
	
	/**
	 * Creates an employee (POST to base URI, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to create.
	 */
	private void createInvalidEmployee(EmployeeDto employeeDto) {
		webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}
	
	@Test
	void testThatModifiedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		if (!employeesBefore.isEmpty()) {
			EmployeeDto employeeDto = employeesBefore.get(0);
			EmployeeDto employeeDtoSavedState = new EmployeeDto(employeeDto.getId(), 
					employeeDto.getName(), employeeDto.getPosition(), employeeDto.getSalary(), employeeDto.getStartDate());
			employeeDto.setName(employeeDto.getName() + " (modified)");
			employeeDto.setPosition(employeeDto.getPosition() + " (modified)");
			employeeDto.setSalary(employeeDto.getSalary() + 1);
			employeeDto.setStartDate(employeeDto.getStartDate().minusYears(1));
			modifyEmployee(employeeDto);
			List<EmployeeDto> employeesAfter = getAllEmployees();
			
			assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
			if (employeesBefore.size() > 0) {
				assertThat(employeesAfter.subList(1, employeesBefore.size()))
						.containsExactlyElementsOf(employeesAfter.subList(1, employeesAfter.size()));
			}
			assertThat(employeesAfter.get(0)).isEqualTo(employeeDto);
			assertThat(employeesAfter.get(0).getId()).isEqualTo(employeeDtoSavedState.getId());
			assertThat(employeesAfter.get(0).getName()).isEqualTo(employeeDtoSavedState.getName() + " (modified)");
			assertThat(employeesAfter.get(0).getPosition()).isEqualTo(employeeDtoSavedState.getPosition() + " (modified)");
			assertThat(employeesAfter.get(0).getSalary()).isEqualTo(employeeDtoSavedState.getSalary() + 1);
			assertThat(employeesAfter.get(0).getStartDate()).isEqualTo(employeeDtoSavedState.getStartDate().minusYears(1));
		}
	}
	
	/**
	 * Modifies an employee (PUT to base URI + /ID, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to modify.
	 */
	private void modifyEmployee(EmployeeDto employeeDto) {
		webTestClient
				.put()
				.uri(String.format("%s/%d", BASE_URI, employeeDto.getId()))
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	@Test
	void testThatModifiedEmployeeValidationFails() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		if (!employeesBefore.isEmpty()) {
			EmployeeDto employeeDto = employeesBefore.get(0);
			EmployeeDto employeeDtoSavedState = new EmployeeDto(employeeDto.getId(), 
					employeeDto.getName(), employeeDto.getPosition(), employeeDto.getSalary(), employeeDto.getStartDate());
			employeeDto.setName("");
			modifyInvalidEmployee(employeeDto);
			employeeDto.setName(employeeDtoSavedState.getName());
			employeeDto.setPosition("");
			modifyInvalidEmployee(employeeDto);
			employeeDto.setPosition(employeeDtoSavedState.getPosition());
			employeeDto.setSalary(-1);
			modifyInvalidEmployee(employeeDto);
			employeeDto.setSalary(employeeDtoSavedState.getSalary());
			employeeDto.setStartDate(LocalDateTime.now().plusDays(1));
			modifyInvalidEmployee(employeeDto);
			List<EmployeeDto> employeesAfter = getAllEmployees();
			
			assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
			assertThat(employeesAfter).containsExactlyElementsOf(employeesBefore);
			assertThat(employeesAfter.get(0)).isEqualTo(employeeDto);
		}
	}
	
	/**
	 * Modifies an invalid employee (PUT to base URI + /ID, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to modify.
	 */
	private void modifyInvalidEmployee(EmployeeDto employeeDto) {
		webTestClient
				.put()
				.uri(String.format("%s/%d", BASE_URI, employeeDto.getId()))
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}
}
