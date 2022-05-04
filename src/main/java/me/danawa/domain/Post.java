package me.danawa.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;
	
	@NotNull
	private String email;
	
	@NotNull
	private String nickname;
	
	@NotNull
	private String title;
	
	@NotNull
	private String content;
	
	@NotNull @Column(name = "regdate")
	private LocalDate regDate = LocalDate.now();
	
	@NotNull @ColumnDefault("0")
	private int view;
	
	@NotNull @ColumnDefault("0")
	@Column(name = "likes")
	private int like;
	
	@NotNull @ColumnDefault("0")
	private int comment;
	
	@ElementCollection(fetch = FetchType.LAZY)
	private List<Long> fileList = new ArrayList<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Like> likeList = new ArrayList<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comment> commentList = new ArrayList<>();
	
	public Post updateView() {
		this.view += 1;
		return this;
	}
	
	public Post updateLike() {
		this.like = likeList.size();
		return this;
	}
	
	public Post updateComment() {
		this.comment = commentList.size();
		return this;
	}
	
}
