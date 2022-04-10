package me.danawa.beans;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="FREE_COMMENT")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreeComment {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long fcnum;
	@NotNull
	private long fnum;
	@NotNull
	private String fcemail;
	@NotNull
	private String fcnickname;
	@NotNull
	private String fccontent;
	@NotNull
	private LocalDate fcdate = LocalDate.now();
}
