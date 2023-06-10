package com.ono.board.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ono.board.domain.Answer;
import com.ono.board.domain.Recipe;
import com.ono.board.domain.SiteUser;
import com.ono.board.repository.AnswerRepository;
import com.ono.board.util.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	public Answer create(Recipe recipe, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setRecipe(recipe);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		return answer;
	}
	
	// 답변 조회
	public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }
	
	// 답변 수정
	public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
	
	// 답변 삭제
	public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
	
	// 답변 추천
	public void vote(Answer answer, SiteUser siteuser) {
		answer.getVoter().add(siteuser);
		this.answerRepository.save(answer);
	}
	
}
