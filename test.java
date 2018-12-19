import java.util.*;
public class test{
    public static void main(String[] args) throws Exception{
        table t=new table();
        table.var s=t.new var();
        s.name="?";
        t.synbl=new ArrayList<table.var>();
        t.synbl.add(s);
        System.out.println(t.synbl.get(0).name);
    }
}