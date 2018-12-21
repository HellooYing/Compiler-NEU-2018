import java.util.*;
public class test{
    public static void main(String[] args) throws Exception{
        // table t=new table();
        // table.vari s=t.new vari();
        // s.name="?";
        // t.synbl=new ArrayList<table.vari>();
        // t.synbl.add(s);
        // System.out.println(t.synbl.get(0).name);
        List<String[]> qtt=new ArrayList<String[]>();
         String[] inqtt={"if","t6","_","_"};
         qtt.add(inqtt);
         String[] inqtt1={"=","8.0","_","a"};
         qtt.add(inqtt1);
         String[] inqtt2={"=","a","_","b"};
         qtt.add(inqtt2);
         qtt=new optimization().answer(qtt);
        printLS(qtt);
    }
    static void printLS(List<String[]> r) {
		for (String[] a : r) {
			for (String aa : a) {
				System.out.print(aa);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
    static boolean is_c(String c) {
		try {
			Double.valueOf(c);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
    static int reset_t(List<String[]> qtt, int n) {// 避免临时变量的冲突，在每次往qt加qtt的时候都要重置所有t后面的值
		String[] inqtt;
		int num, max = 0;
		for (int j = 0; j < qtt.size(); j++) {
			inqtt = qtt.get(j);
			for (int jj = 1; jj < 4; jj++) {
				if (inqtt[jj].length() >= 2) {
                    System.out.print(inqtt[jj].substring(0, 1).equals("t"));
                    System.out.print(is_c(inqtt[jj].substring(1)));
                    System.out.println();
					if (inqtt[jj].substring(0, 1).equals("t") && is_c(inqtt[jj].substring(1))) {
                        
						num = Double.valueOf(inqtt[jj].substring(1)).intValue();
						if (max < num)
							max = num;
						inqtt[jj] = "t".concat(Integer.toString(num + n));
					}
				}
			}
		}
		return max + n;
	}
}