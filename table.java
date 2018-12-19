import java.util.*;
public class table{
    class var{//全局变量信息
        String name;//变量名
        String tp;//类型：int int[] function char string
        int ofad;//偏移地址
        int other;//（如类型为函数，则指向函数表中的某一个。如类型为数组，则存放数组长度）
    }

    class func{//函数信息
        String name;//函数名
        List<String> xctp=new ArrayList<String>();//形参类型
        List<String> xcname=new ArrayList<String>();//形参名
        List<var> vt=new ArrayList<var>();//函数中的临时变量
    }

    List<var> synbl=new ArrayList<var>();;//全局变量总表
    List<func> pfinfl=new ArrayList<func>();;//函数表
}