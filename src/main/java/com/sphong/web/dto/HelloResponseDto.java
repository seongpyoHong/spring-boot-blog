package com.sphong.web.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HelloResponseDto {
    @NonNull
    private final String name;
    @NonNull
    private final int age;
}
