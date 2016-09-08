import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Demo02 {
	public static void main(String[] args) {
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
					writer.println(year+month+monthday+"="+year+month+monthday+"-6786567783");
					System.out.println(year+month+monthday+"="+year+month+monthday+"-6786567783");
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
