package hu.webuni.logistics.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.logistics.comtur.dto.AddressDto;
import hu.webuni.logistics.comtur.model.Address;

/**
 * {@link Address} entity - {@link AddressDto} MapStruct mapper.
 * 
 * @author comtur
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

	@Mapping(source = "countryIso", target = "country")
	AddressDto addressToDto(Address address);

	List<AddressDto> addressesToDtos(List<Address> addersses);

	@InheritInverseConfiguration
	Address dtoToAddress(AddressDto addressDto);

	List<Address> dtosToAddresses(List<AddressDto> addressDtos);
}
