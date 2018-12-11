import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;
public class object_code{
	public static void main(String[] args) throws Exception {
		// 从文件读取四元式：
		List<String[]> qt = new ArrayList<String[]>();
		String[] t = {}, ttt;
		String tt;
		try {
			File filename = new File("./z.优化后的四元式.txt");
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			t = line.split(" ");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int j = 0; j < t.length; j++) {
			tt = t[j].substring(1, t[j].length() - 1);
			ttt = tt.split(",");
			qt.add(ttt);
		}
		List<String> r = new object_code().answer(qt);
		System.out.println(r.toString());
	}

	List<String> answer(List<String[]> qt){
		List<String> code = new ArrayList<String>();
		String[] inqt;
		boolean[][] active=new boolean[qt.size()][4];//存qt的活跃信息
		HashSet<String> t = new HashSet<String>();//一个集合，为了把每个非数字的变量名加入set去重，获取变量表
		List<String> variable;//set转化为list的变量表
		boolean[] symbl;//存放变量对应的活跃信息
		String RDL;//表示寄存器此时存的是哪个变量。我是写的单寄存器。

		for(int i=0;i<qt.size();i++){//加set去重获取变量表
			inqt=qt.get(i);
			for(int j=1;j<inqt.length;j++){
				if(!is_c(inqt[j])){//如果当前字符不是数字
					t.add(inqt[j]);
				}
			}
		}

		variable = new ArrayList<String>(t);
		symbl = new boolean[variable.size()];
		for(int i=0;i<variable.size();i++){//给变量的活跃信息赋初值
			if(variable.get(i).length()>1&&variable.get(i).substring(0,1).equals("t")&&is_c(variable.get(i).substring(1))){
				//如果variable是t+数字的组合，就是临时变量
				symbl[i]=false;//临时变量初值false
			}
			else symbl[i]=true;//非临时变量初值true
		}

		for(int i=qt.size()-1;i>=0;i--){//逆序扫描四元式组，添加变量在每个四元式的时候的活跃信息
			inqt=qt.get(i);
			if(t.contains(inqt[1])){//如果inqt[1]是变量而不是数字，也就是它在t那个集合里
				active[i][1]=symbl[index(variable,inqt[1])];//给它添加活跃信息
				symbl[index(variable,inqt[1])]=true;
			}
			if(t.contains(inqt[2])){
				active[i][2]=symbl[index(variable,inqt[2])];
				symbl[index(variable,inqt[2])]=true;
			}
			if(t.contains(inqt[3])){
				active[i][3]=symbl[index(variable,inqt[3])];
				symbl[index(variable,inqt[3])]=false;
			}
		}//到这为止，active存放着变量的活跃信息，位置与qt一一对应。而没有活跃信息的数字和符号默认为false，后面应用时应避免查询。

		RDL="";
		for(int i=0;i<qt.size();i++){
			inqt=qt.get(i);
			if(inqt[0].equals("+")){
				if(RDL==""){
					code.add("LD R,".concat(inqt[1]));
					code.add("ADD R,".concat(inqt[2]));
				}
				else if(RDL.equals(inqt[2])){
					code.add("ADD R,".concat(inqt[1]));
				}
				else if(RDL.equals(inqt[1])){
					code.add("ADD R,".concat(inqt[2]));
				}
				else{
					if(active[i-1][3]) code.add("ST R,".concat(RDL));//查询活跃信息表，RDL此时是上一条四元式的结果，如果这个结果变量仍然活跃，才把它弹到内存
					code.add("LD R,".concat(inqt[1]));
					code.add("ADD R,".concat(inqt[2]));
				}
			}
			else if(inqt[0].equals("-")){
				if(RDL==""){
					code.add("LD R,".concat(inqt[1]));
					code.add("SUB R,".concat(inqt[2]));
				}
				else if(RDL.equals(inqt[1])){
					code.add("SUB R,".concat(inqt[2]));
				}
				else{
					if(active[i-1][3]) code.add("ST R,".concat(RDL));
					code.add("LD R,".concat(inqt[1]));
					code.add("SUB R,".concat(inqt[2]));
				}
			}
			else if(inqt[0].equals("*")){
				if(RDL==""){
					code.add("LD R,".concat(inqt[1]));
					code.add("MUL R,".concat(inqt[2]));
				}

				else if(RDL.equals(inqt[2])){
					code.add("MUL R,".concat(inqt[1]));
				}
				else if(RDL.equals(inqt[1])){
					code.add("MUL R,".concat(inqt[2]));
				}
				else{
					if(active[i-1][3]) code.add("ST R,".concat(RDL));
					code.add("LD R,".concat(inqt[1]));
					code.add("MUL R,".concat(inqt[2]));
				}
			}
			else if(inqt[0].equals("/")){
				if(RDL==""){
					code.add("LD R,".concat(inqt[1]));
					code.add("DIV R,".concat(inqt[2]));
				}
				else if(RDL.equals(inqt[1])){
					code.add("DIV R,".concat(inqt[2]));
				}
				else{
					if(active[i-1][3]) code.add("ST R,".concat(RDL));
					code.add("LD R,".concat(inqt[1]));
					code.add("DIV R,".concat(inqt[2]));
				}
			}
			else if(inqt[0].equals("=")){
				//无变量的常数表达式最终运算结果会存到t里
				if(RDL==""){
					code.add("LD R,".concat(inqt[1]));
					code.add("ST R,".concat(inqt[3]));
				}
				else if(RDL.equals(inqt[1])){
					code.add("ST R,".concat(inqt[3]));
				}
				else{
					if(active[i-1][3]) code.add("ST R,".concat(RDL));
					code.add("LD R,".concat(inqt[1]));
					code.add("ST R,".concat(inqt[3]));
				}
			}
			RDL=inqt[3];
		}//这里把所有的都执行完了，再判断最后留在寄存器里的变量是否活跃，活跃就弹出
		if(active[qt.size()-1][3]&&(!qt.get(qt.size()-1)[0].equals("="))) code.add("ST R,".concat(RDL));

		String result = "";
		for (int i = 0; i < code.size(); i++) {
			result=result.concat(code.get(i)).concat("\n");
		}
		try {
			File writename = new File("./z.目标代码.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	int index(List<String> variable,String str){
		int i;
		for(i=0;i<variable.size();i++){
			if(variable.get(i).equals(str)) break;
			else if(i==variable.size()-1){
				i=-1;
				break;
			}
		}
		return i;
	}

	void printLS(List<String[]> r){
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