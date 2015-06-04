package com.cramaniya.apps.dc.salcalc.business;

import java.math.BigDecimal;

/**
 * Logic class for salary calculation.
 * <p>
 * <ul>
 * <li>Working hours: 18.750/hr (Max 160 hours/month or 3.000.000/month)</li>
 * <li>Meals: 2.500/hr (Max 160 hours/month or 400.000/month)</li>
 * <li>Incentives: 12.500/hr (Max 160 hours/month or 2.000.000/month)</li>
 * <li>Call Out area 1: 400.000</li>
 * <li>Call Out area 2: 900.000</li>
 * <li>Call Out area 3: 1.500.000</li>
 * </ul>
 * <p>
 * If the working day is a public holiday, working hour's rate is doubled, but not for meals and incentives.
 *
 * @author Citra Ramaniya
 */
public interface WageCalculator {

	BigDecimal WORKING_HOURS_RATE = new BigDecimal("18750");
	BigDecimal MEALS_RATE = new BigDecimal("2500");
	BigDecimal INCENTIVES_RATE = new BigDecimal("12500");

	BigDecimal CALL_OUT_AREA_ONE_RATE = new BigDecimal("400000");
	BigDecimal CALL_OUT_AREA_TWO_RATE = new BigDecimal("900000");
	BigDecimal CALL_OUT_AREA_THREE_RATE = new BigDecimal("1500000");

	BigDecimal JAMSOSTEK_DEDUCTION = new BigDecimal("60000");
	BigDecimal KOPERASI_DEDUCTION = new BigDecimal("15000");

	int AM_PM_SHIFT_WORKING_HOURS_PER_DAY = 7;
	int NIGHT_SHIFT_WORKING_HOURS_PER_DAY = 10;

}
