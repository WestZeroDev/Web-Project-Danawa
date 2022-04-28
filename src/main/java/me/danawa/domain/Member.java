package me.danawa.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	
	@NotNull @Column(unique = true)
	private String email;
	
	@NotNull
	private String pwd;
	
	@NotNull
	private String name;
	
	@NotNull @Column(unique = true)
	private String nickname;
	
	@NotNull @Column(name = "joindate")
	private LocalDate joinDate = LocalDate.now();
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	private List<Wish> wishList = new ArrayList<>();
}
