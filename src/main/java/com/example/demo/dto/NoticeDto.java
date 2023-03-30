package com.example.demo.dto;

import com.example.demo.domain.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDto {

    private String title;
    private String link;
    private LocalDateTime date;

    public NoticeDto(Notice notice) {
        this.title = notice.getTitle();
        this.link = notice.getLink();
        this.date = notice.getDate();
    }
}
