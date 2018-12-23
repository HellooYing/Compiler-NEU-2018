package cp;
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
    public static Stack<String> st1 = new Stack<String>();// Ԫ��ջ
    public static Stack<String> st2 = new Stack<String>();// ����ջ
    public static List<String[]> qt = new ArrayList<String[]>();// ����Ԫʽ
    public static String f;

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
        List<String[]> r1 = new exp_four().answer(step, i, C, S, c, k, p, tb);
    }

    public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1,
            String[] p1, table tb) {
        i = i1;
        C = C1;
        S = S1;
        c = c1;
        k = k1;
        p = p1;
        qt.clear();
        st1.clear();
        st2.clear();
        now = 0;
        flag = 1;
        num = 1;
        brackets = 0;// �����һ�����µ�ֵ
        String finalout = "";
        if (step1.length > 4 && step1[1].substring(1, 2).equals("p")
                && p[Integer.parseInt(step1[1].substring(3, step1[1].length() - 1))].equals("=")) {
            finalout = i[Integer.parseInt(step1[0].substring(3, step1[0].length() - 1))];
            step = Arrays.copyOfRange(step1, 2, step1.length);
        } else if (step1[1].substring(1, 2).equals("p")
                && p[Integer.parseInt(step1[1].substring(3, step1[1].length() - 1))].equals("=") && step1.length <= 4) {// ��ֵ���
            inqt = new String[4];
            inqt[0] = "=";
            switch (step1[2].substring(1, 2)) {
            case "i":
                inqt[1] = i[Integer.parseInt(step1[2].substring(3, step1[2].length() - 1))];
                break;
            case "c":
                inqt[1] = c[Integer.parseInt(step1[2].substring(3, step1[2].length() - 1))];
                break;
            default:
                System.out.println("������ʽ��Ч");
            }
            inqt[2] = "_";
            inqt[3] = i[Integer.parseInt(step1[0].substring(3, step1[0].length() - 1))];
            qt.add(inqt);
            return qt;
        } else
            step = step1;
        // ����ջ��Ԫ��ջ������Ԫ�ؾͼӽ�Ԫ��ջ���������žͼӽ�����ջ
        // �������ŵ�ʱ��Ҫ������ջ��ջ��Ԫ�رȽϣ������ǣ�
        // ��+- ջ��+-*/����ջ  �ջ� ��ջ
        // ��*/ ջ��+-���� ��ջ   * 
        // ���� ��ջ����
        // ��ջ����˼�ǣ�inqt[0]=st2.pop();inqt[2]=st1.pop();inqt[1]=st1.pop();inqt[3]="t".concat(String.valueOf(num));num++;st1.add(inqt[3]);
        // ��ջ����˼�ǣ�st2.add(����)
        // ����Ԫ��ȫֱ����ջ
        E();
        if (flag == 0)
            System.out.println("������ʽ��Ч");
        else {
            while (!st2.empty()&&st1.size()!=1) {
                inqt = new String[4];
                inqt[0] = st2.pop();
                inqt[2] = st1.pop();
                inqt[1] = st1.pop();
                inqt[3] = "t".concat(String.valueOf(num));
                num++;
                st1.add(inqt[3]);
                qt.add(inqt);
            }
            inqt = qt.get(qt.size() - 1);
            if (!finalout.equals(""))
                inqt[3] = finalout;

            String result = "";
            for (int aa = 0; aa < qt.size(); aa++) {
                result = result.concat("[");
                for (int aaa = 0; aaa < 4; aaa++) {
                    result = result.concat(qt.get(aa)[aaa]);
                    if (aaa != 3)
                        result = result.concat(",");
                }
                result = result.concat("] ");
            }
            try {
                File writename = new File("./z.��Ԫʽ.txt");
                writename.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(writename));
                out.write(result);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return qt;
    }

    public static String getTraceInfo() { // һ����������кŵĺ��������������Һ�ʱreturnҲ���Ǻ�ʱ��Ϊ���ʽ�����˻�����Ϊ���ʽ������
        StringBuffer sb = new StringBuffer();

        StackTraceElement[] stacks = new Throwable().getStackTrace();
        int stacksLen = stacks.length;
        sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName())
                .append("; number: ").append(stacks[1].getLineNumber());
        return sb.toString();
    }

    private void E() {
        if (flag == 0) {
            System.out.println(getTraceInfo());
            return;
        }
        T();
        E1();
    }

    private void T() {
        if (flag == 0) {
            System.out.println(getTraceInfo());
            return;
        }
        F();
        T1();
    }

    private void E1() {
        if (flag == 0) {
            System.out.println(getTraceInfo());
            return;
        }
        String t;
        switch (step[now].substring(1, 2)) {
        case "i":
            return;
        case "c":
            return;
        case "p":
            t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            if (t.equals("+") || t.equals("-")) {
                if (st2.empty() || st2.peek().equals("(")) {
                    st2.add(t);
                } else{
                    while(!st2.empty()&&st1.size()!=1&&(st2.peek().equals("+") || st2.peek().equals("-") || st2.peek().equals("*")
                        || st2.peek().equals("/"))) {
                        inqt = new String[4];
                        inqt[0] = st2.pop();
                        inqt[2] = st1.pop();
                        inqt[1] = st1.pop();
                        inqt[3] = "t".concat(String.valueOf(num));
                        num++;
                        st1.add(inqt[3]);
                        qt.add(inqt);
                    }
                    st2.add(t);
                }
                // System.out.println(t);
                if (now == step.length - 1) {
                    flag = 0;
                    System.out.println(getTraceInfo());
                    return;
                }
                ;
                now++;// +��-ֻ��T1����������ĵط��������Ǵ��
                T();
                E1();
                break;
            } else if (t.equals("*") || t.equals("/") || t.equals("(") || t.equals(")") || t.equals(";"))
                return;
            else {
                flag = 0;
                System.out.println(getTraceInfo());
                return;
            }
        default:
            flag = 0;
            System.out.println(getTraceInfo());
            return;
        }
    }

    private void T1() {
        if (flag == 0) {
            System.out.println(getTraceInfo());
            return;
        }
        String t;
        switch (step[now].substring(1, 2)) {
        case "i":
            return;
        case "c":
            return;
        case "p":
            t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            if (t.equals("*") || t.equals("/")) {
                if (st2.empty() || st2.peek().equals("+") || st2.peek().equals("-") || st2.peek().equals("(")) {
                    st2.add(t);
                } else {
                    while(!st2.empty()&&st1.size()!=1&&(st2.peek().equals("*") || st2.peek().equals("/"))) {
                        inqt = new String[4];
                        inqt[0] = st2.pop();
                        inqt[2] = st1.pop();
                        inqt[1] = st1.pop();
                        inqt[3] = "t".concat(String.valueOf(num));
                        num++;
                        st1.add(inqt[3]);
                        qt.add(inqt);
                    }
                    st2.add(t);
                }
                // System.out.println(t);
                if (now == step.length - 1) {
                    flag = 0;
                    System.out.println(getTraceInfo());
                    return;
                }
                ;
                now++;// *��/ֻ��T1����������ĵط��������Ǵ��
                F();
                T1();
                return;
            } else if (t.equals("+") || t.equals("-") || t.equals("(") || t.equals(")") || t.equals(";"))
                return;
            else {
                flag = 0;
                System.out.println(getTraceInfo());
                return;
            }
        default:
            flag = 0;
            System.out.println(getTraceInfo());
            return;
        }
    }

    private void F() {
        if (flag == 0) {
            System.out.println(getTraceInfo());
            return;
        }
        String t;
        switch (step[now].substring(1, 2)) {
        case "i":
            t = i[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            // System.out.println(t);
            st1.add(t);
            if (now == step.length - 1)
                return;
            now++;
            if (step[now].substring(1, 2).equals("p")) {
                t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                while (t.equals(")")) {
                    brackets--;
                    // System.out.println(t);
                    if (brackets < 0) {
                        flag = 0;
                        System.out.println(getTraceInfo());
                        return;
                    }
                    while (!st2.peek().equals("(")) {
                        inqt = new String[4];
                        inqt[0] = st2.pop();
                        inqt[2] = st1.pop();
                        inqt[1] = st1.pop();
                        inqt[3] = "t".concat(String.valueOf(num));
                        num++;
                        st1.add(inqt[3]);
                        qt.add(inqt);
                    }
                    st2.pop();
                    if (now == step.length - 1) {
                        return;
                    }
                    now++;
                    if (step[now].substring(1, 2).equals("p")) {
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                    }
                }
            }
            break;
        case "c":
            t = c[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            // System.out.println(t);
            st1.add(t);
            if (now == step.length - 1) {
                return;
            }
            now++;
            if (step[now].substring(1, 2).equals("p")) {
                t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                while (t.equals(")")) {
                    brackets--;
                    // System.out.println(t);
                    if (brackets < 0) {
                        flag = 0;
                        System.out.println(getTraceInfo());
                        return;
                    }
                    while (!st2.peek().equals("(")) {
                        inqt = new String[4];
                        inqt[0] = st2.pop();
                        inqt[2] = st1.pop();
                        inqt[1] = st1.pop();
                        inqt[3] = "t".concat(String.valueOf(num));
                        num++;
                        st1.add(inqt[3]);
                        qt.add(inqt);
                    }
                    st2.pop();
                    if (now == step.length - 1) {
                        return;
                    }
                    now++;
                    if (step[now].substring(1, 2).equals("p")) {
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                    }
                }
            }
            break;
        case "p":
            t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            if (t.equals("(")) {
                // System.out.println(t);
                if (now == step.length - 1) {
                    flag = 0;
                    System.out.println(getTraceInfo());
                    return;
                } else if (step[now + 1].substring(1, 2).equals("p")
                        && p[Integer.parseInt(step[now + 1].substring(3, step[now + 1].length() - 1))].equals(")")) {
                    flag = 0;
                    System.out.println(getTraceInfo());
                    return;
                }
                now++;
                brackets++;
                st2.add(t);
                E();
                break;
            } else {
                flag = 0;
                System.out.println(getTraceInfo());
                return;
            }
        default:
            flag = 0;
            System.out.println(getTraceInfo());
            return;
        }
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
    void printLS(List<String[]> r) {
		for (String[] a : r) {
			for (String aa : a) {
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}