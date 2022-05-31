package hu.comtur.airport.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.comtur.airport.dto.AirportDto;
import hu.comtur.airport.model.Airport;

@Mapper(componentModel = "spring")
public interface AirportMapper {

	List<AirportDto> airportToDtos(List<Airport> airports);

	AirportDto airportToDto(Airport airport);

	Airport dtoToAirport(AirportDto airportDto);
}
