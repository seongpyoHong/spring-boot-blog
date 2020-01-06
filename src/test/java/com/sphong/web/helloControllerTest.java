package com.sphong.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest
class helloControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void return_hello () throws Exception {
        String hello = "Hello!";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    /*
    @Disabled
    @Test
    public void return_hello_dto() throws Exception {
        String name = "name";
        int age = 14;

        mvc.perform(get("/hello/dto")
                    .param("name",name)
                    .param("age",String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name" ,is(name)))
                .andExpect(jsonPath("$.age",is(age)));
    }*/
}