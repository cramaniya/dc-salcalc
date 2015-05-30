package com.cramaniya.apps.dc.salcalc.business;

import java.math.BigDecimal;

import com.cramaniya.apps.dc.salcalc.domain.Salary;

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
public class SalaryCalculator {

	private static final BigDecimal WORKING_HOURS_RATE = new BigDecimal("18750");
	private static final BigDecimal MEALS_RATE = new BigDecimal("2500");
	private static final BigDecimal INCENTIVES_RATE = new BigDecimal("12500");

	private static final BigDecimal CALL_OUT_AREA_ONE_RATE = new BigDecimal("400000");
	private static final BigDecimal CALL_OUT_AREA_TWO_RATE = new BigDecimal("900000");
	private static final BigDecimal CALL_OUT_AREA_THREE_RATE = new BigDecimal("1500000");

	private static final int WORKING_HOURS_PER_DAY = 7;


	public static void calculateSalary(Salary salary) {

		// working hours
		BigDecimal workingHours = new BigDecimal(salary.getWorkingHours());

		int incentivesHours = salary.getWorkingHours();
		if (incentivesHours > 160) {
			incentivesHours = 160;
		}
		BigDecimal incentivesWorkingHours = new BigDecimal(incentivesHours);

		salary.setSalaryBase(WORKING_HOURS_RATE.multiply(workingHours));

		int workingHourHoliday = salary.getNumOfPublicHoliday() * WORKING_HOURS_PER_DAY;
		BigDecimal addedSalaryForPublicHoliday = WORKING_HOURS_RATE.multiply(new BigDecimal(workingHourHoliday));
		salary.setSalaryWithPublicHoliday(salary.getSalaryBase().add(addedSalaryForPublicHoliday));

		salary.setMeals(MEALS_RATE.multiply(workingHours));
		salary.setIncentives(INCENTIVES_RATE.multiply(incentivesWorkingHours));

		BigDecimal callOut1 = CALL_OUT_AREA_ONE_RATE.multiply(BigDecimal.valueOf(salary.getNumOfCallOutArea1()));
		BigDecimal callOut2 = CALL_OUT_AREA_TWO_RATE.multiply(BigDecimal.valueOf(salary.getNumOfCallOutArea2()));
		BigDecimal callOut3 = CALL_OUT_AREA_THREE_RATE.multiply(BigDecimal.valueOf(salary.getNumOfCallOutArea3()));
		BigDecimal totalCallOut = callOut1.add(callOut2).add(callOut3);

		BigDecimal totalSalary = salary.getSalaryWithPublicHoliday()
				.add(salary.getMeals())
				.add(salary.getIncentives())
				.add(totalCallOut);

		salary.setSalaryTotal(totalSalary);
	}


}
