import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class SLR{
    public static int now=0,brackets=0;
    public static String[] stept,i,C,S,c,k,p;
    public static String[] SLRGY = { "", "E->EwT", "E->T", "T->TWF", "T->F", "F->(E)", "F->i" };//归约
    //文法：
    //   E -> TD
    //   D-> wTD|ε
    //   T -> FY
    //   Y-> WFY|ε
    //   F -> I | (E) 
    //   w代表+-,W代表*/
    public static String [][] M = { 
        //{ "", "i", "w", "W", "(", ")", "$", "E", "T", "F" },
        { "0", "s5", "", "", "s4", "", "", "1", "2", "3" }, 
        { "1", "", "s6", "", "", "", "ok", "", "", "" },
        { "2", "", "r2", "s7", "", "r2", "r2", "", "", "" }, 
        { "3", "", "r4", "r4", "", "r4", "r4", "", "", "" },
        { "4", "s5", "", "", "s4", "", "", "8", "2", "3" }, 
        { "5", "", "r6", "r6", "", "r6", "r6", "", "", "" },
        { "6", "s5", "", "", "s4", "", "", "", "9", "3" }, 
        { "7", "s5", "", "", "s4", "", "", "", "", "10" },
        { "8", "", "s6", "", "", "s11", "", "", "", "" }, 
        { "9", "", "r1", "s7", "", "r1", "r1", "", "", "" },
        { "10", "", "r3", "r3", "", "r3", "r3", "", "", "" },
        { "11", "", "r5", "r5", "", "r5", "r5", "", "", "" } };
        private int gety(String y){
            if(y.equals("i")) return 1;
            else if(y.equals("w")) return 2;
            else if(y.equals("W")) return 3;
            else if(y.equals("(")) return 4;
            else if(y.equals(")")) return 5;
            else if(y.equals("$")) return 6;
            else if(y.equals("E")) return 7;
            else if(y.equals("T")) return 8;
            else if(y.equals("F")) return 9;
            else return -1;
        }

    public static void main(String[] args) throws Exception {
        String path_in = "./in.txt";
        String path_out = "./out.txt";
        //先进行语法分析,把结果存到out文件里：
        new analyzer().answer(path_in, path_out);
        //打开out文件,读取step和参照表：
        try{
            File filename = new File(path_out);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);// 建立一个对象,它把文件内容转成计算机能读懂的语言
            String line = "";
            line= br.readLine().substring(1);//out.txt的第一行是{c,0}……等,substring(1)是因为analyzer搞得第一个是空格,切的时候会多一个
            stept=line.split(" ");//切成数组
            line=br.readLine();//读第二行的i变量名参照表,下面同理
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

        System.out.println(new SLR().answer());
    }
    public String answer() {
        //SLR分析(2*a+b)/3,num=0,代表从第0步开始,step[now]="(",state=0
        //M[0,"("]=s4,栈为(, num=4,now++,下一个字符为2,是i类型,state=0,4
        //M[4,"i"]=s5,栈为(i,num=5,now++,下一个字符为*,是W,state=0,4,5
        //M[5,"W"]=r6,r6代表i变F,所以栈内（F,先去掉一个state=0,4,再加上一个num=M[4,F]=3,state=0,4,3
        //M[3,"W"]=r4,r4代表F变T,所以栈内（T,先去掉一个state=0,4,再加上一个num=M[4,T]=2,state=0,4,2
        //M[2,"W"]=s7,栈为（TW,num=7,now++,字符为a,是i,state=0,4,2,7
        //M[7,"i"]=s5,栈为（TWi,num=5,now++,下一个字符为+,是w,state=0,4,2,7,5
        //M[5,"w"]=r6,r6代表i变F,所以栈内（TWF,先去掉一个state=0,4,2,7,再加上一个num=M[7,F]=10,state=0,4,2,7,10
        //M[10,"w"]=r3,TWF变T,所以栈内（T。先去掉三个,state=0,4。再加上一个,num=M[0,T]=2,state=0,4,2
        //M[2,"w"]=r2,T变E,所以栈内（E,state=0,4。num=M[4,E]=8,state=0,4,8
        //M[8,"w"]=s6,栈为（Ew,num=6,now++,字符为b,是i,state=0,4,8,6
        //M[6,"i"]=s5,栈为（Ewi,num=5,now++,字符为）,state=0,4,8,6,5
        //M[5,")"]=r6,r6代表i变F,所以栈内（EwF,先去掉一个state=0,4,8,6,再加上一个num=M[6,F]=3,state=0,4,8,6,3
        //M[3,")"]=r4,r4代表F变T,所以栈内（EwT,先去掉一个state=0,4,8,6,再加上一个num=M[6,T]=9,state=0,4,8,6,9
        //M[9,")"]=r1,r1代表EwT变E,所以栈内（E,先去掉3个state=0,4,再加上一个num=M[4,E]=8,state=0,4,8
        //M[8,")"]=s11,栈为（E）,num=11,now++,字符为/,是W,state=0,4,8,11
        //M[11,"W"]=r5,代表（E）变F,所以栈内F,先去掉三个state=[0],再加上一个num=M[0,F]=3,state=0,3
        //M[3,"W"]=r4,r4代表F变T,所以栈内（T,先去掉一个state=0,再加上一个num=M[0,T]=2,state=0,2
        //M[2,"W"]=s7,栈为TW,num=7,now++,字符为3,是i,state=0,2,7
        //M[7,"i"]==s5,栈为TWi,num=5,now++,下一个字符为$,是终结符,state=0,2,7,5
        //M[5,"$"]=r6,r6代表i变F,所以栈内TWF,先去掉一个state=0,2,7,再加上一个num=M[7,F]=10,state=0,2,7,10
        //M[10,"$"]=r3,r3代表TWF变T,所以栈内T。先去掉三个,state=0。再加上一个,num=M[0,T]=2,state=0,2
        //M[2,"$"]=r2,r2代表T变E,所以栈内E。先去掉一个,state=0,再加上一个num=M[0,E]=1,state=0,1
        //M[1,"$"]="ok",return "right"

        //由此可见逻辑是：
        //int num=0,now=0;
        //List<Integer> state=new ArrayList<Integer>();state.add(0);
        //String t,tt;
        //String[] GY;
        //while(true)循环体内,先获取元素t=p[Integer.parseInt(step[now].substring(3,4))]
        //再看tt=M[state.get(state.size()-1)][gety(t)]
        //先检测tt.equals("ok"),是就return "right"
        //switch(tt.substring(0,1))是s是r
        //是s,则栈里加上t（括号加括号原型,+-加w,*/加W,字母数字加i。
        //num=Integer.parseInt(tt.substring(1)),now++,state.add(num)
        //如果是r,则num=Integer.parseInt(tt.substring(1)),GY=SLRGY[num].split("->");
        //GY[1]是从栈删除的,for(int j=0;j<GY[1].length();j++) {st.pop();state.remove(state.size()-1);}
        //st.push(GY[0]);num=Integer.parseInt(M[state.get(state.size()-1)][gety(GY[0])]);state.add(num)
        String[] step=new String[stept.length+1];
        for(int j=0;j<step.length-1;j++){
            step[j]=stept[j];
        }
        step[step.length-1]="$";
        for(String str:step) System.out.println(str);
        int num=0;
        List<Integer> state=new ArrayList<Integer>();
        Stack<String> st = new Stack<String>();
        state.add(0);
        String t,tt;
        String[] GY;
        while(true){
            if(step[now].equals("$")) t="$";
            else{
                t=step[now].substring(1,2);
                if(t.equals("i")||t.equals("c")){
                    t="i";//t变成"i"
                }
                else if(t.equals("p")){
                    t=p[Integer.parseInt(step[now].substring(3,4))];//t变成wW()
                    if(t.equals("+")||t.equals("-")) t="w";
                    else if(t.equals("*")||t.equals("/")) t="W";
                    else if(t.equals(")")) brackets--;
                    else if(t.equals("("))  brackets++;
                }
            }
            System.out.println(t);
            tt=M[state.get(state.size()-1)][gety(t)];
            if(tt.equals("ok")) return "right";
            else if(tt=="") return "wrong";
            
            switch(tt.substring(0,1)){
                case "s":
                st.push(t);
                num=Integer.parseInt(tt.substring(1));
                now++;
                state.add(num);
                System.out.println(state.toString());
                break;
                case "r":
                num=Integer.parseInt(tt.substring(1));
                GY=SLRGY[num].split("->");
                for(int j=0;j<GY[1].length();j++) {
                    st.pop();
                    state.remove(state.size()-1);
                }
                st.push(GY[0]);
                num=Integer.parseInt(M[state.get(state.size()-1)][gety(GY[0])]);
                state.add(num);
                System.out.println(state.toString());
                break;
                default:
                return "wrong";
            }
        }
    }
    
}