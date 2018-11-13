import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class Recursive_decline{
    public static void main(String[] args) throws Exception {
        String path_in = "./in.txt";
        String path_out = "./out.txt";
        new analyzer().answer(path_in, path_out);
        new Recursive_decline().answer(path_out);
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
    // 每次改变step（也包括最开始step[0]）都要检查是否是括号，前括号的话kh++，后括号的话kh--且step再+1。

    // E1是，如果到它这step是+/-，就变成+/- F T1,否则就直接去掉E1
    // T1是到它这step是*//,就*// E T1，否则删掉
    public static int now=0;
    private void E(){
        T();
        E1();
    }
    private void T(){
        F();
        T1();
    }
    private void E1(){
        F();
        T1();
        
    }
    public void answer(String path_in) {
        try{
            File filename = new File(path_in);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);// 建立一个对象，它把文件内容转成计算机能读懂的语言
            String result="",r = "",line = "",t;
            r= br.readLine().substring(1);//out.txt的第一行是{c,0}……等，substring(1)是因为analyzer搞得第一个是空格，切的时候会多一个
            String[] step=r.split(" ");//切成数组
            line=br.readLine();//读第二行的i变量名参照表，下面同理
            String[] i=line.substring(4,line.length()-1).replace(" ", "").split(",");//把参照表弄成数组
            line=br.readLine();
            String[] C=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            String[] S=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            String[] c=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            String[] k=line.substring(4,line.length()-1).replace(" ", "").split(",");
            line=br.readLine();
            String[] p=line.substring(4,line.length()-1).replace(" ", "").split(",");
            br.close();
            
            switch(step[0].substring(1,2)){
                case "i":
                t=i[Integer.parseInt(step[0].substring(3,4))];
                System.out.println(t);
                break;
                case "c":
                t=c[Integer.parseInt(step[0].substring(3,4))];
                System.out.println(t);
                break;
                case "p":
                t=p[Integer.parseInt(step[0].substring(3,4))];
                System.out.println(t);
                break;
                default:
                System.out.println("wrong");
                return;
            }

        }catch (Exception e) {
			e.printStackTrace();
		}      
    }
}