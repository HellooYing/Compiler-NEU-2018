package cp;

import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class analyzer {
	String[] k = { "main", "void", "if", "else", "while", "for", "int", "char", "string", "break", "continue",
			"return","switch","case","default" };// �ؼ��ַŵ�������
	List<String> i = new ArrayList<String>();// ������
	List<String> C = new ArrayList<String>();// �ַ�
	List<String> S = new ArrayList<String>();// �ַ���
	List<Double> c = new ArrayList<Double>();// ����
	List<String> p = new ArrayList<String>();// ���
	// �߼��ǣ���ͷ��β��ȡString rҲ����while(j<r.length)��switch(ascii��)
	// case0���ǿո�(32)��j++
	// case1������ĸ��65-90��97-122�����ͽ������¶������һֱû���ַ��ţ��Ͷ����ո�Ϊֹ����һ�����嵥�ʡ�
	// ��������ʼ���Ƿ�Ϊ�ؼ�����k�У�����{k,n}(n��ȡֵʹk[n]==����)�������������Ϊ���Ǳ�����{i,n}
	// Ϊ��Ҫ����һ��list i��ÿ�γ��ֱ������Ϳ���list����û������û�оͼӽ�ȥ��ʹ��i[n]==������
	// ��Ϊһ�ν���ҲҪj�ӵ����ʵ�ֵ
	// case2�������֣�48-57�����ͽ������¶������ո�Ϊֹ������list c��ͬ�ϲ�����{c,n},j++
	// case3����"��34�������¶���"������list S��{S,n},j++
	// case4����'��39�������¶���'������list C��{C,n},j++
	// case5����������(33-47,58-64��91-96��123-126,��34����39���������һ�����������ַ��Ͷ����������������һ���ַ�
	// ����list p��{p,n},j++
	// default��������ҿ�����ɶ�쳣�����j++

	public static void main(String[] args) throws Exception {
		String path_in = "./z.c���Դ�������.txt";
		String path_out = "./z.token����.txt";
		new analyzer().answer(path_in,path_out);
	}

	public List<List<String>> answer(String path_in,String path_out) {
		List<List<String>> fr = new ArrayList<List<String>>();
		String result = "";
		try {
			File filename = new File(path_in);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // ����һ������������reader
			BufferedReader br = new BufferedReader(reader);// ����һ�����������ļ�����ת�ɼ�����ܶ���������
			String r = "";
			String line = "";
			int j = 0;
			int t, status, jj;
			String word;
			line = br.readLine();
			while (line != null) {
				r = r + " " + line;
				line = br.readLine();
			}
			br.close();
			while (r.indexOf("/*") != -1) {
				if (r.indexOf("*/") != -1)
					r = r.substring(0, r.indexOf("/*")).concat(r.substring(r.indexOf("*/") + 2));
				else
					r = r.substring(r.indexOf("/*"));
			}
			r = r.replaceAll(" {2,}", " ");// ɾȥ����Ŀո�
			if (r.charAt(0) == ' ')
				r = r.substring(1);// ɾȥ�ײ��ո�
			if (r.charAt(r.length() - 1) == ' ')
				r = r.substring(0, r.length() - 1);// ɾȥβ���ո�

			// System.out.println(r);

			while (j < r.length()) {
				t = (int) r.charAt(j);
				if (t == 32)
					status = 0;
				else if ((t >= 65 && t <= 90) || (t >= 97 && t <= 122))
					status = 1;
				else if (t >= 48 && t <= 57)
					status = 2;
				else if (t == 34)
					status = 3;
				else if (t == 39)
					status = 4;
				else if ((t >= 33 && t <= 47 && t != 34 && t != 39) || (t >= 58 && t <= 64) || (t >= 91 && t <= 96)
						|| (t >= 123 && t <= 126))
					status = 5;
				else {
					System.out.println("status�쳣");
					System.out.println(r.charAt(j));
					System.out.println((int) r.charAt(j));
					break;
				}

				switch (status) {
				case 0:// �ո�
					j++;
					break;

				case 1:// ��ĸ�����ʣ�
					jj = j;
					if (j != r.length()) {
						t = (int) r.charAt(j);
						while (((t >= 65 && t <= 90) || (t >= 97 && t <= 122) || (t >= 48 && t <= 57))
								&& j < r.length() - 1) {
							j++;
							t = (int) r.charAt(j);
							if (j == r.length() - 1) {
								if ((t >= 65 && t <= 90) || (t >= 97 && t <= 122) || (t >= 48 && t <= 57)) {
									j = j + 1;
								}
							}
						}
						// ���while����˼��������滹����ĸ�����֣�������ʾ�û�н�����Ҫһֱj++�ҵ����ʵĽ�β
					}
					if (jj == j)
						j++;
					word = r.substring(jj, j);
					int key = 0;
					int m;
					for (m = 0; m < k.length; m++) {
						if (word.equals(k[m])) {
							key = 1;
							break;
						}
					}
					if (key == 1) {
						result = result.concat(" {k,").concat(String.valueOf(m)).concat("}");
					} else {
						if (i == null) {
							i.add(word);
							result = result.concat(" {i,0}");
						} else {
							if (i.contains(word)) {
								result = result.concat(" {i,").concat(String.valueOf(i.indexOf(word))).concat("}");
							} else {
								i.add(word);
								result = result.concat(" {i,").concat(String.valueOf(i.indexOf(word))).concat("}");
							}
						}
					}
					// System.out.println(word);
					// System.out.println(result);
					break;

				case 2:// ����
					jj = j;
					if (j != r.length()) {
						t = (int) r.charAt(j);
						while (((t >= 48 && t <= 57) || t == 46) && j < r.length() - 1) {
							j++;
							t = (int) r.charAt(j);
							if (j == r.length() - 1) {
								if (t >= 48 && t <= 57) {
									j = j + 1;
								}
							}
						}
					}
					if (jj == j)
						j++;
					word = r.substring(jj, j);
					if (c == null) {
						c.add(Double.valueOf(word));
						result = result.concat(" {c,0}");
					} else {
						if (c.contains(Double.valueOf(word))) {
							result = result.concat(" {c,").concat(String.valueOf(c.indexOf(Double.valueOf(word))))
									.concat("}");
						} else {
							c.add(Double.valueOf(word));
							result = result.concat(" {c,").concat(String.valueOf(c.indexOf(Double.valueOf(word))))
									.concat("}");
						}
					}
					// System.out.println(word);
					// System.out.println(result);
					break;

				case 3:// �ַ���
					jj = j;
					j++;
					while ((int) r.charAt(j) != 34 && j < r.length() - 1)
						j++;
					word = r.substring(jj, j + 1);
					if ((int) word.charAt(word.length() - 1) != 34)
						System.out.println("˫���Ų��ɶԴ���");
					if (C == null) {
						C.add(word);
						result = result.concat(" {C,0}");
					} else {
						if (C.contains(word)) {
							result = result.concat(" {C,").concat(String.valueOf(S.indexOf(word))).concat("}");
						} else {
							S.add(word);
							result = result.concat(" {S,").concat(String.valueOf(S.indexOf(word))).concat("}");
						}
					}
					// System.out.println(word);
					// System.out.println(result);
					j++;
					break;

				case 4:// �ַ�
					jj = j;
					j++;
					while ((int) r.charAt(j) != 39 && j < r.length() - 1)
						j++;
					word = r.substring(jj, j + 1);
					if ((int) word.charAt(word.length() - 1) != 39)
						System.out.println("�����Ų��ɶԴ���");
					if (C == null) {
						C.add(word);
						result = result.concat(" {C,0}");
					} else {
						if (C.contains(word)) {
							result = result.concat(" {C,").concat(String.valueOf(C.indexOf(word))).concat("}");
						} else {
							C.add(word);
							result = result.concat(" {C,").concat(String.valueOf(C.indexOf(word))).concat("}");
						}
					}
					// System.out.println(word);
					// System.out.println(result);
					j++;
					break;

				case 5:// ����
					word = r.substring(j, j + 1);
					if (j + 1 < r.length()) {
						t = (int) r.charAt(j + 1);
						if ((t >= 33 && t <= 47 && t != 34 && t != 39) || (t >= 58 && t <= 64) || (t >= 91 && t <= 96)
								|| (t >= 123 && t <= 126)) {
							if ((r.charAt(j) == '&' && r.charAt(j + 1) == '&')
									|| (r.charAt(j) == '|' && r.charAt(j + 1) == '|')
									|| (r.charAt(j) == '>' && r.charAt(j + 1) == '=')
									|| (r.charAt(j) == '<' && r.charAt(j + 1) == '=')
									|| (r.charAt(j) == '=' && r.charAt(j + 1) == '=')
									|| (r.charAt(j) == '!' && r.charAt(j + 1) == '=')) {
								word = r.substring(j, j + 2);
								j++;
							}
						}
					}
					if (p == null) {
						p.add(word);
						result = result.concat(" {p,0}");
					} else {
						if (p.contains(word)) {
							result = result.concat(" {p,").concat(String.valueOf(p.indexOf(word))).concat("}");
						} else {
							p.add(word);
							result = result.concat(" {p,").concat(String.valueOf(p.indexOf(word))).concat("}");
						}
					}
					// System.out.println(word);
					// System.out.println(result);
					j++;
					break;
				}
			}
			List<String> strc = new ArrayList<String>();
			for (Double aa : c) {
				strc.add(aa.toString());
			}
			fr.add(i);
			fr.add(C);
			fr.add(S);
			fr.add(strc);
			fr.add(Arrays.asList(k));
			fr.add(p);
			String[] step = result.substring(1).split(" ");
			fr.add(Arrays.asList(step));
			String show_i, show_C, show_S, show_c, show_k, show_p;
			show_i = "i:".concat(i.toString());
			show_C = "C:".concat(C.toString());
			show_S = "S:".concat(S.toString());
			show_c = "c:".concat(c.toString());
			show_k = "k:[main, void, if, else, while, for, int, char, string, break, continue, return, switch, case, default]";
			show_p = "p:".concat(p.toString());
			File writename = new File(path_out); // ���û�����½�һ���µ�path_out��txt�ļ�
			writename.createNewFile(); // �������ļ�
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			result = result.concat("\n").concat(show_i).concat("\n").concat(show_C).concat("\n").concat(show_S)
					.concat("\n").concat(show_c).concat("\n").concat(show_k).concat("\n").concat(show_p);
			out.write(result); // д��
			out.flush(); // �ѻ���������ѹ���ļ�
			out.close(); // ���ǵùر��ļ�
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fr;
	}
}