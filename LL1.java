import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class LL1{
    public static int now=0,flag=1,brackets=0;
    public static String[] step,i,C,S,c,k,p;
    public static String[][] M=new String[5][6];
    private int gety(String y){
        if(y.equals("i")) return 0;
        else if(y.equals("w")) return 1;
        else if(y.equals("W")) return 2;
        else if(y.equals("(")) return 3;
        else if(y.equals(")")) return 4;
        else return -1;
    }
    private int getx(String x){
        if(x.equals("E")) return 0;
        else if(x.equals("D")) return 1;
        else if(x.equals("T")) return 2;
        else if(x.equals("Y")) return 3;
        else if(x.equals("F")) return 4;
        else return -1;
    }
    public static void main(String[] args) throws Exception {
        String path_in = "./in.txt";
        String path_out = "./out.txt";
        //先进行语法分析，把结果存到out文件里：
        new analyzer().answer(path_in, path_out);
        //打开out文件，读取step和参照表：
        try{
            File filename = new File(path_out);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);// 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line= br.readLine().substring(1);//out.txt的第一行是{c,0}……等，substring(1)是因为analyzer搞得第一个是空格，切的时候会多一个
            step=line.split(" ");//切成数组
            line=br.readLine();//读第二行的i变量名参照表，下面同理
            i=line.substring(4,line.length()-1).replace(" ", "").split(",");//把参照表弄成数组
            line=br.readLine();
            C=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            S=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            c=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            k=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            p=line.substring(4,line.length()-1).replace(" ", "").split(",");
            br.close();
        }catch (Exception e) {
			e.printStackTrace();
        }
    //文法：
    //   E -> TD
    //   D-> wTD|ε
    //   T -> FY
    //   Y-> WFY|ε
    //   F -> I | (E) 
    //   w代表+-，W代表*/
    //  ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┐
    //  │table│  i  │ w+- │ W*/ │  (  │  )  │  #  │
    //  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┤
    //  │  E  │  TD │     │     │  TD │     │     │
    //  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┤
    //  │  D  │     │ wTD │     │     │  ε  │  ε  │
    //  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┤
    //  │  T  │  FY │     │     │  FY │     │     │
    //  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┤
    //  │  Y  │     │  ε  │ WFY │     │  ε  │  ε  │
    //  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┤
    //  │  F  │  i  │     │     │ (E) │     │     │
    //  └─────┴─────┴─────┴─────┴─────┴─────┴─────┘
        M[0][0]="TD";
        M[0][3]="TD";
        M[1][1]="wTD";
        M[1][4]=" ";
        M[1][5]=" ";
        M[2][0]="FY";
        M[2][3]="FY";
        M[3][1]=" ";
        M[3][2]="WFY";
        M[3][4]=" ";
        M[3][5]=" ";
        M[4][0]="i";
        M[4][3]="(E)";
        System.out.println(new LL1().answer());
    }
    public String answer() {
        Stack<String> st = new Stack<String>();
        int flag_t=0,flag=1;
        String pe,t;//pop_elem
        st.push("E");
        
        while(now<step.length){
            flag_t=0;
            pe=st.pop();
            System.out.println("出栈".concat(pe));
            System.out.println("栈内还有：".concat(st.toString()));
            //如果pe是符合step[now]的，就now++，pe再出栈一个
            //符合的情况按照step[now]的类型分为好几种，其中i和c都对应i，p对应w和W
            //如果step[now]是变量名或者数字，则匹配意味着出栈就是i
            //如果是符号，那么，出栈的可能是w代表+- W代表*/
            t=step[now].substring(1,2);
            if(t.equals("i")||t.equals("c")){
                t="i";//t变成"i"
                if(pe.equals("i"))  flag_t=1;//在step[now]类型是数字和变量名的情况下，出栈元素是"i"代表符合
            }
            else if(t.equals("p")){
                t=p[Integer.parseInt(step[now].substring(3,4))];//t变成+-*/()
                if(pe.equals("w")&&(t.equals("+")||t.equals("-"))) flag_t=1;//如果出栈的是w而step[now]类型是+-，就是符合的
                else if(pe.equals("W")&&(t.equals("*")||t.equals("/"))) flag_t=1;//如果出栈的是W而step[now]类型是*/，就是符合的
                else if(pe.equals(")")&&t.equals(")")) flag_t=1;
                else if(pe.equals("(")&&t.equals("(")) flag_t=1;
            }
            //判断完了pe符合不符合step[now],开始执行符合了要干啥和不符合要干啥
            if(flag_t==1){//如果pe是符合step[now]的,也就是说pe是W而step[now]是*/之类的
                System.out.println("匹配上了");
                switch(step[now].substring(1,2)){
                    case "i":
                    System.out.println(i[Integer.parseInt(step[now].substring(3,4))]);//查表找出对应变量名
                    break;
                    case "c"://如果到E1时step[now]是数字，不造成错误，仍然继续
                    System.out.println(c[Integer.parseInt(step[now].substring(3,4))]);//查表找出对应数字
                    break;
                    case "p":
                    System.out.println(p[Integer.parseInt(step[now].substring(3,4))]);
                    break;
                    default:
                    System.out.println("something bad");
                }
                now++;//变成下一个元素
                continue;
            }
            else{
                System.out.println("没匹配上");
                if(t.equals("+")||t.equals("-")) t="w";//将t从+-变成w
                else if(t.equals("*")||t.equals("/")) t="W";//将t从*/变成W
                System.out.println("x:".concat(pe).concat(" "+getx(pe)).concat("   y:").concat(t).concat(" "+gety(t)));
                t=M[getx(pe)][gety(t)];//获取M矩阵中对应的值，也就是出栈元素应该推倒出来的东西，比如说E->TD
                if(t==null) return "wrong";
                else if(!t.equals(" ")){
                    System.out.println("执行的栈操作是".concat(pe).concat("->").concat(t));//输出执行的栈操作。
                    for(int j=t.length();j>0;j--){//逆序压栈，如WFY就压成YFW
                        System.out.println("入栈".concat(t.substring(j-1,j)));
                        st.push(t.substring(j-1,j));
                    }
                }
                else System.out.println("执行的栈操作是".concat(pe).concat("->").concat("ε"));//输出执行的栈操作。
            }
        }
        //还差最后一步，检查栈能不能是空
        while(!st.isEmpty()){
            t=st.pop();
            if(t.equals("D")||t.equals("Y")) continue;
            else return "wrong";
        }
        return "right";
    }
}