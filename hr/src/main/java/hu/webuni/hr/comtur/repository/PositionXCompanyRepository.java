package hu.webuni.hr.comtur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.comtur.model.PositionXCompany;

public interface PositionXCompanyRepository extends JpaRepository<PositionXCompany, Long> {
	
	List<PositionXCompany> findByPositionName(String positionName);
	
	Optional<PositionXCompany> findByPositionNameAndCompanyId(String positionName, long companyId);
	
	List<PositionXCompany> findByCompanyId(long companyId);
	
	@Modifying
	@Transactional
	@Query(
			"DELETE FROM PositionXCompany pxc " +
			"WHERE pxc.id IN (" + 
				"SELECT pxc2.id " +
				"FROM PositionXCompany pxc2 " +
				"WHERE pxc2.company.id = :companyId " + 
			")")
	int deleteOfCompany(long companyId);
}
