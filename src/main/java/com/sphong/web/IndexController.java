package com.sphong.web;


import com.sphong.config.auth.LoginUser;
import com.sphong.config.auth.dto.SessionUser;
import com.sphong.domain.user.User;
import com.sphong.web.dto.PostsResponseDto;
import com.sphong.web.dto.PostsUpdateRequestDto;
import com.sphong.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final HttpSession httpSession;
    private final PostService postService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userName",user.getName());
        }
        model.addAttribute("posts",postService.findAllDesc());
        return "index";
    }

    @GetMapping("posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("posts/update/{id}")
        public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto responseDto = postService.findById(id);
        model.addAttribute("post",responseDto);
            return "posts-update";
        }
}

