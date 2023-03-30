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

        System.out.println(driver.getTitle());
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getPageSource());
//  System.out.println(driver.getPageSource());

        List<WebElement> elements = driver.findElements(By.cssSelector(".ty05"));

//        System.out.println(driver.findElements(By.cssSelector(".ty02")));

//        List<WebElement> noticeElements = driver.findElements(By.cssSelector(".list_item > a"));

        System.out.println("noticeElements = " + elements);

        List<NoticeDto> notices = new ArrayList<>();

        for (WebElement noticeElement : elements) {
            String title = noticeElement.findElement(By.cssSelector(".title")).getText();
            String link = noticeElement.getAttribute("href");

            WebElement dateElement = noticeElement.findElement(By.cssSelector(".time"));
            String dateText = dateElement.getText();
            LocalDateTime date = LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

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


//    public List<NoticeDto> crawlUpbitNotices() {
//        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe"); // 윈도우
////        System.setProperty("webdriver.chrome.driver", "./chromedriver"); // 리눅스, 맥
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        WebDriver driver = new ChromeDriver(options);
//
//        driver.get("https://upbit.com/service_center/notice");
//
//        System.out.println(driver.getTitle());
//        System.out.println(driver.getCurrentUrl());
////        System.out.println(driver.getPageSource());
//
//
//        List<WebElement> noticeElements = driver.findElements(By.cssSelector(".notices .list_item .ty02 > div.ty05 > a"));
//
////        List<WebElement> noticeList = driver.findElements(By.cssSelector(".ty05"));
//
//        System.out.println("noticeList = " + noticeElements);
//
//        List<NoticeDto> notices = new ArrayList<>();
//
//        for (WebElement noticeElement : noticeElements) {
//            String title = noticeElement.findElement(By.cssSelector(".title")).getText();
//            String link = noticeElement.findElement(By.tagName("a")).getAttribute("href");
//            String dateText = noticeElement.findElement(By.cssSelector(".time")).getText();
//            LocalDateTime date = LocalDateTime.parse(dateText, DateTimeFormatter .ofPattern("yyyy-MM-dd HH:mm"));
//
//            Notice notice = Notice.builder()
//                    .title(title)
//                    .link(link)
//                    .date(date)
//                    .build();
//
//            noticeRepository.save(notice);
//
//            notices.add(new NoticeDto(notice));
//        }
//
//        driver.quit();
//
//        return notices;
//    }
}

