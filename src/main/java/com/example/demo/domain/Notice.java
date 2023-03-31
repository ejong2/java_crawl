package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    private String link;
    @Column(nullable = false)
    private LocalDate date;

    @Builder
    public Notice(String title, String link, LocalDate date) {
        this.title = title;
        this.link = link;
        this.date = date;
    }
}