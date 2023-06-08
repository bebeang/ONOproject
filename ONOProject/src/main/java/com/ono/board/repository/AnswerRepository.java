package com.ono.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ono.board.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{

}
