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
		List<String[]> r = new ArrayList<String[]>();
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
}