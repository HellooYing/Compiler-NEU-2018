package cp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
public class if_four {
	public static String[] step, i, C, S, c, k, p;//����
    public static List<String[]> qt = new ArrayList<String[]>();//����Ԫʽ
    String T1,T2,P;     //���������ʽ����ʱ���������������ʽ����ʱ����������Ƚϵķ���
	public static int n=0, now=0, startn, startw, noww;                 //n:��ʱ����tn��ţ�now��token����ţ�startn���ݴ�step_on��ʼtoken��λ��

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
		
        //�����봫��ȫ�ֱ�����
        //����p[Integer.parseInt(step[n].substring(3, 4))]��ʽ�ӿɻ�ȡtoken�����е�n���ľ�����ţ�������ο��·���ʾ




//��Ҫ�����뾡���������꾡��дע�ͣ������ڶ���һ���±���ʱע�������������ںδ�����ʲô����




		//��Ӧ�������������Ĵ���
		String t;           //Token��ֵ
        String[] step_son;  //���׶ε���token����
        List<String[]> qtt; //���׶����ɵ���Ԫʽ
        //String T1,T2,P;     //���������ʽ����ʱ���������������ʽ����ʱ����������Ƚϵķ���
        String tn;          //��ʱ����
        int braceNum=1;     //"{"����������ͳ��while{}�Ľ���

