package top.yh.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author yuhao
 * @date 2023/2/2
 * 读取数据文件
 **/
public class GetProperties {
    /**
     * 存放property的数据
     * key：文件名
     * value：是property中的数据 key，value
     */
    private static final Map<String, Map<String, String>>
            PROPERTY_DATA_MAP = new HashMap<>();

    /**
     * 不能进行初始化
     */
    private GetProperties() {

    }

    /**
     * 获取property文件对象
     * @param filePath 文件路径
     * @return properties对象
     */
    private static Properties getProperty(String filePath) {
        //初始化对象
        Properties pps = new Properties();
        //读取文件 获取输入流
        try (InputStream in = GetProperties.class.getResourceAsStream(filePath)) {
            //以utf-8的编码格式进行读取
            pps.load(new InputStreamReader(Objects.requireNonNull(in), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回对象
        return pps;
    }

    /**
     * 读取Properties的全部信息
     */
    public static synchronized Map<String, String> getAllProperties(String filePath) {
        //判断是否读取过数据
        if (isHaveProperty(filePath)) {
            //如果读取过 直接返回
            return PROPERTY_DATA_MAP.get(filePath);
        }
        //初始化map 存放property文件中的数据
        Map<String, String> dataMap = new HashMap<>(6);
        //获取properties对象
        Properties pps = getProperty(filePath);
        //得到配置文件
        Enumeration<?> en = pps.propertyNames();
        //循环读入
        while (en.hasMoreElements()) {
            //获得key
            String strKey = (String) en.nextElement();
            //获得value
            String strValue = pps.getProperty(strKey);
            //记录数据
            dataMap.put(strKey, strValue);
        }
        //存放数据 避免重复读入文件 浪费资源
        PROPERTY_DATA_MAP.put(filePath, dataMap);
        return dataMap;
    }

    /**
     * 查看是否已经读取过这个文件
     */
    private static boolean isHaveProperty(String filePath) {
        //查看是否有这个文件
        //如果有 则不等于null 如果没有 就返回null
        return PROPERTY_DATA_MAP.get(filePath) != null;
    }

    /**
     * 获取具体的值
     * @param filePath 文件名
     * @param key 存放数据的key
     * @return 返回value
     */
    public static String getSpecificData(String filePath,String key){
        Map<String, String> map = getAllProperties(filePath);
        return Optional.of(map.get(key)).orElse("100");
    }
}