package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.model.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {

	PositionDto positionToDto(Position position);
	
	Position dtoToPosition(PositionDto positionDto);
	
	List<PositionDto> positionsToDtos(List<Position> positions);
	
	List<Position> dtosToPositions(List<PositionDto> positionDtos);
}
