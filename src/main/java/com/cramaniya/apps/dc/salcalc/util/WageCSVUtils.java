package com.cramaniya.apps.dc.salcalc.util;

import com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig;
import com.cramaniya.apps.dc.salcalc.model.Wage;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;

/**
 * @author Citra Ramaniya
 */
@Slf4j
public class WageCSVUtils {

	private static final String COLON_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private DateFormat dateFormat;

	private File csvFile;

	/**
	 * Map for class fieldName and its class.
	 */
	private Map<String, Class> fieldMap = new LinkedHashMap<>();

	public WageCSVUtils(String fileName) {
		Field[] fields = Wage.class.getDeclaredFields();
		Arrays.sort(fields, CustomComparator.FieldComparator);

		for (Field field : fields) {
			if (field.getName().equals("log") || field.getName().equals("serialVersionUID")) continue;
			fieldMap.put(field.getName(), field.getType());
		}

		csvFile = new File(fileName);

		dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	}

	public List<Wage> readFromCSV() {

		BufferedReader bufferedReader = null;
		try {
			List<Wage> result = new ArrayList<>();
			String line;

			// create file reader
			bufferedReader = new BufferedReader(new FileReader(csvFile));

			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.split(COLON_DELIMITER);
				if (fieldMap.size() > 0) {
					Wage wage = new Wage();

					int i = 0;
					for (Map.Entry<String, Class> fieldClassEntry : fieldMap.entrySet()) {
						Field field = Wage.class.getDeclaredField(fieldClassEntry.getKey());
						if (field.getName().equals("log")) continue;
						Class type = fieldClassEntry.getValue();
						setObjectField(field, type, wage, tokens[i]);
						i++;
					}
					result.add(wage);
				}
			}
			return result;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public Wage findWage(Wage wage) {
		assert wage != null;
		List<Wage> list = readFromCSV();
		if (list != null) {
			for (Wage entry : list) {
				if (wage.equals(entry)) {
					return entry;
				}
			}
		}
		return null;
	}

	public void addToCSV(Wage entry) {
		assert entry != null;

		if (!csvFile.exists() || csvFile.length() == 0) {
			try {
				boolean created = csvFile.createNewFile();
				if (!created) {
					throw new IOException("Error during file creation.");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			removeDuplicateWage(entry);
		}

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(csvFile, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (String fieldName : fieldMap.keySet()) {
				Field field = Wage.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				if (field.getType().equals(Date.class)) {
					Date date = (Date) field.get(entry);
					bufferedWriter.append(dateFormat.format(date)).append(COLON_DELIMITER);
				} else {
					bufferedWriter.append(String.valueOf(field.get(entry))).append(COLON_DELIMITER);
				}
				field.setAccessible(false);
			}
			bufferedWriter.append(NEW_LINE_SEPARATOR);
			bufferedWriter.close();

			log.info("Wage saved!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setObjectField(Field field, Class type, Wage wage, String token) throws ParseException,
			IllegalAccessException {
		field.setAccessible(true);
		if (type.equals(Date.class)) {
			field.set(wage, dateFormat.parse(token));
		} else if (type.equals(Month.class)) {
			field.set(wage, Month.valueOf(token));
		} else if (type.equals(Integer.class)) {
			field.set(wage, Integer.valueOf(token));
		} else if (type.equals(Boolean.class)) {
			field.set(wage, Boolean.valueOf(token));
		} else if (type.equals(BigDecimal.class)) {
			field.set(wage, new BigDecimal(token));
		}
		field.setAccessible(false);
	}

	private void removeDuplicateWage(Wage newEntry) {
		assert newEntry != null;
		Wage duplicateWage = findWage(newEntry);
		if (duplicateWage != null) {
			removeLine(duplicateWage);
		}
	}

	private boolean removeLine(Wage oldEntry) {
		BufferedReader reader = null;
		File tempFile;
		BufferedWriter writer = null;
		boolean successful = false;

		try {
			reader = new BufferedReader(new FileReader(csvFile));
			tempFile = new File(WagePropertyConfig.resourcesDir, "temp.csv");
			writer = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = oldEntry.getMonth() + COLON_DELIMITER + oldEntry.getYear();
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				String trimmedLine = currentLine.trim();
				if (trimmedLine.contains(lineToRemove)) continue;
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			successful = tempFile.renameTo(csvFile);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return successful;
	}

}
