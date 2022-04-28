package me.danawa.domain;

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
public class PriceInfo {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "price_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_name", referencedColumnName = "name")
	private Product product;
	
	@NotNull
	private String siteName;
	
	@NotNull
	private String siteLogo;
	
	@NotNull
	private String link;
	
	@NotNull
	private String shipping;
	
	@NotNull
	private int price;
}
