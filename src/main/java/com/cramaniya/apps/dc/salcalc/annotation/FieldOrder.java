package com.cramaniya.apps.dc.salcalc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use for ordering fields.
 *
 * @author Citra Ramaniya
 * @see <a href="http://www.mkyong.com/java/java-custom-annotations-example/">Mkyong.com - java-custom-annotations-example</a>
 * @see <a href="http://stackoverflow.com/questions/1097807/java-reflection-is-the-order-of-class-fields-and-methods
 * -standardized">Stackoverflow - java-reflection-is-the-order-of-class-fields-and-methods-standardized</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldOrder {

	int value();
}
