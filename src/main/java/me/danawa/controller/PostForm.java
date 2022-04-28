package me.danawa.controller;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostForm {
	private String title;
	private String content;
	private List<Long> fileList;
}
