package com.ll.exam.sen_books.app.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyPostDto {
    private String subject;
    private String content;
    private String hashTagContents;
}
