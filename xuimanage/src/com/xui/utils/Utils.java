package com.xui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.xui.db.MusicDB;

import freemarker.template.SimpleDate;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Utils {
	private static Map<String,String> configMap;
	public static Map<String,String> getConfig(){
		if(configMap == null){
			configMap = new HashMap<String,String>();
		}
		try {
			InputStream is = Utils.class.getClassLoader().getResourceAsStream(ConfigUtil.CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
			String line = "";
			while((line=br.readLine())!=null){
				String[] array = line.split("=");
				configMap.put(array[0], array[1]);
			}
			is.close();
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return configMap;
	}

	/**
	 * 解压缩文件到当前文件
	 */
	public static void uncompressZip(File file){
		try {
			ZipFile zip = new ZipFile(file.getPath());
			zip.setFileNameCharset("utf-8");
			zip.extractAll(file.getParentFile().getPath());
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * 检查解压缩后的文件是否包含music与logo文件
	 * afterZipFile : 解压后的文件
	 * newMusicPath : 存放音乐的新路径
	 * newLogopath : 存放logo的新路径
	 */
	public static List<String> checkAndCopyFile(File afterZipFile,String newMusicPath,String newLogoPath){
		MusicDB db = new MusicDB();
		String result = "";
		List<String> list = new ArrayList<String>();
		File[] musicFile = null;
		File[] logoFile = null;
		if(!afterZipFile.exists()){
			result = "请保持压缩文件与文件夹名称相同";
			list.add(result);
			return list;
		}
		//过滤music与logo的文件夹
		File[] files = afterZipFile.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.equals("music") || name.equals("logo"))
					return true;
				return false;
			}
		});
		//压缩后的文件中没有文件夹或只有一个文件夹返回
		if(files==null || files.length <= 1){
			result = "没有找到文件或上传的压缩文件不包含music与Logo文件夹！";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//过滤mp3文件与图片文件保存到musicFile与logoFile集合中
		for(File f : files){
			if(f.getName().equals("music")){
				musicFile = f.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						if(file.getName().endsWith(".mp3")){
							return true;
						}
						return false;
					}
				});
			}
			if(f.getName().equals("logo")){
				logoFile = f.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						String name = file.getName();
						if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".bmp")){
							return true;
						}
						return false;
					}
				});
			}
		}
		//检查music文件夹中是有mp3文件存在
		if(musicFile == null || musicFile.length == 0){
			result = "文件上传错误，请检查music文件夹是否有mp3文件,或你的文件子目录太多";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//检查logo文件夹中是否有图片文件存在
		if(logoFile == null || logoFile.length == 0){
			result = "文件上传错误，请检查logo文件夹是否有图片文件,或你的文件子目录太多";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//将专辑保存到专辑表中,如果回滚就退出
		boolean albumResult = db.insertMusicAlbum(afterZipFile);
		if(albumResult){
			result = "专辑名太长";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//复制logo文件到指定的文件夹中
		for(File f : logoFile){
			LogUtils.log("copy的logo文件名："+f.getName());
			copyFile(f, newLogoPath+db.getMaxId());
		}
		for(File f : musicFile){
			LogUtils.log("copy的music文件名："+f.getName());
			if(f.getName().contains("-")){
				boolean musicResult = db.insertMusic(f);
				//如果回滚就不提交
				if(musicResult){
					result = f.getName()+"，名字太长";;
					LogUtils.log("copy的music文件名太长："+f.getName());
					list.add(result);
				}else{
					copyFile(f, newMusicPath);
				}
			}else{
				result = f.getName()+"，不包含-";
				list.add(result);
			}
		}
		return list;
	}
	
	/***
	 * 讲文件music与logo文件夹中的文件copy到对应的文件夹中
	 * @param args
	 */
	private static void copyFile(File file , String newPath){
		File copyFile = new File(newPath,file.getName());
		System.out.println(copyFile.getPath());
		try{
			//先检查父文件目录是否存在，不存在就创建父文件目录然后在创建文件
			if(!copyFile.getParentFile().exists()){
				if(copyFile.getParentFile().mkdirs()){
					copyFile.createNewFile();
				};
			}else{
				copyFile.createNewFile();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try {
			FileUtils.copyFile(file, copyFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getKey(String path,String nowDate){
		File file = new File(path);
		String nowKey = "";
		try{
			//判断父文件夹路径是否存在，不存在就创建
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			//判断文件是否存在，不存在就创建！
			if(!file.exists()){
				LogUtils.log(file.getName()+"文件不存在！请先执行createKey.action");
				return nowKey;
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		try {
			//先从文件中读
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String line = "";
			while((line = reader.readLine())!=null){
				if(!line.equals("")){
					String[] keys = line.split("=");
					if(keys == null || keys.length < 2){
						continue;
					}else{
						if(keys[0].equals(nowDate)){
							nowKey = keys[1];
							break;
						}
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nowKey;
	}
	public static void createYearKey(File file){
		try{
			//判断父文件夹路径是否存在，不存在就创建
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			//判断文件是否存在，不存在就创建！
			if(!file.exists()){
				file.createNewFile();
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file,true));
			for (int i = 1; i <= 12; i++) {
				String month = "";
				month = i < 10?("0"+i):i+"";
				int day = 0;
				if(i==1 || i==3 || i==5 || i==7 || i==8 || i==10 || i==12){
					day = 31;
				}else if(i==2){
					day = 28;
				}else{
					day = 30;
				}
				for (int j = 1; j <= day; j++) {
					String monthday = "";
					monthday = j < 10?("0"+j):j+""; 
					String md = MD5.getMD5(year+month+monthday+ConfigUtil.KEY);
					writer.println(year+month+monthday+"="+md);
				}
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
