
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class analyzer {
	String[] k = { "main", "void", "if", "else", "while", "for", "int", "char", "string" };// 关键字放到数组里
	List<String> i=new ArrayList<String>();//变量名
	List<String> C=new ArrayList<String>();//字符
	List<String> S=new ArrayList<String>();//字符串
	List<Double> c=new ArrayList<Double>();//数字
	List<String> p=new ArrayList<String>();//界符
	// 逻辑是，从头到尾读取String r也就是while(j<r.length)，switch(ascii码)
	// case0，是空格(32)，j++
	// case1，是字母（65-90，97-122），就接着往下读，如果一直没出现符号，就读到空格为止，是一个整体单词。
	// 对这个单词检查是否为关键字在k中，是则{k,n}(n的取值使k[n]==单词)，如果不是则认为它是变量名{i,n}
	// 为此要构建一个list i，每次出现变量名就看看list里有没有它，没有就加进去，使得i[n]==变量名
	// 作为一次结束也要j加到合适的值
	// case2，是数字（48-57），就接着往下读读到空格为止，构建list c，同上操作，{c,n},j++
	// case3，是"（34），往下读到"，构建list S，{S,n},j++
	// case4，是'（39），往下读到'，构建list C，{C,n},j++
	// case5，其他符号(33-47,58-64，91-96，123-126,非34，非39），如果下一个还是其他字符就读俩，否则就它本身一个字符
	// 构建list p，{p,n},j++
	// default，输出来我看看是啥异常情况？j++
	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		new analyzer().answer(path_in);
	}

	public String answer(String path_in) {
		String result="";
		try{
			File filename = new File(path_in);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
			BufferedReader br = new BufferedReader(reader);// 建立一个对象，它把文件内容转成计算机能读懂的语言
			String r = "";
			String line = "";
			int j=0;
			int t,status,jj;
			String word;
			line= br.readLine();
			while (line != null) {
				r =r+" "+ line;
				line=br.readLine();
			}
			br.close();
			while(r.indexOf("/*")!=-1){
				if(r.indexOf("*/")!=-1) r=r.substring(0,r.indexOf("/*")).concat(r.substring(r.indexOf("*/")+2));
				else r=r.substring(r.indexOf("/*"));
			}
			r=r.replaceAll(" {2,}", " ");//删去多余的空格
			if(r.charAt(0)==' ') r=r.substring(1);//删去首部空格
			if(r.charAt(r.length()-1)==' ') r=r.substring(0,r.length()-1);//删去尾部空格

			//System.out.println(r);


			while(j<r.length()){
				t=(int)r.charAt(j);
				if(t==32) status=0;
				else if((t>=65&&t<=90)||(t>=97&&t<=122)) status=1;
				else if(t>=48&&t<=57) status=2;
				else if(t==34) status=3;
				else if(t==39) status=4;
				else if((t>=33&&t<=47&&t!=34&&t!=39)||(t>=58&&t<=64)||(t>=91&&t<=96)||(t>=123&&t<=126)) status=5;
				else {
					System.out.println("status异常");
					System.out.println(r.charAt(j));
					System.out.println((int)r.charAt(j));
					break;
				}

				switch(status){
				case 0://空格
					j++;
					break;

				case 1://字母（单词）
					jj=j;
					if(j!=r.length()){
						t=(int)r.charAt(j);
						while(((t>=65&&t<=90)||(t>=97&&t<=122)||(t>=48&&t<=57))&&j<r.length()-1){
							j++;
							t=(int)r.charAt(j);
							if(j==r.length()-1) {
								if((t>=65&&t<=90)||(t>=97&&t<=122)||(t>=48&&t<=57))
								{
									j=j+1;
								}
							}
						}
						//这个while的意思是如果后面还是字母或数字，这个单词就没有结束，要一直j++找到单词的结尾
					}
					if(jj==j) j++;
					word=r.substring(jj,j);
					int key=0;
					int m;
					for(m=0;m<k.length;m++) {
						if(word.equals(k[m])){
							key=1;
							break;
						}
					}
					if(key==1){
						result=result.concat(" {k,").concat(String.valueOf(m)).concat("}");
					}
					else{
						if(i==null){
							i.add(word);
							result=result.concat(" {i,0}");
						}
						else{
							if(i.contains(word)){
								result=result.concat(" {i,").concat(String.valueOf(i.indexOf(word))).concat("}");
							}
							else{
								i.add(word);
								result=result.concat(" {i,").concat(String.valueOf(i.indexOf(word))).concat("}");
							}
						}
					}
					//System.out.println(word);
					//System.out.println(result);
					break;

				case 2://数字
					jj=j;
					if(j!=r.length()){
						t=(int)r.charAt(j);
						while(((t>=48&&t<=57)||t==46)&&j<r.length()-1){
							j++;
							t=(int)r.charAt(j);
							if(j==r.length()-1) {
								if(t>=48&&t<=57)
								{
									j=j+1;
								}
							}
						}
					}
					if(jj==j) j++;
					word=r.substring(jj,j);
					if(c==null){
						c.add(Double.valueOf(word));
						result=result.concat(" {c,0}");
					}
					else{
						if(c.contains(Double.valueOf(word))){
							result=result.concat(" {c,").concat(String.valueOf(c.indexOf(Double.valueOf(word)))).concat("}");
						}
						else{
							c.add(Double.valueOf(word));
							result=result.concat(" {c,").concat(String.valueOf(c.indexOf(Double.valueOf(word)))).concat("}");
						}
					}
					//System.out.println(word);
					//System.out.println(result);
					break;

				case 3://字符串
					jj=j;
					j++;
					while((int)r.charAt(j)!=34&&j<r.length()-1) j++;
					word=r.substring(jj,j+1);
					if((int)word.charAt(word.length()-1)!=34) System.out.println("双引号不成对错误");
					if(C==null){
						C.add(word);
						result=result.concat(" {C,0}");
					}
					else{
						if(C.contains(word)){
							result=result.concat(" {C,").concat(String.valueOf(S.indexOf(word))).concat("}");
						}
						else{
							S.add(word);
							result=result.concat(" {S,").concat(String.valueOf(S.indexOf(word))).concat("}");
						}
					}
					//System.out.println(word);
					//System.out.println(result);
					j++;
					break;

				case 4://字符
					jj=j;
					j++;
					while((int)r.charAt(j)!=39&&j<r.length()-1) j++;
					word=r.substring(jj,j+1);
					if((int)word.charAt(word.length()-1)!=39) System.out.println("单引号不成对错误");
					if(C==null){
						C.add(word);
						result=result.concat(" {C,0}");
					}
					else{
						if(C.contains(word)){
							result=result.concat(" {C,").concat(String.valueOf(C.indexOf(word))).concat("}");
						}
						else{
							C.add(word);
							result=result.concat(" {C,").concat(String.valueOf(C.indexOf(word))).concat("}");
						}
					}
					//System.out.println(word);
					//System.out.println(result);
					j++;
					break;

				case 5://符号
					word=r.substring(j,j+1);
					if(j+1<r.length()){
						t=(int)r.charAt(j+1);
						if((t>=33&&t<=47&&t!=34&&t!=39)||(t>=58&&t<=64)||(t>=91&&t<=96)||(t>=123&&t<=126))
						{
							if((r.charAt(j)=='&'&&r.charAt(j+1)=='&')||(r.charAt(j)=='|'&&r.charAt(j+1)=='|')||(r.charAt(j)=='>'&&r.charAt(j+1)=='=')||(r.charAt(j)=='<'&&r.charAt(j+1)=='=')||(r.charAt(j)=='='&&r.charAt(j+1)=='='))
							{
								word=r.substring(j,j+2);
								j++;
							}
						}
					}
					if(p==null){
						p.add(word);
						result=result.concat(" {p,0}");
					}
					else{
						if(p.contains(word)){
							result=result.concat(" {p,").concat(String.valueOf(p.indexOf(word))).concat("}");
						}
						else{
							p.add(word);
							result=result.concat(" {p,").concat(String.valueOf(p.indexOf(word))).concat("}");
						}
					}
					//System.out.println(word);
					//System.out.println(result);
					j++;
					break;
				}
			}
			String show_i,show_C,show_S,show_c,show_k,show_p;
			show_i="i:".concat(i.toString());
			show_C="C:".concat(C.toString());
			show_S="S:".concat(S.toString());
			show_c="c:".concat(c.toString());
			show_k="k:[main, void, if, else, while, for, int, char, string]";
			show_p="p:".concat(p.toString());
			File writename = new File("./z.token序列.txt"); // 如果没有则新建一个新的path_out的txt文件
			writename.createNewFile(); // 创建新文件
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			result=result.concat("\n").concat(show_i).concat("\n").concat(show_C).concat("\n").concat(show_S).concat("\n").concat(show_c).concat("\n").concat(show_k).concat("\n").concat(show_p);
			out.write(result); // 写入
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
		}catch (Exception e) {
			e.printStackTrace();
		}  
		return result;    
	}
}