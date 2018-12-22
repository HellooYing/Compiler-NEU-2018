import java.io.*;
import java.util.*;

public class function_four {
	public static String[] step, i, C, S, c, k, p;// 输入
	public static List<String[]> qt = new ArrayList<String[]>();// 存四元式

	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		String path_out = "./z.token序列.txt";

		// 这一段从"z.token序列.txt"文件读取了需要用到的输入，即token序列(我把它取名为p)和i, C, S, c, k, p表
		List<List<String>> anal = new analyzer().answer(path_in);
		String[] step, i, C, S, c, k, p;
		int n = 0;

		i = (String[]) anal.get(0).toArray(new String[anal.get(0).size()]);
		C = (String[]) anal.get(1).toArray(new String[anal.get(1).size()]);
		S = (String[]) anal.get(2).toArray(new String[anal.get(2).size()]);
		c = (String[]) anal.get(3).toArray(new String[anal.get(3).size()]);
		k = (String[]) anal.get(4).toArray(new String[anal.get(4).size()]);
		p = (String[]) anal.get(5).toArray(new String[anal.get(5).size()]);
		step = (String[]) anal.get(6).toArray(new String[anal.get(6).size()]);
		// 读取完毕

		// 执行for语法分析程序
		table tb = new table();
		init(tb);
		List<String[]> r1 = new function_four().answer(step, i, C, S, c, k, p, tb);
	}

	static List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1,
			String[] p1, table tb) {
		step = step1;// token序列
        i = i1;// 变量
        C = C1;// 字符
        S = S1;// 字符串
        c = c1;// 数字常量
        k = k1;// 关键字
        p = p1;// 符号
		String re=i[Integer.parseInt(step[0].substring(3, step[0].length() - 1))];
		List<String[]> r = new ArrayList<String[]>();
		int j,jj;
		String fnm=i[Integer.parseInt(step[2].substring(3, step[2].length() - 1))];
		String result=i[Integer.parseInt(step[0].substring(3, step[0].length() - 1))];
		String[] inqt = new String[4];
		inqt[0] = "sf";
		inqt[1] = fnm;
		inqt[2] = "_";
		inqt[3] = result;
		r.add(inqt);
		j=0;
		int n1;
		List<String> xcname = new ArrayList<String>();
		for(n1=0;n1<tb.pfinfl.size();n1++){
            if(fnm.equals(tb.pfinfl.get(n1).name)){
                xcname=tb.pfinfl.get(n1).xcname;
            }
        }
		n1=0;
		while(!(step[j].substring(1, 2).equals("p")&&p[Integer.parseInt(step[j].substring(3, step[j].length() - 1))].equals("("))){
			j++;
		}
		jj=j;
		while(!(step[jj].substring(1, 2).equals("p")&&p[Integer.parseInt(step[jj].substring(3, step[jj].length() - 1))].equals(")"))){
			jj++;
		}
		for(;j<jj;j++){
			if(step[j].substring(1, 2).equals("c")||step[j].substring(1, 2).equals("i")){
				inqt = new String[4];
				inqt[0] = "xc";
				inqt[1] = xcname.get(n1);
				n1++;
				if(step[j].substring(1, 2).equals("c"))
					inqt[2] = c[Integer.parseInt(step[j].substring(3, step[j].length() - 1))];
				else inqt[2] = i[Integer.parseInt(step[j].substring(3, step[j].length() - 1))];
				inqt[3] = "_";
				r.add(inqt);
			}
		}
		inqt = new String[4];
		inqt[0] = "esf";
		inqt[1] = fnm;
		inqt[2] = re;
		inqt[3] = "_";
		r.add(inqt);
		printLS(r);
		return r;
	}

	static void init(table tb) {
		table.func s = tb.new func();
		s.name = "test";
		List<String> xctp = new ArrayList<String>();
		xctp.add("int");
		xctp.add("int");
		List<String> xcname = new ArrayList<String>();
		xcname.add("d");
		xcname.add("f");
		s.xctp = xctp;
		s.xcname = xcname;
		table.vari v = tb.new vari();
		v.name = "d";
		v.tp = "int";
		v.ofad = 0;
		v.other = -1;
		s.vt.add(v);
		v = tb.new vari();
		v.name = "f";
		v.tp = "int";
		v.ofad = 1;
		v.other = -1;
		s.vt.add(v);
		tb.pfinfl.add(s);

		v = tb.new vari();
		v.name = "e";
		v.tp = "int";
		v.ofad = 0;
		v.other = -1;
		tb.synbl.add(v);

		List<String> vall = new ArrayList<String>();
		vall.add("main");
		vall.add("test");
		tb.vall = vall;
	}
	static void printLS(List<String[]> r) {
		for (String[] a : r) {
			for (String aa : a) {
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}