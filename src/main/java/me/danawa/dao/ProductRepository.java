package me.danawa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	public List<Product> findBySpecContainsIgnoreCase(String keyword);
	public List<Product> findByNameContainsIgnoreCase(String keyword);
	public Optional<Product> findByName(String name);
}
