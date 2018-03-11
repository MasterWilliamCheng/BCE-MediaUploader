package com.snh.util;

/**
 * Created by Gavin on 2014/8/27.
 */
public class ExceptionUtils {
    /**
     * 获取exception详情信息
     *
     * @param e
     *            Excetipn type
     * @return String type
     */
    public static String getExceptionDetail(Exception e) {
        StringBuffer msg = new StringBuffer("null");
        if (e != null) {
            msg = new StringBuffer("");
            String message = e.toString();
            int length = e.getStackTrace().length;
            if (length > 0) {
                msg.append(message + "\n");
                for (int i = 0; i < length; i++) {
                    msg.append("\t" + e.getStackTrace()[i] + "\n");
                }
            } else {
                msg.append(message);
            }
        }
        return msg.toString();
    }

    public static String getExceptionDetails2(Exception e){
        StringBuffer msg = new StringBuffer("null");
        if (e != null) {
            msg = new StringBuffer("");
            msg.append(e.toString());
            StackTraceElement stackTraceElements[] = e.getStackTrace();
            for(StackTraceElement stackTraceElement : stackTraceElements ){
                msg.append("\t类名：：" + stackTraceElement.getClassName() + "\n");
                msg.append("\t方法名：" + stackTraceElement.getMethodName() + "\n");
                msg.append("\t行号：" + stackTraceElement.getLineNumber() + "\n");
            }
        }
        return msg.toString();
    }

    public static String getExceptionDetails2(Exception e,Class clz){
        StringBuffer msg = new StringBuffer("null");
        if (e != null) {
            msg = new StringBuffer("");
            msg.append(e.toString());
            StackTraceElement stackTraceElements[] = e.getStackTrace();
            for(StackTraceElement stackTraceElement : stackTraceElements ){
                if(stackTraceElement.getClassName().startsWith(clz.getName())){
                    msg.append("\t类名：：" + stackTraceElement.getClassName() + "\n");
                    msg.append("\t方法名：" + stackTraceElement.getMethodName() + "\n");
                    msg.append("\t行号：" + stackTraceElement.getLineNumber() + "\n");
                }
            }
        }
        return msg.toString();
    }

}
