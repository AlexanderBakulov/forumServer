package net.thumbtack.forums.mappers.mapstruct;

import net.thumbtack.forums.dto.request.CreateUserDtoRequest;
import net.thumbtack.forums.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RequestMapper {

    User createUserDtoRequestToUser(CreateUserDtoRequest request);





}
