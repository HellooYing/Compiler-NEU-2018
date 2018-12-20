import java.util.*;

public class table {
    class vari {// 全局变量信息
        String name;// 变量名
        String tp;// 类型：int int[] function char string
        int ofad;// 偏移地址
        int other;// （如类型为函数，则指向函数表中的某一个。如类型为数组，则存放数组长度）
    }

    class func {// 函数信息
        String name;// 函数名
        List<String> xctp = new ArrayList<String>();// 形参类型
        List<String> xcname = new ArrayList<String>();// 形参名
        // 在函数初始化的时候，要注意把形参加入临时变量，否则没地方放- -
        List<vari> vt = new ArrayList<vari>();// 函数中的临时变量
    }

    List<vari> synbl = new ArrayList<vari>();// 全局变量总表
    List<func> pfinfl = new ArrayList<func>();// 函数表
    List<String> vall = new ArrayList<String>();// 活动记录，记录当前位置，是子函数还是主函数，来决定调用临时变量还是全局变量

    void print(table tb) {
        String result = "总表：\n";
        for (int j = 0; j < tb.synbl.size(); j++) {
            table.vari tv = tb.synbl.get(j);
            result = result.concat("变量名：").concat(tv.name).concat(" 类型：").concat(tv.tp).concat(" 偏移地址：")
                    .concat(String.valueOf(tv.ofad)).concat(" 其他：").concat(String.valueOf(tv.other)).concat("\n");
        }
        result = result.concat("\n\n函数表：\n");
        for (int j = 0; j < tb.pfinfl.size(); j++) {
            table.func tf = tb.pfinfl.get(j);
            List<String> xctp = tf.xctp;
            List<String> xcname = tf.xcname;
            List<vari> vt;
            result = result.concat("函数名：").concat(tf.name).concat("\n").concat("形参：").concat("\n");
            for (int jj = 0; jj < xctp.size(); jj++) {
                result = result.concat(xctp.get(jj)).concat(" ").concat(xcname.get(jj)).concat("\n");
            }
            result = result.concat("函数的局部变量：\n");
            vt = tf.vt;
            for (int jj = 0; jj < vt.size(); jj++) {
                result = result.concat("变量名：").concat(vt.get(jj).name).concat(" 类型：").concat(vt.get(jj).tp)
                        .concat(" 偏移地址：").concat(String.valueOf(vt.get(jj).ofad)).concat(" 其他：")
                        .concat(String.valueOf(vt.get(jj).other)).concat("\n");
            }
            result = result.concat("\n\n活动记录：");
            for (int jj = 0; jj < vall.size(); jj++) {
                result = result.concat(vall.get(jj)).concat(" ");
            }
        }
        System.out.print(result);
    }
}