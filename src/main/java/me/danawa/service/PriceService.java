package me.danawa.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.PriceInfo;
import me.danawa.repository.PriceRepository;

@Service
@RequiredArgsConstructor
public class PriceService {
	private final PriceRepository priceRepository;

	public PriceInfo save(PriceInfo price) {
		return priceRepository.save(price);
	}
}
