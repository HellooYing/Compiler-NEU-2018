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
		boolean[][] active = new boolean[qt.size()][4];// 存qt的活跃信息
        HashSet<String> t = new HashSet<String>();// 一个集合，为了把每个非数字的变量名加入set去重，获取变量表
		List<String> variiable;// set转化为list的变量表
		boolean[] symbl;// 存放变量对应的活跃信息
		String RDL;// 表示寄存器此时存的是哪个变量。我是写的单寄存器。
        String[] kt = { "if","es","ie","wh","dw","bk","ct","we","for","df","do","fe","fun","rt","xc","esf","sw","dft","cs"};
        List<String> k=Arrays.asList(kt);
        for (int i = 0; i < qt.size(); i++) {// 加set去重获取变量表
			inqt = qt.get(i);
			for (int j = 1; j < inqt.length; j++) {
				if (!is_c(inqt[j])&&!inqt[j].equals("_")&&!k.contains(inqt[j])) {// 如果当前字符不是数字
					t.add(inqt[j]);
				}
			}
		}

		variiable = new ArrayList<String>(t);
		symbl = new boolean[variiable.size()];
		for (int i = 0; i < variiable.size(); i++) {// 给变量的活跃信息赋初值
			if (variiable.get(i).length() > 1 && variiable.get(i).substring(0, 1).equals("t")
					&& is_c(variiable.get(i).substring(1))) {
				// 如果variiable是t+数字的组合，就是临时变量
				symbl[i] = false;// 临时变量初值false
			} else
				symbl[i] = true;// 非临时变量初值true
		}

		for (int i = qt.size() - 1; i >= 0; i--) {// 逆序扫描四元式组，添加变量在每个四元式的时候的活跃信息
			inqt = qt.get(i);
			if (t.contains(inqt[1])) {// 如果inqt[1]是变量而不是数字，也就是它在t那个集合里
				active[i][1] = symbl[index(variiable, inqt[1])];// 给它添加活跃信息
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
		} // 到这为止，active存放着变量的活跃信息，位置与qt一一对应。而没有活跃信息的数字和符号默认为false，后面应用时应避免查询。

        RDL = "";
        int n=0;//作为WH:/ES: 等等的后缀
        Stack<String> whn = new Stack<String>();//保存while该怎么跳，遇到while新建WH就push，遇到WE就pop
        Stack<String> wen = new Stack<String>();//遇到CMP新建WE就push，遇到WE就pop
        Stack<String> esn = new Stack<String>();//遇到if新建ES就push，遇到ie就pop
        Stack<String> ien = new Stack<String>();//遇到es新建IE就push，遇到ie就pop
        Stack<String> jmpfn = new Stack<String>();
        //根据符号表生成程序前部：
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

        //根据qt生成程序中部：
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
					code.add("  MOV ".concat(getv1(RDL,tb)).concat(",AL"));// 查询活跃信息表，RDL此时是上一条四元式的结果，如果这个结果变量仍然活跃，才把它弹到内存
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
        //加上程序尾部：
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
			File writename = new File("./z.目标代码.txt");
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
			File writename = new File("../z.目标代码.txt");
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
            if(flag==0&&is_t(v)){//如果是临时变量t几的话
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//在活动记录找到了它
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//偏移地址为临时变量在活动记录中的位置-所属函数在活动记录中的位置
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
                        if(flag==0&&is_t(v)){//如果是临时变量t几的话
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//在活动记录找到了它
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//偏移地址为临时变量在活动记录中的位置-所属函数在活动记录中的位置
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
                System.out.println("变量错误！无法在符号表中查询到此变量，变量名为："+v);
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
    
    static String getv1(String v,table tb){//为了在ST时为全局变量生成类似于[DATA+1]，局部变量生成SS:[SP+1]
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
            if(flag==0&&is_t(v)){//如果是临时变量t几的话
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//在活动记录找到了它
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//偏移地址为临时变量在活动记录中的位置-所属函数在活动记录中的位置
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
                        if(flag==0&&is_t(v)){//如果是临时变量t几的话
                            for(int jj=0;jj<tb.vall.size();jj++){
                                if(tb.vall.get(jj).equals(v)){//在活动记录找到了它
                                    ofad=jj;
                                    flag=1;
                                    r="SS:[BP+".concat(String.valueOf(ofad-getfnum(tb)+getflocalnum(tb))).concat("]");//偏移地址为临时变量在活动记录中的位置-所属函数在活动记录中的位置
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
                System.out.println("变量错误！无法在符号表中查询到此变量，变量名为："+v);
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
	
    static int getfnum(table tb){//获取最新函数在活动记录中的位置
        List<String> fnml=new ArrayList<String>();
        for(int i=0;i<tb.pfinfl.size();i++){
            fnml.add(tb.pfinfl.get(i).name);
        }
        for(int i=tb.vall.size()-1;i>=0;i--){
            if(fnml.contains(tb.vall.get(i))) return i;
        }
        return 0;
    }

    static int getflocalnum(table tb){//获取最新函数的局部变量的数量
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

    static boolean hves(List<String[]> qt,int i){//从qt的第i个开始，查到ie前，有没有es
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