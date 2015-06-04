package com.cramaniya.apps.dc.salcalc.util;

import com.cramaniya.apps.dc.salcalc.model.Wage;
import com.cramaniya.apps.dc.salcalc.model.WageProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Collections;
import java.util.List;

/**
 * @author Citra Ramaniya
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConversionUtilsTest {

	File testDir;

	@Before
	public void setUp() throws Exception {
		testDir = new File("test");
		testDir.mkdir();
	}

	@Test
	public void test1ToXML() throws Exception {
		WageProperty wageProperty = new WageProperty();
		wageProperty.setToDefaultValue();

		File result = new File(testDir, "config.xml");

		ConversionUtils.toXML(wageProperty, result);
	}

	@Test
	public void test2FromXML() throws Exception {
		File fromFile = new File(testDir, "config.xml");
		WageProperty wageProperty = ConversionUtils.fromXML(WageProperty.class, fromFile);

		log.info("{}", wageProperty);
	}

	@Test
	public void test3ToJSON() throws Exception {
		WageProperty wageProperty = new WageProperty();
		wageProperty.setToDefaultValue();

		File result = new File(testDir, "config.json");

		ConversionUtils.toJSON(wageProperty, result);
	}

	@Test
	public void test5FromJSON() throws Exception {
		File fromFile = new File(testDir, "config.json");
		WageProperty wageProperty = ConversionUtils.fromJSON(WageProperty.class, fromFile);

		log.info("{}", wageProperty);
	}

}