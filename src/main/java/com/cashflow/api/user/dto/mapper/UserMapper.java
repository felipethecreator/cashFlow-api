package com.cashflow.api.user.dto.mapper;

import com.cashflow.api.user.dto.input.RegisterRequest;
import com.cashflow.api.user.dto.output.UserResponse;
import com.cashflow.api.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);

    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(RegisterRequest registerRequest);
}
