package hu.webuni.hr.comtur.security;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.webuni.hr.comtur.config.HrConfigProperties;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.not_entity.EmployeeJwtDto;

@Service
public class JwtService {

	@Autowired
	HrConfigProperties hrConfigProperties;
	
	private final static String USER_ID = "user_id",
								USER_NAME = "user_name",
								MANAGER = "manager",
								SUBORDINATES = "subordinates";
	
	/**
	 * Creates JWT token based on {@link ExtendedUserPrincipal} object and configuration data.
	 * @param principal Current principal.
	 * @return JWT as encoded String.
	 */
	public String createJwtToken(ExtendedUserPrincipal principal) {
		String algorithmName = hrConfigProperties.getJwtdata().getAlgorithm();
		Algorithm alg = getAlgorithm(algorithmName);
		return JWT.create()
			.withSubject(principal.getUsername())
			.withIssuer(hrConfigProperties.getJwtdata().getIssuer())
			.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(hrConfigProperties.getJwtdata().getExpiration())))
			.withClaim(USER_ID, principal.getEmployee().getId())
			.withClaim(MANAGER, getEmployeeAsString(principal.getEmployee().getManager()))
			.withArrayClaim(SUBORDINATES, getEmployeesAsStringArray(principal.getEmployee().getSubordinates()))
			.sign(alg);
	}
	
	/**
	 * Creates algorithm based on configuration value. Supports methods with 1 String parameter only.
	 * @param algorithmName Algorithm name in the configuration file.
	 * @return Algorithm instance.
	 */
	private Algorithm getAlgorithm(String algorithmName) {
		if (algorithmName.startsWith("HMAC")) {
			try {
				Method method = Algorithm.class.getMethod(algorithmName, String.class);
				return (Algorithm) method.invoke(null, hrConfigProperties.getJwtdata().getSecret());
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return Algorithm.none();
			}
		} else {
			return Algorithm.none();
		}
	}
	
	/**
	 * Gets employee as String.
	 * @param employee Current employee.
	 * @return Employee as JWT String.
	 */
	private String getEmployeeAsString(Employee employee) {
		ObjectMapper mapper = new ObjectMapper();
		return getEmployeeAsString(mapper, employee);
	}
	
	/**
	 * Gets employee as String.
	 * @param mapper   Jackson object mapper.
	 * @param employee Current employee.
	 * @return Employee as JWT String.
	 */
	private String getEmployeeAsString(ObjectMapper mapper, Employee employee) {
		if (employee == null) {
			return null;
		}
		try {
			return mapper.writer().writeValueAsString(employee);
		} catch (IOException e) {
			return String.format("{\"%s\": %d, \"%s\": \"%s\"}", USER_ID, employee.getId(), USER_NAME, employee.getUserName());
		}
	}
	
	/**
	 * Gets employees as String array.
	 * @param employees List of employees.
	 * @return Employees as JWT String array.
	 */
	private String[] getEmployeesAsStringArray(List<Employee> employees) {
		String[] result = new String[employees.size()];
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < employees.size(); ++i) {
			result[i] = getEmployeeAsString(mapper, employees.get(i));
		}
		return result;
	}

	/**
	 * Parses encoded JWT to {@link UserDetails}.
	 * @param jwtToken JWT as encoded String.
	 * @return User details object.
	 */
	public UserDetails parseJwt(String jwtToken) {
		String algorithmName = hrConfigProperties.getJwtdata().getAlgorithm();
		Algorithm alg = getAlgorithm(algorithmName);
		DecodedJWT decodedJWT = JWT
			.require(alg)
			.withIssuer(hrConfigProperties.getJwtdata().getIssuer())
			.build()
			.verify(jwtToken);

		Employee employee = new Employee();
		employee.setId(decodedJWT.getClaim(USER_ID).asLong());
		employee.setUserName(decodedJWT.getSubject());
		employee.setPassword("dummy");
		
		try {
			ObjectMapper mapper = new ObjectMapper();

			Claim claimManager = decodedJWT.getClaim(MANAGER);
			if (!claimManager.isNull()) {
				EmployeeJwtDto managerDto = mapper.readValue(claimManager.asString(), EmployeeJwtDto.class);
				Employee manager = new Employee();
				manager.setId(managerDto.getId());
				manager.setUserName(managerDto.getUserName());
				employee.setManager(manager);
			}
			
			Claim claimSubordinates = decodedJWT.getClaim(SUBORDINATES);
			if (!claimSubordinates.isNull()) {
				String[] subordinateArray = claimSubordinates.asArray(String.class);
				List<Employee> subordinateList = new ArrayList<>(subordinateArray.length);
				for (String employeeString : subordinateArray) {
					EmployeeJwtDto subordinateDto = mapper.readValue(employeeString, EmployeeJwtDto.class);
					Employee subordinate = new Employee();
					subordinate.setId(subordinateDto.getId());
					subordinate.setUserName(subordinateDto.getUserName());
					subordinateList.add(subordinate);
				}
				employee.setSubordinates(subordinateList);
			}
		} catch (JsonProcessingException e) {
			System.out.println("Parsing JWT failed.");
			e.printStackTrace();
		}
		return new ExtendedUserPrincipal(employee, new ArrayList<>());
	}
}
