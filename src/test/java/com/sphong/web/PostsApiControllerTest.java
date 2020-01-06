package com.sphong.web;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    PostsRepository postsRepository;
    private String title = "sphong1";
    private String content = "test1";
    private String author = "sphong";

    private Posts savePosts(String title, String content, String author) {
        return postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
    }
    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    public void test_savePost() {
        //given
        PostsRequestDto postsRequestDto = PostsRequestDto.builder()
                                                                    .title(title)
                                                                    .content(content)
                                                                    .author(author)
                                                                    .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, postsRequestDto, Long.class);

        //then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(responseEntity.getBody() > (0L));
    }

    @Test
    public void test_Update() {
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
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT,requestEntity,Long.class);

        //then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(responseEntity.getBody() > (0L));
        List<Posts> postsList = postsRepository.findAll();
        assertEquals((postsList).get(0).getContent(),updateContent);
        assertEquals((postsList).get(0).getTitle(),updateTitle);
    }

    @Test
    public void test_FindById() {
        //given
        Posts posts = savePosts(title,content,author);
        Long savedId = posts.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + savedId;

        //when
        ResponseEntity<PostsResponseDto> responseEntity = restTemplate.getForEntity(url,PostsResponseDto.class);

        //then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody().getTitle(), title);
        assertEquals(responseEntity.getBody().getContent(), content);
        assertEquals(responseEntity.getBody().getAuthor(), author);
    }

    @Test
    public void test_Delete() {
        //given
        Posts posts = savePosts(title, content, author);
        Long savedId = posts.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + savedId;
        HttpEntity<Long> requestEntity = new HttpEntity<>(savedId);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,requestEntity,Long.class);
        //then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(responseEntity.getBody() > 0L);
    }
}