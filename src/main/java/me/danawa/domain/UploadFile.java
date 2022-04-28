package me.danawa.domain;

import java.time.LocalDate;

import javax.persistence.Column;
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
@Table(name="UPLOAD_FILE")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long id;
	
	@NotNull
	private String originalName;
	
	@NotNull
	private String savedName;
	
	@NotNull
	private String path;
	
	@NotNull
	private Long size;
	
	@NotNull @Column(name = "regdate")
	private LocalDate regDate = LocalDate.now();
}
