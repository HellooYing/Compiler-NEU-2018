package cp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
public class switch_four {
	public static String[] step, i, C, S, c, k, p;//����
    public static List<String[]> qt = new ArrayList<String[]>();//����Ԫʽ
    String T1,T2,P;     //���������ʽ����ʱ���������������ʽ����ʱ����������Ƚϵķ���
	public static int n=0, now=0, startn, startl, noww;                 //n:��ʱ����tn��ţ�now��token����ţ�startn��switch����λ�ã� starl��case��ʽ����ʼλ��

	public static void main(String[] args) throws Exception {
		String path_in = "./z.c���Դ�������.txt";
		String path_out = "./z.token����.txt";

        //��һ��ִ������д�ķ���������������������"z.if����.txt"������Ĵ������"z.token����.txt"
        
        //��һ�δ�"z.token����.txt"�ļ���ȡ����Ҫ�õ������룬��token����(�Ұ���ȡ��Ϊp)��i, C, S, c, k, p��
		List<List<String>> anal=new analyzer().answer(path_in,path_out);
		String[] step, i, C, S, c, k, p;
		int n=0;
		
		i = (String[])anal.get(0).toArray(new String[anal.get(0).size()]);
		C = (String[])anal.get(1).toArray(new String[anal.get(1).size()]);
		S = (String[])anal.get(2).toArray(new String[anal.get(2).size()]);
		c = (String[])anal.get(3).toArray(new String[anal.get(3).size()]);
		k = (String[])anal.get(4).toArray(new String[anal.get(4).size()]);
		p = (String[])anal.get(5).toArray(new String[anal.get(5).size()]);
		step = (String[])anal.get(6).toArray(new String[anal.get(6).size()]);
        //��ȡ���

        //��һ��ִ����д��if�﷨��������
        table tb=new table();
		init(tb);
		List<String[]> r1=new if_four().answer(step, i, C, S, c, k, p, tb);
	}

	
	public List<String[]> answer(String[] step1, String[] i1, String[] C1, String[] S1, String[] c1, String[] k1, String[] p1,table tb) {
		qt.clear();
		step=step1;//token����
        i=i1;//����
		C=C1;//�ַ�
		S=S1;//�ַ���
		c=c1;//���ֳ���
		k=k1;//�ؼ���
        p=p1;//����
        



        String t,ll;           //Token��ֵ��case���ֵ
        String[] step_son;  //���׶ε���token����
        List<String[]> qtt; //���׶����ɵ���Ԫʽ
        String tn;          //��ʱ����
        
        
        
        switch (step[now].substring(1, 2))
        {
            case "k":
            t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            if(t.equals("switch"))
            {
                now=now+2;//����switch��ı���
                t = i[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                addqt("sw",t,"_","_");//����switch��Ԫʽ
                now=now+2;
                while(true)
                {
                    now++;
                    t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    if(t.equals("case"))
                    {
                        now=now+1;
                        t = c[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        addqt("cs",t,"_","_");//����case��Ԫʽ
                        now=now+2;
                        startl=now;//��case����ʼλ��
                        t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        switch(t)
                        {
                            case "break":
                            addqt("sbk","_","_","_");//����break��Ԫʽ
                            now=now+1;
                            break;
                            case "default":
                            now=now-1;
                            break;
                            case "case":
                            now=now-1;
                            break;
                            default:
                            {
                                while(!t.equals("break"))
                                {
                                    now++;
                                    t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                                }
                                step_son=Arrays.copyOfRange(step, startl, now);
                                qtt=new block().answer(step_son,i,C,S,c,k,p, tb);
                                for(int j=0; j<qtt.size(); j++)
                                {
                                    qt.add(qtt.get(j));
                                }
                                addqt("sbk","_","_","_");//����break��Ԫʽ
                            }
                            break;
                        }
                    }
                    

                    t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                    if(t.equals("default"))
                    {
                        addqt("dft","_","_","_");//����defult��Ԫʽ
                        now=now+2;
                        startl=now;//��defult����ʼλ��
                        t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                        switch(t)
                        {
                            case "break":
                            addqt("sbk","_","_","_");//����break��Ԫʽ
                            break;
                            case "case":
                            now=now-1;
                            break;
                            default:
                            {
                                while(!t.equals("break"))
                                {
                                    now++;
                                    t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
                                }
                                //now=now-1;
                                //System.out.println(now);
                                step_son=Arrays.copyOfRange(step, startl, now);
                                qtt=new block().answer(step_son,i,C,S,c,k,p, tb);
                                for(int j=0; j<qtt.size(); j++)
                                {
                                    qt.add(qtt.get(j));
                                }
                                //now=now+1;
                                addqt("sbk","_","_","_");//����break��Ԫʽ
                            }
                            break;
                        }
                    }
                    if(now==step.length-1)
                    {
                        break;
                    }
                }
            }
        }





        //
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
			File writename = new File("./z.��Ԫʽ.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
        //д�����

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
	{//������ʱ�����ĳ�ͻ����ÿ����qt��qtt��ʱ��Ҫ��������t�����ֵ
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