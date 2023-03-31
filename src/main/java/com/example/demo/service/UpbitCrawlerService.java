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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpbitCrawlerService {
    // NoticeRepository 주입
    private final NoticeRepository noticeRepository;

    // 생성자를 통한 의존성 주입
    @Autowired
    public UpbitCrawlerService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // Upbit 공지사항 크롤링 메소드
    public List<NoticeDto> crawlUpbitNotices() {
        // 크롬 드라이버 설정
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe"); // 윈도우
        // System.setProperty("webdriver.chrome.driver", "./chromedriver"); // 리눅스, 맥

        // 크롬 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        // Upbit 공지사항 페이지 접속
        driver.get("https://upbit.com/service_center/notice");

        // 공지사항 목록을 가져옵니다.
        List<WebElement> elements = driver.findElements(By.cssSelector("tr.top"));

        // 반환할 NoticeDto 리스트 생성
        List<NoticeDto> notices = new ArrayList<>();

        // 각 공지사항에 대하여
        for (WebElement noticeElement : elements) {
            // 공지사항의 제목과 링크를 가져옵니다.
            // "td.lAlign > a"는 "td" 태그 중 클래스가 "lAlign"인 태그의 자식 "a" 태그를 선택합니다.
            WebElement anchorElement = noticeElement.findElement(By.cssSelector("td.lAlign > a"));
            String title = anchorElement.getText(); // "a" 태그의 텍스트(제목)를 가져옵니다.
            String link = anchorElement.getAttribute("href"); // "a" 태그의 "href" 속성(링크)를 가져옵니다.

            // 제목과 링크 출력
            System.out.println("title = " + title);
            System.out.println("link = " + link);

            // 공지사항의 날짜를 가져옵니다.
            // "td:nth-child(2)"는 "td" 태그 중 두 번째 자식에 해당하는 태그를 선택합니다.
            WebElement dateElement = noticeElement.findElement(By.cssSelector("td:nth-child(2)"));
            String dateText = dateElement.getText(); // "td" 태그의 텍스트(날짜)를 가져옵니다.
            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy.MM.dd")); // 날짜 문자열을 LocalDate 객체로 변환합니다.

            // Notice 객체 생성 및 저장
            Notice notice = Notice.builder()
                    .title(title)
                    .link(link)
                    .date(date)
                    .build();

            // Notice 객체를 DB에 저장합니다.
            noticeRepository.save(notice);

            // 저장된 Notice 객체를 NoticeDto로 변환하여 리스트에 추가합니다.
            notices.add(new NoticeDto(notice));
        }

        // WebDriver 종료
        driver.quit();

        // NoticeDto 리스트 반환
        return notices;
    }
}

