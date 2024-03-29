package com.ll.exam.sen_books.app.hashTag.repository;

import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByPostIdAndPostKeywordId(Long postId, Long keywordId);
    List<HashTag> findAllByPostId(Long postId);

    List<HashTag> findByPostId(Long postId);
}
