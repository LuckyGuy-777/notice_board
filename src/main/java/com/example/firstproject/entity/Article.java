package com.example.firstproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor // 기본생성자를 추가해주는 대신에 쓰는 어노테이션
@AllArgsConstructor // 생성자를 자동으로 추가해주는 대신에 쓰는 어노테이션
@ToString // toString 대신에 사용하는 어노테이션
@Entity // toEntity 대신에 사용하는 어노테이션
@Getter // ----- 롬복으로 게터 추가 Controller/ArticleController의 getId
public class Article {
    @Id // 엔티티의 대푯값 을 ID 로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id 자동 생성
    private Long id;
    @Column // title 필드 선언, DB 테이블의 title열과 연결
    private String title;
    @Column // content 필드 선언, DB 테이블의 content 열과 연결
    private String content;

    // 갱신할 값이 있다면, this(target)의 title, content를 갱신함
    public void patch(Article article) {
        if(article.title != null)
            this.title = article.title;
        if(article.content != null)
            this.content = article.content;
    }

    //Article 생성자 추가
}
