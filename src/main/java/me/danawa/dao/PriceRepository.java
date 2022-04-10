package me.danawa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.PriceInfo;

public interface PriceRepository extends JpaRepository<PriceInfo, Long> {
	public List<PriceInfo> findAllByName(String name);
	public void deleteAllByName(String name);
}
