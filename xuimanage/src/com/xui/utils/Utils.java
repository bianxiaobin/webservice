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
	 * ��ѹ���ļ�����ǰ�ļ�
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
	 * ����ѹ������ļ��Ƿ����music��logo�ļ�
	 * afterZipFile : ��ѹ����ļ�
	 * newMusicPath : ������ֵ���·��
	 * newLogopath : ���logo����·��
	 */
	public static List<String> checkAndCopyFile(File afterZipFile,String newMusicPath,String newLogoPath){
		MusicDB db = new MusicDB();
		String result = "";
		List<String> list = new ArrayList<String>();
		File[] musicFile = null;
		File[] logoFile = null;
		if(!afterZipFile.exists()){
			result = "�뱣��ѹ���ļ����ļ���������ͬ";
			list.add(result);
			return list;
		}
		//����music��logo���ļ���
		File[] files = afterZipFile.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.equals("music") || name.equals("logo"))
					return true;
				return false;
			}
		});
		//ѹ������ļ���û���ļ��л�ֻ��һ���ļ��з���
		if(files==null || files.length <= 1){
			result = "û���ҵ��ļ����ϴ���ѹ���ļ�������music��Logo�ļ��У�";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//����mp3�ļ���ͼƬ�ļ����浽musicFile��logoFile������
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
		//���music�ļ���������mp3�ļ�����
		if(musicFile == null || musicFile.length == 0){
			result = "�ļ��ϴ���������music�ļ����Ƿ���mp3�ļ�,������ļ���Ŀ¼̫��";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//���logo�ļ������Ƿ���ͼƬ�ļ�����
		if(logoFile == null || logoFile.length == 0){
			result = "�ļ��ϴ���������logo�ļ����Ƿ���ͼƬ�ļ�,������ļ���Ŀ¼̫��";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//��ר�����浽ר������,����ع����˳�
		boolean albumResult = db.insertMusicAlbum(afterZipFile);
		if(albumResult){
			result = "ר����̫��";
			LogUtils.log(result);
			list.add(result);
			return list;
		}
		//����logo�ļ���ָ�����ļ�����
		for(File f : logoFile){
			LogUtils.log("copy��logo�ļ�����"+f.getName());
			copyFile(f, newLogoPath+db.getMaxId());
		}
		for(File f : musicFile){
			LogUtils.log("copy��music�ļ�����"+f.getName());
			if(f.getName().contains("-")){
				boolean musicResult = db.insertMusic(f);
				//����ع��Ͳ��ύ
				if(musicResult){
					result = f.getName()+"������̫��";;
					LogUtils.log("copy��music�ļ���̫����"+f.getName());
					list.add(result);
				}else{
					copyFile(f, newMusicPath);
				}
			}else{
				result = f.getName()+"��������-";
				list.add(result);
			}
		}
		return list;
	}
	
	/***
	 * ���ļ�music��logo�ļ����е��ļ�copy����Ӧ���ļ�����
	 * @param args
	 */
	private static void copyFile(File file , String newPath){
		File copyFile = new File(newPath,file.getName());
		System.out.println(copyFile.getPath());
		try{
			//�ȼ�鸸�ļ�Ŀ¼�Ƿ���ڣ������ھʹ������ļ�Ŀ¼Ȼ���ڴ����ļ�
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
			//�жϸ��ļ���·���Ƿ���ڣ������ھʹ���
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			//�ж��ļ��Ƿ���ڣ������ھʹ�����
			if(!file.exists()){
				LogUtils.log(file.getName()+"�ļ������ڣ�����ִ��createKey.action");
				return nowKey;
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		try {
			//�ȴ��ļ��ж�
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
			//�жϸ��ļ���·���Ƿ���ڣ������ھʹ���
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			//�ж��ļ��Ƿ���ڣ������ھʹ�����
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
