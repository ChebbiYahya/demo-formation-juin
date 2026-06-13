package com.theBridge.demoFormationJuin.api.mapper;

import com.theBridge.demoFormationJuin.api.dto.UserRequestDto;
import com.theBridge.demoFormationJuin.api.dto.UserResponseDto;
import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserRequestDto userRequestDto) {
        UserEntity user = new UserEntity();
        updateEntity(user, userRequestDto);
        return user;
    }

    public UserResponseDto toResponseDto(UserEntity user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getUsername()
        );
    }

    public void updateEntity(UserEntity user, UserRequestDto userRequestDto) {
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setAddress(userRequestDto.getAddress());
        user.setUsername(userRequestDto.getUsername());
        user.setConfirmPassword(userRequestDto.getConfirmPassword());
    }
}
