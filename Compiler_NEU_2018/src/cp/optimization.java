package cp;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class optimization {
	public static void main(String[] args) throws Exception {
		List<String[]> qt = new ArrayList<String[]>();
		String[] t = {}, ttt;
		String tt;
		try {
			File filename = new File("./z.��Ԫʽ.txt");
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
		List<String[]> r = new optimization().answer(qt);

	}

	List<String[]> answer(List<String[]> qt) {
		List<String[]> r = qt;
		String[] inqt;
		String[] t;
		List<String> replace = new ArrayList<String>();
		int flag = 1;// �������ѭ����û���µ���Ҫ�Ż��ĵط����Ŵ��������Ż���ϡ�
		while (flag == 1) {
			flag = 0;
			int flag_appear12;// ����t1�����Ľ�����Ƿ�������ĵ�һλ�͵ڶ�λ���ֹ������ֹ��ʹ���ʹ�ù���������Ч��ֵ��
			int flag_appear3;// ����t1�����Ľ����������ĵ���λ���ֹ����������¸�ֵ����
			for (int i = 0; i < r.size(); i++) {// ������ѭ�������ĳ����Ԫʽ�Ľ���ں��������¸�ֵ������û�����ù�����ô������Ч��ֵ����ɾ��
				inqt = r.get(i);
				if(!(inqt[0].equals("+")||inqt[0].equals("-")||inqt[0].equals("*")||inqt[0].equals("/")||inqt[0].equals("="))) continue;
				flag_appear12 = 0;
				flag_appear3 = 0;
				for (int j = i + 1; j < r.size(); j++) {
					t = r.get(j);
					if (t[1].equals(inqt[3]) || t[2].equals(inqt[3])) {
						flag_appear12 = 1;
					}
					if (t[3].equals(inqt[3])) {
						flag_appear3 = 1;
						break;
					}
				}
				if (flag_appear12 == 0 && flag_appear3 == 1) {
					r.remove(i);
					i--;
				}
			}
			replace.add(" ");// Ϊ�������ѭ�����Խ���
			while (replace.size() > 0) {
				replace.clear();
				for (int i = 0; i < r.size(); i++) {// ��һ��ѭ������鳣���������������������Ԫʽ���һλʱ���Ͳ����滻��
					inqt = r.get(i);
					if(!(inqt[0].equals("+")||inqt[0].equals("-")||inqt[0].equals("*")||inqt[0].equals("/")||inqt[0].equals("="))) continue;
					if ((is_c(inqt[1]) && is_c(inqt[2])&&is_t(r.get(i)[3])) && (inqt[0].equals("*") || inqt[0].equals("+")
							|| inqt[0].equals("-") || inqt[0].equals("/"))) {
						// ����ڶ�λ����λ���������ҵ�һλ�ǼӼ��˳�
						replace.add(inqt[3]);// ���滻�б����������t1 6.28������Ȼ������t1����6.28
						switch (inqt[0]) {// �����Ǽ���ڶ�λ����λ����������Ȼ��ӵ�replace�replace�ɶԼ��룬ż��λ��t1������λ��6.28
						case "*":
							replace.add(Double.toString(Double.valueOf(inqt[1]) * Double.valueOf(inqt[2])));
							break;
						case "+":
							replace.add(Double.toString(Double.valueOf(inqt[1]) + Double.valueOf(inqt[2])));
							break;
						case "-":
							replace.add(Double.toString(Double.valueOf(inqt[1]) - Double.valueOf(inqt[2])));
							break;
						case "/":
							replace.add(Double.toString(Double.valueOf(inqt[1]) / Double.valueOf(inqt[2])));
							break;
						}
						r.remove(i);// Ȼ�����һ��ɾ��
						i--;
						continue;
					}
					if (replace.size() > 0) {// ���replace���ǿգ��ͼ�鵱ǰ��Ԫʽ�Ƿ�Ҫ��t1�滻��6.28֮���
						flag = 1;
						for (int j = 0; j <= replace.size() / 2; j = j + 2) {// ��Ϊreplace�ǳɶԼӵ�
							if (inqt[1].equals(replace.get(j)))
								{inqt[1] = replace.get(j + 1);}
							if (inqt[2].equals(replace.get(j)))
								{inqt[2] = replace.get(j + 1);}
						}
					}
					if (r.size() > 1 && r.get(r.size() - 1)[0].equals("=")
							&& r.get(r.size() - 1)[1].equals(r.get(r.size() - 2)[3])&&is_t(r.get(r.size() - 2)[3])) {
						r.get(r.size() - 2)[3] = r.get(r.size() - 1)[3];
						r.remove(r.size() - 1);
					}
				}
			}
			replace.add(" ");// Ϊ�������ѭ�����Խ���
			while (replace.size() > 0) {// �ڶ���ѭ�������t2��t4�ķ��š���������Ŀ���������һ������ôt4=t2��t4������Ԫʽɾ��
				replace.clear();
				for (int i = 0; i < r.size(); i++) {
					inqt = r.get(i);
					if(!(inqt[0].equals("+")||inqt[0].equals("-")||inqt[0].equals("*")||inqt[0].equals("/")||inqt[0].equals("="))) continue;
					for (int j = i + 1; j < r.size(); j++) {
						t = r.get(j);
						if (inqt[0].equals(t[0]) && inqt[1].equals(t[1]) && inqt[2].equals(t[2])) {
							replace.add(inqt[3]);
							replace.add(t[3]);
							r.remove(j);
							j--;
						}
					}
				}
				for (int i = 0; i < r.size(); i++) {
					inqt = r.get(i);
					for (int j = 0; j < inqt.length - 1; j++) {
						for (int k = 0; k < replace.size() / 2; k = k + 2) {
							if (inqt[j].equals(replace.get(k + 1))) {
								inqt[j] = replace.get(k);
							}
						}
					}
				}
				if (replace.size() > 0)
					flag = 1;
			}

			// // ���ı�ѭ����ɾȥ�����õ���ʱ���������򣬴ӵ�һ�������շ��ص���ʱ�����������������û���������ã�û�о�ɾȥ��
			// for (int i = r.size() - 2; i >= 0; i--) {
			// 	inqt = r.get(i);
			// 	if (!is_t(inqt[3]))
			// 		continue;
			// 	flag_appear12 = 0;
			// 	for (int j = i + 1; j < r.size(); j++) {
			// 		t = r.get(j);
			// 		if (t[1].equals(inqt[3]) || t[2].equals(inqt[3])) {
			// 			flag_appear12 = 1;
			// 		}
			// 	}
			// 	if (flag_appear12 == 0) {
			// 		r.remove(i);
			// 		i++;
			// 	}
			// }

		}
		writeop(r);
		return r;
	}
	static void writeop(List<String[]> r){
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
			File writename = new File("./z.�Ż������Ԫʽ.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static void wtop(List<String[]> r){
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
			File writename = new File("../z.�Ż������Ԫʽ.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	boolean is_c(String c) {
		try {
			Double.valueOf(c);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	boolean is_t(String t) {
		if (t.length() < 2)
			return false;
		if (!t.substring(0, 1).equals("t"))
			return false;
		if (!is_c(t.substring(1)))
			return false;
		return true;
	}
}