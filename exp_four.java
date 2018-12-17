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
    public static Stack<String> st1 = new Stack<String>();//元素栈
	public static Stack<String> st2 = new Stack<String>();//符号栈
    public static List<String[]> qt = new ArrayList<String[]>();//存四元式
	public static String f;
    public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		String path_out = "./z.token序列.txt";
		new analyzer().answer(path_in);
		try {
			File filename = new File(path_out);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine().substring(1);
			step = line.split(" ");
			line = br.readLine();
			i = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			C = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			S = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			c = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			k = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			line = br.readLine();
			p = line.substring(3, line.length() - 1).replace(" ", "").split(",");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String[]> r1=new exp_four().answer(step, i, C, S, c, k, p);
	}
    public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1){
        i=i1;
		C=C1;
		S=S1;
		c=c1;
		k=k1;
		p=p1;
		qt.clear();
        st1.clear();
        st2.clear();
		now = 0;
		flag = 1;
		num = 1;
		brackets = 0;//清除上一次留下的值
        String finalout;
        if(step1[1].substring(1,2).equals("p")&&p[Integer.parseInt(step1[1].substring(3, 4))].equals("=")&&step1.length>4){
            finalout=i[Integer.parseInt(step1[0].substring(3, 4))];
            step=Arrays.copyOfRange(step1,2,step1.length);
        }
        else step=step1;
		//符号栈与元素栈，遇到元素就加进元素栈，遇到符号就加进符号栈
		//遇到符号的时候要跟符号栈的栈顶元素比较，操作是：
		//遇+- 栈顶+-*/，出栈  空或（ 入栈
		//遇*/ 栈顶+-（空 入栈   */出栈
		//遇） 出栈到（
		//出栈的意思是，inqt[0]=st2.pop();inqt[2]=st1.pop();inqt[1]=st1.pop();inqt[3]="t".concat(String.valueOf(num));num++;st1.add(inqt[3]);
		//入栈的意思是，st2.add(符号)
		//遇到元素全直接入栈
		E();
        if(flag==0) System.out.println("输入表达式无效，请检查z.c语言代码输入.txt");
		else{
            while(!st2.empty()){
			    inqt=new String[4];
			    inqt[0]=st2.pop();
			    inqt[2]=st1.pop();
			    inqt[1]=st1.pop();
			    inqt[3]="t".concat(String.valueOf(num));
			    num++;
			    st1.add(inqt[3]);
			    qt.add(inqt);
		    }
        
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
        }
		return qt;
    }

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
            case "i":
            return;
            case "c":
            return;
            case "p":
            t=p[Integer.parseInt(step[now].substring(3,4))];
            if(t.equals("+")||t.equals("-")){
				if(st2.empty()||st2.peek().equals("(")){
					st2.add(t);
				}
				else if(st2.peek().equals("+")||st2.peek().equals("-")||st2.peek().equals("*")||st2.peek().equals("/")){
					inqt=new String[4];
					inqt[0]=st2.pop();
					inqt[2]=st1.pop();
					inqt[1]=st1.pop();
					inqt[3]="t".concat(String.valueOf(num));
					num++;
					st1.add(inqt[3]);
					qt.add(inqt);
                    st2.add(t);
				}
                //System.out.println(t);
                if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());
                    return;
                };
                now++;//+和-只在T1里消除，别的地方遇到都是错的
                T();
                E1();
                break;
            }
            else if(t.equals("*")||t.equals("/")||t.equals("(")||t.equals(")")||t.equals(";")) return;
            else{
                flag=0;
                System.out.println(getTraceInfo());
                return;
            }
            default:
            flag=0;
            System.out.println(getTraceInfo());
            return;
        }    
    }
	private void T1(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        String t;
        switch(step[now].substring(1,2)){
            case "i":
            return;
            case "c":
            return;
            case "p":
            t=p[Integer.parseInt(step[now].substring(3,4))];
            if(t.equals("*")||t.equals("/")){
				if(st2.empty()||st2.peek().equals("+")||st2.peek().equals("-")||st2.peek().equals("(")){
					st2.add(t);
				}
				else if(st2.peek().equals("*")||st2.peek().equals("/")){
					inqt=new String[4];
					inqt[0]=st2.pop();
					inqt[2]=st1.pop();
					inqt[1]=st1.pop();
					inqt[3]="t".concat(String.valueOf(num));
					num++;
					st1.add(inqt[3]);
					qt.add(inqt);
                    st2.add(t);
				}
                //System.out.println(t);
                if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());
					return;
                };
                now++;//*和/只在T1里消除，别的地方遇到都是错的
                F();
                T1();
                return;
            }
            else if(t.equals("+")||t.equals("-")||t.equals("(")||t.equals(")")||t.equals(";")) return;
            else{
                flag=0;
                System.out.println(getTraceInfo());
                return;
            }
            default:
            flag=0;
            System.out.println(getTraceInfo());
            return;
        }    
    }
    
	private void F(){
        if(flag==0) {System.out.println(getTraceInfo());return;}
        String t;
        switch(step[now].substring(1,2)){
            case "i":
            t=i[Integer.parseInt(step[now].substring(3,4))];
            //System.out.println(t);     
            st1.add(t);
            if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());
                    return;
            }
            now++;
            if(step[now].substring(1,2).equals("p")){
                t=p[Integer.parseInt(step[now].substring(3,4))];
                while(t.equals(")")){
                    brackets--;
                    //System.out.println(t);
                    if(brackets<0){
                        flag=0;
                        System.out.println(getTraceInfo());
                        return;
                    }
					while(!st2.peek().equals("(")){
						inqt=new String[4];
						inqt[0]=st2.pop();
						inqt[2]=st1.pop();
						inqt[1]=st1.pop();
						inqt[3]="t".concat(String.valueOf(num));
						num++;
						st1.add(inqt[3]);
						qt.add(inqt);
					}
					st2.pop();
                    if(now==step.length-1) {return;}
                    now++;
                    if(step[now].substring(1,2).equals("p")){t=p[Integer.parseInt(step[now].substring(3,4))];}
                }
            }
            break;
            case "c":
            t=c[Integer.parseInt(step[now].substring(3,4))];
            //System.out.println(t);
			st1.add(t);
            if(now==step.length-1){
                    flag=0;
                    System.out.println(getTraceInfo());
                    return;
            }
            now++;
            if(step[now].substring(1,2).equals("p")){
                t=p[Integer.parseInt(step[now].substring(3,4))];
                while(t.equals(")")){
                    brackets--;
                    //System.out.println(t);
                    if(brackets<0){
                        flag=0;
                        System.out.println(getTraceInfo());
                        return;
                    }
					while(!st2.peek().equals("(")){
						inqt=new String[4];
						inqt[0]=st2.pop();
						inqt[2]=st1.pop();
						inqt[1]=st1.pop();
						inqt[3]="t".concat(String.valueOf(num));
						num++;
						st1.add(inqt[3]);
						qt.add(inqt);
					}
					st2.pop();
                    if(now==step.length-1) {return;}
                    now++;
                    if(step[now].substring(1,2).equals("p")){t=p[Integer.parseInt(step[now].substring(3,4))];}
                }
            }
            break;
            case "p":
            t=p[Integer.parseInt(step[now].substring(3,4))];
            if(t.equals("(")){
                //System.out.println(t);
                if(now==step.length-1) {
                    flag=0;
                    System.out.println(getTraceInfo());
                    return;
                }
                else if(step[now+1].substring(1,2).equals("p")&&p[Integer.parseInt(step[now+1].substring(3,4))].equals(")")){
                    flag=0;
                    System.out.println(getTraceInfo());
                    return;
                }
                now++;
                brackets++;
				st2.add(t);
                E();
                break;
            }
            else{
                flag=0;
                System.out.println(getTraceInfo());
                return;
            }
            default:
            flag=0;
            System.out.println(getTraceInfo());
            return;
        }    
    }
}