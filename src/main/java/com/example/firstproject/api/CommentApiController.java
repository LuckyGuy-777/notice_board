package com.example.firstproject.api;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST 컨트롤러 선언
public class CommentApiController {
    @Autowired
    private CommentService commentService; // 댓글서비스 객체 주입
    // 1. 댓글 조회
    @GetMapping("/api/articles/{articleId}/comments")
    private ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId){
        // 서비스에 위임
        List<CommentDto> dtos = commentService.comments(articleId);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 2. 댓글 생성

    @PostMapping("/api/articles/{articleId}/comments") //댓글생성 요청 접수
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId, @RequestBody CommentDto dto)
    {
        // 서비스에 댓글 생성 작업 위임
        CommentDto createDto = commentService.create(articleId, dto);
        // 결과 응답
        
        return ResponseEntity.status(HttpStatus.OK).body(createDto);
    }

    // ResponseEntity 는 url 요청을 보낼때 사용함(댓글 생성결과를 보내야 하므로)
    // RequestBody는 http에서 오는 Json 데이터를, 자바객체에 매핑할때 사용하는 어노테이션
    // 그리고, RequestBody는, Http 요청 본문에 실린내용을(ex : JSON, XML, YAML)을 자바객체로 변환해줌
    // PathVariable은, url의 {xx} 의 값을 매개변수로 사용할떄 씀


    // 3. 댓글 수정
    @PatchMapping("/api/comments/{id}") // 댓글 수정 요청 접수
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody CommentDto dto){

        // "댓글수정작업을 서비스에 위임하기위해" CommentService의 update(id, dto)메서드를 호출함
        // 수정결과는 CommentDto타입의 updateDto라는 변수로 받음
        CommentDto updatedDto = commentService.update(id,dto);

        
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto); //댓글수정 성공시 반환(200)
    }


    
    // 4. 댓글 삭제
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id)
    {
        // 서비스에 작업 위임
        CommentDto deleteDto = commentService.delete(id); //삭제할 댓글의 id가 매개변수로 전해짐
        
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(deleteDto); // 성공시, 상태는OK, 본문에는 삭제한 댓글데이터 deleteDto를 보냄
    }
    
    
    
    // ResponseEntity는 CRUD의 결과를 보내는 역할을 하는 메서드
}
