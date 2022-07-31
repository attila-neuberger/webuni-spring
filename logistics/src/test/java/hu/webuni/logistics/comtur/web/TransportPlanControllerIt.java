package hu.webuni.logistics.comtur.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.logistics.comtur.config.LogisticsConfigProperties;
import hu.webuni.logistics.comtur.dto.AddressDto;
import hu.webuni.logistics.comtur.dto.DelayDto;
import hu.webuni.logistics.comtur.dto.LoginDto;
import hu.webuni.logistics.comtur.dto.MilestoneDto;
import hu.webuni.logistics.comtur.dto.SectionDto;
import hu.webuni.logistics.comtur.dto.TransportPlanDto;
import hu.webuni.logistics.comtur.service.TransportPlanService;

/**
 * Integration tests for transport plan controller.
 * 
 * @author comtur
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class TransportPlanControllerIt {

	@Autowired
	private WebTestClient webTestClient;
	
	@Autowired
	private TransportPlanService transportPlanService;
	
	@Autowired
	private LogisticsConfigProperties configProperties;
	
	/**
	 * URIs.
	 */
	private final static String BASE_URI_TRANSPORT_PLAN = "/api/transportPlans",
								BASE_URI_LOGIN = "/api/login";
	
	/**
	 * Comparator for transport plans, based on its IDs.
	 */
	private Comparator<TransportPlanDto> transportPlanIdComparator = new Comparator<>() {

		@Override
		public int compare(TransportPlanDto tp1, TransportPlanDto tp2) {
			return (int)(tp1.getId() - tp2.getId());
		}
	};
	
	/**
	 * User login constants.
	 */
	private final static String USER_NAME = "admin_user",
								USER_WITHOUT_TRANSPORT_ROLE = "address_user",
								PASSWORD = "pass";
	
	/**
	 * Creates two transport plan entities.
	 */
	@BeforeAll
	public void beforeAll() {
		createTransportPlan();
		createTransportPlan();
	}
	
	/**
	 * Clears database.
	 */
	@AfterAll
	public void afterAll() {
		transportPlanService.deleteAll();
	}
	
	/**
	 * Creates a transport plan entity with 3 sections.
	 */
	private void createTransportPlan() {
		LocalDateTime now = LocalDateTime.now();
		String jwt = login(USER_NAME, PASSWORD);
		
		TransportPlanDto transportPlanRaw = new TransportPlanDto();
		transportPlanRaw.setExpectedIncome(1000.0);
		
		List<SectionDto> sections = new ArrayList<>(3);
		
		SectionDto section1 = new SectionDto();
		section1.setNumber(0);
		MilestoneDto milestone1From = new MilestoneDto();
		milestone1From.setPlannedTime(now.plusHours(1));
		milestone1From.setAddress(createAddress("HU", "H-2800", "Tatabánya", "Bajcsy-Zs. u.", "31.", null, null));
		section1.setFromMilestone(milestone1From);
		MilestoneDto milestone1To = new MilestoneDto();
		milestone1To.setPlannedTime(now.plusHours(2));
		milestone1To.setAddress(createAddress("HU", "H-2800", "Tatabánya", "Réti u.", "35. 2/3", 47.583164, 18.385277));
		section1.setToMilestone(milestone1To);
		sections.add(section1);
		
		SectionDto section2 = new SectionDto();
		section2.setNumber(1);
		MilestoneDto milestone2From = new MilestoneDto();
		milestone2From.setPlannedTime(now.plusHours(10));
		milestone2From.setAddress(createAddress("HU", "H-2800", "Tatabánya", "Réti u.", "35. 2/3", 47.583164, 18.385277));
		section2.setFromMilestone(milestone2From);
		MilestoneDto milestone2To = new MilestoneDto();
		milestone2To.setPlannedTime(now.plusHours(12));
		milestone2To.setAddress(createAddress("HU", "H-2112", "Veresegyház", "Hajnal u.", "1.", 47.658960, 19.288482));
		section2.setToMilestone(milestone2To);
		sections.add(section2);
		
		SectionDto section3 = new SectionDto();
		section3.setNumber(2);
		MilestoneDto milestone3From = new MilestoneDto();
		milestone3From.setPlannedTime(now.plusHours(24));
		milestone3From.setAddress(createAddress("HU", "H-1111", "Budapest", "Műegyetem rkp.", "3-9", null, null));
		section3.setFromMilestone(milestone3From);
		MilestoneDto milestone3To = new MilestoneDto();
		milestone3To.setPlannedTime(now.plusHours(42));
		milestone3To.setAddress(createAddress("GR", "74133", "Rethymno", "Lefkoniko Bay", "1", null, null));
		section3.setToMilestone(milestone3To);
		sections.add(section3);
		
		transportPlanRaw.setSections(sections);
		
		TransportPlanDto transportPlanDto = createTransportPlan(transportPlanRaw, jwt);
		System.out.println(transportPlanDto);
	}
	
	/**
	 * Creates an address DTO.
	 * 
	 * @param country Country.
	 * @param zipCode ZIP code.
	 * @param city    City.
	 * @param street  Street.
	 * @param number  Number.
	 * @param lat     GPS latitude (optional).
	 * @param lng     GPS longitude (optional).
	 * @return Address DTO.
	 */
	private AddressDto createAddress(String country, String zipCode, String city, String street, String number,
			Double lat, Double lng) {
		AddressDto address = new AddressDto();
		address.setCountry(country);
		address.setZipCode(zipCode);
		address.setCity(city);
		address.setStreet(street);
		address.setNumber(number);
		if (lat != null) {
			address.setLat(lat);
		}
		if (lng != null) {
			address.setLng(lng);
		}
		return address;
	}
	
	/**
	 * Login via web client.
	 * 
	 * @param userName Login name.
	 * @param password Password.
	 * @return Java web token.
	 */
	private String login(String userName, String password) {
		return webTestClient
				.post()
				.uri(BASE_URI_LOGIN)
				.bodyValue(new LoginDto(userName, password))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
	}

	/**
	 * Creates a transport plan via web client.
	 * 
	 * @param transportPlan Transport plan DTO.
	 * @param jwt           Java web token.
	 * @return Created transport plan as DTO.
	 */
	private TransportPlanDto createTransportPlan(TransportPlanDto transportPlan, String jwt) {
		return webTestClient
				.post()
				.uri(BASE_URI_TRANSPORT_PLAN)
				.headers(headers -> headers.setBearerAuth(jwt))
				.bodyValue(transportPlan)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(TransportPlanDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	/**
	 * Tests count of created transport plans.
	 */
	@Test
	void checkCreatedTransportPlans() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlans = getAllTransportPlans(jwt);
		
		assertThat(transportPlans.size()).isEqualTo(2);
	}
	
	/**
	 * Gets list of transport plans.
	 * 
	 * @param jwt Java web token.
	 * @return List of transport plan DTOs.
	 */
	private List<TransportPlanDto> getAllTransportPlans(String jwt) {
		List<TransportPlanDto> responseList = webTestClient
				.get()
				.uri(BASE_URI_TRANSPORT_PLAN)
				.headers(headers -> headers.setBearerAuth(jwt))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(TransportPlanDto.class)
				.returnResult()
				.getResponseBody();
		return responseList;
	}
	
	/**
	 * Tests create request if transport plan not exists.
	 */
	@Test
	void checkDelayTransportPlanNotExists() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlans = getAllTransportPlans(jwt);
		Set<Long> transportPlanIds = new HashSet<>(transportPlans.size());
		for (TransportPlanDto transportPlanDto : transportPlans) {
			transportPlanIds.add(transportPlanDto.getId());
		}
		long id = 1L;
		while (transportPlanIds.contains(id)) {
			++id;
		}
		DelayDto delayDto = new DelayDto();
		delayDto.setDelay(30);
		delayDto.setMilestoneId(0);
		
		delay404(jwt, id, delayDto);
	}
	
	/**
	 * Checks status assertion's 404 response (not found).
	 * 
	 * @param jwt             Java web token.
	 * @param transportPlanId ID of the transport plan.
	 * @param delayDto        Delay DTO object.
	 */
	private void delay404(String jwt, long transportPlanId, DelayDto delayDto) {
		delay(jwt, transportPlanId, delayDto)
				.isNotFound();
	}
	
	/**
	 * Exchanges delay end point, gets its status assertion.
	 * 
	 * @param jwt             Java web token.
	 * @param transportPlanId ID of the transport plan.
	 * @param delayDto        Delay DTO object.
	 * @return Status assertion object.
	 */
	private StatusAssertions delay(String jwt, long transportPlanId, DelayDto delayDto) {
		 return webTestClient
				.post()
				.uri(String.format("%s/%d/delay", BASE_URI_TRANSPORT_PLAN, transportPlanId))
				.headers(headers -> headers.setBearerAuth(jwt))
				.bodyValue(delayDto)
				.exchange()
				.expectStatus();
	}
	
	/**
	 * Tests create request if milestone not exists.
	 */
	@Test
	void checkDelayMilestoneNotExists() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlans = getAllTransportPlans(jwt);
		Set<Long> milestoneIds = new HashSet<>(transportPlans.get(0).getSections().size() * 2);
		for (SectionDto sectionDto : transportPlans.get(0).getSections()) {
			milestoneIds.add(sectionDto.getFromMilestone().getId());
			milestoneIds.add(sectionDto.getToMilestone().getId());
		}
		long id = 1L;
		while (milestoneIds.contains(id)) {
			++id;
		}
		DelayDto delayDto = new DelayDto();
		delayDto.setDelay(30);
		delayDto.setMilestoneId(id);
		
		delay404(jwt, transportPlans.get(0).getId(), delayDto);
	}
	
	/**
	 * Tests create request if milestone not belongs to the transport plan.
	 */
	@Test
	void checkDelayTransportPlanAndMilestoneNotMatch() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlans = getAllTransportPlans(jwt);
		DelayDto delayDto = new DelayDto();
		delayDto.setDelay(30);
		delayDto.setMilestoneId(transportPlans.get(1).getSections().get(0).getFromMilestone().getId());
		
		delay400(jwt, transportPlans.get(0).getId(), delayDto);
	}
	
	/**
	 * Checks status assertion's 400 response (bad request).
	 * 
	 * @param jwt             Java web token.
	 * @param transportPlanId ID of the transport plan.
	 * @param delayDto        Delay DTO object.
	 */
	private void delay400(String jwt, long transportPlanId, DelayDto delayDto) {
		delay(jwt, transportPlanId, delayDto)
				.isBadRequest();
	}
	
	/**
	 * Tests invalid Delay DTO.
	 */
	@Test
	void checkDelayDtoIsInvalid() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlans = getAllTransportPlans(jwt);
		DelayDto delayDto = new DelayDto();
		delayDto.setDelay(-30);
		delayDto.setMilestoneId(transportPlans.get(0).getSections().get(0).getFromMilestone().getId());
		
		delay400(jwt, transportPlans.get(0).getId(), delayDto);
	}
	
	/**
	 * Tests delay of a section's "from" milestone.
	 */
	@Test
	void checkDelayMilestoneIncreasesFrom() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlansBefore = getAllTransportPlans(jwt);
		Collections.sort(transportPlansBefore, transportPlanIdComparator);
		DelayDto delayDto = new DelayDto();
		int delay = 10;
		delayDto.setDelay(delay);
		SectionDto sectionChosen = transportPlansBefore.get(0).getSections().get(0);
		delayDto.setMilestoneId(sectionChosen.getFromMilestone().getId());
		
		delayOk(jwt, transportPlansBefore.get(0).getId(), delayDto);
		
		List<TransportPlanDto> transportPlansAfter = getAllTransportPlans(jwt);
		Collections.sort(transportPlansAfter, transportPlanIdComparator);
		
		assertThat(transportPlansBefore.size()).isEqualTo(transportPlansAfter.size());
		assertThat(transportPlansBefore.subList(1, transportPlansBefore.size()))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(transportPlansAfter.subList(1, transportPlansAfter.size()));
		assertThat(transportPlansBefore.get(0))
				.usingRecursiveComparison()
				.ignoringFields("sections")
				.isEqualTo(transportPlansAfter.get(0));
		List<SectionDto> sectionsBefore = transportPlansBefore.get(0).getSections();
		Collections.sort(sectionsBefore);
		List<SectionDto> sectionsAfter = transportPlansAfter.get(0).getSections();
		Collections.sort(sectionsAfter);
		for (int i = 0; i < sectionsAfter.size(); ++i) {
			if (sectionsAfter.get(i).getId() == sectionChosen.getId()) {
				assertThat(sectionsBefore.get(i))
						.usingRecursiveComparison()
						.ignoringFields("fromMilestone.plannedTime", "toMilestone.plannedTime")
						.isEqualTo(sectionsAfter.get(i));
				assertThat(sectionsBefore.get(i).getFromMilestone().getPlannedTime().plusMinutes(delay))
						.isCloseTo(sectionsAfter.get(i).getFromMilestone().getPlannedTime(), within(1, ChronoUnit.MICROS));
				assertThat(sectionsBefore.get(i).getToMilestone().getPlannedTime().plusMinutes(delay))
						.isCloseTo(sectionsAfter.get(i).getToMilestone().getPlannedTime(), within(1, ChronoUnit.MICROS));
			} else {
				assertThat(sectionsBefore.get(i))
						.usingRecursiveComparison()
						.isEqualTo(sectionsAfter.get(i));
			}
		}
	}
	
	/**
	 * Checks status assertion's 200 response (OK).
	 * 
	 * @param jwt             Java web token.
	 * @param transportPlanId ID of the transport plan.
	 * @param delayDto        Delay DTO object.
	 * @return Transport plan DTO in response body.
	 */
	private TransportPlanDto delayOk(String jwt, long transportPlanId, DelayDto delayDto) {
		return delay(jwt, transportPlanId, delayDto)
				.isOk()
				.expectBody(TransportPlanDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	/**
	 * Tests delay of a section's "to" milestone.
	 */
	@Test
	void checkDelayMilestoneIncreasesTo() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlansBefore = getAllTransportPlans(jwt);
		Collections.sort(transportPlansBefore, transportPlanIdComparator);
		DelayDto delayDto = new DelayDto();
		int delay = 10;
		delayDto.setDelay(delay);
		List<SectionDto> sections = transportPlansBefore.get(0).getSections();
		Collections.sort(sections);
		SectionDto sectionChosen = sections.get(0);
		assertThat(sectionChosen.getNumber() == 0);
		delayDto.setMilestoneId(sectionChosen.getToMilestone().getId());
		
		delayOk(jwt, transportPlansBefore.get(0).getId(), delayDto);
		
		List<TransportPlanDto> transportPlansAfter = getAllTransportPlans(jwt);
		Collections.sort(transportPlansAfter, transportPlanIdComparator);
		
		assertThat(transportPlansBefore.size()).isEqualTo(transportPlansAfter.size());
		assertThat(transportPlansBefore.subList(1, transportPlansBefore.size()))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(transportPlansAfter.subList(1, transportPlansAfter.size()));
		assertThat(transportPlansBefore.get(0))
				.usingRecursiveComparison()
				.ignoringFields("sections")
				.isEqualTo(transportPlansAfter.get(0));
		List<SectionDto> sectionsBefore = transportPlansBefore.get(0).getSections();
		Collections.sort(sectionsBefore);
		List<SectionDto> sectionsAfter = transportPlansAfter.get(0).getSections();
		Collections.sort(sectionsAfter);
		for (int i = 0; i < sectionsAfter.size(); ++i) {
			if (sectionsAfter.get(i).getId() == sectionChosen.getId()) {
				assertThat(sectionsBefore.get(i))
						.usingRecursiveComparison()
						.ignoringFields("toMilestone.plannedTime")
						.isEqualTo(sectionsAfter.get(i));
				assertThat(sectionsBefore.get(i).getToMilestone().getPlannedTime().plusMinutes(delay))
						.isCloseTo(sectionsAfter.get(i).getToMilestone().getPlannedTime(), within(1, ChronoUnit.MICROS));
			} else if (sectionsAfter.get(i).getNumber() == 1) {
				assertThat(sectionsBefore.get(i))
						.usingRecursiveComparison()
						.ignoringFields("fromMilestone.plannedTime")
						.isEqualTo(sectionsAfter.get(i));
				assertThat(sectionsBefore.get(i).getFromMilestone().getPlannedTime().plusMinutes(delay))
						.isCloseTo(sectionsAfter.get(i).getFromMilestone().getPlannedTime(), within(1, ChronoUnit.MICROS));
			} else {
					assertThat(sectionsBefore.get(i))
					.usingRecursiveComparison()
					.isEqualTo(sectionsAfter.get(i));
			}
		}
	}

	/**
	 * Tests delay of a section's "to" milestone. This milestone is the last one.
	 */
	@Test
	void checkDelayMilestoneIncreasesToLast() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlansBefore = getAllTransportPlans(jwt);
		Collections.sort(transportPlansBefore, transportPlanIdComparator);
		DelayDto delayDto = new DelayDto();
		int delay = 10;
		delayDto.setDelay(delay);
		List<SectionDto> sections = transportPlansBefore.get(0).getSections();
		Collections.sort(sections);
		SectionDto sectionChosen = sections.get(sections.size() - 1);
		assertThat(sectionChosen.getNumber() == sections.size() - 1);
		delayDto.setMilestoneId(sectionChosen.getToMilestone().getId());
		
		delayOk(jwt, transportPlansBefore.get(0).getId(), delayDto);
		
		List<TransportPlanDto> transportPlansAfter = getAllTransportPlans(jwt);
		Collections.sort(transportPlansAfter, transportPlanIdComparator);
		
		assertThat(transportPlansBefore.size()).isEqualTo(transportPlansAfter.size());
		assertThat(transportPlansBefore.subList(1, transportPlansBefore.size()))
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(transportPlansAfter.subList(1, transportPlansAfter.size()));
		assertThat(transportPlansBefore.get(0))
				.usingRecursiveComparison()
				.ignoringFields("sections")
				.isEqualTo(transportPlansAfter.get(0));
		List<SectionDto> sectionsBefore = transportPlansBefore.get(0).getSections();
		Collections.sort(sectionsBefore);
		List<SectionDto> sectionsAfter = transportPlansAfter.get(0).getSections();
		Collections.sort(sectionsAfter);
		for (int i = 0; i < sectionsAfter.size(); ++i) {
			if (sectionsAfter.get(i).getId() == sectionChosen.getId()) {
				assertThat(sectionsBefore.get(i))
						.usingRecursiveComparison()
						.ignoringFields("toMilestone.plannedTime")
						.isEqualTo(sectionsAfter.get(i));
				assertThat(sectionsBefore.get(i).getToMilestone().getPlannedTime().plusMinutes(delay))
						.isCloseTo(sectionsAfter.get(i).getToMilestone().getPlannedTime(), within(1, ChronoUnit.MICROS));
			} else {
					assertThat(sectionsBefore.get(i))
					.usingRecursiveComparison()
					.isEqualTo(sectionsAfter.get(i));
			}
		}
	}
	
	/**
	 * Tests transport plan's delay penalty based on the delay minutes.
	 */
	@Test
	void checkDelayPenalty() {
		String jwt = login(USER_NAME, PASSWORD);
		List<TransportPlanDto> transportPlansBefore = getAllTransportPlans(jwt);
		Collections.sort(transportPlansBefore, transportPlanIdComparator);
		
		int[] delayMinutes = configProperties.getTransportplan().getDelayminutes();
		double[] delayPenalties = configProperties.getTransportplan().getDelaypenalty();
		int[] delayMinutesExpanded = new int[delayMinutes.length * 2 + 1];
		double[] delayPenaltiesExpanded = new double[delayPenalties.length * 2 + 1];
		delayMinutesExpanded[0] = 1;
		delayPenaltiesExpanded[0] = 0.0;
		for (int i = 0; i < delayMinutes.length; ++i) {
			delayMinutesExpanded[1 + 2 * i] = delayMinutes[i] - 1;
			delayPenaltiesExpanded[1 + 2 * i] = i == 0 ? 0.0 : delayPenalties[i - 1];
			delayMinutesExpanded[1 + 2 * i + 1] = delayMinutes[i];
			delayPenaltiesExpanded[1 + 2 * i + 1] = delayPenalties[i];
		}
		
		DelayDto delayDto = new DelayDto();
		delayDto.setMilestoneId(transportPlansBefore.get(0).getSections().get(0).getFromMilestone().getId());
		Offset<Double> delta = Offset.<Double>offset(0.00001);
		for (int i = 0; i < delayMinutesExpanded.length; ++i) {
			int delay = delayMinutesExpanded[i];
			delayDto.setDelay(delay);
			delayOk(jwt, transportPlansBefore.get(0).getId(), delayDto);
			
			List<TransportPlanDto> transportPlansAfter = getAllTransportPlans(jwt);
			Collections.sort(transportPlansAfter, transportPlanIdComparator);
			assertThat(transportPlansBefore.get(0).getExpectedIncome() * (1 - 0.01 * delayPenaltiesExpanded[i]))
					.isCloseTo(transportPlansAfter.get(0).getExpectedIncome(), delta);
			transportPlansBefore = transportPlansAfter;
		}
	}
	
	/**
	 * Tests transport plan delay's user role.
	 */
	@Test
	void checkUserWithoutRole() {
		String jwt = login(USER_WITHOUT_TRANSPORT_ROLE, PASSWORD);
		List<TransportPlanDto> transportPlans = getAllTransportPlans(jwt);
		DelayDto delayDto = new DelayDto();
		delayDto.setMilestoneId(transportPlans.get(0).getSections().get(0).getFromMilestone().getId());
		delayDto.setDelay(30);
		delay403(jwt, transportPlans.get(0).getId(), delayDto);
	}
	
	/**
	 * Checks status assertion's 403 response (forbidden).
	 * 
	 * @param jwt             Java web token.
	 * @param transportPlanId ID of the transport plan.
	 * @param delayDto        Delay DTO object.
	 */
	private void delay403(String jwt, long transportPlanId, DelayDto delayDto) {
		delay(jwt, transportPlanId, delayDto)
				.isForbidden();
	}
}
