package ftn.userservice.domain.mappers;

import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import ftn.userservice.domain.entities.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User fromCreateRequest(UserCreateRequest request);

    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "password")
    void update(@MappingTarget User user, UserUpdateRequest request);

}
