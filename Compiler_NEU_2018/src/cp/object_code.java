package cp;
import java.io.*;
import java.util.*;
public class object_code {
	public static void main(String[] args) throws Exception {
        List<String[]> qt = new ArrayList<String[]>();

        //List<String> r = new object_code().answer(qt);
		//System.out.println(r.toString());
	}
    List<String> answer(List<String[]> qt,table tb) {
        List<String> code = new ArrayList<String>();
        String[] inqt;
		boolean[][] active = new boolean[qt.size()][4];// ��qt�Ļ�Ծ��Ϣ
        HashSet<String> t = new HashSet<String>();// һ�����ϣ�Ϊ�˰�ÿ�������ֵı���������setȥ�أ���ȡ������
		List<String> variiable;// setת��Ϊlist�ı�����
		boolean[] symbl;// ��ű�����Ӧ�Ļ�Ծ��Ϣ
		String RDL;// ��ʾ�Ĵ�����ʱ������ĸ�����������д�ĵ��Ĵ�����
        String[] kt = { "if","es","ie","wh","dw","bk","ct","we","for","df","do","fe","fun","rt","xc","esf","sw","dft","cs"};
        List<String> k=Arrays.asList(kt);
        for (int i = 0; i < qt.size(); i++) {// ��setȥ�ػ�ȡ������
			inqt = qt.get(i);
			for (int j = 1; j < inqt.length; j++) {
				if (!is_c(inqt[j])&&!inqt[j].equals("_")&&!k.contains(inqt[j])) {// �����ǰ�ַ���������
					t.add(inqt[j]);
				}
			}
		}

		variiable = new ArrayList<String>(t);
		symbl = new boolean[variiable.size()];
		for (int i = 0; i < variiable.size(); i++) {// �������Ļ�Ծ��Ϣ����ֵ
			if (variiable.get(i).length() > 1 && variiable.get(i).substring(0, 1).equals("t")
					&& is_c(variiable.get(i).substring(1))) {
				// ���variiable��t+���ֵ���ϣ�������ʱ����
				symbl[i] = false;// ��ʱ������ֵfalse
			} else
				symbl[i] = true;// ����ʱ������ֵtrue
		}

		for (int i = qt.size() - 1; i >= 0; i--) {// ����ɨ����Ԫʽ�飬��ӱ�����ÿ����Ԫʽ��ʱ��Ļ�Ծ��Ϣ
			inqt = qt.get(i);
			if (t.contains(inqt[1])) {// ���inqt[1]�Ǳ������������֣�Ҳ��������t�Ǹ�������
				active[i][1] = symbl[index(variiable, inqt[1])];// ������ӻ�Ծ��Ϣ
				symbl[index(variiable, inqt[1])] = true;
			}
			if (t.contains(inqt[2])) {
				active[i][2] = symbl[index(variiable, inqt[2])];
				symbl[index(variiable, inqt[2])] = true;
			}
			if (t.contains(inqt[3])) {
				active[i][3] = symbl[index(variiable, inqt[3])];
				symbl[index(variiable, inqt[3])] = false;
			}
		} // ����Ϊֹ��active����ű����Ļ�Ծ��Ϣ��λ����qtһһ��Ӧ����û�л�Ծ��Ϣ�����ֺͷ���Ĭ��Ϊfalse������Ӧ��ʱӦ�����ѯ��

        RDL = "";
        int n=0;//��ΪWH:/ES: �ȵȵĺ�׺
        Stack<String> whn = new Stack<String>();//����while����ô��������while�½�WH��push������WE��pop
        Stack<String> wen = new Stack<String>();//����CMP�½�WE��push������WE��pop
        Stack<String> esn = new Stack<String>();//����if�½�ES��push������ie��pop
        Stack<String> ien = new Stack<String>();//����es�½�IE��push������ie��pop
        Stack<String> jmpfn = new Stack<String>();
        //���ݷ��ű����ɳ���ǰ����
        code.add("DATAS SEGMENT");
        table.vari v;
        for(int i=0;i<tb.synbl.size();i++){
            v=tb.synbl.get(i);
            if(v.tp.equals("function")) continue;
            code.add("  ".concat(v.name).concat(" DB ?"));
        }
        code.add("DATAS ENDS");
        code.add("STACKS SEGMENT");
        code.add("  STK DB 20 DUP (0)");
        code.add("STACKS ENDS");
        code.add("CODES SEGMENT");
        code.add("  ASSUME CS:CODES,DS:DATAS,SS:STACKS");
        code.add("START:");
        code.add("  MOV AX,DATAS");
        code.add("  MOV DS,AX");
        code.add("  MOV SP,BP");

        //����qt���ɳ����в���
        for (int i = 0; i < qt.size(); i++) {
            inqt = qt.get(i);
            if (inqt[0].equals("+")) {
                if (RDL == "") {
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
					code.add("  ADD AL,".concat(getv(inqt[2],tb)));
				} else if (RDL.equals(getv(inqt[2],tb))) {
					code.add("  ADD AL,".concat(getv(inqt[1],tb)));
				} else if (RDL.equals(getv(inqt[1],tb))) {
					code.add("  ADD AL,".concat(getv(inqt[2],tb)));
				} else if (active[i - 1][3]) {
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));// ��ѯ��Ծ��Ϣ��RDL��ʱ����һ����Ԫʽ�Ľ�������������������Ȼ��Ծ���Ű��������ڴ�
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
					code.add("  ADD AL,".concat(getv(inqt[2],tb)));
				}
                RDL = inqt[3];
                if(i+1<qt.size()&&!(qt.get(i+1)[0].equals("+")||qt.get(i+1)[0].equals("-")||qt.get(i+1)[0].equals("*")||qt.get(i+1)[0].equals("/")||qt.get(i+1)[0].equals("="))){
                    code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
                }
			} else if (inqt[0].equals("-")) {
				if (RDL == "") {
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
					code.add("  SUB AL,".concat(getv(inqt[2],tb)));
				} else if (RDL.equals(getv(inqt[1],tb))) {
					code.add("  SUB AL,".concat(getv(inqt[2],tb)));
				} else {
					if (active[i - 1][3])
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
					code.add("  SUB AL,".concat(getv(inqt[2],tb)));
				}
                RDL = inqt[3];
                if(i+1<qt.size()&&!(qt.get(i+1)[0].equals("+")||qt.get(i+1)[0].equals("-")||qt.get(i+1)[0].equals("*")||qt.get(i+1)[0].equals("/")||qt.get(i+1)[0].equals("="))){
                    code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
                }
			} else if (inqt[0].equals("*")) {
				if (RDL == "") {
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
					code.add("  MOV AH,".concat(getv(inqt[2],tb)));
                    code.add("  MUL AH");
				}

				else if (RDL.equals(getv(inqt[2],tb))) {
                    code.add("  MOV AH,".concat(getv(inqt[1],tb)));
                    code.add("  MUL AH");
				} else if (RDL.equals(getv(inqt[1],tb))) {
					code.add("  MOV AH,".concat(getv(inqt[2],tb)));
                    code.add("  MUL AH");
				} else {
					if (active[i - 1][3])
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
					code.add("  MOV AH,".concat(getv(inqt[2],tb)));
                    code.add("  MUL AH");
				}
                RDL = inqt[3];
                if(i+1<qt.size()&&!(qt.get(i+1)[0].equals("+")||qt.get(i+1)[0].equals("-")||qt.get(i+1)[0].equals("*")||qt.get(i+1)[0].equals("/")||qt.get(i+1)[0].equals("="))){
                    code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
                }
			} else if (inqt[0].equals("/")) {
				if (RDL == "") {
					code.add("  MOV AX,".concat(getv(inqt[1],tb)));
                    code.add("  MOV BL,".concat(getv(inqt[2],tb)));
					code.add("  DIV BL");
				} else if (RDL.equals(getv(inqt[1],tb))) {
                    code.add("  MOV AX,".concat(getv(inqt[1],tb)));
					code.add("  MOV BL,".concat(getv(inqt[2],tb)));
					code.add("  DIV BL");
				} else {
					if (active[i - 1][3])
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
					code.add("  MOV AX,".concat(getv(inqt[1],tb)));
					code.add("  MOV BL,".concat(getv(inqt[2],tb)));
					code.add("  DIV BL");
				}
                RDL = inqt[3];
                if(i+1<qt.size()&&!(qt.get(i+1)[0].equals("+")||qt.get(i+1)[0].equals("-")||qt.get(i+1)[0].equals("*")||qt.get(i+1)[0].equals("/")||qt.get(i+1)[0].equals("="))){
                    code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
                }
			} else if (inqt[0].equals("=")) {

				if (RDL == "") {
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
				} else if (RDL.equals(getv(inqt[1],tb))) {
				} else {
					if (active[i - 1][3])
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
					code.add("  MOV AL,".concat(getv(inqt[1],tb)));
				}
                RDL = inqt[3];
                if(i+1<qt.size()&&!(qt.get(i+1)[0].equals("+")||qt.get(i+1)[0].equals("-")||qt.get(i+1)[0].equals("*")||qt.get(i+1)[0].equals("/")||qt.get(i+1)[0].equals("="))){
                    code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
                }
			} else if(inqt[0].equals(">")||inqt[0].equals(">=")||inqt[0].equals("<")||inqt[0].equals("<=")||inqt[0].equals("==")||inqt[0].equals("!=")){
                if (active[i - 1][3]){
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));
                }
                code.add("  MOV AL,".concat(getv(inqt[1],tb)));
                code.add("  MOV AH,".concat(getv(inqt[2],tb)));
                code.add("  CMP AL,AH");
                if(i+1<qt.size()){
                    if(qt.get(i+1)[0].equals("dw")){
                        wen.push("WE".concat(String.valueOf(n)));
                        n++;
                        code.add(cmpn(inqt[0]).concat(" ").concat(wen.peek()));
                        i++;
                    }
                    else if(qt.get(i+1)[0].equals("df")){
                        wen.push("FE".concat(String.valueOf(n)));
                        n++;
                        code.add(cmpn(inqt[0]).concat(" ").concat(wen.peek()));
                        i++;
                    }
                }
                RDL = "";
            } else if(inqt[0].equals("wh")){
                code.add("  MOV CX,02H");
                whn.push("WH".concat(String.valueOf(n)));
                n++;
                code.add(whn.peek().concat(":"));
                code.add("  INC CX");
                RDL = "";
            }else if(inqt[0].equals("for")){
                code.add("  MOV CX,02H");
                whn.push("FOR".concat(String.valueOf(n)));
                n++;
                code.add(whn.peek().concat(":"));
                code.add("  INC CX");
                RDL = "";
            }else if(inqt[0].equals("we")){
                code.add("  LOOP ".concat(whn.pop()));
                code.add(wen.pop().concat(":"));
                RDL = "";
            }else if(inqt[0].equals("fe")){
                code.add("  LOOP ".concat(whn.pop()));
                code.add(wen.pop().concat(":"));
                RDL = "";
            }else if(inqt[0].equals("bk")){
                code.add("  JMP ".concat(wen.peek()));
                RDL = "";
            }else if(inqt[0].equals("ct")){
                code.add("  JMP ".concat(whn.peek()));
                RDL = "";
            }else if(inqt[0].equals("if")){
                if(hves(qt,i)){
                    esn.push("ES".concat(String.valueOf(n)));
                    n++;
                    code.add(cmpn(qt.get(i-1)[0]).concat(" ").concat(esn.peek()));
                }
                else{
                    ien.push("IE".concat(String.valueOf(n)));
                    n++;
                    code.add(cmpn(qt.get(i-1)[0]).concat(" ").concat(ien.peek()));
                }
                RDL = "";
            }else if(inqt[0].equals("es")){
                ien.push("IE".concat(String.valueOf(n)));
                n++;
                code.add("  JMP ".concat(ien.peek()));
                code.add(esn.pop().concat(":"));
                RDL = "";
            }else if(inqt[0].equals("ie")){
                code.add(ien.pop().concat(":"));
                RDL = "";
            }else if(inqt[0].equals("fun")){
                jmpfn.push("JMPF".concat(String.valueOf(n)));
                n++;
                code.add("  JMP ".concat(jmpfn.peek()));
                code.add(inqt[1].concat(" PROC NEAR"));
                tb.vall.add(inqt[1]);
                RDL = "";
            }else if(inqt[0].equals("rt")){
                code.add("  MOV AL,".concat(getv(inqt[1],tb)));
                code.add("  RET");
                code.add(getf(tb).concat(" ENDP"));
                code.add(jmpfn.pop().concat(":"));
                String fname=getf(tb);
                int jjj;
                for(jjj=tb.vall.size()-1;jjj>=0;jjj--){
                    if(tb.vall.get(jjj).equals(fname)) break;
                    else tb.vall.remove(jjj);
                }
                tb.vall.remove(jjj);
                RDL = "";
            }else if(inqt[0].equals("sf")){
                tb.vall.add(inqt[1]);
                RDL = "";
            }else if(inqt[0].equals("xc")){
                code.add("  MOV AL,".concat(getv(inqt[2],tb)));
                code.add("  MOV ".concat(getv(inqt[1],tb)).concat(",AL"));
                RDL = "";
            }else if(inqt[0].equals("esf")){
                code.add("  CALL ".concat(getf(tb)));
                tb.vall.remove(tb.vall.size()-1);
                code.add("  MOV ".concat(getv1(inqt[2],tb)).concat(",AL"));
                RDL = "";
            }
        }
        //���ϳ���β����
        code.add("  MOV AH,4CH");
        code.add("  INT 21H");
        code.add("CODES ENDS");
        code.add("  END START");
        return code;
    }

    static void writebc(List<String> code){
        String result = "";
		for (int i = 0; i < code.size(); i++) {
			result = result.concat(code.get(i)).concat("\n");
		}
		try {
			File writename = new File("./z.Ŀ�����.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    static void wtoc(List<String> code){
        String result = "";
		for (int i = 0; i < code.size(); i++) {
			result = result.concat(code.get(i)).concat("\n");
		}
		try {
			File writename = new File("../z.Ŀ�����.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    static String getv(String v,table tb){
        int ofad=0;
        int flag=0;
        String r="";
        if(getf(tb).equals("main")){
            for(int i=0;i<tb.synbl.size();i++){
                if(tb.synbl.get(i).name.equals(v)){
                    ofad=tb.synbl.get(i).ofad;
                    flag=1;
                    r="DS:[".concat(String.valueOf(ofad)).concat("]");
                }
            }
            if(flag==0&&is_t(v)){//�������ʱ����t���Ļ�
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//�ڻ��¼�ҵ�����
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//ƫ�Ƶ�ַΪ��ʱ�����ڻ��¼�е�λ��-���������ڻ��¼�е�λ��
                                }
                            }
                            if(flag==0){
                                tb.vall.add(v);
                                ofad=tb.vall.size()-1;
                                flag=1;
                                r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");
                            }
                        }
        }
        else{
            for(int i=0;i<tb.synbl.size();i++){
                if(tb.synbl.get(i).name.equals(v)){
                    ofad=tb.synbl.get(i).ofad;
                    flag=1;
                    r="DS:[".concat(String.valueOf(ofad)).concat("]");
                }
            }
            if(flag==0){
                String fnm=getf(tb);
                for(int i=0;i<tb.pfinfl.size();i++){
                    if(tb.pfinfl.get(i).name.equals(fnm)){
                        for(int j=0;j<tb.pfinfl.get(i).vt.size();j++){
                            if(tb.pfinfl.get(i).vt.get(j).name.equals(v)){
                                ofad=tb.pfinfl.get(i).vt.get(j).ofad;
                                flag=1;
                                if(ofad==0) r="SS:[BP]";
                                else r="SS:[BP+".concat(String.valueOf(ofad)).concat("]");
                            }
                        }
                        if(flag==0&&is_t(v)){//�������ʱ����t���Ļ�
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//�ڻ��¼�ҵ�����
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//ƫ�Ƶ�ַΪ��ʱ�����ڻ��¼�е�λ��-���������ڻ��¼�е�λ��
                                }
                            }
                            if(flag==0){
                                tb.vall.add(v);
                                ofad=tb.vall.size()-1;
                                flag=1;
                                r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");
                            }
                        }
                    }
                }
            }
        }
        if(flag==0){
            if(is_c(v)){
                r="OFFSET ".concat(v.substring(0,v.length()-2)).concat("D");
            }
            else{
                System.out.println("���������޷��ڷ��ű��в�ѯ���˱�����������Ϊ��"+v);
                return "";
            }
        }
        return r;
    }

    static String cmpp(String cmp){
        if(cmp.equals(">")) return "  JA";
        if(cmp.equals(">=")) return "  JAE";
        if(cmp.equals("<")) return "  JB";
        if(cmp.equals("<=")) return "  JBE";
        if(cmp.equals("==")) return "  JE";
        if(cmp.equals("!=")) return "  JNE";
        else return "";
    }

    static String cmpn(String cmp){
        if(cmp.equals(">")) return "  JBE";
        if(cmp.equals(">=")) return "  JB";
        if(cmp.equals("<")) return "  JAE";
        if(cmp.equals("<=")) return "  JA";
        if(cmp.equals("==")) return "  JNE";
        if(cmp.equals("!=")) return "  JE";
        else return "";
    }
    
    static String getv1(String v,table tb){//Ϊ����STʱΪȫ�ֱ�������������[DATA+1]���ֲ���������SS:[SP+1]
        int ofad=0;
        int flag=0;
        String r="";
        if(getf(tb).equals("main")){
            for(int i=0;i<tb.synbl.size();i++){
                if(tb.synbl.get(i).name.equals(v)){
                    ofad=tb.synbl.get(i).ofad;
                    flag=1;
                    r="[".concat(v).concat("]");
                }
            }
            if(flag==0&&is_t(v)){//�������ʱ����t���Ļ�
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//�ڻ��¼�ҵ�����
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//ƫ�Ƶ�ַΪ��ʱ�����ڻ��¼�е�λ��-���������ڻ��¼�е�λ��
                                }
                            }
                            if(flag==0){
                                tb.vall.add(v);
                                ofad=tb.vall.size()-1;
                                flag=1;
                                r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");
                            }
                        }
        }
        else{
            for(int i=0;i<tb.synbl.size();i++){
                if(tb.synbl.get(i).name.equals(v)){
                    ofad=tb.synbl.get(i).ofad;
                    flag=1;
                    r="[".concat(v).concat("]");
                }
            }
            if(flag==0){
                String fnm=getf(tb);
                for(int i=0;i<tb.pfinfl.size();i++){
                    if(tb.pfinfl.get(i).name.equals(fnm)){
                        for(int j=0;j<tb.pfinfl.get(i).vt.size();j++){
                            if(tb.pfinfl.get(i).vt.get(j).name.equals(v)){
                                ofad=tb.pfinfl.get(i).vt.get(j).ofad;
                                flag=1;
                                if(ofad==0) r="SS:[BP]";
                                else r="SS:[BP+".concat(String.valueOf(ofad)).concat("]");
                            }
                        }
                        if(flag==0&&is_t(v)){//�������ʱ����t���Ļ�
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//�ڻ��¼�ҵ�����
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//ƫ�Ƶ�ַΪ��ʱ�����ڻ��¼�е�λ��-���������ڻ��¼�е�λ��
                                }
                            }
                            if(flag==0){
                                tb.vall.add(v);
                                ofad=tb.vall.size()-1;
                                flag=1;
                                r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");
                            }
                        }
                    }
                }
            }
        }
        if(flag==0){
            if(is_c(v)){
                r="OFFSET ".concat(v.substring(0,v.length()-2)).concat("D");
            }
            else{
                System.out.println("���������޷��ڷ��ű��в�ѯ���˱�����������Ϊ��"+v);
                return "";
            }
        }
        return r;
    }

    static String getf(table tb){
        List<String> fnml=new ArrayList<String>();
        for(int i=0;i<tb.pfinfl.size();i++){
            fnml.add(tb.pfinfl.get(i).name);
        }
        for(int i=tb.vall.size()-1;i>=0;i--){
            if(fnml.contains(tb.vall.get(i))) return tb.vall.get(i);
        }
        return "main";
    }
	
    static int getfnum(table tb){//��ȡ���º����ڻ��¼�е�λ��
        List<String> fnml=new ArrayList<String>();
        for(int i=0;i<tb.pfinfl.size();i++){
            fnml.add(tb.pfinfl.get(i).name);
        }
        for(int i=tb.vall.size()-1;i>=0;i--){
            if(fnml.contains(tb.vall.get(i))) return i;
        }
        return 0;
    }

    static int getflocalnum(table tb){//��ȡ���º����ľֲ�����������
        String s=getf(tb);
        if(s.equals("main")) return 0;
        else{
            for(int i=0;i<tb.pfinfl.size();i++){
                if(s.equals(tb.pfinfl.get(i).name)){
                    return tb.pfinfl.get(i).vt.size();
                }
            }
        }
        return -1;
    }

    static boolean hves(List<String[]> qt,int i){//��qt�ĵ�i����ʼ���鵽ieǰ����û��es
        for(;i<qt.size();i++){
            String[] ii=qt.get(i);
            if(ii[0].equals("ie")) break;
            if(ii[0].equals("es")) return true;
        }
        return false;
    }

    static boolean is_t(String t) {
		if (t.length() < 2)
			return false;
		if (!t.substring(0, 1).equals("t"))
			return false;
		if (!is_c(t.substring(1)))
			return false;
		return true;
	}

    static int index(List<String> variiable, String str) {
		int i;
		for (i = 0; i < variiable.size(); i++) {
			if (variiable.get(i).equals(str))
				break;
			else if (i == variiable.size() - 1) {
				i = -1;
				break;
			}
		}
		return i;
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
}