package com.cramaniya.apps.dc.salcalc.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * String helper class.
 *
 * @author Citra Ramaniya
 */
public class StringUtils {

	/**
	 * Formats {@link BigDecimal} to Indonesian currency.
	 * 
	 * @param value
	 *            BigDecimal input
	 * @return String in Indonesian Currency
	 */
	public static String formatToIndonesianCurrency(BigDecimal value) {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
		currencyFormat.setMinimumFractionDigits(1);
		currencyFormat.setMaximumFractionDigits(2);
		return currencyFormat.format(value.doubleValue());
	}
}
