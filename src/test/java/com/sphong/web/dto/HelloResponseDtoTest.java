package com.sphong.web.dto;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloResponseDtoTest {

    @Test
    public void createHelloResponseDto () {
        //given
        String name = "name";
        int age = 23;

        //when
        HelloResponseDto helloResponseDto = new HelloResponseDto(name,age);

        //then
        assertEquals(helloResponseDto.getAge(),age);
        assertEquals(helloResponseDto.getName(),name);
    }
}