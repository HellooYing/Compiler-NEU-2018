import java.util.*;
public class Main{
    static void printLS(List<String[]> r){
        for(String[] a:r){
            for(String aa:a){
                System.out.print(aa);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
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
        List<String[]> qt = new exp_four().answer(step, i, C, S, c, k, p);
        qt = new optimization().answer(qt);
        List<String> code = new object_code().answer(qt);
        for(String s:code) System.out.println(s);
    }
}

