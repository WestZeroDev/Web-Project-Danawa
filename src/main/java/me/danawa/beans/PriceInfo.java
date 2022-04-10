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
import lombok.ToString;

@Entity
@Table(name="PRICE_INFO")
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfo {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pnum;
	@NotNull
	private String name;
	@NotNull
	private String sitename;
	@NotNull
	private String sitelogo;
	@NotNull
	private String link;
	@NotNull
	private String shipping;
	@NotNull
	private int price;
}
