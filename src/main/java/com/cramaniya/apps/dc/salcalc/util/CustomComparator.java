package com.cramaniya.apps.dc.salcalc.util;

import com.cramaniya.apps.dc.salcalc.annotation.FieldOrder;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Comparator for ordering class fields.
 *
 * @author Citra Ramaniya
 * @see <a href="http://stackoverflow.com/questions/1097807/java-reflection-is-the-order-of-class-fields-and-methods
 * -standardized">Stackoverflow - java-reflection-is-the-order-of-class-fields-and-methods-standardized</a>
 */
public class CustomComparator {

	public static Comparator<Field> FieldComparator
			= (o1, o2) -> {
				FieldOrder fieldOrder1 = o1.getAnnotation(FieldOrder.class);
				FieldOrder fieldOrder2 = o2.getAnnotation(FieldOrder.class);

				if (fieldOrder1 != null && fieldOrder2 != null) {
					return fieldOrder1.value() - fieldOrder2.value();
				} else if (fieldOrder1 != null && fieldOrder2 == null) {
					return -1;
				} else if (fieldOrder1 == null && fieldOrder2 != null) {
					return 1;
				}
				return o1.getName().compareTo(o2.getName());
			};
}
