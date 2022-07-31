package hu.webuni.logistics.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.logistics.comtur.dto.SectionDto;
import hu.webuni.logistics.comtur.model.Section;

/**
 * {@link Section} entity - {@link SectionDto} MapStruct mapper.
 * 
 * @author comtur
 */
@Mapper(componentModel = "spring")
public interface SectionMapper {

	@Mapping(source = "fromMilestone.address.countryIso", target = "fromMilestone.address.country")
	@Mapping(source = "toMilestone.address.countryIso", target = "toMilestone.address.country")
	SectionDto sectionToDto(Section section);

	List<SectionDto> sectionsToDtos(List<Section> sections);

	@InheritInverseConfiguration
	Section dtoToSection(SectionDto sectionDto);

	List<Section> dtosToSections(List<SectionDto> sectionDtos);
}
