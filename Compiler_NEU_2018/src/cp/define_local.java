package cp;
import java.io.*;
import java.util.*;

public class define_local {
	// �����������ã���ȫ�ֱ���������£�����int a���ֶ���������token���к͵�ǰ�����ܱ�
	// ����޸ĺ�ķ��ű�
	// Ȼ��������Ŀ������ʱ���Ȳ�ѯһ�������ڲ����ܱ���ھ���ȫ�ֱ����Ĳ�����ֱ�������ݶζ�д����
	// ���ھ�����ʱ�����Ĳ������ŵ���ջ�Σ��û��¼����ջ��һһ��Ӧ��ÿ��Ӧ�ö���ջ�ε���������ʱ������ֵ��
	// ��ˣ���define_local����int a,��ֻ���в鿴a�Ƿ����ܱ�����ֹ����ض����⣩��Ȼ���ڶ�Ӧ�������

	// List<String[]> synbl;�ܱ������������ͣ�int int[] function char)�������ݶ��е�ƫ�Ƶ�ַ��������Ϣ
	// ��������Ϊ��������ָ�������е�ĳһ����������Ϊ���飬�������鳤�ȣ�
	// �����ܱ��е�String[]��length=4��synbl.size()=���������ִ���

	// List<List<String>> pfinfl;
	// ������������0���β�����1���β��������β�����һһ��Ӧ��2����������(��["int"])3����ʱ������[a,b,c]��4����ʱ�����ڶ�ջ�ε�ƫ�Ƶ�ַ5
	// �����ʱ������һ�����飬������һ����ʱ������ƫ�Ƶ�ַ��Ҫ�����Ǹ�����ĳ���

	// List<String> vall;

	// ��ǰ�ܽ��յ����ֻ��int a;/int a,b,c;
	public static void main(String[] args) throws Exception {
		String path_in = "./z.c���Դ�������.txt";
		String path_out = "./z.token����.txt";
		List<List<String>> anal = new analyzer().answer(path_in,path_out);
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
		new define_local().answer(step, i, C, S, c, k, p, tb);
	}

	boolean answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1,
			table tb) {
		String[] step, i, C, S, c, k, p;
		step = step1;// token����
		i = i1;// ����
		C = C1;// �ַ�
		S = S1;// �ַ���
		c = c1;// ���ֳ���
		k = k1;// �ؼ���
		p = p1;// ����
		table.func func = tb.new func();
		String fnm = getf(tb);
		List<table.vari> vt = new ArrayList<table.vari>();// �����������ʱ������
		
		for (int j = 0; j < tb.pfinfl.size(); j++) {
			if (tb.pfinfl.get(j).name.equals(fnm)) {// ���ű�ĺ�����ĺ�������fnm��ͬ���Ǹ�func
				// �ҵ��˱��ζ������ڵ��Ӻ���
				func = tb.pfinfl.get(j);
				vt = tb.pfinfl.get(j).vt;
			}
		}

		// ���ڽ����ľ��ӣ����ж��Ƿ��ж��ţ����б��Ǽ�������
		String tp = k[Integer.parseInt(step[0].substring(3, step[0].length() - 1))];// ����������һ������������int
		List<String> name = new ArrayList<String>();
		List<Integer> other = new ArrayList<Integer>();
		if (tp.equals("int") || tp.equals("char")) {// ��int����char�Ķ��壬other��"_"
			for (int j = 1; j < step.length; j++) {
				// �Ҷ����жϼ�������������������Ż�ֺţ�������ڶ��Ż�ֺ�ǰһ��
				if (step[j].substring(1, 2).equals("p")) {
					if (p[Integer.parseInt(step[j].substring(3, step[j].length() - 1))].equals(",")
							|| p[Integer.parseInt(step[j].substring(3, step[j].length() - 1))].equals(";")) {
						name.add(i[Integer.parseInt(step[j - 1].substring(3, step[j - 1].length() - 1))]);
						other.add(-1);
					}
				}
			}
		}
		// ����������֧��ʱд����

		// �����õ�����α�����������Ҫ�½�һ��vari���ͣ������汾��������Ϣ������������Ϣ����һ��ƫ�Ƶ�ַ��

		for (int j = 0; j < name.size(); j++) {// ������ζ����ÿ������
			table.vari thisv = tb.new vari();// �½�һ��vari
			thisv.name = name.get(j);
			for(int jj=0;jj<vt.size();jj++){
				if(vt.get(jj).name.equals(thisv.name)){
					System.out.println("�ֲ������ض��壡�ñ���Ϊ��"+thisv.name+". ������Ϊ��"+fnm);
					return false;
				}
			}
			thisv.tp = tp;
			thisv.other = other.get(j);
			thisv.ofad = getofad(vt);
			vt.add(thisv);
		}

		func.vt = vt;
		for (int j = 0; j < tb.pfinfl.size(); j++) {
			if (tb.pfinfl.get(j).name.equals(fnm)) {// ���ű�ĺ�����ĺ�������fnm��ͬ���Ǹ�func
				// �ҵ��˱��ζ������ڵ��Ӻ���
				tb.pfinfl.set(j, func);
			}
		}
		// result��txt�д�ŷ�ʽ���ȴ�ӡ�ܱ��ܱ��е�ÿ��variһ��
		// �ٴ�ӡ��������������ǰ����Ԫ��һ�У�vt��n��
		//tb.print(tb);
		writedl(tb);
		return true;
	}

	static void writedl(table tb) {
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
			File writename = new File("./z.���ű�.txt");
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
	static void wtdl(table tb) {
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
			File writename = new File("../z.���ű�.txt");
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
		// ��ƫ�Ƶ�ַ�����vt.size()Ϊ0����ƫ�Ƶ�ַΪ0��
		// ���vt.size()��Ϊ0����ȥ������һ��vari�������Ƿ���int[]��
		// �����int[]����ôƫ�Ƶ�ַΪ��һ��vari��other+ofad
		// ��������������int����char����ôƫ�Ƶ�ַΪofad+1
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
	static String getf(table tb){
        List<String> fnml=new ArrayList<String>();
        for(int i=0;i<tb.pfinfl.size();i++){
            fnml.add(tb.pfinfl.get(i).name);
        }
        for(int i=tb.vall.size()-1;i>=0;i--){
            if(fnml.contains(tb.vall.get(i))) return tb.vall.get(i);
        }
        return "main";
    }
}