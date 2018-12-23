import java.util.*;
import java.io.*;
public class Main {
	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		String path_out = "./z.token序列.txt";
		List<List<String>> anal = new analyzer().answer(path_in,path_out);
		String[] step, i, C, S, c, k, p;
		int n = 0;
		List<String[]> qtt;
		List<List<String[]>> qqtt=new ArrayList<List<String[]>>();

		i = (String[]) anal.get(0).toArray(new String[anal.get(0).size()]);
		C = (String[]) anal.get(1).toArray(new String[anal.get(1).size()]);
		S = (String[]) anal.get(2).toArray(new String[anal.get(2).size()]);
		c = (String[]) anal.get(3).toArray(new String[anal.get(3).size()]);
		k = (String[]) anal.get(4).toArray(new String[anal.get(4).size()]);
		p = (String[]) anal.get(5).toArray(new String[anal.get(5).size()]);
		step = (String[]) anal.get(6).toArray(new String[anal.get(6).size()]);
		
		if(step[0].substring(1, 2).equals("k")&&k[Integer.parseInt(step[0].substring(3, step[0].length() - 1))].equals("int")&&step[1].substring(1, 2).equals("k")&&k[Integer.parseInt(step[1].substring(3, step[1].length() - 1))].equals("main")){
			step=Arrays.copyOfRange(step,5,step.length-1);
		}
		table tb = new table();
		tb.vall.add("main");
		List<String[]> qt = new block().answer(step, i, C, S, c, k, p, tb);
		new block().wtblock(qt);
		List<String[]> opqt = new ArrayList<String[]>();
		System.out.println("\n原始四元式：");
		printLS(qt);
		int j,jj=0;
		for(j=0;j<qt.size();j++){
			String t=qt.get(j)[0];
			if(t.equals("if")||t.equals("es")||t.equals("ie")||t.equals("wh")||t.equals("we")||t.equals("for")||t.equals("fe")||t.equals("fun")||t.equals("fe")){
				qtt=new ArrayList<String[]>();
				for(String[] s:qt.subList(jj,j)) qtt.add(s);
				qqtt.add(qtt);
				jj=j;
			}
		}
		qtt=new ArrayList<String[]>();
		for(String[] s:qt.subList(jj,j)) qtt.add(s);
		qqtt.add(qtt);
		for(j=0;j<qqtt.size();j++){
			qtt=qqtt.get(j);
			qtt=new optimization().answer(qtt);
			opqt.addAll(qtt);
		}
		System.out.println("\n优化后的四元式：");
		printLS(opqt);
		writeopqt(opqt);
		new optimization().wtop(opqt);
		tb.print(tb);
		List<String> code = new object_code().answer(opqt,tb);
		for (String s : code)
			System.out.println(s);
		new object_code().wtoc(code);
		new define_local().wtdl(tb);
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

	static void writeopqt(List<String[]> r){
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
	}

}
