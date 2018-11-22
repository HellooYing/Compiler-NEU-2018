import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class Recursive_decline{
    public static int now=0,flag=1,brackets=0;
    public static String[] step,i,C,S,c,k,p;
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
        //进行递归下降分析，判断算数表达式合理性
        System.out.println(new Recursive_decline().answer());;
    }
    //文法：
    //   E -> T E1
    //   E1-> ω0 T E1|ε
    //   T -> F T1
    //   T1-> ω1 F T1|ε
    //   F -> I | ( E ) 

    // 输入就相当于是step数组，第一个是啥第二个是啥的。设置一个全局变量now代表转换到step[now]。

    // E是代表开始是一个算数表达式，然后表达式变成T E1

    // T则要变成FT1

    // F就可以变成一个变量名或者数字或者一个括号括起来的表达式，当前step是（就（E），别的I。每转换一个F就step+1.
    // 每次改变step（也包括最开始step[now]）都要检查是否是括号，前括号的话kh++，后括号的话kh--且step再+1。

    // E1是，如果到它这step是+/-，就变成+/- F T1,否则就直接去掉E1
    // T1是到它这step是*//,就*// E T1，否则删掉 
    // 如何在发生错误时立刻返回呢？设置flag，默认1，若flag=0则立刻返回

    public static String getTraceInfo(){  //一个输出代码行号的函数，用来告诉我何时return也就是何时认为表达式出错了或者认为表达式结束了
        StringBuffer sb = new StringBuffer();   
          
        StackTraceElement[] stacks = new Throwable().getStackTrace();  
        int stacksLen = stacks.length;  
        sb.append("class: " ).append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append("; number: ").append(stacks[1].getLineNumber());  
        return sb.toString();  
    }
    private void E(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        T();
        E1();
    }
    private void T(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        F();
        T1();
    }
    private void E1(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        String t;
        switch(step[now].substring(1,2)){
            case "i"://如果到E1时step[now]是变量名，不造成错误，仍然继续
            t=i[Integer.parseInt(step[now].substring(3,4))];//查表找出对应变量名
            break;
            case "c"://如果到E1时step[now]是数字，不造成错误，仍然继续
            t=c[Integer.parseInt(step[now].substring(3,4))];//查表找出对应数字
            break;
            case "p"://如果到E1时step[now]是+-*/(),仍然继续，别的符号是错的
            t=p[Integer.parseInt(step[now].substring(3,4))];//查表找出对应符号
            if(t.equals("+")||t.equals("-")){
                System.out.println(t);
                if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());return;
                };
                now++;//+和-只在T1里消除，别的地方遇到都是错的
                T();
                E1();
                break;
            }
            else if(t.equals("*")||t.equals("/")||t.equals("(")||t.equals(")")) break;
            else{
                flag=0;
                System.out.println(getTraceInfo());return;
            }
            default:
            flag=0;
            System.out.println(getTraceInfo());return;
        }    
    }

    private void T1(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        String t;
        switch(step[now].substring(1,2)){
            case "i"://如果到E1时step[now]是变量名，则应该转换为变量名
            t=i[Integer.parseInt(step[now].substring(3,4))];//查表找出对应变量名
            System.out.println(t);
            if(now==step.length-1) {System.out.println(getTraceInfo());return;}
            now++;
            break;
            case "c"://如果到E1时step[now]是数字，则应该转换为数字
            t=c[Integer.parseInt(step[now].substring(3,4))];//查表找出对应数字
            System.out.println(t);
            if(now==step.length-1) {System.out.println(getTraceInfo());return;}
            now++;
            break;
            case "p"://如果到E1时step[now]是+-*/(),仍然继续，别的符号是错的
            t=p[Integer.parseInt(step[now].substring(3,4))];//查表找出对应符号
            if(t.equals("*")||t.equals("/")){
                System.out.println(t);
                if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());return;
                };
                now++;//*和/只在T1里消除，别的地方遇到都是错的
                F();
                T1();
                break;
            }
            else if(t.equals("+")||t.equals("-")||t.equals("(")||t.equals(")")) break;
            else{
                flag=0;
                System.out.println(getTraceInfo());return;
            }
            default:
            flag=0;
            System.out.println(getTraceInfo());return;
        }    
    }
    private void F(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        String t;
        switch(step[now].substring(1,2)){
            case "i":
            t=i[Integer.parseInt(step[now].substring(3,4))];//查表找出对应变量名 
            System.out.println(t);     
            if(now==step.length-1) {System.out.println(getTraceInfo());return;}
            now++;
            if(step[now].substring(1,2).equals("p")){
                t=p[Integer.parseInt(step[now].substring(3,4))];
                while(t.equals(")")){
                    brackets--;
                    System.out.println(t);
                    if(now==step.length-1) {System.out.println(getTraceInfo());return;}
                    now++;
                    if(brackets<0){
                        flag=0;
                        System.out.println(getTraceInfo());return;
                    }
                    if(step[now].substring(1,2).equals("p")){t=p[Integer.parseInt(step[now].substring(3,4))];}
                }
            }
            break;
            case "c":
            t=c[Integer.parseInt(step[now].substring(3,4))];//查表找出对应数字
            System.out.println(t);
            if(now==step.length-1) {System.out.println(getTraceInfo());return;}
            now++;
            if(step[now].substring(1,2).equals("p")){
                t=p[Integer.parseInt(step[now].substring(3,4))];
                while(t.equals(")")){
                    brackets--;
                    System.out.println(t);
                    if(now==step.length-1) {System.out.println(getTraceInfo());return;}
                    now++;
                    if(brackets<0){
                        flag=0;
                        System.out.println(getTraceInfo());return;
                    }
                    if(step[now].substring(1,2).equals("p")){t=p[Integer.parseInt(step[now].substring(3,4))];}
                }
            }
            break;
            case "p"://如果到E1时step[now]是符号，只接受（，别的都是错的
            t=p[Integer.parseInt(step[now].substring(3,4))];//查表找出对应符号
            if(t.equals("(")){
                System.out.println(t);
                if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());return;
                }
                else if(step[now+1].substring(1,2).equals("p")&&p[Integer.parseInt(step[now+1].substring(3,4))].equals(")")){
                    flag=0;
                    System.out.println(getTraceInfo());return;
                }
                now++;
                brackets++;
                E();
                break;
            }
            else{
                flag=0;
                System.out.println(getTraceInfo());return;
            }
            default:
            flag=0;
            System.out.println(getTraceInfo());return;
        }    
    }
    public String answer() {
        E();
        if(brackets!=0||flag==0||now!=step.length-1) return "wrong";
        else return "right";
    }
}