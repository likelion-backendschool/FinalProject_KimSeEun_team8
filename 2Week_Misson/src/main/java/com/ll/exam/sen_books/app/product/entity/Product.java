package com.ll.exam.sen_books.app.product.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access=PROTECTED)
public class Product extends BaseEntity {
    private String subject;
    @ManyToOne
    private Member author;
    @ManyToOne(fetch = LAZY)
    private Post post;

    public Product(long id) {
        super(id);
    }
}