package me.danawa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.PriceInfo;
import me.danawa.dao.PriceRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PriceService {
	private final PriceRepository priceRepository;

	public PriceInfo save(PriceInfo price) {
		return priceRepository.save(price);
	}
	
	public List<PriceInfo> getPriceList(String name) {
		return priceRepository.findAllByName(name);
	}
}
