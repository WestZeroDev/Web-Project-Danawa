package me.danawa.controller;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @EqualsAndHashCode
public class SearchForm {
	public List<String> brand;
	public List<String> cpu;
	public int[] size;
	public List<String> memory;
	public int[] storage;
	public List<String> os;
	public int[] weight;
	public String minPrice;
	public String maxPrice;
	public String keyword;
}
