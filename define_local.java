import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;
public class define_four{
	List<String[]> synbl;//总表
	List<String[]> tapel;//类型表
	List<String[]> ainfl;//数组表
	List<String[]> pfinfl;//函数表
	List<String> vall;//活动记录

	public static void main(String[] args) throws Exception{
		String path_in = "./z.c语言代码输入.txt";
		String anal=new analyzer().answer(path_in);
		String[] t=anal.split("\n");
		String[] step, i, C, S, c, k, p;
		int n=0;
		String line=t[n];
		step = line.substring(1).split(" ");
		n++;
		line=t[n];
		i = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		C = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		S = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		c = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		k = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		p = line.substring(3, line.length() - 1).replace(" ", "").split(",");
        //new define.answer(step, i, C, S, c, k, p);
	}
	List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1){
		String[] step, i, C, S, c, k, p;
		step=step1;//token序列
        i=i1;//变量
		C=C1;//字符
		S=S1;//字符串
		c=c1;//数字常量
		k=k1;//关键字
		p=p1;//符号
		List<String[]> r=new ArrayList<String[]>();
        //符号表，在定义变量或函数时加入新的
		List<String[]> synbl=new ArrayList<String[]>();//总表
		List<String[]> tapel=new ArrayList<String[]>();//类型表
		List<String[]> ainfl=new ArrayList<String[]>();//数组表
		List<String[]> pfinfl=new ArrayList<String[]>();//函数表
		List<String> vall=new ArrayList<String[]>();//活动记录




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
			File writename = new File("./z.符号表.txt");
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