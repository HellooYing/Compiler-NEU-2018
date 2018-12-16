import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
public class exp_four {
	public static int now = 0, flag = 1, num = 1, brackets = 0;
	public static String[] step, i, C, S, c, k, p, inqt;
	public static Stack<String> st = new Stack<String>();//栈
	public static Stack<String> symbol = new Stack<String>();//符号栈
	public static Stack<Integer> synum = new Stack<Integer>();//符号对应四元式的位置
	public static List<String[]> qt = new ArrayList<String[]>();//存四元式
	public static String f;
	public static List<Integer> qtok = new ArrayList<Integer>();

	//四元式组的顺序在后面是有需要的，所以重构一下这部分内容。原先是，生成四元式的时候就把它加进qt。现在则需要在完成时才这样做。
	//新建一个完成序列，代表第几个四元式是第几个完成的，然后最后对四元式按此顺序重新读取就ok啦
	//同时还要改一下输入输出，answer的输入别从文件读取了，改成输入token序列和iCSckp，输出qt
	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		String path_out = "./z.token序列.txt";
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
		List<String[]> r1=new exp_four().answer(step, i, C, S, c, k, p);
	}

	public static String getTraceInfo() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName())
		.append("; number: ").append(stacks[1].getLineNumber());
		return sb.toString();
	}

	public static void printall() {
		System.out.println(symbol.toString());
		System.out.println(synum.toString());
		for (int aa = 0; aa < qt.size(); aa++) {
			System.out.print("[");
			for (int aaa = 0; aaa < 4; aaa++) {
				System.out.print(qt.get(aa)[aaa]);
				if (aaa != 3)
					System.out.print(",");
				else {
					System.out.print(",");
					System.out.print(aa);
				}
			}
			System.out.print("] ");
		}
		System.out.println();
	}

	private void E() {
		if (flag == 0)
			return;
		st.push("E");
		T();
		D();
		st.pop();
		if (st.empty() && (!symbol.empty())) {
			while (!symbol.empty()) {
				if (f != null) {
					inqt = qt.get(qt.size() - 1);
					if (f != null)
						inqt[2] = f;
					else
						inqt[2] = "t".concat(String.valueOf(num - 1));
					f = null;
					inqt[3] = "t".concat(String.valueOf(num));
					qtok.add(synum.peek());
					num++;
					symbol.pop();
					synum.pop();
				} else {
					while ((!synum.empty()) && synum.peek() == -1) {
						symbol.pop();
						synum.pop();
					}
					if (!symbol.empty()) {
						inqt = qt.get(synum.peek());
						inqt[2] = "t".concat(String.valueOf(num - 1));
						inqt[3] = "t".concat(String.valueOf(num));
						qtok.add(synum.peek());
						num++;
						symbol.pop();
						synum.pop();
					}
				}
			}
		}
	}

	private void T() {
		if (flag == 0)
			return;
		st.push("T");
		F();
		Y();
		st.pop();
	}

	private void D() {
		if (flag == 0)
			return;
		st.push("D");
		String t;
		switch (step[now].substring(1, 2)) {
		case "i":
			t = i[Integer.parseInt(step[now].substring(3, 4))];
			st.pop();
			return;
		case "c":
			t = c[Integer.parseInt(step[now].substring(3, 4))];
			st.pop();
			return;
		case "p":
			t = p[Integer.parseInt(step[now].substring(3, 4))];
			if (t.equals("+") || t.equals("-")) {
				if (now == step.length - 1) {
					flag = 0;
					return;
				}
				;
				if (symbol.empty()) {
					symbol.push(t);
					qt.add(new String[4]);
					inqt = qt.get(qt.size() - 1);
					inqt[0] = t;
					if (f != null)
						inqt[1] = f;
					else
						inqt[1] = "t".concat(String.valueOf(num - 1));
					f = null;
					synum.push(qt.size() - 1);
				} else if (symbol.peek().equals("(") || ((symbol.peek().equals("+") || symbol.peek().equals("-"))
						&& (t.equals("*") || t.equals("/")))) {
					symbol.push(t);
					qt.add(new String[4]);
					inqt = qt.get(qt.size() - 1);
					inqt[0] = t;
					if (f != null)
						inqt[1] = f;
					else
						inqt[1] = "t".concat(String.valueOf(num - 1));
					f = null;
					synum.push(qt.size() - 1);
				} else {
					while ((!symbol.empty())
							&& (!(symbol.peek().equals("(") || ((symbol.peek().equals("+") || symbol.peek().equals("-"))
									&& (t.equals("*") || t.equals("/")))))) {
						if (f != null) {
							inqt = qt.get(qt.size() - 1);
							if (f != null)
								inqt[2] = f;
							else
								inqt[2] = "t".concat(String.valueOf(num - 1));
							f = null;
							inqt[3] = "t".concat(String.valueOf(num));
							qtok.add(synum.peek());
							num++;
							symbol.pop();
							synum.pop();
						} else {
							inqt = qt.get(synum.peek());
							inqt[2] = "t".concat(String.valueOf(num - 1));
							inqt[3] = "t".concat(String.valueOf(num));
							qtok.add(synum.peek());
							num++;
							symbol.pop();
							synum.pop();
						}
					}
					qt.add(new String[4]);
					inqt = qt.get(qt.size() - 1);
					inqt[0] = t;
					inqt[1] = "t".concat(String.valueOf(num - 1));
					symbol.push(t);
					synum.push(qt.size() - 1);
				}
				now++;
				T();
				D();
				break;
			} else if (t.equals("*") || t.equals("/") || t.equals("(") || t.equals(")")) {
				st.pop();
				return;
			} else {
				flag = 0;
				return;
			}
		default:
			flag = 0;
			return;
		}
		st.pop();
		if (now == step.length - 1 && f != null) {
			inqt = qt.get(qt.size() - 1);
			if (f != null)
				inqt[2] = f;
			else
				inqt[2] = "t".concat(String.valueOf(num - 1));
			f = null;
			inqt[3] = "t".concat(String.valueOf(num));
			qtok.add(synum.peek());
			num++;
			symbol.pop();
			synum.pop();
		}
	}

	private void Y() {
		if (flag == 0)
			return;
		st.push("Y");
		String t;
		switch (step[now].substring(1, 2)) {
		case "i":
			t = i[Integer.parseInt(step[now].substring(3, 4))];
			if (now == step.length - 1) {
				st.pop();
				return;
			}
			now++;
			st.pop();
			return;
		case "c":
			t = c[Integer.parseInt(step[now].substring(3, 4))];
			if (now == step.length - 1) {
				st.pop();
				return;
			}
			now++;
			st.pop();
			return;
		case "p":
			t = p[Integer.parseInt(step[now].substring(3, 4))];
			if (t.equals("*") || t.equals("/")) {
				if (now == step.length - 1) {
					flag = 0;
					return;
				}
				;
				if (symbol.empty()) {
					symbol.push(t);
					qt.add(new String[4]);
					inqt = qt.get(qt.size() - 1);
					inqt[0] = t;
					if (f != null)
						inqt[1] = f;
					else
						inqt[1] = "t".concat(String.valueOf(num - 1));
					f = null;
					synum.push(qt.size() - 1);
				} else if (symbol.peek().equals("(") || ((symbol.peek().equals("+") || symbol.peek().equals("-"))
						&& (t.equals("*") || t.equals("/")))) {
					symbol.push(t);
					qt.add(new String[4]);
					inqt = qt.get(qt.size() - 1);
					inqt[0] = t;
					if (f != null)
						inqt[1] = f;
					else
						inqt[1] = "t".concat(String.valueOf(num - 1));
					f = null;
					synum.push(qt.size() - 1);
				} else {
					while ((!symbol.empty())
							&& (!(symbol.peek().equals("(") || ((symbol.peek().equals("+") || symbol.peek().equals("-"))
									&& (t.equals("*") || t.equals("/")))))) {
						if (f != null) {
							inqt = qt.get(qt.size() - 1);
							if (f != null)
								inqt[2] = f;
							else
								inqt[2] = "t".concat(String.valueOf(num - 1));
							f = null;
							inqt[3] = "t".concat(String.valueOf(num));
							qtok.add(synum.peek());
							num++;
							symbol.pop();
							synum.pop();
						} else {
							inqt = qt.get(synum.peek());
							inqt[2] = "t".concat(String.valueOf(num - 1));
							inqt[3] = "t".concat(String.valueOf(num));
							qtok.add(synum.peek());
							num++;
							symbol.pop();
							synum.pop();
						}
					}
					qt.add(new String[4]);
					inqt = qt.get(qt.size() - 1);
					inqt[0] = t;
					inqt[1] = "t".concat(String.valueOf(num - 1));
					symbol.push(t);
					synum.push(qt.size() - 1);
				}
				now++;
				F();
				Y();
				break;
			} else if (t.equals("+") || t.equals("-") || t.equals("(") || t.equals(")")) {
				st.pop();
				return;
			} else {
				flag = 0;
				return;
			}
		default:
			flag = 0;
			return;
		}
		st.pop();
		if (now == step.length - 1 && f != null) {
			inqt = qt.get(qt.size() - 1);
			if (f != null)
				inqt[2] = f;
			else
				inqt[2] = "t".concat(String.valueOf(num - 1));
			f = null;
			inqt[3] = "t".concat(String.valueOf(num));
			qtok.add(synum.peek());
			num++;
			symbol.pop();
			synum.pop();
		}
	}

	private void F() {
		st.push("F");
		if (flag == 0)
			return;
		String t;
		switch (step[now].substring(1, 2)) {
		case "i":
			t = i[Integer.parseInt(step[now].substring(3, 4))];
			f = t;
			if (now == step.length - 1) {
				st.pop();
				return;
			}
			now++;
			if (step[now].substring(1, 2).equals("p")) {
				t = p[Integer.parseInt(step[now].substring(3, 4))];
				while (t.equals(")")) {
					brackets--;
					if (now == step.length - 1) {
						st.pop();
						return;
					}
					now++;
					inqt = qt.get(synum.peek());
					if (f != null)
						inqt[2] = f;
					else
						inqt[2] = "t".concat(String.valueOf(num - 1));
					f = null;
					inqt[3] = "t".concat(String.valueOf(num));
					qtok.add(synum.peek());
					num++;
					symbol.pop();
					synum.pop();
					if (synum.peek() == -1) {
						symbol.pop();
						synum.pop();
					}
					if (brackets < 0) {
						flag = 0;
						return;
					}
					if (step[now].substring(1, 2).equals("p")) {
						t = p[Integer.parseInt(step[now].substring(3, 4))];
					}
				}
			}
			break;
		case "c":
			t = c[Integer.parseInt(step[now].substring(3, 4))];
			f = t;
			if (now == step.length - 1) {
				st.pop();
				return;
			}
			now++;
			if (step[now].substring(1, 2).equals("p")) {
				t = p[Integer.parseInt(step[now].substring(3, 4))];
				while (t.equals(")")) {
					brackets--;
					inqt = qt.get(synum.peek());
					if (f != null)
						inqt[2] = f;
					else
						inqt[2] = "t".concat(String.valueOf(num - 1));
					f = null;
					inqt[3] = "t".concat(String.valueOf(num));
					qtok.add(synum.peek());
					num++;
					symbol.pop();
					synum.pop();
					if (synum.peek() == -1) {
						symbol.pop();
						synum.pop();
					}
					if (now == step.length - 1) {
						st.pop();
						return;
					}
					now++;
					if (brackets < 0) {
						flag = 0;
						return;
					}
					if (step[now].substring(1, 2).equals("p")) {
						t = p[Integer.parseInt(step[now].substring(3, 4))];
					}
				}
			}
			break;
		case "p":
			t = p[Integer.parseInt(step[now].substring(3, 4))];
			if (t.equals("(")) {
				if (now == step.length - 1) {
					flag = 0;
					return;
				} else if (step[now + 1].substring(1, 2).equals("p")
						&& p[Integer.parseInt(step[now + 1].substring(3, 4))].equals(")")) {
					flag = 0;
					return;
				}
				now++;
				brackets++;
				symbol.push("(");
				synum.push(-1);
				E();
				break;
			} else {
				flag = 0;
				return;
			}
		default:
			flag = 0;
			return;
		}
		st.pop();
	}

	public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1) {
		i=i1;
		C=C1;
		S=S1;
		c=c1;
		k=k1;
		p=p1;
		qt.clear();
		now = 0;
		flag = 1;
		num = 1;
		brackets = 0;
		st = new Stack<String>();//栈
		symbol = new Stack<String>();//符号栈
		synum = new Stack<Integer>();//符号对应四元式的位置
		qt = new ArrayList<String[]>();//存四元式
		qtok = new ArrayList<Integer>();
		List<String[]> final_qt = new ArrayList<String[]>();
		if(p[Integer.parseInt(step1[1].substring(3, 4))].equals("=")&&step1.length>4){
			step=Arrays.copyOfRange(step1,2,step1.length);
			E();
			// printall();
			System.out.print(qt.size());
				printLS(qt);
				System.out.print(qtok.size());
				for(int a:qtok){
					System.out.print(a);
				}
			for(int aa=0;aa<qt.size();aa++){
				inqt=new String[4];
				
				inqt[0]=qt.get(qtok.get(aa))[0];
				inqt[1]=qt.get(qtok.get(aa))[1];
				inqt[2]=qt.get(qtok.get(aa))[2];
				inqt[3]=qt.get(qtok.get(aa))[3];
				final_qt.add(inqt);
			}
			inqt=new String[4];
			inqt[0]="=";
			inqt[1]=final_qt.get(final_qt.size()-1)[3];
			inqt[2]="_";
			inqt[3]=i[Integer.parseInt(step1[0].substring(3, 4))];
			final_qt.add(inqt);
		}
		else if(p[Integer.parseInt(step1[1].substring(3, 4))].equals("=")){
			step=step1;
			final_qt = new ArrayList<String[]>();
			inqt=new String[4];
			inqt[0]=p[Integer.parseInt(step[1].substring(3, 4))];
			String t;
        	switch (step[2].substring(1, 2)) {
			case "i":
				inqt[1] = i[Integer.parseInt(step[2].substring(3, 4))];
        	    break;
			case "c":
				inqt[1] = c[Integer.parseInt(step[2].substring(3, 4))];
        	    break;
        	//case "C":
        	//case "S":
        	default:
				System.out.println("输入错误！请检查c语言代码输入.txt");
			}
			inqt[2]="_";
			inqt[3]=i[Integer.parseInt(step[0].substring(3, 4))];
			final_qt.add(inqt);
			
		}
		else{
			step=step1;
			E();
			// printall();
			for(int aa=0;aa<qt.size();aa++){
				inqt=new String[4];
				inqt[0]=qt.get(qtok.get(aa))[0];
				inqt[1]=qt.get(qtok.get(aa))[1];
				inqt[2]=qt.get(qtok.get(aa))[2];
				inqt[3]=qt.get(qtok.get(aa))[3];
				final_qt.add(inqt);
			}
			// inqt=new String[4];
			// inqt[0]="=";
			// inqt[1]=final_qt.get(final_qt.size()-1)[3];
			// inqt[2]="_";
			// inqt[3]="t".concat(String.valueOf(num));
			// final_qt.add(inqt);
			
		}
		String result = "";
			for (int aa = 0; aa < final_qt.size(); aa++) {
				result = result.concat("[");
				for (int aaa = 0; aaa < 4; aaa++) {
					result = result.concat(final_qt.get(aa)[aaa]);
					if (aaa != 3)
						result = result.concat(",");
				}
				result = result.concat("] ");
			}
			try {
				File writename = new File("./z.四元式.txt");
				writename.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(writename));
				out.write(result);
				out.flush();
				out.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		return final_qt;
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
}