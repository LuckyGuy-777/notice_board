package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service // 이 어노테이션이 있으면, 서비스로 인식함(서비스객체 생성가능)
public class ArticleService {
    @Autowired // 클래스를 불러와 , 객체를 주입함(클래스 내용을 불러오는듯)
    private ArticleRepository articleRepository;//리파지터리(데이터베이스)의 내용을 불러오는코드

    public List<Article> index() {
        return articleRepository.findAll(); // 리파지터리에서 데이터 전체를 가져옴
    }

    public Article show(Long id) {
        // 리파지터리에서 id값에 해당하는 값을 가져오고, 없다면 null 반환
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        // dto 를 엔티티로 변환
        Article article = dto.toEntity();
        if(article.getId() != null)
        {
            return null;
        }

        // article을 db에 저장
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {

        // 1. DTO -> 엔티티 변환하기
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());

        // 2. 타깃 조회하기
        Article target = articleRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리하기
        if(target == null || id != article.getId())
        {
            // 400 잘못된 요청 응답
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null; // 응답은 컨트롤러가 하므로 여기서는 null 반환
        }


        // 4. 업데이트 및 정상 응답(200) 하기
        target.patch(article);
        Article updated = articleRepository.save(target); // article 엔티티 db에 저장 , 수정내용 DB에 최종 저장
        return updated; // 응답은 컨트롤러가 하므로 여기서는 수정데이터만 반환

    }

    public Article delete(Long id) {
        // 1. DB에서 대상 엔티티가 있는지 조회
        Article target = articleRepository.findById(id).orElse(null);
        // 2. 대상 엔티티가 없어서 요청자체가 잘못됬을경우 처리
        if(target == null){
            return null;
        }
        // 3. 대상 엔티티가 있으면 삭제하고 정상응답(200) 반환하기
        articleRepository.delete(target);
        return target; // DB에서 삭제한 대상을 컨트롤러에 반환
    }

    @Transactional // 트랜잭션 선언.
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());
        
        // 2. 엔티티 묶음을 DB에 저장하기
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // 3. 강제 예외 발생시키기
        articleRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));//찾는데이터가 없으면 예외 발생
        
        // 4. 결과 값 반환하기
        return articleList;
    }
}
