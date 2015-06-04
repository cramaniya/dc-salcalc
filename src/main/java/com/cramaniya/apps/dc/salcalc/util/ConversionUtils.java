package com.cramaniya.apps.dc.salcalc.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 * @author Citra Ramaniya
 */
@Slf4j
public class ConversionUtils {

	public static <T> void toXML(T obj, File resultFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(obj, resultFile);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXML(Class<T> resultClass, File xmlFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(resultClass);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (T) unmarshaller.unmarshal(xmlFile);
	}

	@SuppressWarnings("unchecked")
	public static <T> void toJSON(T obj, File resultFile) throws IllegalAccessException, InstantiationException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		// convert user object to json string, and save to a file
		mapper.writeValue(resultFile, obj);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJSON(Class<T> resultClass, File jsonFile) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// read from file, convert it to user class
		return mapper.readValue(jsonFile, resultClass);
	}

}
