package com.sphong.web.service;

import com.sphong.domain.posts.Posts;
import com.sphong.domain.posts.PostsRepository;
import com.sphong.web.dto.PostsUpdateRequestDto;
import com.sphong.web.dto.PostsRequestDto;
import com.sphong.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                        .orElseThrow( () -> new
                                IllegalArgumentException("사용자 X. id = " + id ));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts entity = postsRepository.findById(id)
                        .orElseThrow( () -> new
                                IllegalArgumentException(("사용자 X. id = " + id)));

        entity.update(requestDto.getTitle(),requestDto.getContent());
        return id;
    }

    public Long delete(Long id) {
        postsRepository.deleteById(id);
        return id;
    }
}
