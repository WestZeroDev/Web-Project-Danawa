package me.danawa.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
	
	@NotNull
	private String email;
	
	@NotNull
	private String nickname;
	
	@NotNull
	private String content;
	
	@NotNull @Column(name = "regdate")
	private LocalDate regDate = LocalDate.now();
}
