package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {

    @Autowired // 스프링 부트가 미리 생성해놓은 리파지터리 객체 주입(DI)
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/new")
    public String newArticleForm(){

        return "articles/new";

    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){
        
        //DTO에 폼 데이터가 잘 담겼는지 확인
        log.info(form.toString());
        //System.out.println(form.toString());
        
        // 1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        log.info(article.toString());
        //System.out.println(article.toString());

        // 2. 리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        //System.out.println(saved.toString());

        //article엔티티를 저장해 save 객체에 반환
        return "redirect:/articles/" + saved.getId(); // id 값을 가져오기위해 saved 객체 이용
    }

    @GetMapping("articles/{id}")
    public String show(@PathVariable Long id, Model model){// 매개변수로 id 받아오기
        log.info("id = "+ id);
        // 데이터 조회해 출력하기

        // 1. id를 조회해 DB에서 해당 데이터 가져오기
       Article articleEntity = articleRepository.findById(id).orElse(null);
       List<CommentDto> commentsDtos = commentService.comments(id);

        // 2. 가져온 데이터를 모델에 등록
        model.addAttribute("article",articleEntity);
        model.addAttribute("commentDtos",commentsDtos); // 댓글 목록 모델에 등록
        
        // 3. 조회한 데이터를 사용자에게 보여주기 위한 뷰페이지 만들고 반환하기
        return "articles/show";
    }
    @GetMapping("/articles")
    public String index(Model model){ // model 객체 받아오기
        
        // 1. DB에서 모든 Article 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();

        // 2. 가져온 Article 묶음을 모델에 등록하기
        model.addAttribute("articleList",articleEntityList);

        // 3. 사용자에게 보여줄 뷰 페이지 설정
        return "articles/index";

    }
    
    // 컨트롤러에서 URL변수를 쓸때는 중괄호{} 하나만 씁니다 -> {id}
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){ // 모델 객체 받아오기

        // DB 에서 수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터 등록하기 ( articleEntity를 article로 등록 )
        model.addAttribute("article", articleEntity);

        // 뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) //매개변수로 DTO 받아오기
    {
        log.info(form.toString());
        // 1. DTO를 엔티티로 변환
        Article articleEntity = form.toEntity(); // DTO(form)을 엔티티(articleEntity)로 변환
        log.info(articleEntity.toString());//엔티티로 잘 변환됬는지 로그 찍기

        // 2. 엔티티를 DB에 저장

        // 2.1 DB에서 기존데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        // 2.2 기존데이터 값 갱신
        if(target != null)
        {
            articleRepository.save(articleEntity); // 엔티티를 DB에 저장(갱신)
        }

        // 3. 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr)
    {
        log.info("삭제 요청이 들어왔습니다!");

        //1. 삭제할 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        //2. 대상 엔티티 삭제하기
        if(target != null)
        {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제됐습니다!");
        }

        //3. 결과페이지로 리다이렉트
        return "redirect:/articles";
    }
}
