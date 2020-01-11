package com.sphong.domain.posts;

import jdk.nashorn.internal.runtime.regexp.joni.constants.TargetInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();;
    }

    @Test
    public void testPostsRepository() {
        String title = "seongpyo";
        String content = "content1";
        String author = "sphong0417";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author).build());

        List<Posts> postsList = postsRepository.findAll();

        assertEquals(postsList.get(0).getTitle(), title);
        assertEquals(postsList.get(0).getContent(),content);
        assertEquals(postsList.get(0).getAuthor(),author);
    }
}