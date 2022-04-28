package me.danawa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.domain.PriceInfo;
import me.danawa.domain.Product;

public interface PriceRepository extends JpaRepository<PriceInfo, Long> {
	public List<PriceInfo> findAllByProduct(Product prod);
	public void deleteAllByProduct(Product prod);
}
