import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


public class ll2 {
    public static void main(String[] args) {
        Test test = new Test();
        test.getNvNt();
        test.Init();
        test.createTable();
        test.analyzeLL();
        test.analyzeSLR();
        test.ouput();
    }
}

class Test {
    public HashMap<Character, HashSet<Character>> firstSet = new HashMap<Character, HashSet<Character>>();
    public HashMap<String, HashSet<Character>> firstSetX = new HashMap<String, HashSet<Character>>();
    // E T F : i ( | K : + $ |M : * $
    public Character S = 'E';
    public HashMap<Character, HashSet<Character>> followSet = new HashMap<Character, HashSet<Character>>();
    // E K: ) $ | T M : + ) $ | F : + * ) $
    public HashSet<Character> VnSet = new HashSet<Character>();// E K T M F
    public HashSet<Character> VtSet = new HashSet<Character>();// $ ) i + * (
    public HashMap<Character, ArrayList<String>> experssionSet = new HashMap<Character, ArrayList<String>>();
    // E: TK | K: +TK $ | T : FM | M: *FM $|F :i (E)
    public String [][] table;
    public String [][] tableSLR = { 
            { "", "i", "+", "*", "(", ")", "$", "E", "T", "F" },
            { "0", "s5", "", "", "s4", "", "", "1", "2", "3" }, 
            { "1", "", "s6", "", "", "", "acc", "", "", "" },
            { "2", "", "r2", "s7", "", "r2", "r2", "", "", "" }, 
            { "3", "", "r4", "r4", "", "r4", "r4", "", "", "" },
            { "4", "s5", "", "", "s4", "", "", "8", "2", "3" }, 
            { "5", "", "r6", "r6", "", "r6", "r6", "", "", "" },
            { "6", "s5", "", "", "s4", "", "", "", "9", "3" }, 
            { "7", "s5", "", "", "s4", "", "", "", "", "10" },
            { "8", "", "s6", "", "", "s11", "", "", "", "" }, 
            { "9", "", "r1", "s7", "", "r1", "r1", "", "", "" },
            { "10", "", "r3", "r3", "", "r3", "r3", "", "", "" },
            { "11", "", "r5", "r5", "", "r5", "r5", "", "", "" } };
    public String [] inputExperssion = { "E->TK", "K->+TK", "K->~", "T->FM", "M->*FM", "M->~", "F->i", "F->(E)"    
};

    public Stack<Character> analyzeStatck = new Stack<Character>();
    public Stack<String> stackState = new Stack<String>();
    public Stack<Character> stackSymbol = new Stack<Character>();
    public String strInput = "(i*i+i)*i$";
    public String action = "";
    public String[] LRGS = { "E->E+T", "E->T", "T->T*F", "T->F", "F->(E)", "F->i" };
    int index = 0;
    int followHashCode = followSet.hashCode();

    public void Init() {
        for (String e : inputExperssion) {
            String[] str = e.split("->");
            char c = str[0].charAt(0);
            ArrayList<String> list = experssionSet.containsKey(c) ? experssionSet.get(c) : new ArrayList<String>();
            list.add(str[1]);
            experssionSet.put(c, list);
        }
        for (Character c : VnSet)
            getFirst(c);

        for (Character c : VnSet) {
            ArrayList<String> l = experssionSet.get(c);
            for (String s : l)
                    getFirst(s);
        }
        getFollow(S);
        for (Character c : VnSet) {
            getFollow(c);
        }
    }

    public void getNvNt() {
        for (String e : inputExperssion) {
            String[] str = e.split("->");
            VnSet.add(str[0].charAt(0));
        }
        for (String e : inputExperssion) {
            String[] str = e.split("->");
            String right = str[1];
            for (int i = 0; i < right.length(); i++) 
                if (!VnSet.contains(right.charAt(i)))
                    VtSet.add(right.charAt(i));      
        }
    }

    public void getFirst(Character c) {
        ArrayList<String> list = experssionSet.get(c);
        HashSet<Character> set = firstSet.containsKey(c) ? firstSet.get(c) : new HashSet<Character>();
        // c为终结符 直接添加
        if (VtSet.contains(c)) {
            set.add(c);
            firstSet.put(c, set);
            return;
        }
        // c为非终结符 处理其每条产生式
        for (String s : list) {
            // c 推出空串 直接添加
            if (s == Character.toString('~')) {
                set.add('~');
            }
            // X -> Y1Y2Y3… 情况
            else {
                // 从左往右扫描生成式右部
                int i = 0;
                while (i < s.length()) {
                    char tn = s.charAt(i);
                    //递归处理防止未初始化
                    getFirst(tn);
                    HashSet<Character> tvSet = firstSet.get(tn);
                    // 将其first集加入左部
                    for (Character tmp : tvSet)
                        set.add(tmp);
                    // 若包含空串 处理下一个符号
                    if (tvSet.contains('~'))
                        i++;
                    // 否则退出 处理下一个产生式
                    else
                        break;
                }
            }
        }
        firstSet.put(c, set);
    }

