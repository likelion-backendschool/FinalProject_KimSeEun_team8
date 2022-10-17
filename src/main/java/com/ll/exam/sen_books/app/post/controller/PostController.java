package com.ll.exam.sen_books.app.post.controller;


import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.form.PostForm;
import com.ll.exam.sen_books.app.post.repository.PostRepository;
import com.ll.exam.sen_books.app.post.service.PostService;
import com.ll.exam.sen_books.app.security.dto.MemberContext;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final PostRepository postRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String write(
    ) {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid PostForm postForm) {
        Member author = memberContext.getMember();
        Post post = postService.create( author , postForm.getSubject(), postForm.getContent());
        return "post/write";
    }

//    @PreAuthorize("isAnonymous()")
//    @GetMapping("/list")
//    public String findAll(int page) {
//        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("registerTime").descending());
//
//
//
//
//
//        postService.findAll(pageRequest));
//
//
//        return "";
//    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/{id}")
    public String detail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member author = memberContext.getMember();

        if (postService.authorCanModify(author, post) ==false) {
            throw new RuntimeException();
        }

        model.addAttribute("post", post);

        return "post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member author = memberContext.getMember();

        if (postService.authorCanModify(author, post) == false) {
            throw new RuntimeException();
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modifyPost(@AuthenticationPrincipal MemberContext memberContext, @Valid PostForm postForm, @PathVariable long id) {
        Post post = postService.findById(id).get();
        Member author = memberContext.getMember();

        if (postService.authorCanModify(author, post) == false) {
            throw new RuntimeException();
        }

        postService.modify(post, postForm.getSubject(), postForm.getContent());
        return "redirect:/post/" + post.getAuthorId() + "msg=" + Ut.url.encode("%d번 음원이 수정되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String deletePost (@AuthenticationPrincipal MemberContext memberContext, @Valid PostForm postForm, @PathVariable long id) {
        Post post = postService.findById(id).get();
        Member author = memberContext.getMember();

        if (postService.authorCanModify(author, post) == false) {
            throw new RuntimeException();
        }

        postService.delete(post);

        return "";
    }

}
