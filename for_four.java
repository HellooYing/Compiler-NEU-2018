import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class for_four {
    public static String[] step, i, C, S, c, k, p;// 输入
    public static List<String[]> qt = new ArrayList<String[]>();// 存四元式
    public static int n = 0, now = 0, startn, startnt; // n:临时变量tn序号，now：token串序号，startn：暂存step_on开始token串位置,startnt:暂存尾处理区开始token串位置
    public static String LROT, RROT, P; // 保存代表左式的临时变量，保存代表右式的临时变量，保存比较的符号

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
        List<String[]> r1 = new for_four().answer(step, i, C, S, c, k, p, tb);
    }

    public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1,
            String[] p1, table tb) {
        step = step1;// token序列
        i = i1;// 变量
        C = C1;// 字符
        S = S1;// 字符串
        c = c1;// 数字常量
        k = k1;// 关键字
        p = p1;// 符号

        String t, tTail = "i"; // t：Token的值 , tTail：尾处理区 tTail++,初始化为“i”
        String[] step_son; // 各阶段的子token序列
        List<String[]> qtt; // 各阶段生成的四元式
        String tn; // 临时变量
        int braceNum = 1; // "{"个数，用来统计for{}的结束

        switch (step[now].substring(1, 2)) {
        case "k":
            t = k[Integer.parseInt(step[now].substring(3, step[now].length() - 1))];
            if (t.equals("for")) {
                addqt("for", "_", "_", "_");// 生成for四元式
                startn = now + 2;// 暂存初始化区step_on首Token串的序号
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
                if (step_son.length == 1)// 判断初始化区有一个标识符,不产生四元式
                {
                } else// 左式多于一个标示符
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n); // 获得当前临时变量tn的n值
                    for (int j = 0; j < qtt.size(); j++)// 将初始化区四元式序列送入四元式区
                    {
                        qt.add(qtt.get(j));
                    }
                }

                startn = now + 1;// 暂存比较区左式step_on首Token串的序号
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
                        P = t;// 暂存比较符号
                        break;
                    }
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                if (step_son.length == 1)// 判断比较区左式有一个标识符
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
                } else// 左式多于一个标示符
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n); // 获得当前临时变量tn的n值
                    LROT = qtt.get(qtt.size() - 1)[3];// 暂存比较的左式的临时变量
                    for (int j = 0; j < qtt.size(); j++)// 将比较的左式四元式序列送入四元式区
                    {
                        qt.add(qtt.get(j));
                    }
                }

                startn = now + 1;// 暂存比较区右式step_on首Token串的序号
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
                if (step_son.length == 1)// 判断比较区右式有一个标识符
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
                } else// 比较区右式多于一个标示符
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n) + 1;// 获得当前临时变量tn的n值
                    RROT = qtt.get(qtt.size() - 1)[3];// 暂存比较区的右式的临时变量
                    for (int j = 0; j < qtt.size(); j++)// 将比较区的右式四元式序列送入四元式区
                    {
                        qt.add(qtt.get(j));
                    }
                }
                tn = "t".concat(String.valueOf(n));
                addqt(P, LROT, RROT, tn); // 生成比较四元式
                addqt("df", tn, "_", "_");// 生成判断四元式

                startnt = now + 1;// 暂存尾处理区开始token串位置
                while (true)// 跳过尾处理区，在for主程序之后生成四元式
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

                startn = now + 1;// 暂存for{}内程序开始位置
                t = "zhaozhiyi";// 强制给t赋予某个值，使其不为“{”，防止下面判断for{}内首个token发生错误
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
                        t = "zhaozhiyi";// 强制给t赋予某个值，使其不为“{”，防止下面有连续的"{"时判断发生错误
                    }
                    if (t.equals("}")) {
                        t = "zhaozhiyi";// 强制给t赋予某个值，使其不为“}”，防止下面有连续的"}"时判断发生错误
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

                startn = startnt;// 保存尾处理区step_on首Token串的序号
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
                if (step_son.length == 3)// 三个token串，有可能是i++/i--情况
                {
                    switch (step[startnt - 4].substring(1, 2))// 保存“i”
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
                        if (t.equals("+")) // i++情况
                        {
                            tn = "t".concat(String.valueOf(n+1));// 生成临时变量
                            addqt("+", tTail, "1", tn);
                            addqt("=", tn, "_", tTail);
                        } else if (t.equals("-")) // i--情况
                        {
                            tn = "t".concat(String.valueOf(n+1));// 生成临时变量
                            addqt("-", tTail, "1", tn);
                            addqt("=", tn, "_", tTail);
                        } else // 普通算术表达式
                        {
                            qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                            n = reset_t(qtt, n); // 获得当前临时变量tn的n值
                            for (int j = 0; j < qtt.size(); j++)// 将初始化区四元式序列送入四元式区
                            {
                                qt.add(qtt.get(j));
                            }
                        }
                        break;

                    default:// 最后一个token不是“+”/“-”，不可能是i++/i--情况
                        qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                        n = reset_t(qtt, n); // 获得当前临时变量tn的n值
                        for (int j = 0; j < qtt.size(); j++)// 将初始化区四元式序列送入四元式区
                        {
                            qt.add(qtt.get(j));
                        }
                        break;
                    }
                } else// 不是三个token串，不可能是i++/i--情况
                {
                    qtt = new exp_four().answer(step_son, i, C, S, c, k, p, tb);
                    n = reset_t(qtt, n); // 获得当前临时变量tn的n值
                    for (int j = 0; j < qtt.size(); j++)// 将初始化区四元式序列送入四元式区
                    {
                        qt.add(qtt.get(j));
                    }
                }

                addqt("fe", "_", "_", "_");// 跳转四元式
            }
        default:
            break;
        }
        // 这一段代码是将你生成的四元式写入文件，给老师检查方便
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
            File writename = new File("./z.四元式.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 写入完毕

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

    static int reset_t(List<String[]> qtt, int n) // 避免临时变量的冲突，在每次往qt加qtt的时候都要重置所有t后面的值
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
