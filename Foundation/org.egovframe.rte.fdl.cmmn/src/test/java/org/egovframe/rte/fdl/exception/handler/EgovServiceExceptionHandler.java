package org.egovframe.rte.fdl.exception.handler;

import org.egovframe.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import org.egovframe.rte.mail.SimpleSSLMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class EgovServiceExceptionHandler implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovServiceExceptionHandler.class);

	@Resource(name = "otherSSLMailSender")
	private SimpleSSLMail mailSender;

	public void occur(Exception ex, String packageName) {
		LOGGER.debug(" EgovServiceExceptionHandler run...............{}", ex.getMessage());
		try {
			mailSender.send("[Exception Handler Subject]" , ex.getMessage() +" <strong>occur</strong> !!! find the exception problem ");
			LOGGER.debug(" sending a alert mail  is completed ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
