package com.sphong.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sphong.domain.posts.Posts;
import com.sphong.domain.posts.PostsRepository;
import com.sphong.web.dto.PostsRequestDto;
import com.sphong.web.dto.PostsResponseDto;
import com.sphong.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    PostsRepository postsRepository;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    private String title = "sphong1";
    private String content = "test1";
    private String author = "sphong";

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    private Posts savePosts(String title, String content, String author) {
        return postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
    }

    @Test
    @WithMockUser(roles="USER")
    public void test_savePost() throws Exception{
        //given
        PostsRequestDto postsRequestDto = PostsRequestDto.builder()
                                                                    .title(title)
                                                                    .content(content)
                                                                    .author(author)
                                                                    .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(postsRequestDto)));

        //then
        List<Posts> allPosts = postsRepository.findAll();
        assertEquals(allPosts.get(0).getTitle(), title);
        assertEquals(allPosts.get(0).getContent(),content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void test_Update() throws Exception{
        //given
        String updateTitle = "sphongUpdate";
        String updateContent = "testUpdate";

        Posts savedPosts = savePosts(title, content, author);
        Long updateId = savedPosts.getId();
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        //when
        mvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isOk());

        //then
        List<Posts> allPosts = postsRepository.findAll();
        assertEquals((allPosts).get(0).getContent(),updateContent);
        assertEquals((allPosts).get(0).getTitle(),updateTitle);
    }

    @Test
    @WithMockUser(roles="USER")
    public void test_FindById() throws Exception{
        //given
        Posts posts = savePosts(title,content,author);
        Long savedId = posts.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + savedId;

        //when
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(posts.getTitle()));

      }

    @Test
    @WithMockUser(roles="USER")
    public void test_Delete() throws Exception{
        //given
        Posts posts = savePosts(title, content, author);
        Long savedId = posts.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + savedId;
        HttpEntity<Long> requestEntity = new HttpEntity<>(savedId);

        //when
       mvc.perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }
}