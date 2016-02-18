package com.gotra.kbdt.core.exceptions;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author gotra
 */

public class UncaughtExceptions implements Thread.UncaughtExceptionHandler {

    static Logger logger = Logger.getLogger(UncaughtExceptions.class.getName());

    public void uncaughtException(Thread t, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        e.printStackTrace(out);
        logger.error(t.getName() + " thread. " + sw.toString(), e);
        System.out.println(t.getName() + " thread. " + sw.toString());
    }
}
