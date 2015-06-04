package com.cramaniya.apps.dc.salcalc.configuration;

import com.cramaniya.apps.dc.salcalc.App;
import com.cramaniya.apps.dc.salcalc.model.WageProperty;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

import static com.cramaniya.apps.dc.salcalc.util.ConversionUtils.fromJSON;
import static com.cramaniya.apps.dc.salcalc.util.ConversionUtils.toJSON;

/**
 * @author Citra Ramaniya
 */
@Slf4j
public class WagePropertyConfig {

	public static final String APP_NAME = "Tiffany's Wage Calculator App";

	public static final String APP_VERSION = "0.1";

	public static final String APP_CONFIG_DIR = "dc-salcalc-config";

	private static final String PROPERTIES_FILE = "config.json";

	public static final String STORAGE_FILE = "wage.csv";

	public static File resourcesDir;

	private static File configFile;

	public static WageProperty currentWageProperty;

	public static final boolean logConfigDetail = false;

	static {
		try {
			init();
			assert resourcesDir != null;
			assert configFile != null;
			assert currentWageProperty != null;

		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
	}

	/**
	 * Initializes the resource directory and configuration file.
	 */
	private static void init() throws JAXBException, IOException, URISyntaxException {
		if (currentWageProperty == null) {
			log.info("Setting wage configuration...");

			File jarParent = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
					.getParentFile();
			resourcesDir = new File(jarParent, APP_CONFIG_DIR);
			resourcesDir.mkdir();

			assert resourcesDir != null;
			configFile = new File(resourcesDir, PROPERTIES_FILE);
			if (!configFile.exists()) {
				setDefaultSettings();
			} else {
				currentWageProperty = fromJSON(WageProperty.class, configFile);
				assert currentWageProperty != null;
			}

			 printWageConfiguration();
		}
	}

	/**
	 * Loads new wage configuration.
	 */
	public static void load(WageProperty wageProperty, boolean save) {
		log.info("Saving new wage configuration...");
		assert wageProperty != null;
		currentWageProperty = wageProperty;
		if (save) {
			assert configFile != null;
			createConfigFile();
		}

		 printWageConfiguration();
	}

	/**
	 * Creates default config file and sets the wage configuration to the default values.
	 */
	private static void setDefaultSettings() {
		currentWageProperty = new WageProperty();
		currentWageProperty.setToDefaultValue();

		try {
			toJSON(currentWageProperty, configFile);
			assert configFile != null;
		} catch (IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

//		createConfigFile();
	}

	/**
	 * Creates new config.xml file from {@link #currentWageProperty} object.
	 */
	private static void createConfigFile() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(WageProperty.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(currentWageProperty, configFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static void printWageConfiguration() {
		if (logConfigDetail) {
			WageProperty.class.getDeclaredFields();
			for (Field field : WageProperty.class.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					log.debug(" - {} : {}", field.getName(), field.get(currentWageProperty));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} finally {
					field.setAccessible(false);
				}
			}
		}
		log.info("Wage Property loaded!");
	}

}
