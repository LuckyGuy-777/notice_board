package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // JPA와 연동해 테스트한다는 뜻.(리파지터리를 테스트할때 씀)

    //테스트케이스 (CommentRepository)
class CommentRepositoryTest {
    @Autowired // 외부객체 주입용 어노테이션
    CommentRepository commentRepository; // commentRepository 객체 주입
    @Test
    @DisplayName("특정 게시글의 모든 댓글 조회") // 테스트이름을 붙일때 사용하는 어노테이션, 메서드 이름은 그대로 둔채 테스트이름을 바꾸고싶을때 사용
    void findByArticleId() {
        /* Case 1 : 4번 게시글의 모든 댓글 조회 */
        {
            // 1. 입력데이터 준비
            Long articleId = 4L; // 4번 게시글 조회 ( 4L )
            // 2. 실제 데이터
            List<Comment> comments = commentRepository.findByArticleId(articleId);

            // 3. 예상 데이터
            Article article = new Article(4L, "당신의 인생 영화는?", "댓글 고"); // 부모 게시글 객체생성

            Comment a = new Comment(1L, article, "Park", "굿 윌 헌팅"); // 댓글객체 생성
            Comment b = new Comment(2L, article, "Kim", "아이 엠 샘");
            Comment c = new Comment(3L, article, "Choi", "쇼생크 탈출");
            List<Comment> expected = Arrays.asList(a, b, c); // 댓글 객체 합치기

            // 4. 비교 및 검증. (왼쪽부터, 예상데이터의 문자열, 실제데이터의 문자열을 비교 함. 마지막전달값은 검증실패시 출력메시지
            assertEquals(expected.toString(), comments.toString(), "4번 글의 모든 댓글을 출력!");
        }
        /* Case 2 : 1번 게시글의 모든 댓글 조회 */
        {
            // 1. 입력데이터 준비
            Long articleId = 1L; // 조회할 id 수정
            // 2. 실제 데이터
            List<Comment> comments = commentRepository.findByArticleId(articleId);

            // 3. 예상 데이터
            Article article = new Article(1L, "가가가가", "1111");
            List<Comment> expected = Arrays.asList();

            // 4. 비교 및 검증. (왼쪽부터, 예상데이터의 문자열, 실제데이터의 문자열을 비교 함. 마지막전달값은 검증실패시 출력메시지
            assertEquals(expected.toString(), comments.toString(), "1번 글은 댓글이 없음"); // 메시지 수정
        }


    }

    @Test
    @DisplayName("특정 닉네임의 모든 댓글 조회")
    void findByNickname() {
        /* Case 1 : "Park" 의 모든 댓글 조회 */
        {
            // 1. 입력데이터 준비
            String nickname = "Park";

            // 2. 실제 데이터
            List<Comment> comments = commentRepository.findByNickname(nickname);
            
            // 3. 예상 데이터
            Comment a = new Comment(1L, new Article(4L, "당신의 인생 영화는?","댓글 고"),nickname,"굿 윌 헌팅");
            Comment b = new Comment(4L, new Article(5L, "당신의 소울 푸드는?","댓글 고고"),nickname,"치킨");
            Comment c = new Comment(7L, new Article(6L, "당신의 취미는?","댓글 고고고"),nickname,"조깅");

            List<Comment> expected = Arrays.asList(a,b,c); // 댓글객체 합치기
            // 4. 비교 및 검증
            assertEquals(expected.toString(), comments.toString(), "Park의 모든 댓글을 출력!");
        }
    }
}