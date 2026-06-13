package com.theBridge.demoFormationJuin.application.interfaces;

import com.theBridge.demoFormationJuin.api.dto.UserRequestDto;
import com.theBridge.demoFormationJuin.api.dto.UserResponseDto;

import java.util.List;

public interface UserInterface {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);

    boolean existsByUsernameNamed(String username);

    UserResponseDto findByUsernameNamed(String username);

    List<UserResponseDto> findByEmailAndAddressStartingWithNamed(String email, String address);

    UserResponseDto findByEmailNamed(String email);

    UserResponseDto findUserByUsernameJPQL(String username);

    boolean existsByUsernameJPQL(String username);

    UserResponseDto findByUsernameSQL(String username);

    boolean existsByUsernameSQL(String username);

    List<UserResponseDto> findByCleSQL(String cle);

    List<UserResponseDto> findByDomainSQL(String domain);
}
