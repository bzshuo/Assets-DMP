package com.mengs.springboot.utils;

import cn.hutool.core.map.MapUtil;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：ms(YX)
 * @description ：
 * @date ：Born in 2019/12/7 17:38
 */
public class CommonUtil {
    /**
     * 实体类转Map
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = MapUtil.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 实体类所有父节点都转换为对应的map
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanAllToMap(T bean) {
        Map<String, Object> map = MapUtil.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) instanceof List) {
                    List<T> list = (List) beanMap.get(key);
                    List<Map> mapList = new ArrayList<>();
                    for (T bean1 : list) {
                        if (bean1 instanceof Map) {
                            map.putAll((Map) bean1);
                            continue;
                        }
                        mapList.add(beanAllToMap(bean1));
                    }
                    map.put(key + "", mapList);
                } else if (null == beanMap.get(key) || beanMap.get(key) instanceof String) {
                    map.put(key + "", beanMap.get(key));
                } else {
                    map.put(key + "", beanAllToMap(beanMap.get(key)));
                }
            }
        }
        return map;
    }

    /**
     * 实体类转Map
     * @param object
     * @return
     */
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String getRate1(Object d) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("0.00");
        return df.format(d);
    }

    //newScale为小数点位数
    public static Double roundValue(Double d, int newScale) {

        Double retValue = null;
        if (d != null) {
            BigDecimal bd = new BigDecimal(d);
            retValue = bd.setScale(newScale,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return retValue;
    }

    public static String getRate(Object d) {
        return String.format("%.2f", d);
    }

    /**
     * 获取map中第一个数据值
     *
     * @param map 数据源
     * @return
     */
    public static Object getFirstOrNull(Map<String, Object> map) {
        Object obj = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }

    //去重
    public static List<Double> REDuplicate(List<Double> a) {
        HashSet h = new HashSet(a);
        a.clear();
        a.addAll(h);
        return a;
    }

    //重新排序
    public static List<Double> RESort(List<Double> a) {
        List<Double> sort = new ArrayList();
        for (Double b : a) {
            double index = b;
            sort.add(index);
        }
        Double[] arr = new Double[sort.size()];
        sort.toArray(arr);
        Arrays.sort(arr);
        sort = Arrays.asList(arr);
        return sort;
    }

    //transId
    public static String makeTransId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        String timeStamp = sdf.format(new Date());
        return timeStamp + makeRandom();
    }
    private static String makeRandom(){
        int random = (int) ((Math.random()*9+1)*10000000 );
        return String.valueOf(random);
    }
}