    public void getFirst(String s) {
        HashSet<Character> set = (firstSetX.containsKey(s))? firstSetX.get(s) : new HashSet<Character>();
        // 从左往右扫描该式
        int i = 0;
        while (i < s.length()) {
            char tn = s.charAt(i);  
            HashSet<Character> tvSet = firstSet.get(tn);
            // 将其非空 first集加入左部
            for (Character tmp : tvSet)
                if(tmp != '~')
                    set.add(tmp);
            // 若包含空串 处理下一个符号
            if (tvSet.contains('~'))
                i++;
            // 否则结束
            else
                break;
            // 到了尾部 即所有符号的first集都包含空串 把空串加入
            if (i == s.length()) {
                set.add('~');
            }
        }
        firstSetX.put(s, set);
    }


    public void getFollow(char c) {
        ArrayList<String> list = experssionSet.get(c);
        HashSet<Character> setA = followSet.containsKey(c) ? followSet.get(c) : new HashSet<Character>();
        //如果是开始符 添加 $ 
        if (c == S) {
            setA.add('$');
        }
        //查找输入的所有产生式，确定c的后跟 终结符
        for (Character ch : VnSet) {
            ArrayList<String> l = experssionSet.get(ch);
            for (String s : l) 
                for (int i = 0; i < s.length(); i++)
                    if (s.charAt(i) == c && i + 1 < s.length() && VtSet.contains(s.charAt(i + 1)))
                        setA.add(s.charAt(i + 1));
        }
        followSet.put(c, setA);
        //处理c的每一条产生式
        for (String s : list) {
            int i = s.length() - 1;
            while (i >= 0 ) {
                char tn = s.charAt(i);
                //只处理非终结符
                if(VnSet.contains(tn)){
                    // 都按 A->αBβ  形式处理
                    //若β不存在   followA 加入 followB
                    //若β存在，把β的非空first集  加入followB
                    //若β存在  且 first(β)包含空串   followA 加入 followB

                    //若β存在 
                    if (s.length() - i - 1 > 0) {
                        String right = s.substring(i + 1);
                        //非空first集 加入 followB
                        HashSet<Character> setF = null;
                        if( right.length() == 1 && firstSet.containsKey(right.charAt(0)))
                            setF = firstSet.get(right.charAt(0));
                        else{
                            if(!firstSetX.containsKey(right)){
                                HashSet<Character> set = new HashSet<Character>();
                                firstSetX.put(right, set);
                            }
                            setF = firstSetX.get(right);
                        }
                        HashSet<Character> setX = followSet.containsKey(tn) ? followSet.get(tn) : new HashSet<Character>();
                        for (Character var : setF)
                            if (var != '~')
                                setX.add(var);
                        followSet.put(tn, setX);
 
                        // 若first(β)包含空串   followA 加入 followB
                        if(setF.contains('~')){
                            if(tn != c){
                                HashSet<Character> setB = followSet.containsKey(tn) ? followSet.get(tn) : new HashSet<Character>();
                                for (Character var : setA)
                                    setB.add(var);
                                followSet.put(tn, setB);
                             }  
                        }
                     }
                    //若β不存在   followA 加入 followB
                    else{
                        // A和B相同不添加 
                        if(tn != c){
                            HashSet<Character> setB = followSet.containsKey(tn) ? followSet.get(tn) : new HashSet<Character>();
                            for (Character var : setA)
                                setB.add(var);
                            followSet.put(tn, setB);
                         }   
                    }
                    i--;
                }  
                //终结符往前看  如 A->aaaBCDaaaa  此时β为 CDaaaa 
                else i--;         
             }
        }    
    }


    public void createTable() {
        Object[] VtArray = VtSet.toArray();
        Object[] VnArray = VnSet.toArray();
        // 预测分析表初始化
        table = new String[VnArray.length + 1][VtArray.length + 1];
        table[0][0] = "Vn/Vt";
        //初始化首行首列
        for (int i = 0; i < VtArray.length; i++) 
            table[0][i + 1] = (VtArray[i].toString().charAt(0) == '~') ? "$" : VtArray[i].toString();  
        for (int i = 0; i < VnArray.length; i++) 
            table[i + 1][0] = VnArray[i] + "";
        //全部置error
        for (int i = 0; i < VnArray.length; i++)
            for (int j = 0; j < VtArray.length; j++)
                table[i + 1][j + 1] = "error";
            
        //插入生成式
        for (char A : VnSet) {
            ArrayList<String> l = experssionSet.get(A);
            for(String s : l){
                HashSet<Character> set = firstSetX.get(s);
                 for (char a : set)
                    insert(A, a, s);
                 if(set.contains('~'))  {
                    HashSet<Character> setFollow = followSet.get(A);
                    if(setFollow.contains('$'))
                        insert(A, '$', s);  
                    for (char b : setFollow)
                        insert(A, b, s);                    
                 }
            }
        }
    }

