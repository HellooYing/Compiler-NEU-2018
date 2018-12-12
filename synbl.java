import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;
public class optimization{
	public static void main(String[] args) throws Exception{
		String path_in = "./z.c语言代码输入.txt";
        new analyzer().answer(path_in);
        try {
			File filename = new File(path_out);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine().substring(1);
			step = line.split(" ");
			line = br.readLine();
			i = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			C = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			S = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			c = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			k = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			p = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	List<String[]> answer(List<String[]> qt){
		
		String result = "";
		for (int aa = 0; aa < r.size(); aa++) {
			result = result.concat("[");
			for (int aaa = 0; aaa < 4; aaa++) {
				result = result.concat(r.get(aa)[aaa]);
				if (aaa != 3)
					result = result.concat(",");
			}
			result = result.concat("] ");
		}
		try {
			File writename = new File("./z.优化后的四元式.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;        
	}
	static void printLS(List<String[]> r){
		for(String[] a:r){
			for(String aa:a){
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	boolean is_c(String c){
		try{
			Double.valueOf(c);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}