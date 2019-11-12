package core.test.utils;

import org.apache.log4j.Logger;

import io.qameta.allure.Attachment;

public class BaseUtils {

	private static final Logger LOG = Logger.getLogger(BaseUtils.class.getName());
	
	/* This method make Text attachment for Allure report */
	@Attachment(value = "{0}", type = "text/plain")
	public static synchronized String attachText(final String nameOfAttachment, final String bodyOfMessage) {

		LOG.info(String.format("TID [%d] - Attached to allure file [%s].", Thread.currentThread().getId(),
				nameOfAttachment));

		return bodyOfMessage;
	}

	@Attachment(value = "{0}", type = "text/html")
	public static synchronized String attachHtml(final String nameOfAttachment, final String bodyOfMessage) {

		LOG.info(String.format("TID [%d] - Attached to allure file [%s].", Thread.currentThread().getId(),
				nameOfAttachment));

		return bodyOfMessage;
	}
	
}
