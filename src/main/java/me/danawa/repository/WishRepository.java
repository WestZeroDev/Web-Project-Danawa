package me.danawa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.domain.Member;
import me.danawa.domain.Product;
import me.danawa.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
	public void deleteByMemberAndProduct(Member m, Product p);
}
