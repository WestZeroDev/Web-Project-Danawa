package me.danawa.beans;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="FREE_POST")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreePost {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long fnum;
	@NotNull
	private String femail;
	@NotNull
	private String fnickname;
	@NotNull
	private String ftitle;
	@NotNull
	private String fcontent;
	@NotNull
	private LocalDate fdate = LocalDate.now();
	@NotNull @ColumnDefault("0")
	private int fcount;
	@NotNull @ColumnDefault("0")
	private int flike;
	@NotNull @ColumnDefault("0")
	private int fcomment;
	@ElementCollection(fetch = FetchType.LAZY)
	private List<Long> fileList;
}
