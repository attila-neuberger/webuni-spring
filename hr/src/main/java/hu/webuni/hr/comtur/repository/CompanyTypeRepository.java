package hu.webuni.hr.comtur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.comtur.model.CompanyType;

public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {
}
