package org.serious.dev.service;

import org.serious.dev.dto.UserResponseDto;

public interface UserService {

    UserResponseDto getUserById(Long id);
}
