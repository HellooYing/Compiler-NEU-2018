import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;
public class optimization{
	public static void main(String[] args) throws Exception{
		List<String[]> qt = new ArrayList<String[]>();
		String[] t={},ttt;
		String tt;
		try {
			File filename = new File("./z.四元式.txt");
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			t = line.split(" ");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int j=0;j<t.length;j++){
			tt=t[j].substring(1,t[j].length()-1);
			ttt=tt.split(",");
			qt.add(ttt);
		}
		List<String[]> r=new optimization().answer(qt);

	}
	List<String[]> answer(List<String[]> qt){
		List<String[]> r=qt;
		String[] inqt;
		String[] t;
		List<String> replace=new ArrayList<String>();
		int flag=1;//如果整体循环都没有新的需要优化的地方，才代表真正优化完毕。
		while(flag==1){
			flag=0;
		replace.add(" ");//为了下面的循环可以进入
		while (replace.size() > 0) {// 第二遍循环，如果t2和t4的符号、操作数、目标操作数都一样，那么t4=t2，t4那条四元式删除
			replace.clear();
			for(int i=0;i<r.size();i++){//第一遍循环，检查常数
				inqt=r.get(i);
				if((is_c(inqt[1])&&(is_c(inqt[2])))&&(inqt[0].equals("*")||inqt[0].equals("+")||inqt[0].equals("-")||inqt[0].equals("/"))){
					//如果第二位第三位都是数字且第一位是加减乘除
					replace.add(inqt[3]);//在替换列表加入类似于t1 6.28，代表等会把所有t1换成6.28
					switch(inqt[0]){//这里是计算第二位第三位的运算结果，然后加到replace里。replace成对加入，偶数位是t1，奇数位是6.28
					case "*":
						replace.add(Double.toString(Double.valueOf(inqt[1])*Double.valueOf(inqt[2])));
						break;
					case "+":
						replace.add(Double.toString(Double.valueOf(inqt[1])+Double.valueOf(inqt[2])));
						break;
					case "-":
						replace.add(Double.toString(Double.valueOf(inqt[1])-Double.valueOf(inqt[2])));
						break;
					case "/":
						replace.add(Double.toString(Double.valueOf(inqt[1])/Double.valueOf(inqt[2])));
						break;
					}
					r.remove(i);//然后把这一条删掉
					i--;
					continue;
				}
				if(replace.size()>0){//如果replace不是空，就检查当前四元式是否要把t1替换成6.28之类的
					flag=1;
					for(int j=0;j<replace.size()/2;j=j+2){//因为replace是成对加的
						if(inqt[1].equals(replace.get(j))) inqt[1]=replace.get(j+1);
						if(inqt[2].equals(replace.get(j))) inqt[2]=replace.get(j+1);
					}
				}
				if(r.size()>1&&r.get(r.size()-1)[0].equals("=")&&r.get(r.size()-1)[1].equals(r.get(r.size()-2)[3])){
					r.get(r.size()-2)[3]=r.get(r.size()-1)[3];
					r.remove(r.size()-1);
				}
			}
		}
		replace.add(" ");//为了下面的循环可以进入
		while(replace.size()>0){//第二遍循环，如果t2和t4的符号、操作数、目标操作数都一样，那么t4=t2，t4那条四元式删除
			replace.clear();
			for(int i=0;i<r.size();i++){
				inqt=r.get(i);
				for(int j=i+1;j<r.size();j++){
					t=r.get(j);
					if(inqt[0].equals(t[0])&&inqt[1].equals(t[1])&&inqt[2].equals(t[2])){
						replace.add(inqt[3]);
						replace.add(t[3]);
						r.remove(j);
						j--;
					}
				}
			}
			for(int i=0;i<r.size();i++){
				inqt=r.get(i);
				for(int j=0;j<inqt.length-1;j++){
					for(int k=0;k<replace.size()/2;k=k+2){
						if(inqt[j].equals(replace.get(k+1))){
							inqt[j]=replace.get(k);
						}
					}
				}
			}
			if(replace.size()>0) flag=1;
		}

		int flag_appear12;//代表t1这样的结果，是否在下面的第一位和第二位出现过。出现过就代表被使用过，不是无效赋值。
		int flag_appear3;//代表t1这样的结果，在下面的第三位出现过，代表被重新赋值过。
		for(int i=0;i<r.size();i++){//第三遍循环，如果某个四元式的结果在后续被重新赋值过并且没被引用过，那么它是无效赋值可以删掉
			inqt=r.get(i);
			flag_appear12=0;
			flag_appear3=0;
			for(int j=i+1;j<r.size();j++){
				t=r.get(j);
				if(t[1].equals(inqt[3])||t[2].equals(inqt[3])){
					flag_appear12=1;
				}
				if(t[3].equals(inqt[3])){
					flag_appear3=1;
					break;
				}
			}
			if(flag_appear12==0&&flag_appear3==1){
				r.remove(i);
				i--;
			}
		}
		//第四遍循环，删去无引用的临时变量：逆序，从第一个非最终返回的临时变量找起，找这后面有没有它的引用，没有就删去它
		for(int i=r.size()-2;i>=0;i--){
			inqt=r.get(i);
			if(!is_t(inqt[3])) continue;
			flag_appear12=0;
			for(int j=i+1;j<r.size();j++){
				t=r.get(j);
				if(t[1].equals(inqt[3])||t[2].equals(inqt[3])){
					flag_appear12=1;
				}
			}
			if(flag_appear12==0){
				r.remove(i);
				i++;
			}
		}

		}
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
	boolean is_t(String t){
		if(t.length()<2) return false;
		if(!t.substring(0,1).equals("t")) return false;
		if(!is_c(t.substring(1))) return false;
		return true;
	}
}