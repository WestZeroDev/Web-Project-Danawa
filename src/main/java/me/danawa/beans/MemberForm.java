package me.danawa.beans;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
	private String email;
	private String pwd;
	private String newPwd;
	private String name;
	private String nickname;
}
