package com.sphong.domain;

import com.sphong.domain.posts.Posts;
import com.sphong.domain.posts.PostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BaseTimeEntityTest {

    @Autowired
    private PostsRepository postsRepository;


    @Test
    public void test_CreatedTime_ModifiedTime () {
        //given
        LocalDateTime now = LocalDateTime.of(2020,1,10,0,0,0);
        String title = "title";
        String content = "content";
        String author = "author";

        //when
        postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
        //then
        List<Posts> postsList = postsRepository.findAll();
        assertTrue(postsList.get(0).getCreatedDate().isBefore(now));
        assertTrue(postsList.get(0).getCreatedDate().isBefore(now));
    }
}