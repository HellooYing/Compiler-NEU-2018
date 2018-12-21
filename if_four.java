import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
public class if_four {
	public static String[] step, i, C, S, c, k, p;//输入
    public static List<String[]> qt = new ArrayList<String[]>();//存四元式
    String T1,T2,P;     //保存代表左式的临时变量，保存代表右式的临时变量，保存比较的符号
	public static int n=0, now=0, startn, startw, noww;                 //n:临时变量tn序号，now：token串序号，startn：暂存step_on开始token串位置

	public static void main(String[] args) throws Exception {
		String path_in = "./z.c语言代码输入.txt";
		String path_out = "./z.token序列.txt";

        //这一句执行了我写的分析器程序，它将根据你在"z.if代码.txt"中输入的代码更新"z.token序列.txt"
        
        //这一段从"z.token序列.txt"文件读取了需要用到的输入，即token序列(我把它取名为p)和i, C, S, c, k, p表
		List<List<String>> anal=new analyzer().answer(path_in);
		String[] step, i, C, S, c, k, p;
		int n=0;
		
		i = (String[])anal.get(0).toArray(new String[anal.get(0).size()]);
		C = (String[])anal.get(1).toArray(new String[anal.get(1).size()]);
		S = (String[])anal.get(2).toArray(new String[anal.get(2).size()]);
		c = (String[])anal.get(3).toArray(new String[anal.get(3).size()]);
		k = (String[])anal.get(4).toArray(new String[anal.get(4).size()]);
		p = (String[])anal.get(5).toArray(new String[anal.get(5).size()]);
		step = (String[])anal.get(6).toArray(new String[anal.get(6).size()]);
        //读取完毕

        //下一句执行你写的if语法分析程序
        table tb=new table();
		init(tb);
		List<String[]> r1=new if_four().answer(step, i, C, S, c, k, p, tb);
	}

	
	public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1,table tb) {
		qt.clear();
		step=step1;//token序列
        i=i1;//变量
		C=C1;//字符
		S=S1;//字符串
		c=c1;//数字常量
		k=k1;//关键字
		p=p1;//符号
		
        //把输入传到全局变量中
        //形如p[Integer.parseInt(step[n].substring(3, 4))]的式子可获取token序列中第n个的具体符号，具体请参考下方提示




//重要！：请尽量清晰且详尽的写注释，至少在定义一个新变量时注释明白它将用于何处、起什么作用




		//你应该在这里填充你的代码
		String t;           //Token的值
        String[] step_son;  //各阶段的子token序列
        List<String[]> qtt; //各阶段生成的四元式
        //String T1,T2,P;     //保存代表左式的临时变量，保存代表右式的临时变量，保存比较的符号
        String tn;          //临时变量
        int braceNum=1;     //"{"个数，用来统计while{}的结束

        switch (step[now].substring(1, 2))
        {
        case "k":
            t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            if(t.equals("if"))
            {
                startn=now+2;//暂存左式step_on首Token串的序号
                while(true)
                {
                    now++;
                    switch (step[now].substring(1, 2))
                    {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        break;
                    case "i":
                        break;
                    case "c":
                        break;
                    case "k":
                        break;
                    case "C":
                        break;
                    case "S":
                        break;
                    default:
                        break;
                    }
                    if(t.equals(">=")||t.equals("<=")||t.equals("==")||t.equals("!=")||t.equals("<")||t.equals(">"))
                    {
                        P=t;//暂存比较符号
                        break;
                    }
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                if(step_son.length==1)//判断左式有一个标识符
                {
                    switch (step[now-1].substring(1, 2))
                    {
                    case "i":
                        T1=i[Integer.parseInt(step[now-1].substring(3,step[now-1].length()-1))];
                        break;
                    case "c":
                        T1=c[Integer.parseInt(step[now-1].substring(3,step[now-1].length()-1))];
                        break;
                    default:
                        break;
                    }
                }
                else
                {
                qtt=new exp_four().answer(step_son,i,C,S,c,k,p, tb);
                n=reset_t(qtt,n);           //获得当前临时变量tn的n值
                T1=qtt.get(qtt.size()-1)[3];//暂存比较的左式的临时变量
                for(int j=0; j<qtt.size(); j++)//将比较的左式四元式序列送入四元式区
                {
                    qt.add(qtt.get(j));
                }
                }


                startn=now+1;//暂存右式step_on首Token串的序号
                while(true)
                {
                    now++;
                    switch (step[now].substring(1, 2))
                    {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        break;
                    case "i":
                        break;
                    case "c":
                        break;
                    case "k":
                        break;
                    case "C":
                        break;
                    case "S":
                        break;
                    default:
                        break;
                    }
                    if(t.equals("{"))
                        break;
                }
                step_son=Arrays.copyOfRange(step, startn, now-1);
                if(step_son.length==1)//判断右式有一个标识符
                {
                    switch (step[now-2].substring(1, 2))
                    {
                    case "i":
                        T2=i[Integer.parseInt(step[now-2].substring(3,step[now-2].length()-1))];
                        n=n+1;
                        break;
                    case "c":
                    T2=c[Integer.parseInt(step[now-2].substring(3,step[now-2].length()-1))];
                    n=n+1;
                    break;
                    default:
                        break;
                    }
                }
                else
                {
                qtt=new exp_four().answer(step_son,i,C,S,c,k,p, tb);
                n=reset_t(qtt,n)+1;//获得当前临时变量tn的n值
                T2=qtt.get(qtt.size()-1)[3];//暂存比较的右式的临时变量
                for(int j=0; j<qtt.size(); j++)//将比较的右式四元式序列送入四元式区
                {
                    qt.add(qtt.get(j));
                }
                }
                tn="t".concat(String.valueOf(n));
                addqt(P,T1,T2,tn);     //生成比较四元式
                addqt("if",tn,"_","_");//生成if四元式



                startn=now+1;//暂存if{}内程序开始位置
                t="hh";//强制给t赋予某个值，使其不为“{”，防止下面判断if{}内首个token发生错误
                while(true)
                {
                    now++;
                    switch (step[now].substring(1, 2))
                    {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        break;
                    case "i":
                        break;
                    case "c":
                        break;
                    case "k":
                        break;
                    case "C":
                        break;
                    case "S":
                        break;
                    default:
                        break;
                    }
                    if(t.equals("{"))
                    {
                        braceNum++;
                    }
                    if(t.equals("}"))
                    {
                        braceNum--;
                        if(braceNum==0)
                            break;
                    }
                }
                step_son=Arrays.copyOfRange(step, startn, now);
                qtt=new block().answer(step_son,i,C,S,c,k,p, tb);
                reset_t(qtt,n);
                for(int j=0; j<qtt.size(); j++)
                {
                    qt.add(qtt.get(j));
                }


            now=now+1;
            if(now<step.length)
            {
               t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];//当前字符
			   if(t.equals("else"))
			   {
                braceNum=0;
                n=n+1;//获得当前临时变量tn的n值
                tn="t".concat(String.valueOf(n));
                addqt("es",tn,"_","_");
                startn=now+2;//暂存else{}内程序开始位置
                t="hh";//强制给t赋予某个值，使其不为“{”，防止下面判断else{}内首个token发生错误
                while(true)
                {
                    now++;
                    switch (step[now].substring(1, 2))
                    {
                    case "p":
                        t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        //System.out.println(t);
                        //System.out.println(now);
                        break;
                    case "i":
                    t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    //System.out.println(t);
                   // System.out.println(now);
                        break;
                    case "c":
                    t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    //System.out.println(t);
                    //System.out.println(now);
                        break;
                    case "k":
                    //t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    //System.out.println(t);
                        break;
                    case "C":
                    //t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    //System.out.println(t);
                        break;
                    case "S":
                    //t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    //System.out.println(t);
                        break;
                    default:
                        break;
                    }
                    if(t.equals("{"))
                    {
                        braceNum++;
                    }
                    if(t.equals("}"))
                    {
                        braceNum--;
                        if(braceNum==0)
                            break;
                    }
                }
                step_son=Arrays.copyOfRange(step, startn, now);
                qtt=new block().answer(step_son,i,C,S,c,k,p, tb);
                for(int j=0; j<qtt.size(); j++)
                {
                    qt.add(qtt.get(j));
                }
               }
            }
            n=reset_t(qtt,n)+1;//获得当前临时变量tn的n值
            addqt("ie","_","_","_");
            }
        case "i":
            break;
        case "c":
            break;
        case "p":
            break;
        case "C":
            break;
        case "S":
            break;
        default:
            break;
        }

        

        //如果需要一个全局变量，去上面找qt定义和初始化的地方。
        //比如在那里定义一个now=0来代表目前执行到step[now]这一个token序列中的{？,？}，然后读下一个词的时候now++
		//请注意全局变量在第一次使用前要重新初始化，也就是在answer函数的第一行重新初始化，不然多次调用时会有上一次的结果残留

        


        //步骤大概是，要求实现：if(>/>=/</<=/==/!=){……} else{……}
		//对于大括号里的{……}部分，我写好了针对这种一块代码的类。

		//List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p, tb)将会返回{……}的四元式,因此你只需要关注分支语句就可以。
		//注意 这里的step_son是step的子数组，内容是{……}中不包括{}的……部分的{i,0},{p,1}等等。
		//也就是你需要找到if和else的{和}的位置，然后通过step_son=Arrays.copyOfRange(step, start, end)方法获取中间的……，
		//从而调用block获取{……}的四元式

		//如果按照我的想法，都不需要用递归下降法，
		//就上去先把if(a+b>c+d)直接变成+ a b t1，+ c d t2，> t1 t2 t3,if t3 _ _ ,加到qt里
		//然后把到else之前的全交给List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p, tb)得到它们的四元式,也加到qt里
		//判断有没有else，有就再else一句 (el _ _ _ ),再把剩下的交给new block()，齐活了
		//以上是大的想法

		//具体的操作大概是，先判断"("和">"(或</<=/…）之间的token数量是否超过一个，不超过则说明是if(a>?)这种，左边就不需要多管了
		//如果超过一个，则需要把它变成t1=a+b的形式然后调用List<String[]> qtt=new exp_four().answer(step_son,i,C,S,c,k,p, tb)方式获取t1=a+b的中间代码
		//现在你要做的是获取step_son，这个step_son应该是a+b翻译成token,去step里找到它们然后切出来吧，上面有获取子数组的方法
		//另一个问题是你将会用很多t1 t2这样的中间变量，为了让它们是t1 t2而不是t1 t1 t1重复，你需要定义一个变量n=1来作为t几的那个几
		//得到t几的方法是String tn="t".concat(String.valueOf(n))
		//但exp_four 以及block都只会为你提供t1-tn，因为它们不知道你是用到了t1还是t100
		//为了避免这种冲突，请你在List<String[]> qtt=new block或exp_four().answer(step_son,i,C,S,c,k,p, tb)之后执行reset_t(qtt,n)
		//这是我为了避免这种情况而封装的函数，加入你这里以及把t用到了t10，执行这个函数会让qtt中的t1变成t11，依次类推，从而不会让临时变量重复
		//到了这里，你应该已经得到了a+b部分的四元式，它们存在qtt里。
		//而将在[> t1 t2 t3]这个四元式代表a+b的t1是qtt中最后一个四元式的第三位也就是qtt.get(qtt.size()-1)[3],你应该把它存一下，不然等会qtt被用了就找不到了
		//注：把qtt加进qt的方法就是循环qtt里的所有然后qt.add(qtt[j]);
		//然后对">"与")"之间的c+d重复上面的操作，得到c+d的四元式（因为c+d也可以是c*(10+a)/b*d-100+f，所以你必须通过已有的exp_four来处理它）
		//把a+b对应的四元式和c+d对应的四元式都加到qt里，这时qt应该是[+,a,b,t1] [+,c,d,t2] 
		//如果不是c+d而是c*(10+a)/b*d-100+f，会有很多四元式产生，所以记得下一步[>,t1,t2,t3]的t1 t2是t几很重要
		//然后qt应该是[+,a,b,t1] [+,c,d,t2] [>,t1,t2,t3]。记得printqt();可以输出qt看
		//然后再加上[if,t3,_,_],if就ok了，我觉得你应该也能想出来else怎么写了
		//当然 [+,a,b,t1] [+,c,d,t2] [>,t1,t2,t3] [if,t3,_,_]后面要加上List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p, tb)搞出来的{……}部分四元式，还要记得reset_t(qtt,n)





        



        //一些或许有帮助的提示：


        //输出：你需要在最后return qt;
        //qt中应包含你算出来的所有四元式。
        //qt是最上面已定义的全局变量List<String[]> qt
        //在你获得了四元式的四个元素，如[ > a b t ]时，你可以使用如下操作把它加进qt(我封装了一下）：
        //  addqt(">","a","b","t");
        //你可以使用   printqt();  来输出qt的内容



        

        //在读取{k,2} {p,0} {i,0}……中的一个的时候，你应该
        /*
        String t;
        switch (step[now].substring(1, 2)) {
        case "k":
			t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];//用t获取到了k[2]，"if"，现在t的内容就是"if"
            ……当前字符是关键字如if……操作……
            break;
		case "i":
			t = i[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            ……当前字符是变量名如c……操作……
            break;
		case "c":
			t = c[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            ……当前字符是数字如3.0……操作……
            break;
		case "p":
            t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            ……当前字符是符号如+->=或者==……操作……
            break;
        case "C":

        case "S":

        default:

         */


        //这一段代码是将你生成的四元式写入文件，给老师检查方便
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
        //写入完毕

		return qt;
	}

    static void addqt(String a1,String a2,String a3,String a4){
        String[] in=new String[4];
        in[0]=a1;
        in[1]=a2;
        in[2]=a3;
        in[3]=a4;
        qt.add(in);
    }

    static void printqt(){
        for(String[] a:qt){
			for(String aa:a){
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
    }
	static int reset_t(List<String[]> qtt,int n)
	{//避免临时变量的冲突，在每次往qt加qtt的时候都要重置所有t后面的值
		String[] inqtt;
        int num,max=0;
        for(int j=0; j<qtt.size(); j++)
        {
            inqtt=qtt.get(j);
            for(int jj=1; jj<4; jj++)
            {
                if(inqtt[jj].length()>=2)
                {
                    if(inqtt[jj].substring(0,1).equals("t")&&is_c(inqtt[jj].substring(1)))
                    {
                        num=Double.valueOf(inqtt[jj].substring(1)).intValue();
                        if(max<num)
                            max=num;
                        inqtt[jj]="t".concat(Integer.toString(num+n));
                    }
                }
            }
        }
        return max+n;
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
    static void init(table tb){
		table.func s=tb.new func();
		s.name="test";
		List<String> xctp=new ArrayList<String>();
		xctp.add("int");xctp.add("int");
		List<String> xcname=new ArrayList<String>();
		xcname.add("d");xcname.add("f");
		s.xctp=xctp;
		s.xcname=xcname;
		table.vari v=tb.new vari();
		v.name="d";
		v.tp="int";
		v.ofad=0;
		v.other=-1;
		s.vt.add(v);
		v=tb.new vari();
		v.name="f";
		v.tp="int";
		v.ofad=1;
		v.other=-1;
		s.vt.add(v);
		tb.pfinfl.add(s);

		v=tb.new vari();
		v.name="e";
		v.tp="int";
		v.ofad=0;
		v.other=-1;
		tb.synbl.add(v);

		List<String> vall=new ArrayList<String>();
		vall.add("main");
		vall.add("test");
		tb.vall=vall;
	}
}