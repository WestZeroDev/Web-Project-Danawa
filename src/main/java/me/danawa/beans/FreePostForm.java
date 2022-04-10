package me.danawa.beans;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FreePostForm {
	private String title;
	private String content;
	private List<Long> fileList;
}
