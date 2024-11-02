package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor // ArticleForm() 생성자 간소화목적 어노테이션
@ToString // toString() 메서드를 사용하는것과 같은 효과.
public class ArticleForm {

    private Long id; // id 필드 추가
    
    private String title; //제목을 받을 필드
    
    private String content; // 내용을 받을 필드

    // 전송받은 제목과 내용을 필드에 저장하는 생성자 추가


    // 데이터를 잘 받았는지 확인할 toString() 메서드 추가

    public Article toEntity()
    {
        return new Article(id, title, content); // null -> id로 수정
    }
}
