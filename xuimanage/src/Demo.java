import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.xui.utils.ConfigUtil;
import com.xui.utils.MD5;
import com.xui.utils.Utils;

public class Demo {
	public static void main(String[] args) {
		String a = "asd";
		String b = "asd";
		System.out.println(StringUtils.contains(a, b));
	}
	private static String createRandomDate(){
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(df.format(date)+"-");
		Random random = new Random();
		sb.append(1000+random.nextInt(9000));
		return sb.toString();
	}
	private static void read(){
		String path = "/mnt/webdata/key/key.properties";
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
				file.createNewFile();
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String nowDate = sdf.format(date);
			Map<String,String> keyMap = new HashMap<String,String>();
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String line = "";
			while((line = reader.readLine())!=null){
				if(!line.equals("")){
					String[] keys = line.split("=");
					keyMap.put(keys[0], keys[1]);
				}
			}
			if(keyMap.containsKey(nowDate)){
				nowKey = keyMap.get(nowDate);
			}else{
				PrintWriter writer = new PrintWriter(new FileWriter(file,true));
				writer.println(nowDate+"="+nowDate+"6786567783");
				keyMap.put(nowKey, nowDate+"6786567783");
				nowKey = keyMap.get(nowKey);
				writer.flush();
				writer.close();
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(nowKey);
	}
	
	private static void read02(){
		String path = "/mnt/webdata/key/key02.properties";
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
				file.createNewFile();
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String nowDate = sdf.format(date);
			Map<String,String> keyMap = new HashMap<String,String>();
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String line = "";
			while((line = reader.readLine())!=null){
				if(!line.equals("")){
					String[] keys = line.split("=");
					keyMap.put(keys[0], keys[1]);
				}
			}
			if(keyMap.containsKey(nowDate)){
				nowKey = keyMap.get(nowDate);
			}else{
				PrintWriter writer = new PrintWriter(new FileWriter(file));
				writer.println(nowDate+"="+nowDate+"6786567783");
				keyMap.put(nowKey, nowDate+"6786567783");
				nowKey = keyMap.get(nowKey);
				writer.flush();
				writer.close();
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(nowKey);
	}
}
