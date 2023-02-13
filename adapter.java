package com.example.testlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adapter {
    public static class  Parameter{
       public static int high;
       public static int weight;
       public static String profile;
    }
    private Parameter zhangsan=null;
    private Parameter lisi=null;
    private Parameter wangmazi=null;
    private String [] strs={"张三","里斯","王麻子"};
    private int [] value={20,30,25};
    private Parameter[] parameters={zhangsan,lisi,wangmazi};

    List<Map<String,Object>> list_item=new ArrayList<Map<String, Object>>();
    public static void main(String[] args) {
        adapter adp = new adapter();
        adp.init();
        adp.transfer();
        System.out.println("测试成功！");
    }
   public void init(){
        for(int i=0;i<3;i++){
            Map<String,Object> mapvector=new HashMap<String, Object>();
            mapvector.put("name",strs[i]);
            mapvector.put("years",value[i]);
            mapvector.put("parameter",parameters[i]);
            list_item.add(mapvector);
       }
   }
   public void transfer(){
        for (int i=0;i<3;i++){
            Map<String,Object> X=list_item.get(i);
            System.out.println(String.valueOf(X.get("name")));
            System.out.println(X.get("years").toString());
            System.out.println("NEXT!");
        }
   }
}
