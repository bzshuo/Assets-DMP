package com.mengs.springboot.utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtils {

    /**
     * *******************************************
     * method name   : trim
     * description   : 去掉字符串的空格如果为null则返回""
     *
     * @param : @param str
     * @param : @return
     * @return : String
     * @see :
     ********************************************/
    public static String trim(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    public static String trim(Object str) {
        if (str == null) {
            return "";
        } else {
            return String.valueOf(str).trim();
        }
    }

    /**
     * ********************************************
     * method name   : isEmpty
     * description   : 判断对象，集合，Map是否为空
     *
     * @param : @param v
     * @param : @return
     * @return : boolean
     * @see :
     * *******************************************
     */
    public static boolean isEmpty(Object v) {
        return isEmpty(v, true);
    }

    /**
     * ********************************************
     * method name   : isEmpty
     * description   : 判断对象是否为空
     *
     * @param : @param v
     * @param : @param trim
     * @param : @return
     * @return : boolean
     * @see :
     * *******************************************
     */
    public static boolean isEmpty(Object v, boolean trim) {
        if (v == null) {
            return true;
        }
        if ((v instanceof String)) {
            String sv = (String) v;
            return sv.trim().length() == 0;
        }
        if ((v instanceof Collection)) {
            Collection<?> c = (Collection<?>) v;
            return c.size() == 0;
        }
        if ((v instanceof Map)) {
            Map<?, ?> m = (Map<?, ?>) v;
            return m.size() == 0;
        }
        if (isArray(v.getClass())) {
            return getArrayLength(v) == 0;
        }
        return false;
    }

    /**
     * ********************************************
     * method name   : getArrayLength
     * description   : 获取数组长度
     *
     * @param : @param array
     * @param : @return
     * @return : int
     * @see :
     * *******************************************
     */
    public static int getArrayLength(Object array) {
        return (array instanceof List) ? ((List<?>) array).size()
                : array == null ? 0 : Array.getLength(array);
    }

    /**
     * ********************************************
     * method name   : getUUID
     * description   : 获取uuid
     *
     * @param : @return
     * @return : String
     * @see :
     * *******************************************
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    /**
     * ********************************************
     * method name   : getUUID
     * description   : 获取uuid
     *
     * @param : @return
     * @return : String
     * @see :
     * *******************************************
     */
    public static String getUUIDRomve() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * ********************************************
     * method name   : isArray
     * description   : 判断对象是否为数组
     *
     * @param : @param c
     * @param : @return
     * @return : boolean
     * @see :
     * *******************************************
     */
    public static boolean isArray(Class<?> c) {
        return (c.isArray()) || (List.class.isAssignableFrom(c));
    }

    /**
     * ********************************************
     * description  : 格式化字符串
     *
     * @param : @param sourceDate 用零补齐
     * @param : @param formatLength 总长度
     * @param : @return
     * @see : sourceDate= 1,formatLength=6 返回000001
     * *******************************************
     */
    public static String frontCompWithZore(long sourceDate, int formatLength) {
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }

 /**
     * ********************************************
     * description  : 格式化字符串
     *
     * @param : @param sourceDate 用零补齐
     * @param : @param formatLength 总长度
     * @param : @return
     * @see : sourceDate= 1,formatLength=6 返回000001
     * *******************************************
     */
    public static String frontCompWithZore(long sourceDate, Long formatLength) {
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }

    /**
     * ********************************************
     * method name   : checkNull
     * description   : 校验是否为 null, null抛出异常
     *
     * @param : @param value
     * @param : @param name
     * @return : void
     * @see :
     * *******************************************
     */
    public static void checkNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(" the '" + name
                    + "' is null argument! ");
        }
    }

    /**
     * ********************************************
     * method name   : checkEmpty
     * description   : 校验是 null或者"", 是则抛出异常
     *
     * @param : @param value
     * @param : @param name
     * @return : void
     * @see :
     * *******************************************
     */
    public static void checkEmpty(Object value, String name) {
        checkEmpty(value, true, name);
    }

    /**
     * ********************************************
     * method name   : checkEmpty
     * description   : 校验是 null或者"", 是则抛出异常
     *
     * @param : @param value
     * @param : @param trim
     * @param : @param name
     * @return : void
     * @see :
     * *******************************************
     */
    public static void checkEmpty(Object value, boolean trim, String name) {
        if (isEmpty(value, trim)) {
            throw new IllegalArgumentException(" the '" + name + "' is "
                    + (value == null ? "null" : "empty") + " argument! ");
        }
    }

    /**
     * 判断字符串是否只包含unicode数字。
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回<code>true</code>。
     * </p>
     * <pre>
     * StringUtil.isNumeric(null)   = false
     * StringUtil.isNumeric("")     = true
     * StringUtil.isNumeric("  ")   = false
     * StringUtil.isNumeric("123")  = true
     * StringUtil.isNumeric("12 3") = false
     * StringUtil.isNumeric("ab2c") = false
     * StringUtil.isNumeric("12-3") = false
     * StringUtil.isNumeric("12.3") = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode数字组成，则返回<code>true</code>
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank("")        = true
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isBlank("bob")     = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否不是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = false
     * StringUtil.isBlank("")        = false
     * StringUtil.isBlank(" ")       = false
     * StringUtil.isBlank("bob")     = true
     * StringUtil.isBlank("  bob  ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }


    /**
     * @return boolean
     * @Description 判断字符串是否为空或者空格
     * @Date 2019/4/29 15:16
     * @Param [tmp]
     **/
    public static boolean isEmptyTrim(String tmp) {
        if (tmp == null || tmp.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static int strToInt(Object obj) {
        try {
            return Integer.valueOf(strTrim(obj));
        } catch (Exception e) {
            return 0;
        }
    }

    public static String strTrim(Object obj) {
        return obj != null ? obj.toString().trim() : "";
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToNull(Object str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 扩展并左对齐字符串，用指定字符填充右边。
     * <pre>
     * StringUtil.alignLeft(null, *, *)     = null
     * StringUtil.alignLeft("", 3, 'z')     = "zzz"
     * StringUtil.alignLeft("bat", 3, 'z')  = "bat"
     * StringUtil.alignLeft("bat", 5, 'z')  = "batzz"
     * StringUtil.alignLeft("bat", 1, 'z')  = "bat"
     * StringUtil.alignLeft("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     要对齐的字符串
     * @param size    扩展字符串到指定宽度
     * @param padChar 填充字符
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignLeft(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }

        int pads = size - str.length();

        if (pads <= 0) {
            return str;
        }

        return alignLeft(str, size, String.valueOf(padChar));
    }

    /**
     * 扩展并左对齐字符串，用指定字符串填充右边。
     * <pre>
     * StringUtil.alignLeft(null, *, *)      = null
     * StringUtil.alignLeft("", 3, "z")      = "zzz"
     * StringUtil.alignLeft("bat", 3, "yz")  = "bat"
     * StringUtil.alignLeft("bat", 5, "yz")  = "batyz"
     * StringUtil.alignLeft("bat", 8, "yz")  = "batyzyzy"
     * StringUtil.alignLeft("bat", 1, "yz")  = "bat"
     * StringUtil.alignLeft("bat", -1, "yz") = "bat"
     * StringUtil.alignLeft("bat", 5, null)  = "bat  "
     * StringUtil.alignLeft("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str    要对齐的字符串
     * @param size   扩展字符串到指定宽度
     * @param padStr 填充字符串
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignLeft(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }

            return str.concat(new String(padding));
        }
    }

    public static String getRpid(String prefix) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(System.currentTimeMillis());
        buffer.append(String.format("%04d", new Random().nextInt(10000)));
        return buffer.toString();
    }

    /**
     * @return :   java.util.Set<java.lang.String>
     * @methodname :   getSetByStr
     * @Description :  根据分隔符把str分割,并存到set中
     * @Param :   [str]
     **/
    public static Set<String> getSetByStrAndSplitChar(String str, String splitChar) {
        if (splitChar.equals(str.substring(0, 1))) {
            str = str.substring(1);
        }
        Set<String> set = new HashSet<>();
        String[] all = str.split(splitChar);
        for (String a : all) {
            set.add(a);
        }
        return set;
    }


    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String get6Random() {
        Random random = new Random();
        // 保证这个随机码的位数是4位的
        int num = random.nextInt(1000000);
        random.setSeed(111222333);

        return String.format("%06d", num);
    }

    /*
     * 将字符串"分"转换成"元"（短格式），如：100分被转换为1元。
     */
    public static String Cent2DollarShort(String s) {
        String ss = Cent2Dollar(s);
        ss = "" + Double.parseDouble(ss);
        if (ss.endsWith(".0")) {
            return ss.substring(0, ss.length() - 2);
        }
        if (ss.endsWith(".00")) {
            return ss.substring(0, ss.length() - 3);
        }
        return ss;
    }

    /*
     * 将字符串"分"转换成"元"（长格式），如：100分被转换为1.00元。
     */
    public static String Cent2Dollar(String s) {
        long l = 0;
        try {
            if (s.charAt(0) == '+') {
                s = s.substring(1);
            }
            l = Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        boolean negative = false;
        if (l < 0) {
            negative = true;
            l = Math.abs(l);
        }
        s = Long.toString(l);
        if (s.length() == 1) {
            return (negative ? ("-0.0" + s) : ("0.0" + s));
        }
        if (s.length() == 2) {
            return (negative ? ("-0." + s) : ("0." + s));
        } else {
            return (negative ? ("-" + s.substring(0, s.length() - 2) + "." + s
                    .substring(s.length() - 2)) : (s.substring(0,
                    s.length() - 2)
                    + "." + s.substring(s.length() - 2)));
        }
    }


    /**
     * 分转万元,保留两位小数,没有四舍五入
     */
    public static String Cent2Thousand(BigDecimal value) {
        BigDecimal decimal = value.divide(new BigDecimal("1000000"));
        String decimalStr = String.format("%.2f", decimal);
        return decimalStr;
    }

    /*  */

    /**
     * 元转万元且保留两位小数并四舍五入
     *//*
    public static String getNumberWanTwo(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        DecimalFormat formater = new DecimalFormat("00");
        formater.setRoundingMode(RoundingMode.HALF_UP);
        String rs = formater.format(decimal);
        return rs;
    }
*/
    public static void main(String[] args) {
//        System.out.println(getNumberWan("12353"));
//        System.out.println(getNumberWanTwo("1235"));

        // 具体的金额（单位元）
        String value = "1013000";
        BigDecimal bigDecimal = new BigDecimal(value);
// 转换为万元（除以10000）
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("1000000"));
        System.out.println(decimal.setScale(2, RoundingMode.HALF_UP));
//        String decimalStr = decimal.toString();
        String decimalStr = String.format("%.2f", decimal);
        System.out.println(decimalStr);
    }

    //生成六位的随机码
    public static String code6(){
        String code = String.valueOf(ThreadLocalRandom.current().ints(100000, 1000000)
                .distinct().limit(1).findFirst().getAsInt());
        return code;
    }

}