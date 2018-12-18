### 函数功能、输入、输出设计：

 - analyzer 输入c语言代码（目前是输入算数表达式），输出c语言的token序列和iCSckp表
      - 输入：String path_in
      
             "./z.c语言代码输入.txt"（txt内容如：a=2+3*(4+5)+c-d*3）
      
      - 输出：String 
      
             {i,0} {p,0} {c,0} {p,1} {c,1} {p,2} {p,3} {c,2} {p,1} {c,3} {p,4} {p,1} {i,1} {p,5} {i,2} {p,2} {c,1}
             i:[a, c, d]
             C:[]
             S:[]
             c:[2.0, 3.0, 4.0, 5.0]
             k:[main, void, if, else, while, for, int, char, string]
             p:[=, +, *, (, ), -]
             
 - exp_four 输入算数表达式的token序列和iCSckp表，输出算数表达式的四元式中间代码
      - 输入：String[] step, i, C, S, c, k, p;
      
             step:{i,0} {p,0} {c,0} {p,1} {c,1} {p,2} {p,3} {c,2} {p,1} {c,3} {p,4} {p,1} {i,1} {p,5} {i,2} {p,2} {c,1}
             i:[a, c, d]
             C:[]
             S:[]
             c:[2.0, 3.0, 4.0, 5.0]
             k:[main, void, if, else, while, for, int, char, string]
             p:[=, +, *, (, ), -]
      
      - 输出：List<String[]> qt
      
             [+,4.0,5.0,t1] [*,3.0,t1,t2] [+,2.0,t2,t3] [+,t3,c,t4] [*,d,3.0,t5] [-,t4,t5,t6] [=,t6,_,a] 
             
 - block 输入算数表达式和if和while混合的token序列和iCSckp表，输出四元式中间代码。在此方法中，if、while、算数表达式的求中间代码都是通过引用if_four.java while_four.java exp_four.java实现的。
      - 输入：String[] step, i, C, S, c, k, p;
      
             step:{i,0} {p,0} {c,0} {p,1} {c,1} {p,2} {p,3} {c,2} {p,1} {c,3} {p,4} {p,1} {i,1} {p,5} {i,2} {p,2} {c,1}
             i:[a, c, d]
             C:[]
             S:[]
             c:[2.0, 3.0, 4.0, 5.0]
             k:[main, void, if, else, while, for, int, char, string]
             p:[=, +, *, (, ), -]
      
      - 输出：List<String[]> qt
      
             [+,4.0,5.0,t1] [*,3.0,t1,t2] [+,2.0,t2,t3] [+,t3,c,t4] [*,d,3.0,t5] [-,t4,t5,t6] [=,t6,_,a] 
 - optimization 输入算数表达式的四元式中间代码，输出算数表达式的优化后的四元式
      - 输入：List<String[]> qt
      
             [+,4.0,5.0,t1] [*,3.0,t1,t2] [+,2.0,t2,t3] [+,t3,c,t4] [*,d,3.0,t5] [-,t4,t5,t6] [=,t6,_,a] 
      
      - 输出：List<String[]> qt
      
             [+,29.0,c,t4] [*,d,3.0,t5] [-,t4,t5,a] 
             
 - object_code 输入算数表达式的优化后的四元式，输出算数表达式的汇编代码
      - 输入：List<String[]> qt
      
             [+,29.0,c,t4] [*,d,3.0,t5] [-,t4,t5,a] 
      
      - 输出：List< String > code
      
             LD R,29.0
             ADD R,c
             ST R,t4
             LD R,d
             MUL R,3.0
             ST R,t5
             LD R,t4
             SUB R,t5
             ST R,a
 
 - Main 调用1,2,3,4，也就是输入算数表达式的c语言代码，输出算数表达式的汇编代码
      - 输入：String path_in
      
             "./z.c语言代码输入.txt"（txt内容如：a=2+3*(4+5)+c-d*3;b=3;）暂时没支持if while
             
      - 输出：List< String > code
      
             LD R,29.0
             ADD R,c
             ST R,t4
             LD R,d
             MUL R,3.0
             ST R,t5
             LD R,t4
             SUB R,t5
             ST R,a


### 中间代码设计：

 - 算数表达式
 
       [ +, a, b, t1 ] ———————————— t1=a+b
             
 - 判断大小、相等
 
       [ >, a, b, t1 ] ———————————— a>b
       [ !=, a, b, t1] ———————————— a!=b
             
 - if
 
       [ if, t1, _, _] ———————————— if(t1)
       [ es, _, _, _ ] ———————————— else
       [ ie, _, _, _ ] ———————————— if结束
             
 - while
 
       [ wh, t1, _, _] ———————————— while(t1)
       [ bk, _, _, _ ] ———————————— break
       [ ct, _, _, _ ] ———————————— continue
       [ we, _, _, _ ] ———————————— while结束
       
             
 - for
 
       [ for, _, _, _] ———————————— for
       [ do, t0, _, _] ———————————— 如果t0，执行
       [ bk, _, _, _ ] ———————————— break
       [ ct, _, _, _ ] ———————————— continue
       [ fe, _, _, _ ] ———————————— for循环结束
       
 - function(a,b,c)
 
       未选定


### 目标代码说明：目标代码为8086汇编语言

 - 定义全局变量：在数据段中定义
 
       DATAS SEGMENT
           DATA	DB	1D,2D ———————————— 数组DATA初始化，[1,2]
           a DB 0D ———————————— int a=0；
       DATAS ENDS
       
 - 修改全局变量的值：
 
       MOV a,OFFSET 1D ———————————— a=1；
       MOV [DATA+1],offset 3D ———————————— DATA[1]=3;
       
- 定义局部变量：在堆栈段定义，通过活动记录读写
 
       STACKS SEGMENT
           STK		DB	200 DUP (0) ———————————— 定义一个堆栈段
       STACKS ENDS
       
       MOV AX,32D
       PUSH AX ———————————— 将变量值加入堆栈段
       
       POP AX ———————————— 清除堆栈段的变量
       
- 读取局部变量：

       MOV AX,3132H
       PUSH AX
       MOV AX,3334H
       PUSH AX
       MOV AX,3536H
       PUSH AX ———————————— 当前堆栈段为1 2 3 4 5 6（字符'1'，ascii对应31H）
       
       MOV BP,SP
       MOV AX,SS:[BP] ———————————— AX=6，SS:[BP]对应的是栈顶元素
	      MOV AX,SS:[BP+5] ———————————— AX=1，SS:[BP+5]对应的是倒数第五个元素1
