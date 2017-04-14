package com.sc.sc.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.sc.sc.model.City;
import com.sc.sc.model.Flt;
import com.sc.sc.util.Utils;

public class ScAction {

	private static String URL = "http://localhost:8080/mybatisplus-spring-mvc";
	private static Integer ERROR_CODE = 1;
	private static City city;
	private static Integer Y = 3;
	private static Integer T = 0;

	public static List<City> getCityList() {

		String respong = Utils.sendPost(URL + "/city/list", "current=" + Y + "&size=20");
		return (List<City>) Utils.parseJsonToList(respong, "records", City.class);
	}

	private static void init() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		try {
			List<City> cities = getCityList();
			for (int i = 0; i < cities.size(); i++) {
				Calendar cal = Calendar.getInstance();
				Calendar cal1 = Calendar.getInstance();
				// 下面的就是把当前日期加一个月
				cal.add(Calendar.MONTH, 1);
				long startTime1 = System.currentTimeMillis();// 获取当前时间
				ERROR_CODE = 1;
				T = 0;
				while (cal1.getTime().before(cal.getTime())) {
					if (ERROR_CODE == 0) {
						break;
					}
					T++;
					long startTime = System.currentTimeMillis();// 获取当前时间
					// doSomeThing(); //要运行的java程序
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					String str = sdf.format(cal1.getTime());
					city = cities.get(i);
					System.out.println();
					new ScAction4().testShandongairData(city, str);
					long endTime = System.currentTimeMillis();
					System.out.println(str + "航班" + "执行时间:" + (endTime - startTime) / 1000 + "秒");
					cal1.add(Calendar.DATE, 1);// 进行当前日期月份加1
				}
				long endTime1 = System.currentTimeMillis();
				System.out.println(city.toString() + "程序运行时间：" + formatter.format(endTime1 - startTime1) + "ms");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		init();
	}

	public static void WriteStringToFile(URL filePath, String content) throws URISyntaxException {
		try {
			File file = new File(filePath.toURI());
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.append(content);// 在已有的基础上添加字符串
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private WebDriver driver;

	private String baseUrl;

	private JavascriptExecutor jse;

	public ScAction4() {

	}

	public ScAction4(int page) {
		this.Y = page;
	}

	public boolean ElementExist(WebDriver driver, By locator) {
		try {
			driver.findElement(locator);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private void getSdDate(String content) {
		Document doc = Jsoup.parse(content);
		Elements doc1 = doc.select(".__cabin_table__");
		StringBuffer buffer = new StringBuffer();
		Flt flt = new Flt();

		for (int i = 0; i < doc1.size(); i++) {
			flt.setFltNo(doc1.select(".dashed").get(i).text());
			int triggerCount = doc1.get(i).select(".cabintable").select(".rdo-trigger").size();

			for (int j = 0; j < triggerCount; j++) {
				Element doc2 = doc1.get(i).select(".cabintable").select(".rdo-trigger").get(j);

				flt.setFltArrivalDatetime(doc2.attr("data-arrivaldatetime").trim());
				flt.setFltDepartureDatetime(doc2.attr("data-departuredatetime").trim());
				flt.setFltPrice(doc2.attr("data-price").trim());

				String string = doc2.select("p").text();

				flt.setFltType(string.substring(string.indexOf("舱") - 1, string.indexOf("舱") + 1).trim());

				flt.setFltCount(string.substring(string.indexOf("舱") + 1).trim());

				String parameter = "fltCount=" + flt.getFltCount() + "&" + "fltNo=" + flt.getFltNo() + "&" + "fltType="
						+ flt.getFltType() + "&" + "fltArrivalDatetime=" + flt.getFltArrivalDatetime() + "&"
						+ "fltPrice=" + flt.getFltPrice() + "&" + "fltDepartureDatetime="
						+ flt.getFltDepartureDatetime() + "&" + "cityNo=" + city.getId();
				System.out.println(parameter);
				Utils.sendPost(URL + "/flt/save", parameter);
			}
		}
		driver.quit();
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void setUp() throws Exception {

		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");

		driver = new ChromeDriver();
		jse = (JavascriptExecutor) driver;
		baseUrl = "http://www.shandongair.com.cn/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void tearDown() throws Exception {
		Set<String> winHandels = driver.getWindowHandles();
		List<String> it = new ArrayList<String>(winHandels);
		driver.switchTo().window(it.get(0));
		if (T < 2 && ElementExist(driver, By.xpath("/html/body/div[1]/img"))) { // 第一次通过了就不检查第二次了
			driver.quit();
			ERROR_CODE = 0;
			return;
		}
		ERROR_CODE = 1;

		Long count = (Long) jse.executeScript("return  document.getElementsByClassName('showallcabin').length");
		for (int i = 0; i < count; i++) {
			jse.executeScript("document.getElementsByClassName('showallcabin')[" + i + "].click()");
			Thread.sleep(1000);
		}
		Thread.sleep(5000);
		String cont = driver.getPageSource();
		getSdDate(cont);
	}

	public void testShandongairData(City city, String time) throws Exception {
		setUp();
		driver.get(baseUrl);
		StringBuffer buffer = new StringBuffer();
		buffer.append("document.getElementById('J_area1').value='" + city.getCitynameorg() + "';"); // 出发地
		buffer.append("document.getElementById('city_code_org_d').value='" + city.getCitycodeorg() + "';"); // 出发地代码
		buffer.append("document.getElementById('J_area2').value='" + city.getCitynamedes() + "';");
		buffer.append("document.getElementById('city_code_dst_d').value='" + city.getCitycodedes() + "';");

		buffer.append("document.getElementById('dep_date_d').value='" + time + "';");
		buffer.append("document.getElementById('ret_date_d').value='" + time + "';");

		jse.executeScript(buffer.toString());
		driver.findElement(By.id("domestic_panel")).submit();
		driver.close();

		tearDown();
	}
}
