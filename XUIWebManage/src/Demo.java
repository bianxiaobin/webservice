import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Demo {
	public static void main(String[] args) {
		int i = 0;
		while(true){
			if(i > 10000){
				break;
			}
			System.out.println(createRandomDate());
			i++;
		}
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
}
