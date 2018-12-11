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

	public static void main(String[] args) throws Exception {
		String path_in = "./z.if代码.txt";
		String path_out = "./z.token序列.txt";

        //这一句执行了我写的分析器程序，它将根据你在"z.if代码.txt"中输入的代码更新"z.token序列.txt"
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
		List<String[]> r1=new if_four().answer(step, i, C, S, c, k, p);
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
        


        //步骤大概是，上网查一个条件语句if(>/>=/</<=/==){} else{}的文法




        



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
}