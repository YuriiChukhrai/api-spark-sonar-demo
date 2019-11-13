package core.qa.demo.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BaseUtils {
	
	private BaseUtils() {
		throw new UnsupportedOperationException("Illegal access to private constructor");
	}

	private static final Logger LOG = Logger.getLogger(BaseUtils.class.getName());

	public static boolean isOs(final String osNameExpected) {
		final String osNameActual = System.getProperty("os.name").trim().toUpperCase();
		LOG.info(String.format("Detected OS [%s]", osNameActual));
		return osNameActual.contains(osNameExpected.toUpperCase());
	}

	public static final <T> T stringXmlToObj(final String stringXml, final Class<T> classOfT) {

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Object instanceXml = null;

		try {
			db = dbf.newDocumentBuilder();

			final Document doc = db.parse(new ByteArrayInputStream(stringXml.getBytes(StandardCharsets.UTF_8)));

			doc.getDocumentElement().normalize();

			instanceXml = JAXBContext.newInstance(classOfT).createUnmarshaller().unmarshal(doc);
		} catch (ParserConfigurationException | SAXException | IOException | JAXBException e) {
			System.err.println(String.format("ERROR - MSG [%s]. Incorrect XML content [%s] for schema [%s]",
					e.getMessage(), stringXml, classOfT.getName()));
			e.printStackTrace();
		}
		return (T) instanceXml;
	}

	public static final <T> String objToStringXml(Object objectXml, Class<T> classOfT, boolean isFormatedOutput) {
		final StringWriter sw = new StringWriter();

		final JAXBContext jaxbContext;

		try {
			jaxbContext = JAXBContext.newInstance(classOfT);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			/* Output pretty printed */
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormatedOutput);
			jaxbMarshaller.marshal(objectXml, sw);

		} catch (JAXBException e) {
			LOG.error(String.format("Error. MSG [%s]", e.getMessage()));
			e.printStackTrace();
		}
		return sw.toString();
	}

}
