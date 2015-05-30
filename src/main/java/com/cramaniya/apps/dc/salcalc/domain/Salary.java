package com.cramaniya.apps.dc.salcalc.domain;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Domain class for salary.
 *
 * @author Citra Ramaniya
 */
@Data
public class Salary {

	/**
	 * Month.
	 */
	private int month;

	/**
	 * Year.
	 */
	private int year;

	/**
	 * Working hours logged.
	 */
	private int workingHours;

	/**
	 * How many days on duty on public holiday.
	 */
	private int numOfPublicHoliday;

	/**
	 * Base salary.
	 */
	private BigDecimal salaryBase;

	/**
	 * Salary with public holiday calculation.
	 */
	private BigDecimal salaryWithPublicHoliday;

	/**
	 * Salary for meals.
	 */
	private BigDecimal meals;

	/**
	 * Salary for incentives.
	 */
	private BigDecimal incentives;

	/**
	 * Number of times got called out in area 1.
	 */
	private int numOfCallOutArea1;

	/**
	 * Number of times got called out in area 2.
	 */
	private int numOfCallOutArea2;

	/**
	 * Number of times got called out in area 3.
	 */
	private int numOfCallOutArea3;

	/**
	 * Total salary.
	 */
	private BigDecimal salaryTotal;

	public Salary(int workingHours, int numOfPublicHoliday, int numOfCallOutArea1, int numOfCallOutArea2, int numOfCallOutArea3) {
		this.workingHours = workingHours;
		this.numOfPublicHoliday = numOfPublicHoliday;
		this.numOfCallOutArea1 = numOfCallOutArea1;
		this.numOfCallOutArea2 = numOfCallOutArea2;
		this.numOfCallOutArea3 = numOfCallOutArea3;
	}

}
