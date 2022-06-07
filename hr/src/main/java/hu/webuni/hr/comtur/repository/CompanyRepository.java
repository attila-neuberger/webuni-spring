package hu.webuni.hr.comtur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.comtur.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
