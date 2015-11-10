package com.arvin.koalapush.util;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.os.Environment;
import android.provider.Settings.Global;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jUtil {
	private static Logger gLogger = null;

	public static Log4jUtil getLogger(String className) {
		return new Log4jUtil(className);
	}
	
	public Log4jUtil(String className){
			final LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(Environment.getExternalStorageDirectory()
					+ File.separator + Log4jConfig.appName + File.separator
					+ "koala_push.log");
			logConfigurator.setRootLevel(Level.DEBUG);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
			// logConfigurator.setMaxFileSize(1024);
			logConfigurator.setImmediateFlush(true);
			logConfigurator.configure();
			gLogger = Logger.getLogger(className);
	}
	
	
	public void info(Object message) {
		if(Log4jConfig.showLog){
			gLogger.info(message);
		}
	}

	public void info(Object message, Throwable t) {
		if(Log4jConfig.showLog){
			gLogger.info(message, t);
		}
	}
	
	public void error(Object message) {
		if(Log4jConfig.showLog){
			gLogger.error(message);
		}
	}

	public void error(Object message, Throwable t) {
		if(Log4jConfig.showLog){
			gLogger.error(message, t);
		}
	}
	
	public void warn(Object message) {
		if(Log4jConfig.showLog){
			gLogger.warn(message);
		}
	}

	public void warn(Object message, Throwable t) {
		if(Log4jConfig.showLog){
			gLogger.warn(message, t);
		}
	}
	
	public void debug(Object message) {
		if(Log4jConfig.showLog){
			gLogger.debug(message);
		}
	}

	public void debug(Object message, Throwable t) {
		if(Log4jConfig.showLog){
			gLogger.debug(message, t);
		}
	}
	/**
	 * @return the gLogger
	 */
	public static Logger getgLogger() {
		return gLogger;
	}
}
