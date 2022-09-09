package com.todoAppWithAws.book.springboot.web.dto;

import com.todoAppWithAws.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