    public void analyzeLL() {
        System.out.println("****************LL分析过程**********");
        System.out.println("               Stack           Input     Action");
        analyzeStatck.push('$');
        analyzeStatck.push('E');
        displayLL();
        char X = analyzeStatck.peek();
        while (X != '$') {
            char a = strInput.charAt(index);
            if (X == a) {
                action = "match " + analyzeStatck.peek();
                analyzeStatck.pop();
                index++;
            } else if (VtSet.contains(X))
                return;
            else if (find(X, a).equals("error"))
                return;
            else if (find(X, a).equals("~")) {
                analyzeStatck.pop();
                action = X + "->~";
            } else {
                String str = find(X, a);
                if (str != "") {
                    action = X + "->" + str;
                    analyzeStatck.pop();
                    int len = str.length();
                    for (int i = len - 1; i >= 0; i--)
                        analyzeStatck.push(str.charAt(i));
                } else {
                    System.out.println("error at '" + strInput.charAt(index) + " in " + index);
                    return;
                }
            }
            X = analyzeStatck.peek();
            displayLL();
        }
        System.out.println("analyze LL1 successfully");
        System.out.println("****************LL分析过程**********");
    }

    public void analyzeSLR() {
        action = "";
        index = 0;
        stackState.push("0");
        char a = strInput.charAt(index);
        System.out.println("****************SLR分析过程**********");
        System.out.println("                    State         Symbol        Input         Action");
        this.displaySLR();
        while (true) {
            String s = stackState.peek();
            // 查表为移进
            if (Action(s, a).charAt(0) == 's') {
                stackState.push(Action(s, a).substring(1));
                stackSymbol.push(a);
                a = strInput.charAt(++index);
                action = "shift ";
                displaySLR();
            }
            // 查表为归约
            else if (Action(s, a).charAt(0) == 'r') {
                // 获取文法串
                String str = LRGS[Integer.parseInt(Action(s, a).substring(1)) - 1];
                int len = str.substring(3).length();
                // 弹出右部长度的符号和状态
                for (int i = 0; i < len; i++) {
                    stackSymbol.pop();
                    stackState.pop();
                }
                // goto的值进栈
                String t = stackState.peek();
                stackState.push(Action(t, str.charAt(0)));
                stackSymbol.push(str.charAt(0));
                action = "reduce:" + str;
                displaySLR();
            } else if (Action(s, a) == "acc")
                break;
            else
                return;
        }
        System.out.println("analyze SLR successfully");
        System.out.println("****************SLR分析过程**********");
    }

    public String Action(String s, char a) {
        for (int i = 1; i < 13; i++)
            if (tableSLR[i][0].equals(s))
                for (int j = 1; j < 10; j++)
                    if (tableSLR[0][j].charAt(0) == a)
                        return tableSLR[i][j];
        return "";
    }

    public String find(char X, char a) {
        for (int i = 0; i < VnSet.size() + 1; i++) {
            if (table[i][0].charAt(0) == X)
                for (int j = 0; j < VtSet.size() + 1; j++) {
                    if (table[0][j].charAt(0) == a)
                        return table[i][j];
                }
        }
        return "";
    }
    public void insert(char X, char a,String s) {
        if(a == '~') a = '$';
        for (int i = 0; i < VnSet.size() + 1; i++) {
            if (table[i][0].charAt(0) == X)
                for (int j = 0; j < VtSet.size() + 1; j++) {
                    if (table[0][j].charAt(0) == a){
                        table[i][j] = s;
                        return;
                    }
                }
        }
    }

    public void displayLL() {
        // 输出 LL1
        Stack<Character> s = analyzeStatck;
        System.out.printf("%23s", s);
        System.out.printf("%13s", strInput.substring(index));
        System.out.printf("%10s", action);
        System.out.println();
    }

    public void displaySLR() {
        // 输出 SLR
        System.out.printf("%25s", stackState);
        System.out.printf("%15s", stackSymbol);
        System.out.printf("%15s", strInput.substring(index));
        System.out.printf("%15s", action);
        System.out.println();
    }

    public void ouput() {
        System.out.println("*********first集********");
        for (Character c : VnSet) {
            HashSet<Character> set = firstSet.get(c);
            System.out.printf("%10s",c + "  ->   ");
            for (Character var : set)
                System.out.print(var);
            System.out.println();
        }
        System.out.println("**********first集**********");
        System.out.println("*********firstX集********");
        Set<String> setStr =  firstSetX.keySet();
        for (String s : setStr) {
                HashSet<Character> set = firstSetX.get(s);
                System.out.printf("%10s",s + "  ->   ");
                for (Character var : set)
                    System.out.print(var);
                System.out.println();
            }
        System.out.println("**********firstX集**********");
        System.out.println("**********follow集*********");

        for (Character c : VnSet) {
            HashSet<Character> set = followSet.get(c);
            System.out.print("Follow " + c + ":");
            for (Character var : set)
                System.out.print(var);
            System.out.println();
        }
        System.out.println("**********follow集**********");

        System.out.println("**********LL1预测分析表********");

        for (int i = 0; i < VnSet.size() + 1; i++) {
            for (int j = 0; j < VtSet.size() + 1; j++) {
                System.out.printf("%6s", table[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("**********LL1预测分析表********");

        System.out.println("**********SLR语法分析表********");

        for (int i = 0; i < 12 + 1; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf("%6s", tableSLR[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("**********SLR语法分析表********");

    }

}