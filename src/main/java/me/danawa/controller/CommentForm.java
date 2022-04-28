package me.danawa.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {
	private Long postId;
	private Long commentId;
	private String comment;
}
