package me.danawa.crawling;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import me.danawa.beans.PriceInfo;
import me.danawa.beans.Product;

public class Data {
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
	public static final String WEB_DRIVER_PATH = "E:\\crawling\\chromedriver.exe"; //드라이버 경로
	
	public static void main(String[] args) {

	}

	public static List<PriceInfo> price() {
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		
		String url = "http://prod.danawa.com/list/?cate=112758&searchOption=searchMaker=6792,702,1452,2869,2137,2904/innerSearchKeyword=";
		driver.get(url);
		
		driver.findElement(By.className("qnt_selector")).click();
		driver.findElement(By.xpath("//*[@id=\"productListArea\"]/div[2]/div[2]/div[2]/select/option[3]")).click();
		
		try {Thread.sleep(3000);} catch(InterruptedException e) {}

		List<WebElement> ahref =  driver.findElements(By.cssSelector(".prod_name a"));
		List<String> prodList = new ArrayList<>();
		for(WebElement e : ahref) {
			prodList.add(e.getAttribute("href"));
		}
		
		List<PriceInfo> priceBeanList = new ArrayList<>();

		for(int i = 4 ; i < prodList.size(); i++) {
			if(prodList.get(i).contains("pcode")) {
				String prodUrl = prodList.get(i);
				try {Thread.sleep(3000);} catch(InterruptedException e) {}
				driver.get(prodUrl); //url 바꿔주기

				WebElement nameEle = driver.findElement(By.className("prod_tit"));
				String name = nameEle.getText(); //제품명
				
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
				
				for(int j = 0; j < linkEle.size(); j++) {
					if(linkEle.get(j).getText().length() > 1) {
						String siteName = linkEle.get(j).getText();
						if(j > siteNameList.size()) {
							siteNameList.add(siteName); //사이트 이름
							logoList.add("X"); //로고 이미지 없는 경우
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
	            	priceBean.setName(name);
	            	priceBean.setSitename(siteNameList.get(k));
	            	priceBean.setSitelogo(logoList.get(k));
	            	priceBean.setLink(linkList.get(k));
	            	priceBean.setPrice(priceList.get(k));
	            	priceBean.setShipping(shippingList.get(k));
	            	priceBeanList.add(priceBean);
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
		
		return priceBeanList;
	}
	
	public static List<Product> product() {
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		
		String url = "http://prod.danawa.com/list/?cate=112758&searchOption=searchMaker=6792,702,1452,2869,2137,2904/innerSearchKeyword=";
		driver.get(url);
		
		driver.findElement(By.className("qnt_selector")).click();
		driver.findElement(By.xpath("//*[@id=\"productListArea\"]/div[2]/div[2]/div[2]/select/option[3]")).click();
		
		try {Thread.sleep(3000);} catch(InterruptedException e) {}

		List<WebElement> ahref =  driver.findElements(By.cssSelector(".prod_name a"));
		List<String> prodList = new ArrayList<>();
		for(WebElement e : ahref) {
			prodList.add(e.getAttribute("href"));
		}
		
		List<Product> productBeanList = new ArrayList<>();

		for(int i = 4 ; i < prodList.size(); i++) {
			if(prodList.get(i).contains("pcode")) {
				String prodUrl = prodList.get(i);
				try {Thread.sleep(3000);} catch(InterruptedException e) {}
				driver.get(prodUrl); //url 바꿔주기

				WebElement nameEle = driver.findElement(By.className("prod_tit"));
				String name = nameEle.getText(); //제품명
				
				WebElement imgEle = driver.findElement(By.cssSelector("#baseImage"));
	            String image = imgEle.getAttribute("src"); //이미지
	            
	            List<WebElement> specEle = driver.findElements(By.className("items"));
	    		String spec = "";
	    		for(WebElement e : specEle) {
	    			spec += e.getText();
	    		}
	    		System.out.println(spec);
				
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
						System.out.println(regdate);
					}
					else if(field.get(j).getText().equals("화면 크기")) {
						String str = value.get(j).getText();
						String[] res = str.split("\\(");
						size = Double.parseDouble(res[1].replace("인치)", ""));
						System.out.println(size);
					}
					else if(field.get(j).getText().equals("CPU 종류")) {
						cpu = value.get(j).getText();
						System.out.println(cpu);
					}
					else if(field.get(j).getText().contains("운영체제")) {
						os = value.get(j).getText();
						System.out.println(os);
					}
					else if(field.get(j).getText().equals("메모리")) {
						memory = value.get(j).getText();
						System.out.println(memory);
					}
					else if(field.get(j).getText().equals("저장 용량")) {
						String res = value.get(j).getText().replaceAll("[^0-9]", "");
						storage = Integer.parseInt(res);
						if(storage < 10) {
							storage *= 1000;
						}
						System.out.println(storage);
					}
					else if(field.get(j).getText().equals("무게")) {
						String res = value.get(j).getText().replaceAll("[a-z]", "");
						weight = Double.parseDouble(res);
						System.out.println(weight);
					}
				}
				
				Product productBean = new Product();
				productBean.setName(name);
				productBean.setBrand(brand);
				productBean.setCpu(cpu);
				productBean.setMemory(memory);
				productBean.setOs(os);
				productBean.setSize(size);
				productBean.setStorage(storage);
				productBean.setWeight(weight);
				productBean.setImage(image);
				productBean.setRegdate(regdate);
				productBean.setSpec(spec);
				productBeanList.add(productBean);
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
		
		return productBeanList;
	}

}
