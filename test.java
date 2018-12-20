import java.util.*;
public class test{
    public static void main(String[] args) throws Exception{
        table t=new table();
        table.vari s=t.new vari();
        s.name="?";
        t.synbl=new ArrayList<table.vari>();
        t.synbl.add(s);
        System.out.println(t.synbl.get(0).name);
    }
}