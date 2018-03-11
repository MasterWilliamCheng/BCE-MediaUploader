package com.snh.util;

import org.apache.log4j.Logger;

/**
 */
public class LogUtil {
    public static final Logger logger=Logger.getLogger(LogUtil.class);

    public static boolean isDebugEnabled(){
        return logger.isDebugEnabled();
    }

    public static boolean isInfoEnabled(){
        return logger.isInfoEnabled();
    }

    public static void debug(Exception ex){
        logger.debug(ExceptionUtils.getExceptionDetail(ex));
    }

    public static void debug(String message){
        logger.debug(message);
    }

    public static void debug(String message,Exception ex){
        logger.debug(message + "." + ExceptionUtils.getExceptionDetail(ex));
    }

    public static void info(Exception ex){
        logger.info(ExceptionUtils.getExceptionDetail(ex));
    }

    public static void info(String message){
        logger.info(message);
    }

    public static void info(String message,Exception ex){
        logger.info(message + "." + ExceptionUtils.getExceptionDetail(ex));
    }

    public static void warn(Exception ex){
        logger.warn(ExceptionUtils.getExceptionDetail(ex));
    }

    public static void warn(String message){
        logger.warn(message);
    }

    public static void warn(String message,Exception ex){
        logger.warn(message + "." + ExceptionUtils.getExceptionDetail(ex));
    }

    public static void error(Exception ex){
        logger.error(ExceptionUtils.getExceptionDetail(ex));
    }

    public static void error(String message){
        logger.error(message);
    }

    public static void error(String message,Exception ex){
        logger.error(message + "." + ExceptionUtils.getExceptionDetail(ex));
    }


}
