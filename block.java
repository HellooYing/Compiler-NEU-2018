import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
public class block{
	public static void main(String[] args) throws Exception{
		String path_in = "./z.c语言代码输入.txt";
		String anal=new analyzer().answer(path_in);
		String[] t=anal.split("\n");
		String[] step, i, C, S, c, k, p;
		
		int n=0;
		String line=t[n];
		step = line.substring(1).split(" ");
		n++;
		line=t[n];
		i = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		C = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		S = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		c = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		k = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		n++;
		line=t[n];
		p = line.substring(3, line.length() - 1).replace(" ", "").split(",");
		new block().answer(step,i,C,S,c,k,p);
	}
	public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1) {
		String[] step, i, C, S, c, k, p;
		step=step1;
		i=i1;
		C=C1;
		S=S1;
		c=c1;
		k=k1;
		p=p1;
		int bracket,n;
		List<String[]> qt=new ArrayList<String[]>();
		List<String[]> qtt=new ArrayList<String[]>();//加入qt之前要检查临时变量t几是否有重复，所以要临时放一下改完再加进去
		List<String> code=new ArrayList<String>();
		List<String[]> sentence=new ArrayList<String[]>();
		//对于一个块，要知道step几-几是引用了分支或循环结构生成的四元式，几-几是引用了函数生成的四元式
		//比如说0-12是赋值运算直接一句一句的用exp_four生成code，13-50是if{}else的结构用的是if_four生成code

		for(int j=0;j<step.length;j++){
			//先找if
			if(step[j].substring(1, 2).equals("k")){//是关键字
			
				if(k[Integer.parseInt(step[j].substring(3, 4))].equals("if")){//是if
				
					int jj;
					for(jj=j+1;jj<step.length;jj++){//找到if{}的前大括号
						if(step[jj].substring(1, 2).equals("p")){
							if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("{")) break;
						}
					}
					bracket=1;
					while(bracket>0){//找到跟之前找到的那个前大括号匹配的后大括号
						jj++;
						if(step[jj].substring(1, 2).equals("p")){
							if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("{")) bracket++;
							else if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("}")) bracket--;
						}
					}
					if(jj+1<step.length&&step[jj+1].substring(1, 2).equals("k")){//如果后大括号紧跟着的是关键字else
						if(k[Integer.parseInt(step[jj+1].substring(3, 4))].equals("else")){
							for(jj=jj+1;jj<step.length;jj++){//找else的前大括号
								if(step[jj].substring(1, 2).equals("p")){
									if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("{")) break;
								}
							}
							bracket=1;
							while(bracket>0){//找else的后大括号
								jj++;
								if(step[jj].substring(1, 2).equals("p")){
									if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("{")) bracket++;
									else if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("}")) bracket--;
								}
							}
						}
					}
					sentence.add(Arrays.copyOfRange(step, j, jj+1));//sentence把整个if的一大句话加进去
					j=jj;//开始找下面的
				}
				//再找while
				else if(k[Integer.parseInt(step[j].substring(3, 4))].equals("while")){

					int jj;
					for(jj=j+1;jj<step.length;jj++){//找到while{}的前大括号
						if(step[jj].substring(1, 2).equals("p")){
							if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("{")) break;
						}
					}
					bracket=1;
					while(bracket>0){//找到跟之前找到的那个前大括号匹配的后大括号
						jj++;
						if(step[jj].substring(1, 2).equals("p")){
							if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("{")) bracket++;
							else if(p[Integer.parseInt(step[jj].substring(3, 4))].equals("}")) bracket--;
						}
					}
					sentence.add(Arrays.copyOfRange(step, j, jj+1));//sentence把整个while的一大句话加进去
					j=jj;//开始找下面的
				}
			}
			//else if(是i 在function列表里)
			else if(step[j].substring(1, 2).equals("p")){
				if(p[Integer.parseInt(step[j].substring(3, 4))].equals("=")){
					int jj;
					for(jj=j+1;jj<step.length;jj++){
						if(step[jj].substring(1, 2).equals("p")){
							if(p[Integer.parseInt(step[jj].substring(3, 4))].equals(";")) break;
						}
					}
					sentence.add(Arrays.copyOfRange(step, j-1, jj+1));
					j=jj;
				}
			}
		}
		printLS(sentence);

		int l=0;
		for(String[] s:sentence) l+=s.length;
		if(l!=step.length) System.out.println("出现了未知的语句，请检查输入代码是否正确");//检查是否所有单词都有句子可加，没有就说明有问题

		n=0;
		for(int j=0;j<sentence.size();j++){
			if(sentence.get(j)[0].substring(1, 2).equals("k")){
				if(k[Integer.parseInt(sentence.get(j)[0].substring(3, 4))].equals("if")){
					qtt=new if_four().answer(sentence.get(j), i, C, S, c, k, p);
					reset_t(qtt,n);
					qt.addAll(qtt);
					int m=0;
					for(int jj=1;jj<4;jj++){
						if(qtt.get(qtt.size()-1)[jj].length()>=2){
							if(qtt.get(qtt.size()-1)[jj].substring(0,1).equals("t")&&is_c(qtt.get(qtt.size()-1)[jj].substring(1))){
								if(Double.valueOf(qtt.get(qtt.size()-1)[jj].substring(1))>m) 
									m=Double.valueOf(qtt.get(qtt.size()-1)[jj].substring(1)).intValue();
							}
						}
					}
					n=n+m;
				}
				else if(k[Integer.parseInt(sentence.get(j)[0].substring(3, 4))].equals("while")){
					qtt=new while_four().answer(sentence.get(j), i, C, S, c, k, p);
					reset_t(qtt,n);
					qt.addAll(qtt);
					int m=0;
					for(int jj=1;jj<4;jj++){
						if(qtt.get(qtt.size()-1)[jj].length()>=2){
							if(qtt.get(qtt.size()-1)[jj].substring(0,1).equals("t")&&is_c(qtt.get(qtt.size()-1)[jj].substring(1))){
								if(Double.valueOf(qtt.get(qtt.size()-1)[jj].substring(1))>m) 
									m=Double.valueOf(qtt.get(qtt.size()-1)[jj].substring(1)).intValue();
							}
						}
					}
					n=n+m;
				}
			}
			
			else if(sentence.get(j)[1].substring(1, 2).equals("p")){
				if(p[Integer.parseInt(sentence.get(j)[1].substring(3, 4))].equals("=")){
					qtt=new exp_four().answer(sentence.get(j), i, C, S, c, k, p);
					reset_t(qtt,n);
					for(String[] s:qtt){
						qt.add(s);
					}
					int m=0;
					for(int jj=1;jj<4;jj++){
						if(qtt.get(qtt.size()-1)[jj].length()>=2){
							if(qtt.get(qtt.size()-1)[jj].substring(0,1).equals("t")&&is_c(qtt.get(qtt.size()-1)[jj].substring(1))){
								if(Double.valueOf(qtt.get(qtt.size()-1)[jj].substring(1))>m) 
									m=Double.valueOf(qtt.get(qtt.size()-1)[jj].substring(1)).intValue();
							}
						}
					}
					n=n+m;//这里我觉得应该是n+m+1，但是实验是n+m比较好看
				}
			}
		}
		
		printLS(qt);
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
        
		return qt;
		// List<String[]> qt = new exp_four().answer(step, i, C, S, c, k, p);
		// qt = new optimization().answer(qt);
		// List<String> code = new object_code().answer(qt);
		// for(String s:code) System.out.println(s);
	}
	
	static boolean is_c(String c){
		try{
			Double.valueOf(c);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	static int reset_t(List<String[]> qtt,int n){//避免临时变量的冲突，在每次往qt加qtt的时候都要重置所有t后面的值
		String[] inqtt;
		int num,max=0;
		for(int j=0;j<qtt.size();j++){
			inqtt=qtt.get(j);
			for(int jj=1;jj<4;jj++){
				if(inqtt[jj].length()>=2){
					if(inqtt[jj].substring(0,1).equals("t")&&is_c(inqtt[jj].substring(1))){
						num=Double.valueOf(inqtt[jj].substring(1)).intValue();
						if(max<num) max=num;
						inqtt[jj]="t".concat(Integer.toString(num+n));
					}
				}
			}
		}
		return max+n;
	}
	static void printLS(List<String[]> r){
		for(String[] a:r){
			for(String aa:a){
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}

