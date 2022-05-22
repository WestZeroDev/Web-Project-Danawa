package me.danawa.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.Member;
import me.danawa.domain.Product;
import me.danawa.domain.Wish;
import me.danawa.repository.WishRepository;

@Service
@RequiredArgsConstructor
public class WishService {
	private final WishRepository wishRepository;
	
	public void save(Wish wish) {
		wishRepository.save(wish);
	}
	
	public void delete(Wish wish) {
		wishRepository.delete(wish);
	}
	
	public void delete(Member m, Product p) {
		wishRepository.deleteByMemberAndProduct(m, p);
	}

}
