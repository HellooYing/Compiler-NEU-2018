import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class backend {
    public static void main(String[] args) throws Exception {
        String path_in = "./in.txt";
        String path_out = "./out.txt";
        String path_out2 = "./out2.txt";
        String path_out3 = "./out3.txt";
        new analyzer().answer(path_in, path_out);
        new four().answer(path_out, path_out2);
        new backend().answer(path_out2, path_out3);
    }
    public void answer(String path_in, String path_out) {
        String tt;
        String[] t={},ttt;
        String[][] qt;
        int num=0;
        try {
            File filename = new File(path_in);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            t = line.split(" ");
            line = br.readLine();
            num = Integer.valueOf(line);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num==0) return;
        qt=new String[num][4];
        for(int j=0;j<num;j++){
            tt=t[j].substring(1,t[j].length()-1);
            ttt=tt.split(",");
            qt[j][0]=ttt[0];
            qt[j][1]=ttt[1];
            qt[j][2]=ttt[2];
            qt[j][3]=ttt[3];
        }
    }
}