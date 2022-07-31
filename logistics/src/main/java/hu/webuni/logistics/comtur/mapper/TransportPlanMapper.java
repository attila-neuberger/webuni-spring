package hu.webuni.logistics.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.logistics.comtur.dto.SectionDto;
import hu.webuni.logistics.comtur.dto.TransportPlanDto;
import hu.webuni.logistics.comtur.model.Section;
import hu.webuni.logistics.comtur.model.TransportPlan;

/**
 * {@link TransportPlan} entity - {@link TransportPlanDto} MapStruct mapper.
 * 
 * @author comtur
 */
@Mapper(componentModel = "spring")
public interface TransportPlanMapper {

	@Named("detailed")
	TransportPlanDto transportPlanToDto(TransportPlan transportPlan);

	@IterableMapping(qualifiedByName = "detailed")
	List<TransportPlanDto> transportPlansToDtos(List<TransportPlan> transportPlans);

	@InheritInverseConfiguration(name = "transportPlanToDto")
	TransportPlan dtoToTransportPlan(TransportPlanDto transportPlanDto);

	List<TransportPlan> dtosToTransportPlans(List<TransportPlanDto> transportPlanDtos);

	@Named("summary")
	@Mapping(target = "sections", ignore = true)
	TransportPlanDto transportPlanToDtoWithNoSections(TransportPlan transportPlan);

	@IterableMapping(qualifiedByName = "summary")
	List<TransportPlanDto> transportPlansToDtosWithNoSections(List<TransportPlan> transportPlans);

	// ===== Mappings of nested DTOs ===== //

	@Mapping(target = "transportPlan", ignore = true)
	@Mapping(source = "fromMilestone.address.countryIso", target = "fromMilestone.address.country")
	@Mapping(source = "toMilestone.address.countryIso", target = "toMilestone.address.country")
	SectionDto sectionToDtoWithNoTransportPlan(Section section);

	List<SectionDto> sectionsToDtosWithNoTransportPlans(List<Section> sections);

	@InheritInverseConfiguration(name = "sectionToDtoWithNoTransportPlan")
	Section dtoToSectionWithNoTransportPlan(SectionDto sectionDto);

	List<Section> dtosToSectionsWithNoTransportPlans(List<SectionDto> sectionDtos);
}
