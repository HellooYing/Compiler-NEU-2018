package cp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class for_four {
    public static String[] step, i, C, S, c, k, p;// ����
    public static List<String[]> qt = new ArrayList<String[]>();// ����Ԫʽ
    public static int n = 0, now = 0, startn, startnt; // n:��ʱ����tn��ţ�now��token����ţ�startn���ݴ�step_on��ʼtoken��λ��,startnt:�ݴ�β��������ʼtoken��λ��
    public static String LROT, RROT, P; // ���������ʽ����ʱ���������������ʽ����ʱ����������Ƚϵķ���

    public static void main(String[] args) throws Exception {
        String path_in = "./z.c���Դ�������.txt";
        String path_out = "./z.token����.txt";

        // ��һ�δ�"z.token����.txt"�ļ���ȡ����Ҫ�õ������룬��token����(�Ұ���ȡ��Ϊp)��i, C, S, c, k, p��
        List<List<String>> anal = new analyzer().answer(path_in,path_out);
        String[] step, i, C, S, c, k, p;
        int n = 0;

        i = (String[]) anal.get(0).toArray(new String[anal.get(0).size()]);
        C = (String[]) anal.get(1).toArray(new String[anal.get(1).size()]);
        S = (String[]) anal.get(2).toArray(new String[anal.get(2).size()]);
        c = (String[]) anal.get(3).toArray(new String[anal.get(3).size()]);
        k = (String[]) anal.get(4).toArray(new String[anal.get(4).size()]);
        p = (String[]) anal.get(5).toArray(new String[anal.get(5).size()]);
        step = (String[]) anal.get(6).toArray(new String[anal.get(6).size()]);
        // ��ȡ���

        // ִ��for�﷨��������
        table tb = new table();
        init(tb);
        List<String[]> r1 = new for_four().answer(step, i, C, S, c, k, p, tb);
    }

    public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1,
            String[] p1, table tb) {
        step = step1;// token����
        i = i1;// ����
        C = C1;// �ַ�
        S = S1;// �ַ���
        c = c1;// ���ֳ���
        k = k1;// �ؼ���
        p = p1;// ����

        String t, tTail = "i"; // t��Token��ֵ , tTail��β������ tTail++,��ʼ��Ϊ��i��
        String[] step_son; // ���׶ε���token����
        List<String[]> qtt; // ���׶����ɵ���Ԫʽ
        String tn; // ��ʱ����
        int braceNum = 1; // "{"����������ͳ��for{}�Ľ���

        switch (step[now].substring(1, 2)) {
        case "k":
            t = k[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            if (t.equals("for")) {
                
                startn = now + 2;// �ݴ��ʼ����step_on��Token�������
                while (true) {
                    now++;
                    switch (step[now].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    if (t.equals(";"))
                        break;
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                if (step_son.length == 1)// �жϳ�ʼ������һ����ʶ��,��������Ԫʽ
                {
                } else// ��ʽ����һ����ʾ��
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n); // ��õ�ǰ��ʱ����tn��nֵ
                    for (int j = 0; j < qtt.size(); j++)// ����ʼ������Ԫʽ����������Ԫʽ��
                    {
                        qt.add(qtt.get(j));
                    }
                }
                addqt("for", "_", "_", "_");// ����for��Ԫʽ
                startn = now + 1;// �ݴ�Ƚ�����ʽstep_on��Token�������
                while (true) {
                    now++;
                    switch (step[now].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    if (t.equals(">=") || t.equals("<=") || t.equals("==") || t.equals("!=") || t.equals("<")
                            || t.equals(">")) {
                        P = t;// �ݴ�ȽϷ���
                        break;
                    }
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                if (step_son.length == 1)// �жϱȽ�����ʽ��һ����ʶ��
                {
                    switch (step[now - 1].substring(1, 2)) {
                    case "i":
                        LROT = i[Integer.parseInt(step[now - 1].substring(3, step[now - 1].length() - 1))];
                        break;
                    case "c":
                        LROT = c[Integer.parseInt(step[now - 1].substring(3, step[now - 1].length() - 1))];
                        break;
                    default:
                        break;
                    }
                } else// ��ʽ����һ����ʾ��
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n); // ��õ�ǰ��ʱ����tn��nֵ
                    LROT = qtt.get(qtt.size() - 1)[3];// �ݴ�Ƚϵ���ʽ����ʱ����
                    for (int j = 0; j < qtt.size(); j++)// ���Ƚϵ���ʽ��Ԫʽ����������Ԫʽ��
                    {
                        qt.add(qtt.get(j));
                    }
                }

                startn = now + 1;// �ݴ�Ƚ�����ʽstep_on��Token�������
                while (true) {
                    now++;
                    switch (step[now].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    if (t.equals(";"))
                        break;
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                if (step_son.length == 1)// �жϱȽ�����ʽ��һ����ʶ��
                {
                    n=n+1;
                    switch (step[now - 1].substring(1, 2)) {
                    case "i":
                        RROT = i[Integer.parseInt(step[now - 1].substring(3, step[now - 1].length() - 1))];
                        break;
                    case "c":
                        RROT = c[Integer.parseInt(step[now - 1].substring(3, step[now - 1].length() - 1))];
                        break;
                    default:
                        break;
                    }
                } else// �Ƚ�����ʽ����һ����ʾ��
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n) + 1;// ��õ�ǰ��ʱ����tn��nֵ
                    RROT = qtt.get(qtt.size() - 1)[3];// �ݴ�Ƚ�������ʽ����ʱ����
                    for (int j = 0; j < qtt.size(); j++)// ���Ƚ�������ʽ��Ԫʽ����������Ԫʽ��
                    {
                        qt.add(qtt.get(j));
                    }
                }
                tn = "t".concat(String.valueOf(n));
                addqt(P, LROT, RROT, tn); // ���ɱȽ���Ԫʽ
                addqt("df", tn, "_", "_");// �����ж���Ԫʽ

                startnt = now + 1;// �ݴ�β��������ʼtoken��λ��
                while (true)// ����β����������for������֮��������Ԫʽ
                {
                    now++;
                    switch (step[now].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    if (t.equals("{"))
                        break;
                }

                startn = now + 1;// �ݴ�for{}�ڳ���ʼλ��
                t = "zhaozhiyi";// ǿ�Ƹ�t����ĳ��ֵ��ʹ�䲻Ϊ��{������ֹ�����ж�for{}���׸�token��������
                while (true) {
                    now++;
                    switch (step[now].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    if (t.equals("{")) {
                        braceNum++;
                        t = "zhaozhiyi";// ǿ�Ƹ�t����ĳ��ֵ��ʹ�䲻Ϊ��{������ֹ������������"{"ʱ�жϷ�������
                    }
                    if (t.equals("}")) {
                        t = "zhaozhiyi";// ǿ�Ƹ�t����ĳ��ֵ��ʹ�䲻Ϊ��}������ֹ������������"}"ʱ�жϷ�������
                        braceNum--;
                        if (braceNum == 0)
                            break;
                    }
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                qtt = new block().answer(step_son, i, C, S, c, k, p, tb);
                n=reset_t(qtt, n);
                for (int j = 0; j < qtt.size(); j++) {
                    qt.add(qtt.get(j));
                }

                startn = startnt;// ����β������step_on��Token�������
                while (true) {
                    startnt++;
                    switch (step[startnt].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[startnt].substring(3, step[startnt].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    if (t.equals("{"))
                        break;
                }
                step_son = Arrays.copyOfRange(step, startn, startnt - 1);
                if (step_son.length == 3)// ����token�����п�����i++/i--���
                {
                    switch (step[startnt - 4].substring(1, 2))// ���桰i��
                    {
                    case "i":
                        tTail = i[Integer.parseInt(step[startnt - 4].substring(3, step[startnt - 4].length() - 1))];
                        break;
                    case "c":
                        tTail = c[Integer.parseInt(step[startnt - 4].substring(3, step[startnt - 4].length() - 1))];
                        break;
                    default:
                        break;
                    }
                    
                    switch (step[startnt - 2].substring(1, 2)) {
                    case "p":
                        t = p[Integer.parseInt(step[startnt - 2].substring(3, step[startnt - 2].length() - 1))];
                        if (t.equals("+")) // i++���
                        {
                            tn = "t".concat(String.valueOf(n+1));// ������ʱ����
                            addqt("+", tTail, "1.0", tn);
                            addqt("=", tn, "_", tTail);
                        } else if (t.equals("-")) // i--���
                        {
                            tn = "t".concat(String.valueOf(n+1));// ������ʱ����
                            addqt("-", tTail, "1.0", tn);
                            addqt("=", tn, "_", tTail);
                        } else // ��ͨ�������ʽ
                        {
                            qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                            n = reset_t(qtt, n); // ��õ�ǰ��ʱ����tn��nֵ
                            for (int j = 0; j < qtt.size(); j++)// ����ʼ������Ԫʽ����������Ԫʽ��
                            {
                                qt.add(qtt.get(j));
                            }
                        }
                        break;

                    default:// ���һ��token���ǡ�+��/��-������������i++/i--���
                        qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                        n = reset_t(qtt, n); // ��õ�ǰ��ʱ����tn��nֵ
                        for (int j = 0; j < qtt.size(); j++)// ����ʼ������Ԫʽ����������Ԫʽ��
                        {
                            qt.add(qtt.get(j));
                        }
                        break;
                    }
                } else// ��������token������������i++/i--���
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n); // ��õ�ǰ��ʱ����tn��nֵ
                    for (int j = 0; j < qtt.size(); j++)// ����ʼ������Ԫʽ����������Ԫʽ��
                    {
                        qt.add(qtt.get(j));
                    }
                }

                addqt("fe", "_", "_", "_");// ��ת��Ԫʽ
            }
        default:
            break;
        }
        // ��һ�δ����ǽ������ɵ���Ԫʽд���ļ�������ʦ��鷽��
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
        // д�����

        return qt;
    }

    static void addqt(String a1, String a2, String a3, String a4) {
        String[] in = new String[4];
        in[0] = a1;
        in[1] = a2;
        in[2] = a3;
        in[3] = a4;
        qt.add(in);
    }

    static void printqt() {
        for (String[] a : qt) {
            for (String aa : a) {
                System.out.print(aa);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    static int reset_t(List<String[]> qtt, int n) // ������ʱ�����ĳ�ͻ����ÿ����qt��qtt��ʱ��Ҫ��������t�����ֵ
    {
        String[] inqtt;
        int num, max = 0;
        for (int j = 0; j < qtt.size(); j++) {
            inqtt = qtt.get(j);
            for (int jj = 1; jj < 4; jj++) {
                if (inqtt[jj].length() >= 2) {
                    if (inqtt[jj].substring(0, 1).equals("t") && is_c(inqtt[jj].substring(1))) {
                        num = Double.valueOf(inqtt[jj].substring(1)).intValue();
                        if (max < num)
                            max = num;
                        inqtt[jj] = "t".concat(Integer.toString(num + n));
                    }
                }
            }
        }
        return max + n;
    }

    static boolean is_c(String c) {
        try {
            Double.valueOf(c);
        } catch (Exception e) {
            return false;
        }
        return true;
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
