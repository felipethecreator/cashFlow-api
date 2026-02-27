package com.eventhub.eventhub_api.user.dto.mapper;

import com.eventhub.eventhub_api.user.dto.input.RegisterRequest;
import com.eventhub.eventhub_api.user.dto.output.UserResponse;
import com.eventhub.eventhub_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);

    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(RegisterRequest registerRequest);
}
