package me.danawa.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	@Id @Column(unique = true)
	private String email;
	@NotNull
	private String pwd;
	@NotNull
	private String name;
	@NotNull @Column(unique = true)
	private String nickname;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Long> wishList;
}
