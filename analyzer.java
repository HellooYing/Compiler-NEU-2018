
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class analyzer {
    String[] k={"main","void","if","else","while","for","int","char","string"};//关键字放到数组里
    
    //逻辑是，从头到尾读取String r也就是while(j<r.length)，switch(ascii码)
    //case0，是空格(32)，j++
    //case1，是字母（65-90，97-122），就接着往下读，如果一直没出现符号，就读到空格为止，是一个整体单词。
    //对这个单词检查是否为关键字在k中，是则{k,n}(n的取值使k[n]==单词)，如果不是则认为它是变量名{i,n}
    //为此要构建一个list i，每次出现变量名就看看list里有没有它，没有就加进去，使得i[n]==变量名
    //作为一次结束也要j加到合适的值
    //case2，是数字（48-57），就接着往下读读到空格为止，构建list c，同上操作，{c,n},j++
    //case3，是"（34），往下读到"，构建list S，{S,n},j++
    //case4，是'（39），往下读到'，构建list C，{C,n},j++
    //case5，其他符号(33-47,58-64，91-96，123-126,非34，非39），如果下一个还是其他字符就读俩，否则就它本身一个字符
    //构建list p，{p,n},j++
    //default，输出来我看看是啥异常情况？j++
    public static void main(String[] args) throws Exception {
        String path_in = "./in.txt";
        String path_out = "./out.txt";
        new analyzer().main(path_in, path_out);
    }

    public void main(String path_in, String path_out) {
        try{
            File filename = new File(path_in);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);// 建立一个对象，它把文件内容转成计算机能读懂的语言
            String r = "";
            String line = "";
            line= br.readLine();
            while (line != null) {
                r =r+" "+ line;
                line=br.readLine();
            }
            br.close();
            r=r.replaceAll(" {2,}", " ");
            System.out.println((int)r.charAt(1));
            int j=0;
            int t,status;
            while(j<r.length()){
                System.out.println(j);
                t=(int)r.charAt(j);
                if(t==32) status=0;
                else if((t>=65&&t<=90)||(t>=97&&t<=122)) status=1;
                else if(t>=48&&t<=57) status=2;
                else if(t==34) status=3;
                else if(t==39) status=4;
                else if((t>=33&&t<=47&&t!=34&&t!=39)||(t>=58&&t<=64)||(t>=123&&t<=126)) status=5;
                else {
                    System.out.println(r.charAt(j));
                    break;
                }
                switch(status){
                    case 0:
                    j++;
                    case 1:
                    int jj=j+1;
                    t=(int)r.charAt(jj);
                    while(((t>=65&&t<=90)||(t>=97&&t<=122)||(t>=48&&t<=57))&&jj<r.length()-1){
                        jj++;
                        t=(int)r.charAt(jj);
                    }
                    //这个while的意思是如果后面还是字母或数字，这个单词就没有结束，要一直jj++找到单词的结尾
                    String word=r.substring(j,jj+1);
                    System.out.println(word);
                    j=jj+1;
                }
            }


            File writename = new File(path_out); // 如果没有则新建一个新的path_out的txt文件
			writename.createNewFile(); // 创建新文件
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(r); // 写入
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
        }catch (Exception e) {
			e.printStackTrace();
		}      
    }
}