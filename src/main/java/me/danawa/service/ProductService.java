package me.danawa.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.PriceInfo;
import me.danawa.domain.Product;
import me.danawa.domain.Wish;
import me.danawa.repository.PriceRepository;
import me.danawa.repository.ProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final PriceRepository priceRepository;
	
	public final String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
	public final String WEB_DRIVER_PATH = "E:\\crawling\\chromedriver.exe"; //드라이버 경로
	
	public Product save(Product prod) {
		return productRepository.save(prod);
	}
	
	public Optional<Product> findById(Long prodId) {
		return productRepository.findById(prodId);
	}
	
	//노트북 전체
	public Page<Product> getProdList(Pageable pageable, String sort) {
		pageable = sort(pageable, sort);
		return productRepository.findAll(pageable);
	}
	
	//관심상품 목록
	public Page<Product> getWishProdList(List<Wish> wishList, Pageable pageable, String sort) {
		List<Product> prodList = new ArrayList<>();
		for(Wish wish : wishList) {
			prodList.add(wish.getProduct());
		}
		
		if(sort.equals("none")) {
			Collections.reverse(prodList);
		}
		else {
			sort(prodList, sort);
		}
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        	pageable = PageRequest.of(page, 10);
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), prodList.size());
		return new PageImpl<>(prodList.subList(start, end), pageable, prodList.size());
    	}
	
	//메인 검색창
	public Page<Product> mainSearch(Pageable pageable, String keyword) {
		List<Product> resultList = productRepository.findBySpecContainsIgnoreCase(keyword);
		resultList.addAll(productRepository.findByNameContainsIgnoreCase(keyword));
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        	pageable = PageRequest.of(page, 5);
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), resultList.size());
		return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
	}
	
	//노트북 상세검색
	public Page<Product> search(List<String> brand,	List<String> cpu, int[] size,
			List<String> memory, int[] storage, List<String> os, int[] weight,
			String sort, String minPrice, String maxPrice, String keyword, Pageable pageable) {
		
		List<List<Product>> resultList = new ArrayList<>();
		List<Product> prodList = new ArrayList<>();
		
		if(brand != null) {
			resultList.add(productRepository.brandOption(brand));
		}
		if(cpu != null) {
			resultList.add(productRepository.cpuOption(cpu));
		}
		if(size != null) {
			resultList.add(productRepository.sizeOption(size));
		}
		if(memory != null) {
			resultList.add(productRepository.memoryOption(memory));
		}
		if(storage != null) {
			resultList.add(productRepository.storageOption(storage));
		}
		if(os != null) {
			resultList.add(productRepository.osOption(os));
		}
		if(weight != null) {
			resultList.add(productRepository.weightOption(weight));
		}
		if(minPrice != "" && maxPrice != "") {
			int min = Integer.parseInt(minPrice);
			int max = Integer.parseInt(maxPrice);
			resultList.add(productRepository.priceOption(min, max));
		}
		if(keyword != "") {
			List<Product> tmp = productRepository.findBySpecContainsIgnoreCase(keyword);
			tmp.addAll(productRepository.findByNameContainsIgnoreCase(keyword));
			resultList.add(tmp);
		}
		
		for(int i = 0; i < resultList.size(); i++) {
			if(resultList.get(i).size() == 0) {
				prodList.clear();
				break;
			}
			else {
				if(i == 0) prodList = resultList.get(i);
				else prodList.retainAll(resultList.get(i));
			}
		}
		
		sort(prodList, sort);
		
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        	pageable = PageRequest.of(page, 10);
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), prodList.size());
		return new PageImpl<>(prodList.subList(start, end), pageable, prodList.size());
	}
	
	//정렬
	public void sort(List<Product> prodList, String sort) {
		if(sort.equals("minPrice")) {
			prodList.sort(new Comparator<Product>() {
				@Override
				public int compare(Product o1, Product o2) {
					if(o1.getPrice() > o2.getPrice()) return 1;
					else if(o1.getPrice() == o2.getPrice()) return o1.getRegDate().compareTo(o2.getRegDate()) * -1;
					else return -1;
				}
			});
		}
		else if(sort.equals("maxPrice")) {
			prodList.sort(new Comparator<Product>() {
				@Override
				public int compare(Product o1, Product o2) {
					if(o1.getPrice() < o2.getPrice()) return 1;
					else if(o1.getPrice() == o2.getPrice()) return o1.getRegDate().compareTo(o2.getRegDate()) * -1;
					else return -1;
				}
			});
		}
		else if(sort.equals("latest")) {
			prodList.sort(new Comparator<Product>() {
				@Override
				public int compare(Product o1, Product o2) {
					return o1.getRegDate().compareTo(o2.getRegDate()) * -1;
				}
			});
		}
	}
	
	//정렬
	public Pageable sort(Pageable pageable, String sort) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		if(sort.equals("latest")) {
			pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "regDate").and(Sort.by(Sort.Direction.DESC, "id")));
		}
		else if(sort.equals("minPrice")) {
			pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "price").and(Sort.by(Sort.Direction.DESC, "regDate")).and(Sort.by(Sort.Direction.DESC, "id")));
		}
		else if(sort.equals("maxPrice")) {
			pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "price").and(Sort.by(Sort.Direction.DESC, "regDate")).and(Sort.by(Sort.Direction.DESC, "id")));
		}
		return pageable;
	}
	
	//제품정보, 가격정보 크롤링
	public void crawling() {
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		
		String url = "http://prod.danawa.com/list/?cate=11236463";
		driver.get(url);
		
		//90개 보기
//		driver.findElement(By.className("qnt_selector")).click();
//		driver.findElement(By.xpath("//*[@id=\"productListArea\"]/div[2]/div[2]/div[2]/select/option[3]")).click();
	
		//품절 상품 제외
		driver.findElement(By.xpath("//*[@id=\"danawa_content\"]/div[6]/div/div[1]/div/a")).click();
		driver.findElement(By.xpath("//*[@id=\"defaultExptFilterArea\"]/div[3]/label/span")).click();
		driver.findElement(By.xpath("//*[@id=\"detailSearchSubmitBtn\"]")).click();

		try {Thread.sleep(3000);} catch(InterruptedException e) {}
		
		List<WebElement> ahref =  driver.findElements(By.cssSelector(".prod_name a"));
		List<String> prodList = new ArrayList<>();
		for(WebElement e : ahref) {
			prodList.add(e.getAttribute("href"));
		}
		
		for(int i = 0; i < prodList.size(); i++) {
			if(prodList.get(i).contains("pcode")) {
				String prodUrl = prodList.get(i);
				try {Thread.sleep(3000);} catch(InterruptedException e) {}
				driver.get(prodUrl); //url 바꿔주기

				WebElement nameEle = driver.findElement(By.className("prod_tit"));
				String name = nameEle.getText(); //제품명
				
				if(productRepository.findByName(name).isPresent()) continue;
				if(name.contains("FX516PR-HN002")) continue;
				
				System.out.println(name);
				
				/* 노트북 정보 */
	    			WebElement imageEle = driver.findElement(By.cssSelector("#baseImage"));
	            		String image = imageEle.getAttribute("src"); //이미지
	            
				List<WebElement> specEle = driver.findElements(By.className("items"));
				String spec = "";
				for(WebElement e : specEle) {
					spec += e.getText();
				}
				System.out.println(spec); //상세정보

				List<WebElement> field = driver.findElements(By.cssSelector("th.tit"));
				List<WebElement> value = driver.findElements(By.cssSelector("td.dsc"));

				String brand = "";
				String cpu = "";
				double size = 0;
				String memory = "";
				int storage = 0; 
				String os = "";
				double weight = 0;
				String regdate = "";

				for(int j = 0; j < field.size(); j++) {
					if(field.get(j).getText().equals("제조회사")) {
						String[] res = value.get(j).getText().split(" ");
						brand = res[0];
					}
					else if(field.get(j).getText().equals("등록년월")) {
						regdate = value.get(j).getText();
					}
					else if(field.get(j).getText().equals("화면 크기")) {
						String str = value.get(j).getText();
						String[] res = str.split("\\(");
						size = Double.parseDouble(res[1].replace("인치)", ""));
					}
					else if(field.get(j).getText().equals("CPU 종류")) {
						cpu = value.get(j).getText();
					}
					else if(field.get(j).getText().contains("운영체제")) {
						os = value.get(j).getText();
					}
					else if(field.get(j).getText().equals("메모리")) {
						memory = value.get(j).getText();
					}
					else if(field.get(j).getText().equals("저장 용량")) {
						String res = value.get(j).getText().replaceAll("[^0-9]", "");
						storage = Integer.parseInt(res);
						if(storage < 10) {
							storage *= 1000;
						}
					}
					else if(field.get(j).getText().equals("무게")) {
						String res = value.get(j).getText().replaceAll("[a-z]", "");
						weight = Double.parseDouble(res);
						if(weight > 100) weight /= 1000;
					}
				}

				Product prod = new Product();
				prod.setName(name);
				prod.setBrand(brand);
				prod.setCpu(cpu);
				prod.setMemory(memory);
				prod.setOs(os);
				prod.setSize(size);
				prod.setStorage(storage);
				prod.setWeight(weight);
				prod.setImage(image);
				prod.setRegDate(regdate);
				prod.setSpec(spec);
				productRepository.save(prod);
				
				/* 가격정보 */
				List<WebElement> priceEle = driver.findElements(By.cssSelector(".high_list .price .prc_t"));
				List<Integer> priceList = new ArrayList<>();
				for(WebElement e : priceEle) {
					if (e.getText().contains(",")) {
						priceList.add(Integer.parseInt(e.getText().replace(",", ""))); //가격
					}
				}
				
				List<WebElement> linkEle = driver.findElements(By.cssSelector(".high_list .logo_over a"));
				List<String> linkList = new ArrayList<>();
				for(WebElement e : linkEle) {
					if(!(e.getAttribute("href").contains("ajax"))) {
						linkList.add(e.getAttribute("href")); //쇼핑몰 링크
					}
				}

				List<String> logoList = new ArrayList<>();
				List<String> siteNameList = new ArrayList<>();
				List<WebElement> imgEle = driver.findElements(By.cssSelector(".high_list .logo_over img"));
				for(WebElement e : imgEle) {
					if(!e.getAttribute("src").contains("noImg")) {
						logoList.add(e.getAttribute("src"));
						siteNameList.add(e.getAttribute("alt"));
					}
				}
				
				for(int j = 0; j < linkEle.size(); j++) {
					if(linkEle.get(j).getText().length() > 1) {
						String siteName = linkEle.get(j).getText();
						if(j > siteNameList.size()) {
							siteNameList.add(siteName);
							logoList.add("X");
						}
						else {
							siteNameList.add(j, siteName);
							logoList.add(j, "X");
						}
					}
				}
				
			    	List<WebElement> shippingEle = driver.findElements(By.cssSelector("#blog_content > div.summary_info > div.detail_summary > div.summary_left > div.lowest_area > div.lowest_list > table > tbody.high_list span.stxt.deleveryBaseSection"));
			    	List<String> shippingList = new ArrayList<>();
			    	for (WebElement e : shippingEle) {
					shippingList.add(e.getText()); //배송비
			    	}

			    	for(int k = 0; k < shippingList.size(); k++) {
					PriceInfo priceBean = new PriceInfo();
					priceBean.setProduct(prod);
					priceBean.setSiteName(siteNameList.get(k));
					priceBean.setSiteLogo(logoList.get(k));
					priceBean.setLink(linkList.get(k));
					priceBean.setPrice(priceList.get(k));
					priceBean.setShipping(shippingList.get(k));
					priceRepository.save(priceBean);
			    	}
				
				productRepository.findByName(name).get().setPrice(Collections.min(priceList));
			}
		}

		try {
			if(driver != null) {
				driver.close();
				driver.quit();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	//제품 가격정보 업데이트
	public void updatePriceInfo() {
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		
		List<Product> prodList = productRepository.findAll();
		
		for(int i = 0; i < prodList.size(); i++) {
			String url = "http://search.danawa.com/dsearch.php?k1=" + prodList.get(i).getName() + "&module=goods&act=dispMain";
			driver.get(url);

			try {Thread.sleep(3000);} catch(InterruptedException e) {}

			List<WebElement> ahref =  driver.findElements(By.cssSelector(".prod_name a"));
			List<String> resutlList = new ArrayList<>();
			for(WebElement e : ahref) {
				resutlList.add(e.getAttribute("href"));
			}

			for(int j = 0; j < resutlList.size(); j++) {
				if(resutlList.get(j).contains("pcode")) {
					String prodUrl = resutlList.get(j);
					try {Thread.sleep(3000);} catch(InterruptedException e) {}
					driver.get(prodUrl); //url 바꿔주기

					/* 가격정보 */
					List<WebElement> priceEle = driver.findElements(By.cssSelector(".high_list .price .prc_t"));
					List<Integer> priceList = new ArrayList<>();
					for(WebElement e : priceEle) {
						if (e.getText().contains(",")) {
							priceList.add(Integer.parseInt(e.getText().replace(",", ""))); //가격
						}
					}

					List<WebElement> linkEle = driver.findElements(By.cssSelector(".high_list .logo_over a"));
					List<String> linkList = new ArrayList<>();
					for(WebElement e : linkEle) {
						if(!(e.getAttribute("href").contains("ajax"))) {
							linkList.add(e.getAttribute("href")); //쇼핑몰 링크
						}
					}

					List<String> logoList = new ArrayList<>();
					List<String> siteNameList = new ArrayList<>();
					List<WebElement> imgEle = driver.findElements(By.cssSelector(".high_list .logo_over img"));
					for(WebElement e : imgEle) {
						if(!e.getAttribute("src").contains("noImg")) {
							logoList.add(e.getAttribute("src")); //로고 이미지 
							siteNameList.add(e.getAttribute("alt")); //사이트 이름
						}
					}

					for(int k = 0; k < linkEle.size(); k++) {
						if(linkEle.get(k).getText().length() > 1) {
							String siteName = linkEle.get(k).getText();
							if(k > siteNameList.size()) {
								siteNameList.add(siteName); //사이트 이름
								logoList.add("X"); //로고 이미지 없는 경우
							}
							else {
								siteNameList.add(k, siteName); //사이트 이름
								logoList.add(k, "X"); //로고 이미지 없는 경우
							}
						}
					}

					List<WebElement> shippingEle = driver.findElements(By.cssSelector("#blog_content > div.summary_info > div.detail_summary > div.summary_left > div.lowest_area > div.lowest_list > table > tbody.high_list span.stxt.deleveryBaseSection"));
					List<String> shippingList = new ArrayList<>();
					for (WebElement e : shippingEle) {
						shippingList.add(e.getText()); //배송비
					}

					priceRepository.deleteAllByProduct(prodList.get(i));
					
					for(int k = 0; k < shippingList.size(); k++) {
						PriceInfo priceBean = new PriceInfo();
						priceBean.setProduct(prodList.get(i));
						priceBean.setSiteName(siteNameList.get(k));
						priceBean.setSiteLogo(logoList.get(k));
						priceBean.setLink(linkList.get(k));
						priceBean.setPrice(priceList.get(k));
						priceBean.setShipping(shippingList.get(k));
						priceRepository.save(priceBean);
					}
					
					break;
				}
			}
		}
		
		try {
			if(driver != null) {
				driver.close();
				driver.quit();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	//제품 최저가 업데이트
	public void updateMinPrice() {
		List<Product> prodList = productRepository.findAll();
		for(Product prod : prodList) {
			List<Integer> priceList = new ArrayList<>();
			for(PriceInfo info : prod.getPriceInfoList()) {
				priceList.add(info.getPrice());
			}
			prod.setPrice(Collections.min(priceList));
		}
	}
}
