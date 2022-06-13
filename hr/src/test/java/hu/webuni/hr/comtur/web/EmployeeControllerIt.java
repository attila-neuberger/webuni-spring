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
import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.model.Education;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIt {

	@Autowired
	WebTestClient webTestClient;
	
	private final static String BASE_URI = "/api/employees";
	
	@Test
	void testThatCreatedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto employeeDto = new EmployeeDto(9L, "Name 9", new PositionDto("Position 9", Education.NONE, 100), 
				999, LocalDateTime.of(2018, 5, 7, 20, 50, 50));
		createEmployee(employeeDto);
		List<EmployeeDto> employeesAfter = getAllEmployees();
		
		assertThat(employeesAfter.subList(0, employeesBefore.size()))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1))
				.usingRecursiveComparison()
				.ignoringFields("id")
				.ignoringFields("title.id")
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
		EmployeeDto employeeWithEmptyName = new EmployeeDto(11L, "", new PositionDto("Position 11", Education.NONE, 100), 
				999, LocalDateTime.now());
		// EmployeeDto employeeWithEmptyPosition = new EmployeeDto(12L, "Name 12", null, 999, LocalDateTime.now());
		EmployeeDto employeeWithNegativeSalary = new EmployeeDto(13L, "Name 13", 
				new PositionDto("Position 13", Education.NONE, 100), -999, LocalDateTime.now());
		EmployeeDto employeeWithFutureStartDate = new EmployeeDto(14L, "Name 14", 
				new PositionDto("Position 14", Education.NONE, 100), 999, LocalDateTime.now().plusDays(1));
		createInvalidEmployee(employeeWithEmptyName);
		// createInvalidEmployee(employeeWithEmptyPosition);
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
			System.out.println("TestThatModifiedEmployeeIsListed employeeDto: " + employeeDto.getName());
			EmployeeDto employeeDtoSavedState = new EmployeeDto(employeeDto.getId(), 
					employeeDto.getName(), employeeDto.getTitle(), employeeDto.getSalary(), employeeDto.getStartDate());
			employeeDto.setName(employeeDto.getName() + " (modified)");
			PositionDto modifiedPositionDto = new PositionDto(employeeDto.getTitle().getName() + " (modified)", 
					employeeDto.getTitle().getMinEducation(), employeeDto.getTitle().getMinSalary());
			modifiedPositionDto.setId(employeeDto.getTitle().getId());
			employeeDto.setTitle(modifiedPositionDto);
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
			assertThat(employeesAfter.get(0).getTitle().getName()).isEqualTo(employeeDtoSavedState.getTitle().getName() + " (modified)");
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
		createEmployee(new EmployeeDto(123L, "Invalid test", new PositionDto("Position of invalid", Education.NONE, 100), 
				1000, LocalDateTime.now()));
		List<EmployeeDto> employeesBefore = getAllEmployees();
		if (!employeesBefore.isEmpty()) {
			EmployeeDto employeeDto = employeesBefore.get(employeesBefore.size() - 1);
			EmployeeDto employeeDtoSavedState = new EmployeeDto(employeeDto.getId(), 
					employeeDto.getName(), employeeDto.getTitle(), employeeDto.getSalary(), employeeDto.getStartDate());
			employeeDto.setName("");
			modifyInvalidEmployee(employeeDto);
			employeeDto.setName(employeeDtoSavedState.getName());
			// employeeDto.setTitle(new PositionDto("", Education.NONE, 50)); // TODO Position validity test.
			// modifyInvalidEmployee(employeeDto);
			// employeeDto.setTitle(employeeDtoSavedState.getTitle());
			employeeDto.setSalary(-1);
			modifyInvalidEmployee(employeeDto);
			employeeDto.setSalary(employeeDtoSavedState.getSalary());
			employeeDto.setStartDate(LocalDateTime.now().plusDays(1));
			modifyInvalidEmployee(employeeDto);
			List<EmployeeDto> employeesAfter = getAllEmployees();
			
			assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
			assertThat(employeesAfter).containsExactlyElementsOf(employeesBefore);
			assertThat(employeesAfter.get(employeesBefore.size() - 1)).isEqualTo(employeeDto);
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
