package com.example.demo.service;


import com.example.demo.domain.Notice;
import com.example.demo.dto.NoticeDto;
import com.example.demo.repository.NoticeRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class UpbitCrawlerService {
    private final NoticeRepository noticeRepository;

    @Autowired
    public UpbitCrawlerService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }
    public List<NoticeDto> crawlUpbitNotices() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe"); // 윈도우
//  System.setProperty("webdriver.chrome.driver", "./chromedriver"); // 리눅스, 맥

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://upbit.com/service_center/notice");

        List<WebElement> elements = driver.findElements(By.cssSelector("tr.top"));

        System.out.println("noticeElements = " + elements);

        List<NoticeDto> notices = new ArrayList<>();

        for (WebElement noticeElement : elements) {

            WebElement anchorElement = noticeElement.findElement(By.cssSelector("td.lAlign > a"));
            String title = anchorElement.getText();
            String link = anchorElement.getAttribute("href");

            System.out.println("title = " + title);
            System.out.println("link = " + link);

            WebElement dateElement = noticeElement.findElement(By.cssSelector("td:nth-child(2)"));
            String dateText = dateElement.getText();
            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

            Notice notice = Notice.builder()
                    .title(title)
                    .link(link)
                    .date(date)
                    .build();

            noticeRepository.save(notice);
            notices.add(new NoticeDto(notice));
        }

        driver.quit();

        return notices;
    }
}

