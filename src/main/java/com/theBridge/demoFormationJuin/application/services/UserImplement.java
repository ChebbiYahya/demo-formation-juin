package com.theBridge.demoFormationJuin.application.services;

import com.theBridge.demoFormationJuin.api.dto.UserRequestDto;
import com.theBridge.demoFormationJuin.api.dto.UserResponseDto;
import com.theBridge.demoFormationJuin.api.exception.ResourceNotFoundException;
import com.theBridge.demoFormationJuin.api.mapper.UserMapper;
import com.theBridge.demoFormationJuin.application.interfaces.UserInterface;
import com.theBridge.demoFormationJuin.domain.entities.RoleEntity;
import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import com.theBridge.demoFormationJuin.domain.enums.RoleName;
import com.theBridge.demoFormationJuin.repository.RoleRepository;
import com.theBridge.demoFormationJuin.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserImplement implements UserInterface {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserRespository userRepository;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        UserEntity user = userMapper.toEntity(userRequestDto);
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        UserEntity user = findUserById(id);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        UserEntity user = findUserById(id);
        userMapper.updateEntity(user, userRequestDto);
        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = findUserById(id);
        userRepository.delete(user);
    }

    private UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec id: " + id));
    }



    @Override
    public boolean existsByUsernameNamed(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserResponseDto findByUsernameNamed(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec username: " + username));
        return userMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findByEmailAndAddressStartingWithNamed(String email, String address) {
        return userRepository.findByEmailAndAddressStartingWith(email, address)
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto findByEmailNamed(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec email: " + email);
        }
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto findUserByUsernameJPQL(String username) {
        UserEntity user = userRepository.findUserByUsernameJPQL(username);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec username: " + username);
        }
        return userMapper.toResponseDto(user);
    }

    @Override
    public boolean existsByUsernameJPQL(String username) {
        return userRepository.existsByUsernameJPQL(username);
    }

    @Override
    public UserResponseDto findByUsernameSQL(String username) {
        UserEntity user = userRepository.findByUsernameSQL(username);
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec username: " + username);
        }
        return userMapper.toResponseDto(user);
    }

    @Override
    public boolean existsByUsernameSQL(String username) {
        return userRepository.existsByUsernameSQL(username) == 1;
    }

    @Override
    public List<UserResponseDto> findByCleSQL(String cle) {
        return userRepository.findByCle(cle)
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<UserResponseDto> findByDomainSQL(String domain) {
        return userRepository.findByDomain(domain)
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserEntity addUserWithRole(UserEntity user, RoleName roleName) {
        Optional<RoleEntity> optionalRole = roleRepository.findByRoleName(roleName);
        RoleEntity role = optionalRole.orElseGet(
                () -> {
                    RoleEntity r = new RoleEntity();
                    r.setRoleName(roleName);
                    return roleRepository.save(r);

                }
        );
        user.setRole(role);
        return userRepository.save(user);
    }


}
