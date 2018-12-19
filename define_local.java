import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;
public class define_local{
	//本函数的作用，在全局变量的情况下，输入int a这种定义性语句的token序列和当前符号总表，
	//输出修改后的符号表。
	//然后在生成目标代码的时候，先查询一个变量在不在总表里，在就用全局变量的操作（直接在数据段读写），
	//不在就用临时变量的操作（放到堆栈段，用活动记录跟堆栈段一一对应，每次应该读堆栈段的哪里是临时变量的值）
	//因此，在define_local输入int a,就只进行查看a是否在总表里出现过（重定义检测），然后在对应函数表的

	//List<String[]> synbl;总表：变量名、类型（int int[] function char)、在数据段中的偏移地址、其他信息
	//（如类型为函数，则指向函数表中的某一个。如类型为数组，则存放数组长度）
	//所以总表中的String[]的length=4，synbl.size()=定义语句出现次数

	//List<List<String>> pfinfl;
	//函数表：函数名0、形参类型1、形参名（与形参类型一一对应）2、返回类型(如["int"])3、临时变量（[a,b,c]）4、临时变量在堆栈段的偏移地址5
	//如果临时变量是一个数组，它的下一个临时变量的偏移地址就要加上那个数组的长度

	//List<String> vall;活动记录好像不需要？

	//当前能接收的语句只有int a;/int a,b,c;
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
		p = new String[2];
		p[0]=",";
		p[1]=";";
		table tb=new table();
		table.func s=tb.new func();
		s.name="test";
		tb.pfinfl.add(s);
		String fnm="test";
        new define_local().answer(step, i, C, S, c, k, p, tb, fnm);
		System.out.println(tb.pfinfl.get(0).vt.get(0).name);
	}

	void answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1, table table, String fnm){
		String[] step, i, C, S, c, k, p;
		step=step1;//token序列
        i=i1;//变量
		C=C1;//字符
		S=S1;//字符串
		c=c1;//数字常量
		k=k1;//关键字
		p=p1;//符号
		table.func func=table.new func();
		List<table.var> vt=new ArrayList<table.var>();//这个函数的临时变量表
		
        for(int j=0;j<table.pfinfl.size();j++){
			if(table.pfinfl.get(j).name.equals(fnm)){//符号表的函数表的函数名与fnm相同的那个func
				//找到了本次定义所在的子函数
				func=table.pfinfl.get(j);
				vt=table.pfinfl.get(j).vt;
			}
		}
		

		//对于进来的句子，先判断是否有逗号，来判别是几个变量
		String tp=k[Integer.parseInt(step[0].substring(3,4))];//进来的语句第一个都是类型如int
		List<String> name=new ArrayList<String>();
		List<Integer> other=new ArrayList<Integer>();
		if(tp.equals("int")||tp.equals("char")){//对int或者char的定义，other是"_"
		for(int j=1;j<step.length;j++){
			//找逗号判断几个变量，如果遇到逗号或分号，则变量在逗号或分号前一个
			if(step[j].substring(1,2).equals("p")){
				if(p[Integer.parseInt(step[j].substring(3,4))].equals(",")||p[Integer.parseInt(step[j].substring(3,4))].equals(";")){
					name.add(i[Integer.parseInt(step[j-1].substring(3,4))]);
					other.add(-1);
				}
			}
		}
		}
		//现在拿到了这次变量名，现在要新建一个var类型，用来存本变量的信息。本变量的信息中有一条偏移地址，
		

		for(int j=0;j<name.size();j++){//对于这次定义的每个变量
        	table.var thisv=table.new var();//新建一个var
			thisv.name=name.get(j);
			thisv.tp=tp;
			thisv.other=other.get(j);
			thisv.ofad=getofad(vt);
			vt.add(thisv);
		}

		func.vt=vt;
		
		for(int j=0;j<table.pfinfl.size();j++){
			if(table.pfinfl.get(j).name.equals(fnm)){//符号表的函数表的函数名与fnm相同的那个func
				//找到了本次定义所在的子函数
				table.pfinfl.set(j,func);
			}
		}
        // String result = "";
		// for (int aa = 0; aa < r.size(); aa++) {
		// 	result = result.concat("[");
		// 	for (int aaa = 0; aaa < 4; aaa++) {
		// 		result = result.concat(r.get(aa)[aaa]);
		// 		if (aaa != 3)
		// 			result = result.concat(",");
		// 	}
		// 	result = result.concat("] ");
		// }
		// try {
		// 	File writename = new File("./z.符号表.txt");
		// 	writename.createNewFile();
		// 	BufferedWriter out = new BufferedWriter(new FileWriter(writename));
		// 	out.write(result);
		// 	out.flush();
		// 	out.close(); 
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// return r;        
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

	static int getofad(List<table.var> vt){
		//求偏移地址：如果vt.size()为0，则偏移地址为0，
		//如果vt.size()不为0，就去看看上一条var的类型是否是int[]，
		//如果是int[]，那么偏移地址为上一条var的other+ofad
		//如果不是数组而是int或者char，那么偏移地址为ofad+1
		if(vt.size()==0) return 0;
		if(vt.get(vt.size()-1).tp.equals("int[]")) return vt.get(vt.size()-1).other+vt.get(vt.size()-1).ofad;
		else return vt.get(vt.size()-1).ofad+1;
	}
}