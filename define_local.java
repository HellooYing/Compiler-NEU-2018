import java.io.*;
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

	//List<String> vall;

	//当前能接收的语句只有int a;/int a,b,c;
	public static void main(String[] args) throws Exception{
		String path_in = "./z.c语言代码输入.txt";
		List<List<String>> anal=new analyzer().answer(path_in);
		String[] step, i, C, S, c, k, p;
		
		i = (String[])anal.get(0).toArray(new String[anal.get(0).size()]);
		C = (String[])anal.get(1).toArray(new String[anal.get(1).size()]);
		S = (String[])anal.get(2).toArray(new String[anal.get(2).size()]);
		c = (String[])anal.get(3).toArray(new String[anal.get(3).size()]);
		k = (String[])anal.get(4).toArray(new String[anal.get(4).size()]);
		p = (String[])anal.get(5).toArray(new String[anal.get(5).size()]);
		step = (String[])anal.get(6).toArray(new String[anal.get(6).size()]);
		table tb=new table();
		init(tb);
        new define_local().answer(step, i, C, S, c, k, p, tb);
	}

	void answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1, table tb){
		String[] step, i, C, S, c, k, p;
		step=step1;//token序列
        i=i1;//变量
		C=C1;//字符
		S=S1;//字符串
		c=c1;//数字常量
		k=k1;//关键字
		p=p1;//符号
		table.func func=tb.new func();
		String fnm=tb.vall.get(tb.vall.size()-1);
		List<table.vari> vt=new ArrayList<table.vari>();//这个函数的临时变量表
		
        for(int j=0;j<tb.pfinfl.size();j++){
			if(tb.pfinfl.get(j).name.equals(fnm)){//符号表的函数表的函数名与fnm相同的那个func
				//找到了本次定义所在的子函数
				func=tb.pfinfl.get(j);
				vt=tb.pfinfl.get(j).vt;
			}
		}
		

		//对于进来的句子，先判断是否有逗号，来判别是几个变量
		String tp=k[Integer.parseInt(step[0].substring(3,step[0].length()-1))];//进来的语句第一个都是类型如int
		List<String> name=new ArrayList<String>();
		List<Integer> other=new ArrayList<Integer>();
		if(tp.equals("int")||tp.equals("char")){//对int或者char的定义，other是"_"
			for(int j=1;j<step.length;j++){
			//找逗号判断几个变量，如果遇到逗号或分号，则变量在逗号或分号前一个
				if(step[j].substring(1,2).equals("p")){
					if(p[Integer.parseInt(step[j].substring(3,step[j].length()-1))].equals(",")||p[Integer.parseInt(step[j].substring(3,step[j].length()-1))].equals(";")){
						name.add(i[Integer.parseInt(step[j-1].substring(3,step[j-1].length()-1))]);
						other.add(-1);
					}
				}
			}
		}
		//增添对数组的支持时写这里


		//现在拿到了这次变量名，现在要新建一个vari类型，用来存本变量的信息。本变量的信息中有一条偏移地址，
		

		for(int j=0;j<name.size();j++){//对于这次定义的每个变量
        	table.vari thisv=tb.new vari();//新建一个vari
			thisv.name=name.get(j);
			thisv.tp=tp;
			thisv.other=other.get(j);
			thisv.ofad=getofad(vt);
			vt.add(thisv);
		}

		func.vt=vt;
		
		for(int j=0;j<tb.pfinfl.size();j++){
			if(tb.pfinfl.get(j).name.equals(fnm)){//符号表的函数表的函数名与fnm相同的那个func
				//找到了本次定义所在的子函数
				tb.pfinfl.set(j,func);
			}
		}

		//result在txt中存放方式，先打印总表，总表中的每个vari一行
		//再打印函数表，函数表中前三个元素一行，vt：n行
        tb.print(tb);
		wt(tb);
	}
	static void  wt(table tb){
		String result = "";
		for(int j=0;j<tb.synbl.size();j++){
			table.vari tv=tb.synbl.get(j);
			result=result.concat(tv.name).concat(" ").concat(tv.tp).concat(" ").concat(String.valueOf(tv.ofad)).concat(" ").concat(String.valueOf(tv.other)).concat("\n");
		}
		result=result.concat("\n");
		for(int j=0;j<tb.pfinfl.size();j++){
			table.func tf=tb.pfinfl.get(j);
			List<String> xctp=tf.xctp;
			List<String> xcname=tf.xcname;
			List<table.vari> vt;
			result=result.concat(tf.name).concat("\n");
			for(int jj=0;jj<xctp.size();jj++){
				result=result.concat(xctp.get(jj)).concat(" ").concat(xcname.get(jj)).concat("\n");
			}
			vt=tf.vt;
			for(int jj=0;jj<vt.size();jj++){
				result=result.concat(vt.get(jj).name).concat(" ").concat(vt.get(jj).tp).concat(" ").concat(String.valueOf(vt.get(jj).ofad)).concat(" ").concat(String.valueOf(vt.get(jj).other)).concat("\n");
			}
			result=result.concat("\n");
			for(int jj=0;jj<tb.vall.size();jj++){
				result=result.concat(tb.vall.get(jj)).concat(" ");
			}
		}
		try {
			File writename = new File("./z.符号表.txt");
			writename.createNewFile();
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(writename),"UTF-8");
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	static int getofad(List<table.vari> vt){
		//求偏移地址：如果vt.size()为0，则偏移地址为0，
		//如果vt.size()不为0，就去看看上一条vari的类型是否是int[]，
		//如果是int[]，那么偏移地址为上一条vari的other+ofad
		//如果不是数组而是int或者char，那么偏移地址为ofad+1
		if(vt.size()==0) return 0;
		if(vt.get(vt.size()-1).tp.equals("int[]")) return vt.get(vt.size()-1).other+vt.get(vt.size()-1).ofad;
		else return vt.get(vt.size()-1).ofad+1;
	}
	static void init(table tb){
		
		table.func s=tb.new func();
		s.name="test";
		List<String> xctp=new ArrayList<String>();
		xctp.add("int");xctp.add("int");
		List<String> xcname=new ArrayList<String>();
		xcname.add("d");xcname.add("f");
		s.xctp=xctp;
		s.xcname=xcname;
		table.vari v=tb.new vari();
		v.name="d";
		v.tp="int";
		v.ofad=0;
		v.other=-1;
		s.vt.add(v);
		v=tb.new vari();
		v.name="f";
		v.tp="int";
		v.ofad=1;
		v.other=-1;
		s.vt.add(v);
		tb.pfinfl.add(s);

		v=tb.new vari();
		v.name="e";
		v.tp="int";
		v.ofad=0;
		v.other=-1;
		tb.synbl.add(v);

		List<String> vall=new ArrayList<String>();
		vall.add("main");
		vall.add("test");
		tb.vall=vall;
	}
}