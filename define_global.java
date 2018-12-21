import java.io.*;
import java.util.*;

public class define_global {
	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		List<List<String>> anal = new analyzer().answer(path_in);
		String[] step, i, C, S, c, k, p;

		i = (String[]) anal.get(0).toArray(new String[anal.get(0).size()]);
		C = (String[]) anal.get(1).toArray(new String[anal.get(1).size()]);
		S = (String[]) anal.get(2).toArray(new String[anal.get(2).size()]);
		c = (String[]) anal.get(3).toArray(new String[anal.get(3).size()]);
		k = (String[]) anal.get(4).toArray(new String[anal.get(4).size()]);
		p = (String[]) anal.get(5).toArray(new String[anal.get(5).size()]);
		step = (String[]) anal.get(6).toArray(new String[anal.get(6).size()]);
		table tb = new table();
		init(tb);
		new define_global().answer(step, i, C, S, c, k, p, tb);
	}

	void answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1,
			table tb) {
		String[] step, i, C, S, c, k, p;
		step = step1;// token序列
		i = i1;// 变量
		C = C1;// 字符
		S = S1;// 字符串
		c = c1;// 数字常量
		k = k1;// 关键字
		p = p1;// 符号

		// 新建的临时变量，就直接加进总表就好了
		// 对于进来的句子，先判断是否有逗号，来判别是几个变量
		String tp = k[Integer.parseInt(step[0].substring(3, step[0].length() - 1))];// 进来的语句第一个都是类型如int
		if (p[Integer.parseInt(step[step.length - 1].substring(3, step[0].length() - 1))].equals(")")) {
			tp = "function";
		}
		List<String> name = new ArrayList<String>();
		List<Integer> other = new ArrayList<Integer>();
		if (tp.equals("int") || tp.equals("char")) {// 对int或者char的定义，other是"_"
			for (int j = 1; j < step.length; j++) {
				// 找逗号判断几个变量，如果遇到逗号或分号，则变量在逗号或分号前一个
				if (step[j].substring(1, 2).equals("p")) {
					if (p[Integer.parseInt(step[j].substring(3, step[j].length() - 1))].equals(",")
							|| p[Integer.parseInt(step[j].substring(3, step[j].length() - 1))].equals(";")) {
						name.add(i[Integer.parseInt(step[j - 1].substring(3, step[j - 1].length() - 1))]);
						other.add(-1);
					}
				}
			}
			for (int j = 0; j < name.size(); j++) {// 对于这次定义的每个变量
				table.vari thisv = tb.new vari();// 新建一个vari
				thisv.name = name.get(j);
				thisv.tp = tp;
				thisv.other = other.get(j);
				thisv.ofad = getofad(tb.synbl);
				tb.synbl.add(thisv);
			}
		} else if (tp.equals("function")) {
			table.vari thisv = tb.new vari();// 新建一个vari
			int j;
			for (j = 1; j < step.length; j++) {
				if (step[j].substring(1, 2).equals("i")) {
					thisv.name = i[Integer.parseInt(step[j].substring(3, step[j].length() - 1))];
					break;
				}
			}
			thisv.tp = tp;
			thisv.other = tb.pfinfl.size();
			thisv.ofad = 0;
			tb.synbl.add(thisv);

			table.func fuc = tb.new func();
			fuc.name = thisv.name;
			List<String> xctp = new ArrayList<String>();
			List<String> xcname = new ArrayList<String>();
			for (j=j+1; j < step.length; j++) {
				if (step[j].substring(1, 2).equals("i")) {
					xcname.add(i[Integer.parseInt(step[j].substring(3, step[j].length() - 1))]);
				} else if (step[j].substring(1, 2).equals("k")) {
					xctp.add(k[Integer.parseInt(step[j].substring(3, step[j].length() - 1))]);
				}
			}
			fuc.xctp = xctp;
			fuc.xcname = xcname;
			List<table.vari> vt = new ArrayList<table.vari>();
			table.vari xc;
			for (j = 0; j < xctp.size(); j++) {
				xc = tb.new vari();
				xc.name = xcname.get(j);
				xc.tp = xctp.get(j);
				xc.ofad = getofad(vt);
				vt.add(xc);
			}
			fuc.vt = vt;
			tb.pfinfl.add(fuc);
		}
		//tb.print(tb);
		wt(tb);
	}

	static void wt(table tb) {
		String result = "";
		for (int j = 0; j < tb.synbl.size(); j++) {
			table.vari tv = tb.synbl.get(j);
			result = result.concat(tv.name).concat(" ").concat(tv.tp).concat(" ").concat(String.valueOf(tv.ofad))
					.concat(" ").concat(String.valueOf(tv.other)).concat("\n");
		}
		result = result.concat("\n");
		for (int j = 0; j < tb.pfinfl.size(); j++) {
			table.func tf = tb.pfinfl.get(j);
			List<String> xctp = tf.xctp;
			List<String> xcname = tf.xcname;
			List<table.vari> vt;
			result = result.concat(tf.name).concat("\n");
			for (int jj = 0; jj < xctp.size(); jj++) {
				result = result.concat(xctp.get(jj)).concat(" ").concat(xcname.get(jj)).concat("\n");
			}
			vt = tf.vt;
			for (int jj = 0; jj < vt.size(); jj++) {
				result = result.concat(vt.get(jj).name).concat(" ").concat(vt.get(jj).tp).concat(" ")
						.concat(String.valueOf(vt.get(jj).ofad)).concat(" ").concat(String.valueOf(vt.get(jj).other))
						.concat("\n");
			}
			result = result.concat("\n");
			for (int jj = 0; jj < tb.vall.size(); jj++) {
				result = result.concat(tb.vall.get(jj)).concat(" ");
			}
		}
		try {
			File writename = new File("./z.符号表.txt");
			writename.createNewFile();
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(writename), "UTF-8");
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static int getofad(List<table.vari> vt) {
		// 求偏移地址：如果vt.size()为0，则偏移地址为0，
		// 如果vt.size()不为0，就去看看上一条vari的类型是否是int[]，
		// 如果是int[]，那么偏移地址为上一条vari的other+ofad
		// 如果不是数组而是int或者char，那么偏移地址为ofad+1
		if (vt.size() == 0)
			return 0;
		if (vt.get(vt.size() - 1).tp.equals("int[]"))
			return vt.get(vt.size() - 1).other + vt.get(vt.size() - 1).ofad;
		else
			return vt.get(vt.size() - 1).ofad + 1;
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