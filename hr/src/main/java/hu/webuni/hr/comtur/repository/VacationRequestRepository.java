package hu.webuni.hr.comtur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.webuni.hr.comtur.model.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long>, JpaSpecificationExecutor<VacationRequest> {
	
	@Override
	Page<VacationRequest> findAll(Pageable pageable);

	@Override
	List<VacationRequest> findAll(Sort sort);
	
	Page<VacationRequest> findAllByOrderByVacationStart(Pageable pageable);
}
