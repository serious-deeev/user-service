package org.serious.dev.service.impl;

import lombok.RequiredArgsConstructor;
import org.serious.dev.dto.UserResponseDto;
import org.serious.dev.entity.User;
import org.serious.dev.exception.NoSuchUserException;
import org.serious.dev.mapper.UserMapper;
import org.serious.dev.repository.UserRepository;
import org.serious.dev.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchUserException(id));

        return userMapper.userToUserResponseDto(user);
    }
}
