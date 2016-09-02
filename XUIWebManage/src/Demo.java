import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Demo {
	public static void main(String[] args) {
		System.out.println(createRandomDate());
		System.out.println(createRandomDate());
		System.out.println(createRandomDate());
		System.out.println(createRandomDate());
	}
	private static String createRandomDate(){
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(df.format(date)+"-");
		Random random = new Random();
		sb.append(random.nextInt(100));
		sb.append(random.nextInt(100));
		sb.append(random.nextInt(100));
		return sb.toString();
	}
}
