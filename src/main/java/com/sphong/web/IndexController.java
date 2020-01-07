package com.sphong.web;

import com.sphong.web.dto.PostsResponseDto;
import com.sphong.web.dto.PostsUpdateRequestDto;
import com.sphong.web.service.PostService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts",postService.findAllDesc());
        return "index";
    }

    @GetMapping("posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("posts/update/{id}")
        public String postsUpdate(@PathVariable Long id, Model mode) {
        PostsResponseDto responseDto = postService.findById(id);
        mode.addAttribute("post",responseDto);
            return "posts-update";
        }
}