        switch (step[now].substring(1, 2))
        {
        case "k":
            t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            if(t.equals("if"))
            {
                startn=now+2;//�ݴ���ʽstep_on��Token�������
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
                        P=t;//�ݴ�ȽϷ���
                        break;
                    }
                }
                step_son = Arrays.copyOfRange(step, startn, now);
                if(step_son.length==1)//�ж���ʽ��һ����ʶ��
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
                n=reset_t(qtt,n);           //��õ�ǰ��ʱ����tn��nֵ
                T1=qtt.get(qtt.size()-1)[3];//�ݴ�Ƚϵ���ʽ����ʱ����
                for(int j=0; j<qtt.size(); j++)//���Ƚϵ���ʽ��Ԫʽ����������Ԫʽ��
                {
                    qt.add(qtt.get(j));
                }
                }


                startn=now+1;//�ݴ���ʽstep_on��Token�������
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
                if(step_son.length==1)//�ж���ʽ��һ����ʶ��
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
                n=reset_t(qtt,n)+1;//��õ�ǰ��ʱ����tn��nֵ
                T2=qtt.get(qtt.size()-1)[3];//�ݴ�Ƚϵ���ʽ����ʱ����
                for(int j=0; j<qtt.size(); j++)//���Ƚϵ���ʽ��Ԫʽ����������Ԫʽ��
                {
                    qt.add(qtt.get(j));
                }
                }
                tn="t".concat(String.valueOf(n));
                addqt(P,T1,T2,tn);     //���ɱȽ���Ԫʽ
                addqt("if",tn,"_","_");//����if��Ԫʽ



                startn=now+1;//�ݴ�if{}�ڳ���ʼλ��
                t="hh";//ǿ�Ƹ�t����ĳ��ֵ��ʹ�䲻Ϊ��{������ֹ�����ж�if{}���׸�token��������
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
               t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];//��ǰ�ַ�
			   if(t.equals("else"))
			   {
                braceNum=0;
                //n=n+1;//��õ�ǰ��ʱ����tn��nֵ
                addqt("es","_","_","_");
                startn=now+2;//�ݴ�else{}�ڳ���ʼλ��
                t="hh";//ǿ�Ƹ�t����ĳ��ֵ��ʹ�䲻Ϊ��{������ֹ�����ж�else{}���׸�token��������
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
            n=reset_t(qtt,n)+1;//��õ�ǰ��ʱ����tn��nֵ
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

        

        //�����Ҫһ��ȫ�ֱ�����ȥ������qt����ͳ�ʼ���ĵط���
        //���������ﶨ��һ��now=0������Ŀǰִ�е�step[now]��һ��token�����е�{��,��}��Ȼ�����һ���ʵ�ʱ��now++
		//��ע��ȫ�ֱ����ڵ�һ��ʹ��ǰҪ���³�ʼ����Ҳ������answer�����ĵ�һ�����³�ʼ������Ȼ��ε���ʱ������һ�εĽ������

        


        //�������ǣ�Ҫ��ʵ�֣�if(>/>=/</<=/==/!=){����} else{����}
		//���ڴ��������{����}���֣���д�����������һ�������ࡣ

		//List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p, tb)���᷵��{����}����Ԫʽ,�����ֻ��Ҫ��ע��֧���Ϳ��ԡ�
		//ע�� �����step_son��step�������飬������{����}�в�����{}�ġ������ֵ�{i,0},{p,1}�ȵȡ�
		//Ҳ��������Ҫ�ҵ�if��else��{��}��λ�ã�Ȼ��ͨ��step_son=Arrays.copyOfRange(step, start, end)������ȡ�м�ġ�����
		//�Ӷ�����block��ȡ{����}����Ԫʽ

		//��������ҵ��뷨��������Ҫ�õݹ��½�����
		//����ȥ�Ȱ�if(a+b>c+d)ֱ�ӱ��+ a b t1��+ c d t2��> t1 t2 t3,if t3 _ _ ,�ӵ�qt��
		//Ȼ��ѵ�else֮ǰ��ȫ����List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p, tb)�õ����ǵ���Ԫʽ,Ҳ�ӵ�qt��
		//�ж���û��else���о���elseһ�� (el _ _ _ ),�ٰ�ʣ�µĽ���new block()�������
		//�����Ǵ���뷨

		//����Ĳ�������ǣ����ж�"("��">"(��</<=/����֮���token�����Ƿ񳬹�һ������������˵����if(a>?)���֣���߾Ͳ���Ҫ�����
		//�������һ��������Ҫ�������t1=a+b����ʽȻ�����List<String[]> qtt=new exp_four().answer(step_son,i,C,S,c,k,p, tb)��ʽ��ȡt1=a+b���м����
		//������Ҫ�����ǻ�ȡstep_son�����step_sonӦ����a+b�����token,ȥstep���ҵ�����Ȼ���г����ɣ������л�ȡ������ķ���
		//��һ���������㽫���úܶ�t1 t2�������м������Ϊ����������t1 t2������t1 t1 t1�ظ�������Ҫ����һ������n=1����Ϊt�����Ǹ���
		//�õ�t���ķ�����String tn="t".concat(String.valueOf(n))
		//��exp_four �Լ�block��ֻ��Ϊ���ṩt1-tn����Ϊ���ǲ�֪�������õ���t1����t100
		//Ϊ�˱������ֳ�ͻ��������List<String[]> qtt=new block��exp_four().answer(step_son,i,C,S,c,k,p, tb)֮��ִ��reset_t(qtt,n)
		//������Ϊ�˱��������������װ�ĺ����������������Լ���t�õ���t10��ִ�������������qtt�е�t1���t11���������ƣ��Ӷ���������ʱ�����ظ�
		//���������Ӧ���Ѿ��õ���a+b���ֵ���Ԫʽ�����Ǵ���qtt�
		//������[> t1 t2 t3]�����Ԫʽ����a+b��t1��qtt�����һ����Ԫʽ�ĵ���λҲ����qtt.get(qtt.size()-1)[3],��Ӧ�ð�����һ�£���Ȼ�Ȼ�qtt�����˾��Ҳ�����
		//ע����qtt�ӽ�qt�ķ�������ѭ��qtt�������Ȼ��qt.add(qtt[j]);
		//Ȼ���">"��")"֮���c+d�ظ�����Ĳ������õ�c+d����Ԫʽ����Ϊc+dҲ������c*(10+a)/b*d-100+f�����������ͨ�����е�exp_four����������
		//��a+b��Ӧ����Ԫʽ��c+d��Ӧ����Ԫʽ���ӵ�qt���ʱqtӦ����[+,a,b,t1] [+,c,d,t2] 
		//�������c+d����c*(10+a)/b*d-100+f�����кܶ���Ԫʽ���������Լǵ���һ��[>,t1,t2,t3]��t1 t2��t������Ҫ
		//Ȼ��qtӦ����[+,a,b,t1] [+,c,d,t2] [>,t1,t2,t3]���ǵ�printqt();�������qt��
		//Ȼ���ټ���[if,t3,_,_],if��ok�ˣ��Ҿ�����Ӧ��Ҳ�������else��ôд��
		//��Ȼ [+,a,b,t1] [+,c,d,t2] [>,t1,t2,t3] [if,t3,_,_]����Ҫ����List<String[]> qtt=new block().answer(step_son,i,C,S,c,k,p, tb)�������{����}������Ԫʽ����Ҫ�ǵ�reset_t(qtt,n)





        



        //һЩ�����а�������ʾ��


        //���������Ҫ�����return qt;
        //qt��Ӧ�������������������Ԫʽ��
        //qt���������Ѷ����ȫ�ֱ���List<String[]> qt
        //����������Ԫʽ���ĸ�Ԫ�أ���[ > a b t ]ʱ�������ʹ�����²��������ӽ�qt(�ҷ�װ��һ�£���
        //  addqt(">","a","b","t");
        //�����ʹ��   printqt();  �����qt������



        

        //�ڶ�ȡ{k,2} {p,0} {i,0}�����е�һ����ʱ����Ӧ��
        /*
        String t;
        switch (step[now].substring(1, 2)) {
        case "k":
			t = k[Integer.parseInt(step[now].substring(3,step[now].length()-1))];//��t��ȡ����k[2]��"if"������t�����ݾ���"if"
            ������ǰ�ַ��ǹؼ�����if������������
            break;
		case "i":
			t = i[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            ������ǰ�ַ��Ǳ�������c������������
            break;
		case "c":
			t = c[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            ������ǰ�ַ���������3.0������������
            break;
		case "p":
            t = p[Integer.parseInt(step[now].substring(3,step[now].length()-1))];
            ������ǰ�ַ��Ƿ�����+->=����==������������
            break;
        case "C":

        case "S":

        default:

         */


        //��һ�δ����ǽ������ɵ���Ԫʽд���ļ�������ʦ��鷽��
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