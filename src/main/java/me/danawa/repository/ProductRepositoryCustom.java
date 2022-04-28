package me.danawa.repository;

import java.util.List;

import me.danawa.domain.Product;

public interface ProductRepositoryCustom {
	public List<Product> brandOption(List<String> brandList);
	public List<Product> cpuOption(List<String> cpuList);
	public List<Product> sizeOption(int[] sizeList);
	public List<Product> memoryOption(List<String> memoryList);
	public List<Product> storageOption(int[] storageList);
	public List<Product> osOption(List<String> osList);
	public List<Product> weightOption(int[] weightList);
	public List<Product> priceOption(int minPrice, int maxPrice);
	public int updatePrice(String name, int price);
}
