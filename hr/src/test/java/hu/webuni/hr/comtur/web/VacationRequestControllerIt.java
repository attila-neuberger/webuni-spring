package hu.webuni.hr.comtur.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.dto.VacationRequestDto;
import hu.webuni.hr.comtur.model.Education;
import hu.webuni.hr.comtur.model.VacationRequestStatus;
import hu.webuni.hr.comtur.repository.VacationRequestRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class VacationRequestControllerIt {

	@Autowired
	private WebTestClient webTestClient;
	
	@Autowired
	private VacationRequestRepository vacationRequestRepository;
	
	private final static String BASE_URI_EMPLOYEE = "/api/employees";
	private final static String BASE_URI_VACATION_REQUEST = "/api/vacationrequests";
	
	private EmployeeDto[] employees;
	
	private Comparator<VacationRequestDto> requestIdComparator = new Comparator<>() {

		@Override
		public int compare(VacationRequestDto vr1, VacationRequestDto vr2) {
			return (int)(vr1.getId() - vr2.getId());
		}
	};
	
	@BeforeAll
	public void beforeEach() {
		EmployeeDto[] employeesRaw = new EmployeeDto[] {
				new EmployeeDto(1001L, "Employee 1", new PositionDto("Test position", Education.NONE), 
						1001, LocalDateTime.of(2021, 1, 21, 21, 1, 1)),
				new EmployeeDto(1002L, "Employee 2", new PositionDto("Test position", Education.NONE), 
						1002, LocalDateTime.of(2022, 1, 22, 22, 2, 2)),
				new EmployeeDto(1003L, "Employee 3", new PositionDto("Test position", Education.NONE), 
						1003, LocalDateTime.of(2013, 1, 23, 23, 3, 3))
		};
		employees = new EmployeeDto[employeesRaw.length];
		for (int i = 0; i < employeesRaw.length; ++i) {
			employees[i] = createEmployee(employeesRaw[i]);
		}
	}
	
	@AfterEach
	public void clean() {
		vacationRequestRepository.deleteAll();
	}
	
	/**
	 * Creates an employee (POST to base URI, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to create.
	 * @return Created employee.
	 */
	private EmployeeDto createEmployee(EmployeeDto employeeDto) {
		return webTestClient
				.post()
				.uri(BASE_URI_EMPLOYEE)
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	@Test
	void testCreateRequest() throws Exception {
		LocalDate now = LocalDate.now();
		List<VacationRequestDto> requestsBefore = getAllVacationRequests();
		Collections.sort(requestsBefore, requestIdComparator);
		VacationRequestDto requestRaw = new VacationRequestDto(now.plusDays(8), now.plusDays(12));
		VacationRequestDto request = createVacationRequest(requestRaw, employees[0].getId());
		List<VacationRequestDto> requestsAfter = getAllVacationRequests();
		Collections.sort(requestsAfter, requestIdComparator);
		
		assertThat(requestsAfter.subList(0, requestsBefore.size()))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(requestsBefore);
		assertThat(requestsAfter.get(requestsAfter.size() - 1))
				.usingRecursiveComparison()
				.ignoringFields("tsCreated")
				.isEqualTo(request);
		assertThat(requestsAfter.get(requestsAfter.size() - 1).getTsCreated()).isCloseTo(request.getTsCreated(), within(1, ChronoUnit.MICROS));
	}
	
	/**
	 * Gets sorted vacation request list (GET to base URI, body is list of {@link VacationRequestDto}s).
	 * @return List of vacation requests.
	 */
	private List<VacationRequestDto> getAllVacationRequests() {
		List<VacationRequestDto> responseList = webTestClient
				.get()
				.uri(BASE_URI_VACATION_REQUEST)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(VacationRequestDto.class)
				.returnResult()
				.getResponseBody();
		Collections.sort(responseList, new Comparator<VacationRequestDto>() {

			@Override
			public int compare(VacationRequestDto vr1, VacationRequestDto vr2) {
				return (int)(vr1.getId() - vr2.getId());
			}
		});
		return responseList;
	}
	
	/**
	 * Creates a vacation request (POST to base URI + ID of the requester).
	 * @param request     Request object.
	 * @param requesterId ID of the requester.
	 * @return Created request.
	 */
	private VacationRequestDto createVacationRequest(VacationRequestDto requestDto, long requesterId) {
		return webTestClient
				.post()
				.uri(String.format("%s/%d", BASE_URI_VACATION_REQUEST, requesterId))
				.bodyValue(requestDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(VacationRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	@Test
	void testCreateInvalidRequest() throws Exception {
		LocalDate now = LocalDate.now();
		List<VacationRequestDto> requestsBefore = getAllVacationRequests();
		VacationRequestDto requestInvalidRaw1 = new VacationRequestDto(now.minusDays(2), now.plusDays(2));
		createInvalidVacationRequest(requestInvalidRaw1, employees[0].getId());
		VacationRequestDto requestInvalidRaw2 = new VacationRequestDto(now.plusDays(2), null);
		createInvalidVacationRequest(requestInvalidRaw2, employees[0].getId());
		List<VacationRequestDto> requestsAfter = getAllVacationRequests();
		
		assertThat(requestsAfter)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(requestsBefore);
	}
	
	/**
	 * Creates a invalid vacation request (POST to base URI + ID of the requester).
	 * @param request     Request object.
	 * @param requesterId ID of the requester.
	 */
	private void createInvalidVacationRequest(VacationRequestDto requestDto, long requesterId) {
		webTestClient
				.post()
				.uri(String.format("%s/%d", BASE_URI_VACATION_REQUEST, requesterId))
				.bodyValue(requestDto)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}
	
	@Test
	void testApproveRequest() throws Exception {
		testApproveOrRefuseRequest("approve", VacationRequestStatus.APPROVED);
	}
	
	/**
	 * Tests approve or refuse request (merged method).
	 * @param action         Action (approve / refuse).
	 * @param expectedStatus Expected request status after the action.
	 */
	private void testApproveOrRefuseRequest(String action, VacationRequestStatus expectedStatus) {
		LocalDate now = LocalDate.now();
		VacationRequestDto requestRaw = new VacationRequestDto(now.plusDays(8), now.plusDays(12));
		VacationRequestDto request = createVacationRequest(requestRaw, employees[0].getId());
		List<VacationRequestDto> requestsBefore = getAllVacationRequests();
		Collections.sort(requestsBefore, requestIdComparator);
		approveOrRefuseRequest(employees[1].getId(), action, request.getId());
		List<VacationRequestDto> requestsAfter = getAllVacationRequests();
		Collections.sort(requestsAfter, requestIdComparator);
		
		assertThat(requestsAfter.subList(0, requestsAfter.size() - 1))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(requestsBefore.subList(0, requestsBefore.size() - 1));
		assertThat(requestsAfter.get(requestsAfter.size() - 1))
				.usingRecursiveComparison()
				.ignoringFields("tsCreated")
				.ignoringFields("approver")
				.ignoringFields("vacationRequestStatus")
				.isEqualTo(request);
		assertThat(requestsAfter.get(requestsAfter.size() - 1).getTsCreated()).isCloseTo(request.getTsCreated(), within(1, ChronoUnit.MICROS));
		assertThat(requestsBefore.get(requestsBefore.size() - 1).getApprover()).isNull();
		assertThat(requestsAfter.get(requestsAfter.size() - 1).getApprover()).isNotNull();
		assertThat(requestsBefore.get(requestsBefore.size() - 1).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.CREATED);
		assertThat(requestsAfter.get(requestsAfter.size() - 1).getVacationRequestStatus()).isEqualTo(expectedStatus);
	}
	
	/**
	 * Approves or refuses a request (GET to base URI + ID of the approver + action + ID of the request).
	 * @param approverId ID of the approver employee.
	 * @param action     Action of the operation (approve / refuse).
	 * @param requestId ID of the request.
	 */
	private void approveOrRefuseRequest(long approverId, String action, long requestId) {
		webTestClient
				.get()
				.uri(String.format("%s/%d/%s/%d", BASE_URI_VACATION_REQUEST, approverId, action, requestId))
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	@Test
	void testRefuseRequest() throws Exception {
		testApproveOrRefuseRequest("refuse", VacationRequestStatus.REFUSED);
	}
	
	@Test
	void testInvalidApproveRequest() throws Exception {
		LocalDate now = LocalDate.now();
		VacationRequestDto requestRaw = new VacationRequestDto(now.plusDays(8), now.plusDays(12));
		VacationRequestDto request = createVacationRequest(requestRaw, employees[0].getId());
		approveOrRefuseRequest(employees[1].getId(), "approve", request.getId());
		List<VacationRequestDto> requestsBefore = getAllVacationRequests();
		Collections.sort(requestsBefore, requestIdComparator);
		approveOrRefuseInvalidRequest(employees[1].getId(), "approve", request.getId());
		approveOrRefuseInvalidRequest(employees[2].getId(), "refuse", request.getId());
		List<VacationRequestDto> requestsAfter = getAllVacationRequests();
		Collections.sort(requestsAfter, requestIdComparator);
		
		assertThat(requestsAfter)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(requestsBefore);
	}
	
	/**
	 * Approves or refuses a request (GET to base URI + ID of the approver + action + ID of the request).
	 * The action is invalid on the request.
	 * @param approverId ID of the approver employee.
	 * @param action     Action of the operation (approve / refuse).
	 * @param requestId ID of the request.
	 */
	private void approveOrRefuseInvalidRequest(long approverId, String action, long requestId) {
		webTestClient
				.get()
				.uri(String.format("%s/%d/%s/%d", BASE_URI_VACATION_REQUEST, approverId, action, requestId))
				.exchange()
				.expectStatus()
				.is5xxServerError();
	}
	
	/*@Test
	void testThatModifiedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		if (!employeesBefore.isEmpty()) {
			EmployeeDto employeeDto = employeesBefore.get(0);
			System.out.println("TestThatModifiedEmployeeIsListed employeeDto: " + employeeDto.getName());
			EmployeeDto employeeDtoSavedState = new EmployeeDto(employeeDto.getId(), 
					employeeDto.getName(), employeeDto.getTitle(), employeeDto.getSalary(), employeeDto.getStartDate());
			employeeDto.setName(employeeDto.getName() + " (modified)");
			PositionDto modifiedPositionDto = new PositionDto(employeeDto.getTitle().getName() + " (modified)", 
					employeeDto.getTitle().getMinEducation());
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
	}*/
	
	/**
	 * Modifies an employee (PUT to base URI + /ID, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to modify.
	 */
	/*private void modifyEmployee(EmployeeDto employeeDto) {
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
		createEmployee(new EmployeeDto(123L, "Invalid test", new PositionDto("Position of invalid", Education.NONE), 
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
	}*/
	
	/**
	 * Modifies an invalid employee (PUT to base URI + /ID, body is an {@link EmployeeDto}).
	 * @param employeeDto Employee to modify.
	 */
	/*private void modifyInvalidEmployee(EmployeeDto employeeDto) {
		webTestClient
				.put()
				.uri(String.format("%s/%d", BASE_URI, employeeDto.getId()))
				.bodyValue(employeeDto)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}*/
}
