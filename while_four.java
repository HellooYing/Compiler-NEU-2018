import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
public class while_four {
	public static String[] step, i, C, S, c, k, p;//输入
	public static List<String[]> qt = new ArrayList<String[]>();//存四元式

	public static void main(String[] args) throws Exception {
		String path_in = "./z.while代码.txt";
		String path_out = "./z.token序列.txt";

        //这一句执行了我写的分析器程序，它将根据你在"z.while代码.txt"中输入的代码更新"z.token序列.txt"
		new analyzer().answer(path_in);
        
        //这一段从"z.token序列.txt"文件读取了需要用到的输入，即token序列(我把它取名为step)和i, C, S, c, k, p表
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
        //读取完毕

        //下一句执行你写的if语法分析程序
		List<String[]> r1=new while_four().answer(step, i, C, S, c, k, p);
	}

	
	public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1) {
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
        //如：第一个递归函数;


        

        //如果需要一个全局变量，去上面找qt定义和初始化的地方。
        //比如在那里定义一个now=0来代表目前执行到step[now]这一个token序列中的{？,？}，然后读下一个词的时候now++


		//要求：while(表达式 </>/>=/<=/==/!= 表达式) {……}
        


        //具体就是把while(a+b>c+d){……}变成[+,a,b,t1] [+,c,d,t2] [<,t1,t2,t3] [wh,t3 _,_ ] ……
		//对于大括号里的{……}部分，我写好了针对这种一块代码的类。
		//List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p)将会返回{……}的四元式,因此你只需要关注循环语句本身就可以。
		//注意 这里的step_son是step的子数组，内容是{……}中不包括{}的……部分的{i,0},{p,1}等等。
		//也就是你需要找到while的{和}的位置，然后通过step_son=Arrays.copyOfRange(step, start, end)方法获取中间的token们，
		//从而调用block获取{……}部分的四元式


		//步骤：
		//先判断"("和">"(或</<=/…）之间的token数量是否超过一个，不超过则说明是while(a>?)这种，左边就不需要多管了
		//如果超过一个，则需要把它变成t1=a+b的形式然后调用List<String[]> qtt=new exp_four().answer(step_son,i,C,S,c,k,p)方式获取t1=a+b的中间代码
		//现在你要做的是获取step_son，这个step_son应该是a+b翻译成token,去step里找到它们然后切出来吧，上面有获取子数组的方法
		//另一个问题是你将会用很多t1 t2这样的中间变量，为了让它们是t1 t2而不是t1 t1 t1重复，你需要定义一个变量n=1来作为t几的那个几
		//得到t几的方法是String tn="t".concat(String.valueOf(n))
		//但exp_four 以及block都只会为你提供t1-tn，因为它们不知道你是用到了t1还是t100
		//为了避免这种冲突，请你在List<String[]> qtt=new block或exp_four().answer(step_son,i,C,S,c,k,p)之后执行reset_t(qtt,n)
		//这是我为了避免这种情况而封装的函数，加入你这里以及把t用到了t10，执行这个函数会让qtt中的t1变成t11，依次类推，从而不会让临时变量重复
		//到了这里，你应该已经得到了a+b部分的四元式，它们存在qtt里。
		//而将在[> t1 t2 t3]这个四元式代表a+b的t1是qtt中最后一个四元式的第三位也就是qtt.get(qtt.size()-1)[3],你应该把它存一下，不然等会qtt被用了就找不到了
		//注：把qtt加进qt的方法就是循环qtt里的所有然后qt.add(qtt[j]),addAll方法会出现奇怪的bug;
		//然后对">"与")"之间的c+d重复上面的操作，得到c+d的四元式（因为c+d也可以是c*(10+a)/b*d-100+f，所以你必须通过已有的exp_four来处理它）
		//把a+b对应的四元式和c+d对应的四元式都加到qt里，这时qt应该是[+,a,b,t1] [+,c,d,t2] 
		//如果不是c+d而是c*(10+a)/b*d-100+f，会有很多四元式产生，所以记得下一步[>,t1,t2,t3]的t1 t2是t几很重要
		//然后qt应该是[+,a,b,t1] [+,c,d,t2] [>,t1,t2,t3]。记得printqt();可以输出qt看
		//然后再加上[wh,t3,_,_],while就ok了
		//当然 [+,a,b,t1] [+,c,d,t2] [>,t1,t2,t3] [wh,t3,_,_]后面要加上List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p)搞出来的{……}部分四元式，还要记得reset_t(qtt,n)
		//可以先写不支持break的，等我想想break该怎么处理



        



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
			t = k[Integer.parseInt(step[now].substring(3, 4))];//用t获取到了k[2]，"if"，现在t的内容就是"if"
            ……当前字符是关键字如if……操作……
            break;
		case "i":
			t = i[Integer.parseInt(step[now].substring(3, 4))];
            ……当前字符是变量名如c……操作……
            break;
		case "c":
			t = c[Integer.parseInt(step[now].substring(3, 4))];
            ……当前字符是数字如3.0……操作……
            break;
		case "p":
            t = p[Integer.parseInt(step[now].substring(3, 4))];
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
	static void reset_t(List<String[]> qtt,int n){//避免临时变量的冲突，在每次往qt加qtt的时候都要重置所有t后面的值
		String[] inqtt;
		for(int j=0;j<qtt.size();j++){
			inqtt=qtt.get(j);
			for(int jj=1;jj<4;jj++){
				if(inqtt[jj].length()>=2){
					if(inqtt[jj].substring(0,1).equals("t")&&is_c(inqtt[jj].substring(1))){
						inqtt[jj]="t".concat(Integer.toString(Double.valueOf(inqtt[jj].substring(1)).intValue()+n));
					}
				}
			}
		}
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
}