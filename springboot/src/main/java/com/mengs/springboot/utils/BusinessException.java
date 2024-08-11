package com.mengs.springboot.utils;

/**
 * 业务异常类。
 *
 * @author mall
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = -1657938434382769721L;

    private String[] replaceStrs = null;

    /**
     * 若此字段不为null和""，则页面上显示它，
     * 忽略messageCode，不通过messageCode取值。
     */
    private String displayMsg = null;

    /**
     * default ctor.
     */
    public BusinessException() {
        super();
    }

    /**
     * ctor.
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * ctor.
     * @param message 异常消息
     * @param cause 抛出原因对象
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * ctor.
     * @param cause 异常致因
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

    /**
     * ctor.
     * @param messageCode 异常编码
     * @param message 异常消息
     */
    public BusinessException(String messageCode, String message) {
        super(messageCode, message);
        setMessageCode(messageCode);
    }

    /**
     * ctor.
     * @param messageCode 异常编码
     * @param message 异常消息
     * @param cause 异常致因
     */
    public BusinessException(String messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
        setMessageCode(messageCode);
    }

    /**
     * ctor.
     * @param messageCode 异常编码
     * @param message 异常消息
     * @param replaceStrs 替换字符串
     */
    public BusinessException(String messageCode, String message, String... replaceStrs) {
        super(messageCode, message);

        setReplaceStrs(replaceStrs);
    }

    /**
     * ctor.
     * @param messageCode 异常编码
     * @param message 异常消息
     * @param cause 异常致因
     * @param replaceStrs 替换字符串
     */
    public BusinessException(String messageCode, String message, Throwable cause, String... replaceStrs) {
        super(messageCode, message, cause);
        setMessageCode(messageCode);
        setReplaceStrs(replaceStrs);
    }

    /**
     * 设置替换字符串数组。
     * @param replaceStrs 替换字符串数组
     */
    public void setReplaceStrs(String[] replaceStrs) {
        this.replaceStrs = replaceStrs;
    }

    /**
     * 取得替换字符串数组。
     * @return 替换字符串数组
     */
    public String[] getReplaceStrs() {
        return replaceStrs;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }

    public String getDisplayMsg() {
        return displayMsg;
    }
}
