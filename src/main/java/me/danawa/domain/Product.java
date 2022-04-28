package me.danawa.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="PROD")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "prod_id")
	private Long id;
	
	@NotNull @Column(unique = true)
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
	
	@NotNull @Column(name = "regdate")
	private String regDate;
	
	@NotNull @ColumnDefault("0")
	private int price;
	
	@NotNull
	private String image;
	
	@NotNull
	private String spec;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	private List<PriceInfo> priceInfoList = new ArrayList<>();
}
