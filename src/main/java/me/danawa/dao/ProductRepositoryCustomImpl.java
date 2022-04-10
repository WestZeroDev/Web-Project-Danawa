package me.danawa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.danawa.beans.Product;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
	@PersistenceContext
	private final EntityManager em;
	
	public ProductRepositoryCustomImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Product> brandOption(List<String> brandList) {
		return em.createQuery("select p from Product p where p.brand in (:brandList)", Product.class)
				.setParameter("brandList", brandList)
				.getResultList();
	}

	@Override
	public List<Product> cpuOption(List<String> cpuList) {
		return em.createQuery("select p from Product p where p.cpu in (:cpuList)", Product.class)
				.setParameter("cpuList", cpuList)
				.getResultList();
	}

	@Override
	public List<Product> sizeOption(int[] sizeList) {
		String query = "select p from Product p where ";
		for(int i = 0; i < sizeList.length; i++) {
			query += "p.size >= " + sizeList[i] + " and p.size < " + (int)(sizeList[i] + 1);
			if(i != sizeList.length - 1) {
				query += " or ";
			}
		}
		return em.createQuery(query, Product.class).getResultList();
	}

	@Override
	public List<Product> memoryOption(List<String> memoryList) {
		return em.createQuery("select p from Product p where p.memory in (:memoryList)", Product.class)
				.setParameter("memoryList", memoryList)
				.getResultList();
	}
	
	@Override
	public List<Product> storageOption(int[] storageList) {
		String query = "select p from Product p where ";
		for(int i = 0; i < storageList.length; i++) {
			if(storageList[i] == 128) {
				query += "p.storage between 120 and 128";
			}
			else if(storageList[i] == 256) {
				query += "p.storage between 129 and 256";
			}
			else if(storageList[i] == 512) {
				query += "p.storage between 257 and 512";
			}
			else if(storageList[i] == 1000) {
				query += "p.storage between 513 and 1000";
			}
			else if(storageList[i] == 1001) {
				query += "p.storage > 1000";
			}
			if(i != storageList.length - 1) {
				query += " or ";
			}
		}
		return em.createQuery(query, Product.class).getResultList();
	}

	@Override
	public List<Product> osOption(List<String> osList) {
		return em.createQuery("select p from Product p where p.os in (:osList)", Product.class)
				.setParameter("osList", osList)
				.getResultList();
	}
	
	@Override
	public List<Product> weightOption(int[] weightList) {
		String query = "select p from Product p where ";
		for(int i = 0; i < weightList.length; i++) {
			if(weightList[i] == 10) {
				query += "p.weight < 1.0";
			}
			else if(weightList[i] == 1014) {
				query += "p.weight >= 1.0 and p.weight < 1.4";
			}
			else if(weightList[i] == 1417) {
				query += "p.weight >= 1.4 and p.weight < 1.7";
			}
			else if(weightList[i] == 1720) {
				query += "p.weight >= 1.7 and p.weight < 2.0";
			}
			else if(weightList[i] == 20) {
				query += "p.weight > 2.0";
			}
			if(i != weightList.length - 1) {
				query += " or ";
			}
		}
		return em.createQuery(query, Product.class).getResultList();
	}

	@Override
	public List<Product> priceOption(int minPrice, int maxPrice) {
		return em.createQuery("select p from Product p where p.price between :minPrice and :maxPrice", Product.class)
				.setParameter("minPrice", minPrice)
				.setParameter("maxPrice", maxPrice)
				.getResultList();
	}

	@Override
	public int updatePrice(String name, int price) {
		return em.createQuery("update Product p set p.price = :price where p.name = :name")
				.setParameter("price", price)
				.setParameter("name", name)
				.executeUpdate();
	}

}
