package com.turbo.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.turbo.app.TurboBaseApp;

/**
 * 属性文件工具类
 * @author Ted
 * @mail tedworld.time@gmail.com
 */
public class PropertiesHelper {
	
	private final static String DEFAULT_PROPERTIES_FILE= "turbo.properties";
	private PropertiesHelper(){}
	
	/**
	 * @param fis
	 * 读取配置文件的信息
	 * @return 如果文件不存在则返回null
	 */
	public static Properties readPropertiesFile(InputStream fis) {
		Properties pro = new Properties();
		try {
			pro.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
		return pro;
	}

	/**
	 * 写入配置文件
	 * @param prop
	 * @param fos
	 * @return 写入失败返回false否则返回true
	 */
	public static boolean writePropertiesFile(Properties prop, OutputStream fos) {
		boolean flag = false;
		try {
			prop.store(fos, null);
			fos.flush();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(fos != null)
					fos.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 使用用户自定义的Properties文件
	 * @return 如果文件不存在则返回null
	 */
	public static Properties readPropertiesFile(File file){
		FileInputStream fis = null;
		if(!file.exists())
			return null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(fis == null)
			return null;
		return readPropertiesFile(fis);
	}  
	
	/**
	 * 使用默认的Properties文件
	 * @return
	 */
	public static Properties readPropertiesFile(){
		File file = new File(TurboBaseApp.getAppContext().getFilesDir(),DEFAULT_PROPERTIES_FILE);
		return readPropertiesFile(file);
	}
	
	/**
	 * 使用默认的Properties文件
	 * @return
	 */
	public static boolean writePropertiesFile(Properties prop){
		File file = new File(TurboBaseApp.getAppContext().getFilesDir(),DEFAULT_PROPERTIES_FILE);
		return writePropertiesFile(file,prop);
	}
	
	/**
	 * 使用用户自定义的Properties文件
	 * @param prop
	 * @param outFile
	 * @return 写入失败返回false，写入成功返回true
	 */
	public static boolean writePropertiesFile(File outFile,Properties prop){
		if(!FileHelper.initFile(outFile))
			return false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(fos == null)
			return false;
		return writePropertiesFile(prop, fos);
	}
	
	
	/**
	 * 得到Properties属性
	 * @param key
	 * @return
	 */
	public static String getPropertiesValue(String key,File file){
		Properties prop = readPropertiesFile(file);
		return (prop != null)?prop.getProperty(key):null;
	}
	
	/**
	 * 设置Properties属性
	 * @param file
	 * @param key
	 * @param value
	 */
	public static void setPropertiesVale(File file,String key, String value){
		Properties prop = readPropertiesFile(file);
		prop.setProperty(key, value);
		writePropertiesFile(file,prop);
	}
	
	/**
	 * 删除Properties属性
	 * @param file
	 * @param key
	 */
	public static void removePropertiesVale(File file,String... key){
		Properties prop = readPropertiesFile(file);
		if(prop == null)
			return;
		for(String temp : key){
			prop.remove(temp);
		}
		writePropertiesFile(file,prop);
	}
	
	/**
	 * 是否含有该属性
	 * @param file
	 * @param key 
	 * @return
	 */
	public static boolean containsProperty(File file,String key){
		Properties prop = readPropertiesFile(file);
		return prop.containsKey(key);
	}
	
	/**
	 * 得到Properties属性
	 * @param key
	 * @return
	 */
	public static String getPropertiesValue(String key){
		Properties prop = readPropertiesFile();
		return (prop != null)?prop.getProperty(key):null;
	}
	
	
	/**
	 * 设置Properties属性
	 * @param key
	 * @param value
	 */
	public static void setPropertiesVale(String key, String value){
		Properties prop = readPropertiesFile();
		prop.setProperty(key, value);
		writePropertiesFile(prop);
	}
	
	/**
	 * 删除Properties属性
	 * @param key
	 */
	public static void removePropertiesVale(String... key){
		Properties prop = readPropertiesFile();
		if(prop == null)
			return;
		for(String temp : key){
			prop.remove(temp);
		}
		writePropertiesFile(prop);
	}
	
	/**
	 * 是否含有该属性
	 * @param key 
	 * @return
	 */
	public static boolean containsProperty(String key){
		Properties prop = readPropertiesFile();
		return prop.containsKey(key);
	}
	
}
