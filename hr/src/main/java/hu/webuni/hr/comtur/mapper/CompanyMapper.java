package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.comtur.dto.CompanyDto;
import hu.webuni.hr.comtur.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	CompanyDto companyToDto(Company company);

	List<CompanyDto> companyToDtos(List<Company> companies);

	Company dtoToCompany(CompanyDto companyDto);
	
	List<Company> dtosToCompanies(List<CompanyDto> companyDtos);
}
