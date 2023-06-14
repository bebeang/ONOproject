package com.ono.board.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AnswerFormDto {
	
    @NotEmpty(message = "내용은 필수항목입니다.")
    @Column(name ="content")
    private String content;
}