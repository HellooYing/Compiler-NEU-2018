import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class four {
    public static int now = 0, flag = 1, num = 1, brackets = 0;
    public static String[] step, i, C, S, c, k, p, inqt;
    public static Stack<String> st = new Stack<String>();
    public static Stack<String> symbol = new Stack<String>();
    public static Stack<Integer> synum = new Stack<Integer>();
    public static List<String[]> qt = new ArrayList<String[]>();
    public static String f;

    public static void main(String[] args) throws Exception {
        String path_in = "./in.txt";
        String path_out = "./out.txt";
        new analyzer().answer(path_in, path_out);
        try {
            File filename = new File(path_out);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine().substring(1);
            step = line.split(" ");
            line = br.readLine();
            i = line.substring(4, line.length() - 1).replace(" ", "").split(",");
            line = br.readLine();
            C = line.substring(4, line.length() - 1).replace(" ", "").split(",");
            line = br.readLine();
            S = line.substring(4, line.length() - 1).replace(" ", "").split(",");
            line = br.readLine();
            c = line.substring(4, line.length() - 1).replace(" ", "").split(",");
            line = br.readLine();
            k = line.substring(4, line.length() - 1).replace(" ", "").split(",");
            line = br.readLine();
            p = line.substring(4, line.length() - 1).replace(" ", "").split(",");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new four().answer());
        ;
    }
    // 文法：
    // E -> T D
    // D-> w T D|ε
    // T -> F Y
    // Y-> W F Y|ε
    // F -> I | ( E )

    // 以a*(b/c-d*e+f)/g-h为例,最终输出[*,a,t4,t5][/,b,c,t1][-,t1,t2,t3][*,d,e,t2][+,t3,f,t4][/,t5,g,t6][-,t6,h,t7]
    // [E]
    // [E, T]
    // [E, T, F] f=a;
    // a
    // [E, T]
    // [E, T, Y] if(symbol.empty())
    // {symbol.push(t);qt.add(new
    // String[4]);inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]=f;f=null;synum.push(qt.size()-1);}
    // 此时qt应为{[*,a, , ]} symbol[*] synum[0]
    // *
    // [E, T, Y, F] symbol.push("(");synum.push(-1);
    // (
    // [E, T, Y, F, E]
    // [E, T, Y, F, E, T]
    // [E, T, Y, F, E, T, F] f=b;
    // b
    // [E, T, Y, F, E, T]
    // [E, T, Y, F, E, T, Y] 虽然symbol不空，但优先级高else if(s
    // mbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/")))) 
    // {symbol.push(t);qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]=f;f=null;synum.push(qt.size()-1);}
    // 此时qt应为{[*,a, , ],[/,b, , ]} symbol[*,(,/] synum[0,-1,1]
    // /
    // [E, T, Y, F, E, T, Y, F] f=c;
    // c
    // [E, T, Y, F, E, T, Y]
    // [E, T, Y, F, E, T, Y, Y]
    // [E, T, Y, F, E, T, Y]
    // [E, T, Y, F, E, T]
    // [E, T, Y, F, E]
    // [E, T, Y, F, E, D] 优先级比别人低的 els
    // {while((!symbol.empty())&&(!(symbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/"))))))
    // {if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    //
    // 此时qt应为{[*,a, , ],[/,b,c,t1],[-,t1, , ]} symbol[*,(,-] synum[0,-1,2] num=2;
    // -
    // [E, T, Y, F, E, D, T]
    // [E, T, Y, F, E, D, T, F] f=d;
    // d
    // [E, T, Y, F, E, D, T]
    // [E, T, Y, F, E, D, T, Y]虽然symbol不空，但优先级高else if(s
    // mbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/")))) 
    // {symbol.push(t);qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]=f;f=null;synum.push(qt.size()-1);}
    // 此时qt应为{[*,a, , ],[/,b,c,t1],[-,t1, , ],[*,d, , ]} symbol[*,(,-,*] s
    // num[0,-1,2,3]
    // *
    // [E, T, Y, F, E, D, T, Y, F] f=e;
    // e
    // [E, T, Y, F, E, D, T, Y]
    // [E, T, Y, F, E, D, T, Y, Y]
    // [E, T, Y, F, E, D, T, Y]
    // [E, T, Y, F, E, D, T]
    // [E, T, Y, F, E, D]
    // [E, T, Y, F, E, D, D]优先级比别人低的 else 
    // 发现逻辑错误：这个时候前面不止一个*要处理，还有-应该处理，也就是比这个+优先度高的全应该处理！
    // 更正为：else{while((!symbol.empty())&&(!(symbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/"))))))
    // {if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // //这个时候qt应为{[*,a, , ],[/,b,c,t1],[-,t1, , ],[*,d,e,t2]} symbol[*,(,-] sy
    // um=[0,-1,2] num=3;
    // else{inqt=qt.get(synum.peek());inqt[2]="t".concat(String.valueOf(num-1));inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}}
    // //为了知道这个-对应的到底是qt中的第几个四元式，所以与symbol同步维护一个栈synum用来存symbol栈内元素在qt里的序号
    // qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]="t".concat(String.valueOf(num-1));symbol.push(t);synum.push(qt.size()-1);}
    // 此时qt应为{[*,a, , ],[/,b,c,t1],[-,t1,t2,t3],[*,d,e,t2],[+,t3, , ]} s
    // mbol[*,(,+] synum[0,-1,4] num=4
    // +
    // [E, T, Y, F, E, D, D, T]
    // [E, T, Y, F, E, D, D, T, F]
    // f=f;遇到括号结束，括号里应该只有一个符号，所以把这个符号运算一遍
    // inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();symbol.pop();synum.pop();
    // 此时qt应为{[*,a, , ],[/,b,c,t1],[-,t1,t2,t3],[*,d,e,t2],[+,t3,f,t4]} symbol[*] s
    // num[0] num=5
    // f
    // )
    // [E, T, Y, F, E, D, D, T]
    // [E, T, Y, F, E, D, D, T,
    // Y]else{while((!symbol.empty())&&(!(symbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/"))))))
    // {if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // else{inqt=qt.get(synum.peek());inqt[2]="t".concat(String.valueOf(num-1));inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}}
    // qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]="t".concat(String.valueOf(num-1));symbol.push(t);synum.push(qt.size()-1);}
    // 这里执行的是else的部分，inqt=qt.get(0),inqt[2]=t4,inqt[3]=t5,num=6,symbol=[],synum=[];再新建inqt,inqt[0]="/",inqt[1]=t5,symbol=[/],synum=[5]
    // 此时qt应为{[*,a,t4,t5],[/,b,c,t1],[-,t1,t2,t3],[*,d,e,t2],[+,t3,f,t4],[/,t5, , ]
    //  
    // /
    // [E, T, Y, F, E, D, D, T, Y, F] t=g
    // g
    // [E, T, Y, F, E, D, D, T, Y]
    // [E, T, Y, F, E, D, D, T, Y, Y]
    // [E, T, Y, F, E, D, D, T, Y]
    // [E, T, Y, F, E, D, D, T]
    // [E, T, Y, F, E, D, D]
    // [E, T, Y, F, E, D, D,
    // D]else{while((!symbol.empty())&&(!(symbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/"))))))
    // {if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // else{inqt=qt.get(synum.peek());inqt[2]="t".concat(String.valueOf(num-1));inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}}
    // qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]="t".concat(String.valueOf(num-1));symbol.push(t);synum.push(qt.size()-1);}
    // 这里执行的是if的部分，inqt=qt[5],inqt[2]="g";f=null;inqt[3]=t6;num=7;symbol[];synum[];再新建inqt,inqt[0]="-",inqt[1]=t6,symbol=[-],synum=[6]
    // 此时qt应为{[*,a,t4,t5],[/,b,c,t1],[-,t1,t2,t3],[*,d,e,t2],[+,t3,f,t4],[/,t5,g,t6],[-,t6, ,
    // ]} 
    // -
    // [E, T, Y, F, E, D, D, D, T]
    // [E, T, Y, F, E, D, D, D, T, F] f=h
    // h
    // [E, T, Y, F, E, D, D, D, T]
    // [E, T, Y, F, E, D, D, D, T,
    // Y]最终DY出栈时检查，if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // h
    // [E, T, Y, F, E, D, D, D, T]
    // [E, T, Y, F, E, D, D, D]
    // [E, T, Y, F, E, D, D, D, D]
    // [E, T, Y, F, E, D, D, D]
    // [E, T, Y, F, E, D, D]
    // [E, T, Y, F, E, D]
    // [E, T, Y, F, E]
    // [E, T, Y, F]
    // [E, T, Y]
    // [E, T, Y, Y]
    // h
    // [E, T, Y]
    // [E, T]
    // [E]
    // [E, D]
    // [E]

    // 总结，
    // DY有效入栈时
    // if(symbol.empty()){symbol.push(t);qt.add(new
    // String[4]);inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]=f;f=null;synum.push(qt.size()-1);}
    // else
    // if(symbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/"))))
    // {symbol.push(t);qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]=f;f=null;synum.push(qt.size()-1);}
    // else{
    // while((!symbol.empty())&&(!(symbol.peek().equals("(")||((symbol.peek().equals("+")||symbol.peek().equals("-"))&&(t.equals("*")||t.equals("/")))))){
    // if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // else{inqt=qt.get(synum.peek());inqt[2]="t".concat(String.valueOf(num-1));inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // }
    // qt.add(new String[4])
    // inqt=qt.get(qt.size()-1);inqt[0]=t;inqt[1]="t".concat(String.valueOf(num-1));symbol.push(t);synum.push(qt.size()-1);
    // }
    // DY有效入栈的出栈时
    // if(f!=null){inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();}
    // F有效入栈时，f=i;
    // (时，symbol.push("(");synum.push(-1);
    // )时，inqt=qt.get(qt.size()-1);inqt[2]=f;f=null;inqt[3]="t".concat(String.valueOf(num));num++;symbol.pop();synum.pop();symbol.pop();synum.pop();
    private void E() {
        if (flag == 0)
            return;
        st.push("E");
        System.out.println(st.toString());
        System.out.println(symbol.toString());
        T();
        D();
        st.pop();
        if (st.empty() && (!symbol.empty())) {
            while (!symbol.empty()) {
                if (f != null) {
                    inqt = qt.get(qt.size() - 1);
                    inqt[2] = f;
                    f = null;
                    inqt[3] = "t".concat(String.valueOf(num));
                    num++;
                    symbol.pop();
                    synum.pop();
                } else {
                    while((!synum.empty())&&synum.peek()==-1){
                        symbol.pop();
                        synum.pop();
                    }
                    if(!symbol.empty()){
                        inqt = qt.get(synum.peek());
                        inqt[2] = "t".concat(String.valueOf(num - 1));
                        inqt[3] = "t".concat(String.valueOf(num));
                        num++;
                        symbol.pop();
                        synum.pop();
                    }
                }
            }
        }
        System.out.println(st.toString());
    }

    private void T() {
        if (flag == 0)
            return;
        st.push("T");
        System.out.println(st.toString());
        System.out.println(symbol.toString());
        F();
        Y();
        st.pop();
        System.out.println(st.toString());
    }

    private void D() {
        if (flag == 0)
            return;
        st.push("D");
        System.out.println(symbol.toString());
        System.out.println(st.toString());
        String t;
        switch (step[now].substring(1, 2)) {
        case "i":
            t = i[Integer.parseInt(step[now].substring(3, 4))];
            st.pop();
            System.out.println(st.toString());
            return;
        case "c":
            t = c[Integer.parseInt(step[now].substring(3, 4))];
            st.pop();
            System.out.println(st.toString());
            return;
        case "p":
            t = p[Integer.parseInt(step[now].substring(3, 4))];
            if (t.equals("+") || t.equals("-")) {
                System.out.println(t);
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
                    inqt[1] = f;
                    f = null;
                    synum.push(qt.size() - 1);
                } else if (symbol.peek().equals("(") || ((symbol.peek().equals("+") || symbol.peek().equals("-"))
                        && (t.equals("*") || t.equals("/")))) {
                    symbol.push(t);
                    qt.add(new String[4]);
                    inqt = qt.get(qt.size() - 1);
                    inqt[0] = t;
                    inqt[1] = f;
                    f = null;
                    synum.push(qt.size() - 1);
                } else {
                    while ((!symbol.empty())
                            && (!(symbol.peek().equals("(") || ((symbol.peek().equals("+") || symbol.peek().equals("-"))
                                    && (t.equals("*") || t.equals("/")))))) {
                        if (f != null) {
                            inqt = qt.get(qt.size() - 1);
                            inqt[2] = f;
                            f = null;
                            inqt[3] = "t".concat(String.valueOf(num));
                            num++;
                            symbol.pop();
                            synum.pop();
                        } else {
                            inqt = qt.get(synum.peek());
                            inqt[2] = "t".concat(String.valueOf(num - 1));
                            inqt[3] = "t".concat(String.valueOf(num));
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
                System.out.println(st.toString());
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
        System.out.println(st.toString());
        if (now == step.length - 1 && f != null) {
            inqt = qt.get(qt.size() - 1);
            inqt[2] = f;
            f = null;
            inqt[3] = "t".concat(String.valueOf(num));
            num++;
            symbol.pop();
            synum.pop();
        }
    }

    private void Y() {
        if (flag == 0)
            return;
        st.push("Y");
        System.out.println(st.toString());
        System.out.println(symbol.toString());
        String t;
        switch (step[now].substring(1, 2)) {
        case "i":
            t = i[Integer.parseInt(step[now].substring(3, 4))];
            System.out.println(t);
            if (now == step.length - 1) {
                st.pop();
                System.out.println(st.toString());
                return;
            }
            now++;
            st.pop();
            System.out.println(st.toString());
            return;
        case "c":
            t = c[Integer.parseInt(step[now].substring(3, 4))];
            System.out.println(t);
            if (now == step.length - 1) {
                st.pop();
                System.out.println(st.toString());
                return;
            }
            now++;
            st.pop();
            System.out.println(st.toString());
            return;
        case "p":
            t = p[Integer.parseInt(step[now].substring(3, 4))];
            if (t.equals("*") || t.equals("/")) {
                System.out.println(t);
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
                    inqt[1] = f;
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
                            inqt[2] = f;
                            f = null;
                            inqt[3] = "t".concat(String.valueOf(num));
                            num++;
                            symbol.pop();
                            synum.pop();
                        } else {
                            inqt = qt.get(synum.peek());
                            inqt[2] = "t".concat(String.valueOf(num - 1));
                            inqt[3] = "t".concat(String.valueOf(num));
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
                System.out.println(st.toString());
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
        System.out.println(st.toString());
        if (now == step.length - 1 && f != null) {
            inqt = qt.get(qt.size() - 1);
            inqt[2] = f;
            f = null;
            inqt[3] = "t".concat(String.valueOf(num));
            num++;
            symbol.pop();
            synum.pop();
        }
    }

    private void F() {
        st.push("F");
        System.out.println(st.toString());
        System.out.println(symbol.toString());
        if (flag == 0)
            return;
        String t;
        switch (step[now].substring(1, 2)) {
        case "i":
            t = i[Integer.parseInt(step[now].substring(3, 4))];
            System.out.println(t);
            f = t;
            if (now == step.length - 1) {
                st.pop();
                System.out.println(st.toString());
                return;
            }
            now++;
            if (step[now].substring(1, 2).equals("p")) {
                t = p[Integer.parseInt(step[now].substring(3, 4))];
                if (t.equals(")")) {
                    brackets--;
                    System.out.println(t);
                    if (now == step.length - 1) {
                        st.pop();
                        System.out.println(st.toString());
                        return;
                    }
                    now++;
                    inqt = qt.get(qt.size() - 1);
                    inqt[2] = f;
                    f = null;
                    inqt[3] = "t".concat(String.valueOf(num));
                    num++;
                    symbol.pop();
                    synum.pop();
                    symbol.pop();
                    synum.pop();
                    if (brackets < 0) {
                        flag = 0;
                        return;
                    }
                }
            }
            break;
        case "c":
            t = c[Integer.parseInt(step[now].substring(3, 4))];
            System.out.println(t);
            f = t;
            if (now == step.length - 1) {
                st.pop();
                System.out.println(st.toString());
                return;
            }
            now++;
            if (step[now].substring(1, 2).equals("p")) {
                t = p[Integer.parseInt(step[now].substring(3, 4))];
                if (t.equals(")")) {
                    brackets--;
                    System.out.println(t);
                    inqt = qt.get(qt.size() - 1);
                    inqt[2] = f;
                    f = null;
                    inqt[3] = "t".concat(String.valueOf(num));
                    num++;
                    symbol.pop();
                    synum.pop();
                    symbol.pop();
                    synum.pop();
                    if (now == step.length - 1) {
                        st.pop();
                        System.out.println(st.toString());
                        return;
                    }
                    now++;
                    if (brackets < 0) {
                        flag = 0;
                        return;
                    }
                }
            }
            break;
        case "p":
            t = p[Integer.parseInt(step[now].substring(3, 4))];
            if (t.equals("(")) {
                System.out.println(t);
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
        System.out.println(st.toString());
        for (String[] aa : qt) {
            System.out.print("[");
            for (String aaa : aa)
                System.out.print(aaa);
            System.out.print("]");
        }
    }

    public String answer() {
        E();
        for (String[] aa : qt) {
            System.out.print("[");
            for (int aaa = 0; aaa < 4; aaa++) {
                System.out.print(aa[aaa]);
                if (aaa != 3)
                    System.out.print(",");
            }
            System.out.print("] ");
        }
        if (brackets != 0 || flag == 0 || now != step.length - 1)
            return "wrong";
        else
            return "right";
    }
}