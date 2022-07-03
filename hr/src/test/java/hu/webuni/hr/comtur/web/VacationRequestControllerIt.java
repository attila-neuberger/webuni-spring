package hu.webuni.hr.comtur.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	 * Gets sorted vacation request list (GET to base URI).
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
	
	// Modifying vacation requests tested via Postman.
	
	// Deleting vacation requests tested via Postman.
		
	@Test
	void testVacationRequestFiltering() throws Exception {
		LocalDate now = LocalDate.now();

		VacationRequestDto requestRaw1 = new VacationRequestDto(now.plusDays(8), now.plusDays(12));
		VacationRequestDto request1 = createVacationRequest(requestRaw1, employees[0].getId());
		VacationRequestDto requestRaw2 = new VacationRequestDto(now.plusDays(20), now.plusDays(20));
		@SuppressWarnings("unused")
		VacationRequestDto request2 = createVacationRequest(requestRaw2, employees[0].getId());
		VacationRequestDto requestRaw3 = new VacationRequestDto(now.plusDays(10), now.plusDays(16));
		VacationRequestDto request3 = createVacationRequest(requestRaw3, employees[1].getId());
		VacationRequestDto requestRaw4 = new VacationRequestDto(now.plusDays(18), now.plusDays(19));
		VacationRequestDto request4 = createVacationRequest(requestRaw4, employees[1].getId());
		approveOrRefuseRequest(employees[2].getId(), "approve", request1.getId());
		approveOrRefuseRequest(employees[2].getId(), "approve", request3.getId());
		approveOrRefuseRequest(employees[2].getId(), "approve", request4.getId());
		
		List<VacationRequestDto> originalList = getAllVacationRequests();
		
		VacationRequestDto emptyVacationRequestDto = new VacationRequestDto();
		emptyVacationRequestDto.setVacationRequestStatus(null);
		
		List<VacationRequestDto> listPage1 = getFoundLocationRequests(0, 20, null, null, null, null, null, emptyVacationRequestDto);
		for (int i = 0; i < listPage1.size(); ++i) {
			assertThat(listPage1.get(i))
				.usingRecursiveComparison()
				.ignoringFields("tsCreated")
				.isEqualTo(originalList.get(i));
			assertThat(listPage1.get(i).getTsCreated()).isCloseTo(originalList.get(i).getTsCreated(), within(1, ChronoUnit.MICROS));
		}
		
		List<VacationRequestDto> listPage2 = getFoundLocationRequests(0, 3, null, null, null, null, null, emptyVacationRequestDto);
		for (int i = 0; i < listPage2.size(); ++i) {
			assertThat(listPage2.get(i))
				.usingRecursiveComparison()
				.ignoringFields("tsCreated")
				.isEqualTo(originalList.get(i));
			assertThat(listPage2.get(i).getTsCreated()).isCloseTo(originalList.get(i).getTsCreated(), within(1, ChronoUnit.MICROS));
		}
		assertThat(listPage2.size()).isEqualTo(3);
		
		List<VacationRequestDto> listPage3 = getFoundLocationRequests(1, 3, null, null, null, null, null, emptyVacationRequestDto);
		for (int i = 0; i < listPage3.size(); ++i) {
			assertThat(listPage3.get(i))
				.usingRecursiveComparison()
				.ignoringFields("tsCreated")
				.isEqualTo(originalList.get(i + 3));
			assertThat(listPage3.get(i).getTsCreated()).isCloseTo(originalList.get(i + 3).getTsCreated(), within(1, ChronoUnit.MICROS));
		}
		assertThat(listPage3.size()).isEqualTo(1);
		
		List<VacationRequestDto> listSort1 = getFoundLocationRequests(null, null, "vacationRequestStatus",
				null, null, null, null, emptyVacationRequestDto);
		assertThat(listSort1.get(0).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.CREATED);
		assertThat(listSort1.get(1).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort1.get(2).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort1.get(3).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		
		List<VacationRequestDto> listSort2 = getFoundLocationRequests(null, null, "vacationRequestStatus,desc",
				null, null, null, null, emptyVacationRequestDto);
		assertThat(listSort2.get(0).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort2.get(1).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort2.get(2).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort2.get(3).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.CREATED);
		
		List<VacationRequestDto> listSort3 = getFoundLocationRequests(null, null, "vacationRequestStatus,desc&sort=requester.id",
				null, null, null, null, emptyVacationRequestDto);
		assertThat(listSort3.get(0).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort3.get(0).getRequester()).isEqualTo(employees[0]);
		assertThat(listSort3.get(1).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort3.get(1).getRequester()).isEqualTo(employees[1]);
		assertThat(listSort3.get(2).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort3.get(2).getRequester()).isEqualTo(employees[1]);
		assertThat(listSort3.get(3).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.CREATED);
		assertThat(listSort3.get(3).getRequester()).isEqualTo(employees[0]);
		
		List<VacationRequestDto> listSort4 = getFoundLocationRequests(null, null, "vacationRequestStatus,desc&sort=requester.id,desc",
				null, null, null, null, emptyVacationRequestDto);
		assertThat(listSort4.get(0).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort4.get(0).getRequester()).isEqualTo(employees[1]);
		assertThat(listSort4.get(1).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort4.get(1).getRequester()).isEqualTo(employees[1]);
		assertThat(listSort4.get(2).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.APPROVED);
		assertThat(listSort4.get(2).getRequester()).isEqualTo(employees[0]);
		assertThat(listSort4.get(3).getVacationRequestStatus()).isEqualTo(VacationRequestStatus.CREATED);
		assertThat(listSort4.get(3).getRequester()).isEqualTo(employees[0]);
		
		List<VacationRequestDto> listRequestedInterval_6_7 = getFoundLocationRequests(null, null, null,
				now.plusDays(6), now.plusDays(7), null, null, emptyVacationRequestDto);
		assertThat(listRequestedInterval_6_7).isEmpty();
		
		List<VacationRequestDto> listRequestedInterval_6_8 = getFoundLocationRequests(null, null, null,
				now.plusDays(6), now.plusDays(8), null, null, emptyVacationRequestDto);
		assertThat(listRequestedInterval_6_8.size()).isEqualTo(1);
		assertThat(listRequestedInterval_6_8.get(0)).isEqualTo(originalList.get(0));
		
		List<VacationRequestDto> listRequestedInterval_6_9 = getFoundLocationRequests(null, null, null,
				now.plusDays(6), now.plusDays(9), null, null, emptyVacationRequestDto);
		assertThat(listRequestedInterval_6_9.size()).isEqualTo(1);
		assertThat(listRequestedInterval_6_9.get(0)).isEqualTo(originalList.get(0));
		
		List<VacationRequestDto> listRequestedInterval_6_10 = getFoundLocationRequests(null, null, null,
				now.plusDays(6), now.plusDays(10), null, null, emptyVacationRequestDto);
		assertThat(listRequestedInterval_6_10.size()).isEqualTo(2);
		assertThat(listRequestedInterval_6_10.get(0)).isEqualTo(originalList.get(0));
		assertThat(listRequestedInterval_6_10.get(1)).isEqualTo(originalList.get(2));
		
		List<VacationRequestDto> listRequestedInterval_11_19 = getFoundLocationRequests(null, null, null,
				now.plusDays(11), now.plusDays(19), null, null, emptyVacationRequestDto);
		assertThat(listRequestedInterval_11_19.size()).isEqualTo(3);
		assertThat(listRequestedInterval_11_19.get(0)).isEqualTo(originalList.get(0));
		assertThat(listRequestedInterval_11_19.get(1)).isEqualTo(originalList.get(2));
		assertThat(listRequestedInterval_11_19.get(2)).isEqualTo(originalList.get(3));
		
		List<VacationRequestDto> listRequestedInterval_15_20 = getFoundLocationRequests(null, null, null,
				now.plusDays(15), now.plusDays(20), null, null, emptyVacationRequestDto);
		assertThat(listRequestedInterval_15_20.size()).isEqualTo(3);
		assertThat(listRequestedInterval_15_20.get(0)).isEqualTo(originalList.get(1));
		assertThat(listRequestedInterval_15_20.get(1)).isEqualTo(originalList.get(2));
		assertThat(listRequestedInterval_15_20.get(2)).isEqualTo(originalList.get(3));
		
		VacationRequestDto approvedVacationRequestDto = new VacationRequestDto();
		approvedVacationRequestDto.setVacationRequestStatus(VacationRequestStatus.APPROVED);
		List<VacationRequestDto> listApprovedRequests = getFoundLocationRequests(null, null, null,
				null, null, null, null, approvedVacationRequestDto);
		assertThat(listApprovedRequests.size()).isEqualTo(3);
		assertThat(listApprovedRequests.get(0)).isEqualTo(originalList.get(0));
		assertThat(listApprovedRequests.get(1)).isEqualTo(originalList.get(2));
		assertThat(listApprovedRequests.get(2)).isEqualTo(originalList.get(3));
		
		VacationRequestDto createdVacationRequestDto = new VacationRequestDto();
		createdVacationRequestDto.setVacationRequestStatus(VacationRequestStatus.CREATED);
		List<VacationRequestDto> listCreatedRequests = getFoundLocationRequests(null, null, null,
				null, null, null, null, createdVacationRequestDto);
		assertThat(listCreatedRequests.size()).isEqualTo(1);
		assertThat(listCreatedRequests.get(0)).isEqualTo(originalList.get(1));
		
		VacationRequestDto emp1VacationRequestDto = new VacationRequestDto();
		emp1VacationRequestDto.setVacationRequestStatus(null);
		emp1VacationRequestDto.setRequester(employees[1]);
		
		List<VacationRequestDto> emp1Requests = getFoundLocationRequests(null, null, null,
				null, null, null, null, emp1VacationRequestDto);
		assertThat(emp1Requests.size()).isEqualTo(2);
		assertThat(emp1Requests.get(0)).isEqualTo(originalList.get(2));
		assertThat(emp1Requests.get(1)).isEqualTo(originalList.get(3));
		
		VacationRequestDto emp1ApprovedVacationRequestDto = new VacationRequestDto();
		emp1ApprovedVacationRequestDto.setVacationRequestStatus(VacationRequestStatus.APPROVED);
		emp1ApprovedVacationRequestDto.setRequester(employees[1]);
		List<VacationRequestDto> emp1ApprovedRequestsMultiple_12_16 = getFoundLocationRequests(0, 2, null,
				now.plusDays(12), now.plusDays(16), null, null, emp1ApprovedVacationRequestDto);
		assertThat(emp1ApprovedRequestsMultiple_12_16.size()).isEqualTo(1);
		assertThat(emp1ApprovedRequestsMultiple_12_16.get(0)).isEqualTo(originalList.get(2));
	}
	
	/**
	 * Gets filtered, paged, sorted vacation requests from example (POST to extended URI, body is list of {@link VacationRequestDto}s).
	 * @param page               Page page value.
	 * @param size               Page size value.
	 * @param sort               Page sort expression.
	 * @param requestedFrom      Optional requested from value.
	 * @param requestedTo        Optional requested to value.
	 * @param createdFrom        Optional created from value.
	 * @param createdTo          Optional created to value.
	 * @param vacationRequestDto Vacation request example.
	 * @return List of vacation requests.
	 */
	private List<VacationRequestDto> getFoundLocationRequests(Integer page, Integer size, String sort, LocalDate requestedFrom,
			LocalDate requestedTo, LocalDateTime createdFrom, LocalDateTime createdTo, VacationRequestDto vacationRequestDto) {
		
		String findUri = String.format("%s/find", BASE_URI_VACATION_REQUEST);
		if (page != null || size != null || sort != null || requestedFrom != null || requestedTo != null
				|| createdFrom != null || createdTo != null) {
			DateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			findUri += '?';
			boolean firstAdded = false;
			if (page != null) {
				findUri += "page=" + page.intValue();
				firstAdded = true;
			}
			if (size != null) {
				if (firstAdded) {
					findUri += "&size=" + size.intValue();
				} else {
					findUri += "size=" + size.intValue();
					firstAdded = true;
				}
			}
			if (sort != null) {
				if (firstAdded) {
					findUri += "&sort=" + sort;
				} else {
					findUri += "sort=" + sort;
					firstAdded = true;
				}
			}
			if (requestedFrom != null) {
				if (firstAdded) {
					findUri += "&requestedFrom=" + dfDay.format(Date.valueOf(requestedFrom).getTime());
				} else {
					findUri += "requestedFrom=" + dfDay.format(Date.valueOf(requestedFrom).getTime());
					firstAdded = true;
				}
			}
			if (requestedTo != null) {
				if (firstAdded) {
					findUri += "&requestedTo=" + dfDay.format(Date.valueOf(requestedTo).getTime());
				} else {
					findUri += "requestedTo=" + dfDay.format(Date.valueOf(requestedTo).getTime());
					firstAdded = true;
				}
			}
			if (createdFrom != null) {
				if (firstAdded) {
					findUri += "&createdFrom=" + (dfSec.format(Timestamp.valueOf(createdFrom).getTime()).replaceAll(" ", "T"));
				} else {
					findUri += "createdFrom=" + (dfSec.format(Timestamp.valueOf(createdFrom).getTime()).replaceAll(" ", "T"));
					firstAdded = true;
				}
			}
			if (createdTo != null) {
				if (firstAdded) {
					findUri += "&createdTo=" + (dfSec.format(Timestamp.valueOf(createdTo).getTime()).replaceAll(" ", "T"));
				} else {
					findUri += "createdTo=" + (dfSec.format(Timestamp.valueOf(createdTo).getTime()).replaceAll(" ", "T"));
					firstAdded = true;
				}
			}
		}
		System.out.println("Request URI: " + findUri);
		List<VacationRequestDto> responseList =  webTestClient
				.post()
				.uri(findUri)
				.bodyValue(vacationRequestDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(VacationRequestDto.class)
				.returnResult()
				.getResponseBody();
		return responseList;
	}
}
