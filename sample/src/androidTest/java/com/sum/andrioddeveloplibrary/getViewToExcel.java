package com.sum.andrioddeveloplibrary;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class getViewToExcel{
	
	private static Scanner scanner=new Scanner(System.in);
//	private XSSFWorkbook wb = null;
	private List<String> headers = new ArrayList<String>();
	private String pageName = "";
	private int line = 0;
	private int count = 1;
	private String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	private String devices="IOS";
	private AppiumDriver<WebElement> driver;
	private HashMap<String, String> nodeInstance = new HashMap<String, String>();
	
	public getViewToExcel() {
		System.out.println("*******请确保界面完全加载，并且不要进入非必要界面，以确保元素获取准确性。同时不要打开或使用待创建的excel。******");
		System.out.println("*******excel不存在会自动创建，存在会覆盖追加。*******");
		System.out.println("*******excel生成完成后会存放在excel-resource目录下*******");
		System.out.println("*******不要在该目录下使用excel文件，生成前尽量清除掉该文件夹的所有文件*******");
		System.out.println("*******存在同名sheet时无法创建*******");
		System.out.println("*******appium超时时间：600s*******");
		System.out.println("请输入设备类型(0:android，其他:ios)：");
		String choose=scanner.next();
		if (choose.equals("0")) {
			devices = "Android";
			androidServer("DU2TDM158B064382");
//			androidServer("7e2eb20c");
		}
		else {
			iosServer("simulator");
		}
//		setUpAndroid("DU2TDM158B064382"); //Android设备
//		setUpIos("");  //IOS设备
//		setUpIos("simulator");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void iosServer(String device){
		System.out.println("Initialize IOS Driver...");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformVersion", "9.2");
		capabilities.setCapability("emulator", true);
		capabilities.setCapability("deviceName", "iPhone 6");
		capabilities.setCapability("noReset", false);
		capabilities.setCapability("app", System.getProperty("user.dir") + "/apps/BaiLianMobileApp-iOS-simulator.zip");
//		capabilities.setCapability("newCommandTimeout", "180");
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "600");
		capabilities.setCapability("autoAcceptAlerts", true);
//		capabilities.setCapability("platformName", "iOS");
//		capabilities.setCapability("platformVersion", "9.0");
//		capabilities.setCapability("deviceName", "iPhone 6s");
//		capabilities.setCapability("bundleId", "com.blemall.dch");
//		capabilities.setCapability("udid", device); //cf1272101dc76b514bd3e802817c19f9726a5f87
		try {
			this.driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}  
		System.out.println("Initialize IOS Driver Completed!");
	}
	
	private void androidServer(String device) {
		System.out.println("Initialize Android Driver...");
	    File classpathRoot = new File(System.getProperty("user.dir"));
	    File appDir = new File(classpathRoot, "apps");
	    File app = new File(appDir, "BaiLianMobileApp.apk");
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability("platformName", "Android");
	    capabilities.setCapability("automationName", "uiautomator2");
	    capabilities.setCapability("deviceName", device);
	    capabilities.setCapability("platformVersion", "4.4.2");
//	    capabilities.setCapability("app", app.getAbsolutePath());
	    capabilities.setCapability("appPackage", "cn.com.bailian.bailianmobile");
//	    capabilities.setCapability("appActivity", ".BaiLianMobileApp");
//	    capabilities.setCapability("appActivity", ".page.Root.RootPage2");
	    capabilities.setCapability("appActivity", ".page.PageContainer");
	    capabilities.setCapability("unicodeKeyboard", "True");
//	    capabilities.setCapability("noReset", true);
//	    capabilities.setCapability("fullReset", true);
	    capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "600");
	    capabilities.setCapability("resetKeyboard", "True");
	    try {
			this.driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	    System.out.println("Initialize Android Driver Completed!");
	}
	
	@AfterTest(alwaysRun=true)
	public void tearDown() throws Exception {
	        driver.quit();
	        System.out.println("Completed get view");
	}
	
	@Test
	public void create(){
//		driver.switchTo().frame(index) //切换到内嵌的driver
//		driver.switchTo().defaultContent() //返回到默认driver
//		driver.findElement(By.xpath("")).click();
//		String dirPath = path.substring(1,path.lastIndexOf("/")).split("bin")[0]+"excel-resource"; //windows使用
		String dirPath = path.substring(0,path.lastIndexOf("/")).split("bin")[0]+"excel-resource"; //mac使用
//		if (devices.equals("IOS")) {
//			dirPath = path.substring(0,path.lastIndexOf("/")).split("bin")[0]+"excel-resource";
//		}
//		System.out.println(path);
//		System.out.println(dirPath);
//		System.out.println(dirPath+"//view_info.xlsx");
		/*
		try {
			wb = new XSSFWorkbook(new FileInputStream(dirPath+"//view_info.xlsx"));
		} catch (Exception e) {
			System.out.println("文件不存在，重新创建。");
			File dPath = new File(dirPath);
			if(!dPath.exists()){ 
				dPath.mkdirs(); 
		       }
			wb = new XSSFWorkbook();
		}
		*/
		File xmlPath = new File(dirPath+"//"+devices+"Xml");
		if(!xmlPath.exists()){ 
			xmlPath.mkdirs(); 
	       }
		/*
		XSSFSheet sheet;
		Boolean isExists = false;
		try {
			sheet = wb.createSheet(devices);
		} catch (Exception e) {
			sheet = wb.getSheet(devices);
			isExists = true;
			line = sheet.getLastRowNum();
			System.out.println("line:"+line);
		}
		
		writeHeaders(sheet,isExists);
		*/
		 int width = driver.manage().window().getSize().width;
		 int height = driver.manage().window().getSize().height;
		 System.err.println("width:"+width);
		 System.err.println("height:"+height);
		while (true) {
			count = 1;
			System.out.println("请输入当前界面名称(0:退出,cut:x.y.width.heigh或cut截取图片)：");
			pageName=scanner.next();
			if (pageName.equals("0")) {
				break;
			}
			if (pageName.toLowerCase().contains("cut")) {
				BufferedImage bufferedImage = null;
				if (pageName.toLowerCase().equals("cut")) {
					pageName = pageName.replace(":", "_")+"0_0_"+width+"_"+height;
					System.out.println("开始截取整张图片：0 0 "+width+" "+height);
					bufferedImage = getCutImage(driver, 0, 0, width, height);
				}
				else {
					String cut = pageName.substring(pageName.indexOf(":")+1);
					pageName = pageName.replace(":", "_").replace(".", "_");
					String area[] = cut.split("\\.");
					if (area.length != 4) {
						System.err.println("截取区域输入错误，输入格式为cut:x.y.width.height或cut");
						continue;
					}
					System.out.println("开始截取区域图片："+area[0]+" "+area[1]+" "+area[2]+" "+area[3]);
					bufferedImage = getCutImage(driver, Integer.valueOf(area[0]), Integer.valueOf(area[1]), Integer.valueOf(area[2]), Integer.valueOf(area[3]));
				}
				if (bufferedImage == null) {
					System.err.println("图片截取错误");
					continue;
				}
				Date date = new Date();
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			    String dateNowStr = sdf.format(date);
				String screenPath = dirPath+"//"+devices+"Xml//"+pageName+"_"+dateNowStr+".jpg";
				try {
					ImageIO.write(bufferedImage, "jpg", new File(screenPath));
					System.out.println("图片截取完成:"+screenPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			System.out.println("开始获取当前界面view集");
			
//			try {
//				System.err.println("text1:"+driver.findElement(By.xpath(".//*[contains(@text,'速达')]")).getText());
//			} catch (Exception e) {
//				// TODO: handle exception
//				System.err.println("速达速达速达"+e.getMessage());
//			}
//			try {
//				System.err.println("text:"+driver.findElement(By.xpath(".//*[contains(@text,'数量')]")).getText());
//			} catch (Exception e) {
//				// TODO: handle exception
//				System.err.println("数量数量数量"+e.getMessage());
//			}
			
			
			String activityName = "";
			if (devices.equals("Android")) {
				activityName = "_"+((AndroidDriver<WebElement>) driver).currentActivity();
			}
//			changeMode(true);
			String strXml = driver.getPageSource();
			/*
			xmlToExcel(writeX(strXml),sheet);
			*/
//			System.out.println(writeX(driver.getPageSource()));
			/*
			try {
	        	FileOutputStream is = new FileOutputStream(dirPath+"//view_info.xlsx");  
	            wb.write(is);
	            is.close();
	            System.out.println(pageName+" view集合已创建,数量："+(count-1));
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(pageName+" view集合创建失败："+e.getMessage());
			}
			*/
			try {
				String saveXmlPath = dirPath+"//"+devices+"Xml//"+pageName+activityName+".xml";
				String screenPath = dirPath+"//"+devices+"Xml//"+pageName+activityName+".jpg";
				saveXmltoFile(strXml, saveXmlPath);
				File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(srcFile, new File(screenPath));
				System.err.println("activityName："+activityName);
				System.out.println(pageName+" xml已创建,路径："+saveXmlPath);
				System.err.println(pageName+" 截图已创建,路径："+screenPath);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(pageName+" xml创建失败："+e.getMessage());
			}
		}
		scanner.close();
		/*
		try {
			wb.close();
			System.out.println("文件路径："+dirPath+"//view_info.xlsx");
            System.out.println("创建完成，总数："+line);
		} catch (Exception e) {
			System.out.println("创建失败:"+e.getMessage());
		}
		*/
	}
	
	/**
	 * 截取设备当前的屏幕并按照给定的区域裁剪
	 * 超出原图片范围的话会按照最大范围来裁剪，坐标小于0的强制为0，范围小于等于0的按照最大范围裁剪
	 * @param driver driver
	 * @param x 截取起始x坐标
	 * @param y 截取起始y坐标
	 * @param width 截取宽度
	 * @param height 截取长度
	 * @return 成功：BufferedImage，失败：null
	 */
	
	public BufferedImage getCutImage(AppiumDriver<WebElement> driver, int x, int y, int width, int height) {
		try {
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage targetImage = ImageIO.read(srcFile);
			return cutImage(targetImage, x, y, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("getCutImage--截图裁剪失败："+e.getMessage());
		}
		return null;
	}
	
    /**
     * 根据坐标裁剪图片
     * 超出原图片范围的话会按照最大范围来裁剪，坐标小于0的强制为0，范围小于等于0的按照最大范围裁剪
     * @param inputImagePath 原图片路径
     * @param outputImagePath 裁剪后的图片路径
     * @param x 裁剪起始X轴坐标
     * @param y 裁剪起始y轴坐标
     * @param width 裁剪的宽度
     * @param height 裁剪的高度
     * @return 成功：BufferedImage，失败：null
     */
    
    public BufferedImage cutImage(BufferedImage inputImage, int x, int y, int width, int height) {
    	try {
	    	Image srcImage = inputImage;
	        int src_w = srcImage.getWidth(null); // 源图宽 
	        int src_h = srcImage.getHeight(null);// 源图高
	        if (x < 0) {
				x = 0;
			}
	        else if (x > src_w){
				x = src_w;
			}
	        if (y < 0) {
				y = src_h;
			}
	        else if (y > src_h) {
				y = src_h;
			}
	        // 目标图片宽  
	        if (width <= 0) {  
	            width = 1;
	        }
	        if (width + x > src_w) {
	        	if (width == 1 || src_w == x) {
					x = src_w - 1;
					width = 1;
				}
	        	else {
	        		width = src_w - x;
				}
			}
	        // 目标图片高  
	        if (height <= 0) {  
	        	height = 1;
	        }
	        if (height + y > src_h) {
	        	if (height == 1 || src_h == y) {
					y = src_h - 1;
					height = 1;
				}
	        	else {
	        		height = src_h - y;
				}
			}
	        // 目标图片  
	        ImageFilter cropFilter = new CropImageFilter(x, y, width, height);  
	        Image cutImage = Toolkit.getDefaultToolkit().createImage(  
	                new FilteredImageSource(srcImage.getSource(), cropFilter));  
	        // 重绘图片  
	        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        Graphics g = tag.getGraphics();  
	        g.drawImage(cutImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null); // 绘制裁剪后的图  
	        g.dispose();
//	        ImageIO.write(tag, "jpg", new File(".//config//cut1_test1"+getCurrentTime("")+".jpg"));
	        return tag;
    	}catch (Exception e) {
    		System.err.println("cutImage——裁剪失败："+e.getMessage());
		}
    	return null;
    }
	
	/**
	 * 切换获取模式(无效。。。)
	 * @param m 获取模式，true：webview，false：NATIVE_APP
	 */
	
	private void changeMode(Boolean m) {
		if (m) {
			Set<String> contextNames = driver.getContextHandles();
			System.err.println(contextNames.size());
			for (String contextName : contextNames) {
			    System.out.println(contextName); //prints out something like NATIVE_APP \n WEBVIEW_1
			}
			if (contextNames.size() > 1) {
				driver.context((String) contextNames.toArray()[1]); //set context to WEBVIEW_1
			}
//			driver.switchTo().window("WEBVIEW");
//			driver.context("WEBVIEW_1");
		}
		else {
//			driver.switchTo().window("NATIVE_APP");
			driver.context("NATIVE_APP"); //set context to NATIVE_APP
		}
	}
	
	private String getCurrentActivity(){
    	return ((AndroidDriver<WebElement>) driver).currentActivity();
    }
	/*
	private void xmlToExcel(String xml,XSSFSheet sheet){
		try {
            SAXReader reader = new SAXReader();
            InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            Document doc = reader.read(in);  
            Element root = doc.getRootElement();  
            readNode(root, sheet);
        } catch (Exception e) {
            e.printStackTrace();  
        }
	}
	
    @SuppressWarnings("unchecked")
    private void readNode(Element root, XSSFSheet sheet) { 
        if (root == null) {
        	return;
        }
        List<Attribute> attrs = root.attributes();  
        if (attrs != null && attrs.size() > 2) {
        	Attribute attr;
        	line+=1;
        	Row exlRow = sheet.createRow(line);
        	writeExcel(sheet, exlRow, 0, pageName+"_view"+count);
        	if (devices.equals("IOS")) {
				writeExcel(sheet, exlRow, headers.indexOf("class"), root.getName());
			}
        	count+=1;
            for (int i = 0; i < attrs.size(); i++) {
            	try {
            		attr=attrs.get(i);
            		int row = headers.indexOf(attr.getName());
            		if (row > 0) {
						String value = attr.getValue();
//						System.err.println("value:"+value);
						writeExcel(sheet, exlRow, row, value);
//						System.out.println("========================row:"+row+" line:"+line+"==================");
//	                    System.out.println(attr.getName() + " "+value);
					}
//            		System.out.println(attr.getName() + " "+attr.getValue());
				} catch (Exception e) {
					System.out.println("属性缺失");
				}
            }
        }
        
        List<Element> childNodes = root.elements();
        for (Element e : childNodes) {
            readNode(e, sheet);
        }
    }
    */
    private void saveXmltoFile(String str,String path){
    	
    	File xmlPath = new File(path);
		if(xmlPath.exists()){ 
			xmlPath.delete();
	       }
    	
		 try {
			 if (devices.equals("Android")) {
				 FileOutputStream outputStream = new FileOutputStream(path); 
				 OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8"); 
				 try { 
				    writer.write(writeX(str));
				 } finally { 
				    writer.close(); 
				 } 
			 }
			 else {
				 Writer osWrite=new OutputStreamWriter(new FileOutputStream(new File(path)));//创建输出流  
			 	 OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式    
			 	 format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式  
			 	 XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter 指定输出文件以及格式    
			 	 try {
			 		writer.write(addXmlAttribute(str));//把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)
				} finally {
					writer.flush();
				 	writer.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(path+" 创建失败："+e.getMessage());
		}
		
	}
    
    private String writeX(String inputXML){
    	System.out.println("当前界面view集获取完成");
//    	System.err.println("inputXML:"+inputXML);
		String requestXML = null;
		try {
			SAXReader reader = new SAXReader();  
	        Document document = reader.read(new StringReader(inputXML),"UTF-8");
	        XMLWriter writer = null;  
	        if (document != null) {
	          try { 
	            StringWriter stringWriter = new StringWriter();  
	            OutputFormat format = new OutputFormat(" ", true);  
	            writer = new XMLWriter(stringWriter, format);  
	            writer.write(document);
	            writer.flush();
	            requestXML = stringWriter.getBuffer().toString();
	          } finally {
	            if (writer != null) {
	              try {
	                writer.close();  
	              } catch (IOException e) {
	            	  e.printStackTrace();
	              }  
	            }  
	          }  
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.err.println("requestXML:"+requestXML);
		return requestXML;
	}
    
    private Document addXmlAttribute(String inputXML) {
	    try {
	    	SAXReader reader = new SAXReader();  
	        Document doc = reader.read(new StringReader(writeX(inputXML)),"UTF-8");
	        if (doc != null) {
	        	Element root = doc.getRootElement();
	        	nodeInstance.clear();
	            traversalNodes(root);
			}
	        return doc;
        } catch (Exception e) { 
        	System.err.println("error:"+e);
        }
		return null;
	}
	
	private void traversalNodes(Element node) {
		String nodeName = node.getName();
//		System.out.println("当前节点名称："+nodeName);//当前节点名称  
//	    System.out.println("当前节点的内容："+node.getTextTrim());//当前节点内容
	    int instangce = 0;
	    if (nodeInstance.containsKey(nodeName)) {
			instangce = Integer.parseInt(nodeInstance.get(nodeName))+1;
			nodeInstance.put(nodeName, String.valueOf(instangce));
		}
	    else {
	    	nodeInstance.put(nodeName, "0");
		}
	    node.addAttribute("instance", String.valueOf(instangce));
//	    List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list  
//	    for(Attribute attr:listAttr){//遍历当前节点的所有属性
//	        String name=attr.getName();//属性名称  
//	        String value=attr.getValue();//属性的值  
//	        System.out.println("属性名称："+name+"属性值："+value);  
//	    }
	    //递归遍历当前节点所有的子节点  
	    List<Element> listElement=node.elements();//所有一级子节点的list  
	    for(Element e:listElement){//遍历所有一级子节点  
	        this.traversalNodes(e);//递归  
	    }
	}
    
    /*
    private void writeHeaders(XSSFSheet sheet,Boolean isExists) {
    	if (devices.equals("IOS")) {
    		headers.add("viewName");
        	headers.add("class");
        	headers.add("name");
        	headers.add("label");
        	headers.add("path");
        	headers.add("hint");
        	headers.add("value");
        	headers.add("x");
        	headers.add("y");
        	headers.add("width");
        	headers.add("height");
		}
    	else {
    		headers.add("viewName");
        	headers.add("class");
        	headers.add("content-desc");
        	headers.add("text");
        	headers.add("bounds");
        	headers.add("resource-id");
        	headers.add("index");
        	headers.add("instance");
		}
    	
    	if (isExists) {
			return;
		}
    	
    	Row exlRow = sheet.createRow(0);
    	for (int i = 0; i < headers.size(); i++) {
    		writeExcel(sheet, exlRow, i, headers.get(i));
		}
    }
    
    private void writeExcel(XSSFSheet sheet,Row exlRow, int line, String value) {
        Cell exlCell = exlRow.createCell(line);
        exlCell.setCellValue(value);
                
        XSSFCellStyle exlStyle = (XSSFCellStyle) wb.createCellStyle();
        exlStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        exlStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        exlStyle.setWrapText(false);
        exlCell.setCellStyle(exlStyle);
        exlCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	}
	*/
}
