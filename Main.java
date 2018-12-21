import java.util.*;

public class Main {
	static void printLS(List<String[]> r) {
		for (String[] a : r) {
			for (String aa : a) {
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
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
		table tb = new table();
		tb.vall.add("main");
		List<String[]> qt = new block().answer(step, i, C, S, c, k, p, tb);
		qt = new optimization().answer(qt);
		printLS(qt);
		tb.print(tb);
		//List<String> code = new object_code().answer(qt);
		//for (String s : code)
		//	System.out.println(s);
	}
}
