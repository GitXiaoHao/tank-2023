package top.yh;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/2
 **/
public class PropertyTest {
    @Test
    public void test(){
        Map<String,String> list = new HashMap<>();
//        list.add("dsa");
        String  da = list.get("da");
        System.out.println(da);
    }
}
