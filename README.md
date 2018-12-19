### 编译器前端设计：

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
             
 - exp_four、while_four、for_four、if_four、function_four 输入token序列和iCSckp表，输出四元式中间代码
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
             
 - define_local、define_global 输入类似于"int a;"的代码对应的token序列、iCSckp表、符号表-总表、符号表-函数表、符号表-活动记录，输出修改后的符号表。
      - 输入：String[] step, i, C, S, c, k, p; List<List<String[]>> table(内容为List<String[]> synbl, pfinfl, vall);
      
             step: {k,6} {i,0} {p,0} {i,1} {p,1} {k,6} {i,0} {p,2} {i,1} {p,2} {i,2} {p,1}
             i:[a, b, c]
             C:[]
             S:[]
             c:[]
             k:[main, void, if, else, while, for, int, char, string, break, continue]
             p:[=, ;, ,]
             synbl:[]
             pfinfl:[]
             vall:[]
      
      - 输出：List<List<String[]>> table
      
             [synbl, pfinfl, vall] 
             
             
 - block 输入基本块token序列和iCSckp表，输出四元式中间代码。在此方法中，if、while、算数表达式等求中间代码都是通过引用其他_four.java实现的。
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
             
 - optimization 输入四元式中间代码，输出优化后的四元式
      - 输入：List<String[]> qt
      
             [+,4.0,5.0,t1] [*,3.0,t1,t2] [+,2.0,t2,t3] [+,t3,c,t4] [*,d,3.0,t5] [-,t4,t5,t6] [=,t6,_,a] 
      
      - 输出：List<String[]> qt
      
             [+,29.0,c,t4] [*,d,3.0,t5] [-,t4,t5,a] 
             
 - object_code 输入优化后的四元式，输出8086汇编代码
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
 
 - Main 调用1,2,3,4，也就是输入c语言代码，输出8086汇编代码
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
 
       [ fun, _, _, _] ———————————— for


### 目标代码说明：8086汇编语言

 - 定义全局变量：在数据段中定义
 
       DATAS SEGMENT
           DATA DB 1D,2D ———————————— 数组DATA初始化，[1,2]
           a DB 0D ———————————— int a=0；
       DATAS ENDS
       
       这部分目标代码是通过符号表-总表的信息生成的
       
- 读取全局变量：

       MOV AX,DS:[0] ———————————— 取到数据段第一个，也就是DATA[0],1D
       MOV AX,DS:[2] ———————————— 取到数据段第三个，也就是a，0D
       
       在符号表-总表中，可以查到某全局变量在数据段中的偏移地址
       
 - 修改全局变量的值：
 
       MOV a,OFFSET 1D ———————————— a=1；
       MOV [DATA+1],offset 3D ———————————— DATA[1]=3;
       
       可以直接通过变量名修改变量的值而不需要通过符号表查询偏移地址
       
- 定义局部变量：在堆栈段定义，通过活动记录读写
 
       STACKS SEGMENT
           STK DB 100 DUP (0) ———————————— 定义一个堆栈段
       STACKS ENDS
       
       MOV AX,2D
       PUSH AX ———————————— 将变量值加入堆栈段。假设这句目标代码的原代码是int b=2,则活动记录将会记录当前偏移地址是存放变量b的
       
       POP AX ———————————— 在子程序的最后，清除堆栈段，释放临时变量
       
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
       
       查询符号表-函数表可获取局部变量在堆栈段中的偏移地址
       
- 修改局部变量的值：
 
       MOV AX,33H
       MOV SS:[BP+1],AX
       
       与全局变量不同的是，在这里仍然需要通过查询符号表-函数表来获取偏移地址才能修改局部变量的值

- 加减乘除基本运算：
       
       MOV AX,49D
       MOV BX,1D
       ADD AX,BX ———————————— 加法运算，结果保存在AX
       
       MOV AX,50D
       MOV BX,1D
       SUB AX,BX ———————————— 减法运算，结果保存在AX
    
       MOV AX,7D
       MOV BX,7D
       MUL BX    ———————————— 乘法运算，结果保存在AX
       
       MOV DX,0D
       MOV AX,345D
       MOV BX,7D
       DIV BX    ———————————— 除法运算，结果保存在AX，余数保留在DX

- 分支结构：

       MOV AH,03D
       MOV AL,02D
       CMP AH,AL
       JE JUMP1 ———————————— 如果AH==AL则跳转到JUMP1,否则向下执行
       MOV DL,32H
       MOV AH,02H
       INT 21H 
       JMP JUMP2 ———————————— 避免再次执行JUMP1,要调到JUMP1的下面
       JUMP1: ———————————— JUMP1要执行的内容
       MOV DL,31H
       MOV AH,02H
       INT 21H
       JUMP2: ———————————— JUMP2要执行的内容
       MOV AH,4CH
       INT 21H

       可以用在这里的转移指令：
       JA  ———————————— 大于
       JAE ———————————— 大于等于
       JB  ———————————— 小于
       JBE ———————————— 小于等于
       JE  ———————————— 等于
       JNE ———————————— 不等于

- 循环结构：

       MOV AL,0H
       MOV BL,39H
       MOV CX,02H
    
       WH: ———————————— while开始
       INC CX ———————————— 避免循环自动终结
       CMP AL,08H ———————————— while终止的条件，等价于最开始的while(?)和break的功能
       JE WE
       INC AL
       DEC BL
       CMP BL,37H
       JE WH ———————————— 等价于continue
       INC AL
       LOOP WH
       WE: ———————————— while终结
       MOV DL,AL
       ADD DL,30H
       MOV AH,02H
       INT 21H
      
- 子程序：

       DATAS SEGMENT
           R DB 31H ———————————— 约定存放结果的全局变量
       DATAS ENDS

       STACKS SEGMENT
           STK DB 20 DUP(0)
       STACKS ENDS

       CODES SEGMENT
           ASSUME CS:CODES,DS:DATAS,SS:STACKS

       F PROC NEAR ———————————— 子函数
           MOV R,OFFSET 32H
           RET
       F ENDP

       START: ———————————— 主函数
           MOV AX,DATAS
           MOV DS,AX
    
           MOV DL,R
           MOV AH,02H
           INT 21H
    
           CALL F ———————————— 调用子函数
    
           MOV DL,R
           MOV AH,02H
           INT 21H
    
           MOV AH,4CH
           INT 21H
       CODES ENDS
           END START
    

