package me.danawa.beans;

import javax.persistence.Entity;
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
@Table(name="PROD")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkey;
	@NotNull
	private String name;
	@NotNull
	private String brand;
	@NotNull
	private String cpu;
	@NotNull
	private double size;
	@NotNull
	private String memory;
	@NotNull
	private int storage;
	@NotNull
	private String os;
	@NotNull
	private double weight;
	@NotNull
	private String regdate;
	@NotNull @ColumnDefault("0")
	private int price; //최저가
	@NotNull
	private String image; //이미지 경로
	@NotNull
	private String spec;
}
