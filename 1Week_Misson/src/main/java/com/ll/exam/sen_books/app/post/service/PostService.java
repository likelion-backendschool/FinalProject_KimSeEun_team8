package com.ll.exam.sen_books.app.post.service;

import com.ll.exam.sen_books.app.hashTag.service.HashTagService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.dto.CreatePost;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.repository.PostRepository;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final HashTagService hashTagService;

    @Transactional
    public Post write(Member author, CreatePost createPostDto) {
        Post post = Post.builder()
                .subject(createPostDto.getSubject())
                .author(author)
                .contentHtml(Ut.markdown(createPostDto.getContent()))
                .content(createPostDto.getContent())
                .hashTagContent(createPostDto.getHashTagContents())
                .build();

        postRepository.save(post);

        hashTagService.applyHashTags(post, createPostDto.getHashTagContents());

        return post;
    }

    public Optional<Post> getPostId(long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void modify(Post post, String subject, String content) {
        post.modify(subject, content, Ut.markdown(content));
        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> getAllAuthorId(Long id) {
        return postRepository.findAllByAuthorId(id);
    }
}
