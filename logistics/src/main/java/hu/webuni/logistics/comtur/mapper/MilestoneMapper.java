package hu.webuni.logistics.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.logistics.comtur.dto.MilestoneDto;
import hu.webuni.logistics.comtur.model.Milestone;

/**
 * {@link Milestone} entity - {@link MilestoneDto} MapStruct mapper.
 * 
 * @author comtur
 */
@Mapper(componentModel = "spring")
public interface MilestoneMapper {

	@Mapping(source = "address.countryIso", target = "address.country")
	MilestoneDto milestoneToDto(Milestone milestone);

	List<MilestoneDto> milestonesToDtos(List<Milestone> milestones);

	@InheritInverseConfiguration
	Milestone dtoToMilestone(MilestoneDto milestoneDto);

	List<Milestone> dtosToMilestones(List<MilestoneDto> milestoneDtos);
}
