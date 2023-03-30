package com.example.demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private String content;
    private String link;
    private LocalDateTime date;

    @Builder
    public Notice(String title, String content, String link, LocalDateTime date) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.date = date;
    }
}