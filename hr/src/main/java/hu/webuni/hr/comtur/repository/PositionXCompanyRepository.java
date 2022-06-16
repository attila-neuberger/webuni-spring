package hu.webuni.hr.comtur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.comtur.model.PositionXCompany;

public interface PositionXCompanyRepository extends JpaRepository<PositionXCompany, Long> {
	
	List<PositionXCompany> findByPositionName(String positionName);
	
	Optional<PositionXCompany> findByPositionNameAndCompanyId(String positionName, long companyId);
}
