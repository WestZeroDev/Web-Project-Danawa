package me.danawa.beans;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private long id;
	private String originalName;
	private String savedName;
	private String path;
	private long size;
	private LocalDate date = LocalDate.now();
}
