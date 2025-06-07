package org.serious.dev.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(Long id, String username) {
}
