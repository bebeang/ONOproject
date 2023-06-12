package com.ono.board.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class UploadFile {

	@Id
	@GeneratedValue
	@Column(name = "id")
	int id;
	
	@Column(name = "filename")
	String fileName;
	
	@Column(name = "savefilename")
	String saveFileName;
	
	@Column(name = "filepath")
	String filePath;
	
	@Column(name = "contenttype")
	String contentType;
	
	@Column(name = "filesize")
	long fileSize;
	
	@Column(name = "regdate")
	Date regDate;
}
