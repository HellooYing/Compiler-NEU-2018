package cp;
import java.util.*;

public class table {
    class vari {// ȫ�ֱ�����Ϣ
        String name;// ������
        String tp;// ���ͣ�int int[] function char string
        int ofad;// ƫ�Ƶ�ַ
        int other;// ��������Ϊ��������ָ�������е�ĳһ����������Ϊ���飬�������鳤�ȣ�
    }

    class func {// ������Ϣ
        String name;// ������
        List<String> xctp = new ArrayList<String>();// �β�����
        List<String> xcname = new ArrayList<String>();// �β���
        List<vari> vt = new ArrayList<vari>();// �����е���ʱ����
    }

    List<vari> synbl = new ArrayList<vari>();// ȫ�ֱ����ܱ�
    List<func> pfinfl = new ArrayList<func>();// ������
    List<String> vall = new ArrayList<String>();// ���¼����¼��ǰλ�ã����Ӻ���������������������������ʱ��������ȫ�ֱ���

    void print(table tb) {
        String result = "\n\n�ܱ�\n";
        for (int j = 0; j < tb.synbl.size(); j++) {
            table.vari tv = tb.synbl.get(j);
            result = result.concat("��������").concat(tv.name).concat(" ���ͣ�").concat(tv.tp).concat(" ƫ�Ƶ�ַ��")
                    .concat(String.valueOf(tv.ofad)).concat(" ������").concat(String.valueOf(tv.other)).concat("\n");
        }
        result = result.concat("\n\n������\n");
        for (int j = 0; j < tb.pfinfl.size(); j++) {
            table.func tf = tb.pfinfl.get(j);
            List<String> xctp = tf.xctp;
            List<String> xcname = tf.xcname;
            List<vari> vt;
            result = result.concat("\n��������").concat(tf.name).concat("\n").concat("�βΣ�").concat("\n");
            for (int jj = 0; jj < xctp.size(); jj++) {
                result = result.concat(xctp.get(jj)).concat(" ").concat(xcname.get(jj)).concat("\n");
            }
            result = result.concat("�����ľֲ�������\n");
            vt = tf.vt;
            for (int jj = 0; jj < vt.size(); jj++) {
                result = result.concat("��������").concat(vt.get(jj).name).concat(" ���ͣ�").concat(vt.get(jj).tp)
                        .concat(" ƫ�Ƶ�ַ��").concat(String.valueOf(vt.get(jj).ofad)).concat(" ������")
                        .concat(String.valueOf(vt.get(jj).other)).concat("\n");
            }
        }
        result = result.concat("\n\n���¼��");
        for (int jj = 0; jj < vall.size(); jj++) {
            result = result.concat(vall.get(jj)).concat(" ");
        }
        result = result.concat("\n");
        System.out.print(result);
    }
}