package com.neo.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Xuzhiyuan on 2019/2/18.
 */
public class JsonFormat {
    private static ObjectMapper MAPPER = new ObjectMapper();


    public static void main(String[] args) throws Exception {

        final File file = new File("D:\\cache.json");
        final String s = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
        System.out.println(s);

        JavaType jvt = MAPPER.getTypeFactory().constructParametricType(Map.class,String.class,Object.class);
        Map<String, Object> urMap = MAPPER.readValue(s, jvt);

        System.out.println(MAPPER.writeValueAsString(urMap));
        System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(urMap));

    }

//        retMap = ismap(s);
//
//        for (String  key : retMap.keySet()
//             ) {
//            System.out.println(String.format("%s : %s",key,retMap.get(key).toString()));
//        }


//    private static HashMap<String, Object> ismap(String s) throws java.io.IOException {
//
//        HashMap<String, Object> retMap = new HashMap<>();
//
//        JavaType jvt = MAPPER.getTypeFactory().constructParametricType(Map.class,String.class,Object.class);
//        Map<String, Object> urMap = null;
//        try {
//            urMap = MAPPER.readValue(s, jvt);
//        }catch (Exception e){
//            System.out.println(s);
//            System.exit(0);
//        }
//
//        Iterator<Map.Entry<String, Object>> iterator = urMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            //System.out.println(iterator.next().toString());
//            final Map.Entry<String, Object> next = iterator.next();
//            if(next.getValue() instanceof Map){
//                retMap.put(next.getKey(),ismap(next.getValue().toString()));
//            }else if(next.getValue() instanceof List){
//                retMap.put(next.getKey(),islist(next.getValue().toString()));
//            }{
//                retMap.put(next.getKey(),next.getValue());
//            }
//
//        }
//        return retMap;
//    }

//    private static List<Object> islist(String s) throws java.io.IOException {
//
//        List<Object> retList = new ArrayList<Object>();
//
//        JavaType jvt = MAPPER.getTypeFactory().constructParametricType(List.class,String.class,Object.class);
//        List<Object> list = MAPPER.readValue(s, jvt);
//
//        Iterator<Object> iterator = list.iterator();
//        while (iterator.hasNext()){
//            //System.out.println(iterator.next().toString());
//            final Object next = iterator.next();
//            if(next instanceof Map){
//                retList.add(ismap(next.toString()));
//            }else if(next instanceof List){
//                retList.add(islist(next.toString()));
//            }{
//                retList.add(next.toString());
//            }
//
//        }
//        return retList;
//    }

    @Test
    public void test1() throws IOException {   //格式化输出测试
        ObjectMapper mapper = new ObjectMapper();
        List list = new ArrayList<>();
        Map map1 = new HashMap<>();
        map1.put("id","111");
        map1.put("users", Arrays.asList(1,2,3,4,5,5));
        list.add(map1);
        Map map2 = new HashMap();
        map2.put("name","zqw");
        list.add(map2);
        //普通输出
        System.out.println(mapper.writeValueAsString(list));
        //格式化/美化/优雅的输出
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list));


        String str = "[ {\n" +
                "  \"id\" : \"111\",\n" +
                "  \"users\" : [ 1, 2, 3, 4, 5, 5 ]\n" +
                "}, {\n" +
                "  \"name\" : \"zqw\"\n" +
                "} ]";
        Object obj = mapper.readValue(str, Object.class);
        System.out.println(mapper.writeValueAsString(obj));
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }


}
