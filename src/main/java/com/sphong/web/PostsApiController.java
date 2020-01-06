package com.sphong.web;

import com.sphong.web.dto.PostsUpdateRequestDto;
import com.sphong.web.dto.PostsRequestDto;
import com.sphong.web.dto.PostsResponseDto;
import com.sphong.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostService postService;

    //Create
    @PostMapping ("/api/v1/posts")
    public Long save(@RequestBody PostsRequestDto requestDto) {
        return postService.save(requestDto);
    }

    //Read
    @GetMapping ("/api/v1/posts/{id}")
    public PostsResponseDto findByID(@PathVariable Long id) {
        return postService.findById(id);
    }

    //Update
    @PutMapping ("api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postService.update(id,requestDto);
    }

    //Delete
    @DeleteMapping ("api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }
}

