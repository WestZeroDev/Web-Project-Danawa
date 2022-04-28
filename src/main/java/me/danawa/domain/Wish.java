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
public class Wish {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "wish_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email", referencedColumnName = "email")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_id")
	private Product product;
	
	@NotNull @Column(name = "regdate")
	private LocalDate regDate = LocalDate.now();
}
