package me.danawa.beans;

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
@Table(name="REPLY_LIKE")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long lnum;
	@NotNull
	private long fnum;
	@NotNull
	private String lemail;
}
