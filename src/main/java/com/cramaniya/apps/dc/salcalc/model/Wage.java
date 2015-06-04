package com.cramaniya.apps.dc.salcalc.model;

import com.cramaniya.apps.dc.salcalc.annotation.FieldOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Month;
import java.util.Date;

import static com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig.currentWageProperty;

/**
 * Domain class for salary.
 *
 * @author Citra Ramaniya
 */
@Getter
@Setter
@ToString
@Slf4j
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Wage implements Serializable {

	private static final long serialVersionUID = -3490459516273739936L;

	/**
	 * Month.
	 */
	@FieldOrder(value = 1)
	private Month month;

	/**
	 * Year.
	 */
	@FieldOrder(value = 2)
	private Integer year;

	/**
	 * Number of working hours logged.
	 */
	@FieldOrder(value = 3)
	private Integer normalWorkingHours;

	/**
	 * Number of working hours logged.
	 */
	@FieldOrder(value = 3)
	private Integer sundayWorkingHours;

	/**
	 * Number of working hours on public holiday logged.
	 */
	@FieldOrder(value = 4)
	private Integer holidayWorkingHours;

	/**
	 * Number of days working the AM/PM shift.
	 * AM shift: 8:00 - 15:00 (7 hours).
	 * PM shift: 15:00 - 22:00 (7 hours).
	 */
	@FieldOrder(value = 5)
	private Integer daysShiftAmPm = 0;

	/**
	 * Number of days working the night shift.
	 * Night shift: 22:00 - 8:00 (10 hours)
	 */
	@FieldOrder(value = 6)
	private Integer daysShiftNight = 0;

	/**
	 * Number of working hours for incentives.
	 * Maximum is always 160.
	 */
	@FieldOrder(value = 7)
	private Integer incentivesHours;

	/**
	 * Number of times got called out in area 1.
	 */
	@FieldOrder(value = 8)
	private Integer numOfCallOutArea1;

	/**
	 * Number of times got called out in area 2.
	 */
	@FieldOrder(value = 9)
	private Integer numOfCallOutArea2;

	/**
	 * Number of times got called out in area 3.
	 */
	@FieldOrder(value = 10)
	private Integer numOfCallOutArea3;

	/**
	 * Jamsostek deduction.
	 */
	@FieldOrder(value = 11)
	private Boolean jamsostekDeduction;

	/**
	 * Koperasi deduction.
	 */
	@FieldOrder(value = 12)
	private Boolean koperasiDeduction;

	/**
	 * Tax deduction.
	 */
	@FieldOrder(value = 13)
	private Boolean taxDeduction;

	/**
	 * Tax.
	 */
	@FieldOrder(value = 14)
	private BigDecimal tax;

	/**
	 * Creation date.
	 */
	@FieldOrder(value = 15)
	private Date createdTs;

	private transient BigDecimal decNormalWorkingHours;
	private transient BigDecimal decSundayWorkingHours;
	private transient BigDecimal decHolidayWorkingHours;

	private void prepare() {
		decNormalWorkingHours = BigDecimal.valueOf(normalWorkingHours);
		decSundayWorkingHours = BigDecimal.valueOf(sundayWorkingHours * 1.5); // normal hours * 1.5
		decHolidayWorkingHours = BigDecimal.valueOf(holidayWorkingHours * 2); // normal hours * 2
	}

	public BigDecimal getTotalWorkingHours() {
		return BigDecimal.ZERO.add(decNormalWorkingHours).add(decSundayWorkingHours).add(decHolidayWorkingHours);
	}

	/**
	 * Returns base wage.
	 */
	public BigDecimal getWageWorkingHoursTotal() {
		return getWageWorkingHours().add(getWageWorkingHoursSundays()).add(getWageWorkingHoursHolidays());
	}

	/**
	 * Returns wage with public holiday calculation.
	 */
	public BigDecimal getWageWorkingHours() {
		return currentWageProperty.getWorkingHoursRate().multiply(decNormalWorkingHours);
	}

	/**
	 * Returns wage with public holiday calculation.
	 */
	public BigDecimal getWageWorkingHoursSundays() {
		return currentWageProperty.getWorkingHoursRate().multiply(decSundayWorkingHours);
	}

	/**
	 * Returns wage with public holiday calculation.
	 */
	public BigDecimal getWageWorkingHoursHolidays() {
		return currentWageProperty.getWorkingHoursRate().multiply(decHolidayWorkingHours);
	}

	/**
	 * Returns wage for meals.
	 */
	public BigDecimal getWageMeals() {
		return currentWageProperty.getMealsRate().multiply(decNormalWorkingHours);
	}

	/**
	 * Returns wage for incentives.
	 */
	public BigDecimal getWageIncentives() {
		return currentWageProperty.getIncentivesRate().multiply(BigDecimal.valueOf(incentivesHours));
	}

	/**
	 * Returns wage deduction.
	 *
	 * @return deduction
	 */
	public BigDecimal getWageDeduction() {
		BigDecimal deduction = BigDecimal.ZERO;

		// calculate wage deduction
		if (jamsostekDeduction) {
			deduction = deduction.add(currentWageProperty.getJamsostekDeduction());
		}
		if (koperasiDeduction) {
			deduction = deduction.add(currentWageProperty.getKoperasiDeduction());
		}
		if (taxDeduction) {
			if (tax != null) { // tax should not be null
				deduction = deduction.add(tax);
			} else {
				log.error("No tax value is entered.");
			}
		}

		return deduction;
	}

	/**
	 * Returns total net wage.
	 */
	public BigDecimal getWageTotal() {
		BigDecimal callOut1 = currentWageProperty.getCallOutAreaOneRate().multiply(BigDecimal.valueOf
				(numOfCallOutArea1));
		BigDecimal callOut2 = currentWageProperty.getCallOutAreaTwoRate().multiply(BigDecimal.valueOf
				(numOfCallOutArea2));
		BigDecimal callOut3 = currentWageProperty.getCallOutAreaThreeRate().multiply(BigDecimal.valueOf
				(numOfCallOutArea3));
		BigDecimal totalCallOut = callOut1.add(callOut2).add(callOut3);

		// Wage without deduction
		BigDecimal totalWage = getWageWorkingHoursTotal()
				.add(getWageMeals())
				.add(getWageIncentives())
				.add(totalCallOut);

		totalWage = totalWage.subtract(getWageDeduction());

		return totalWage;
	}

	/**
	 * Default constructor.
	 */
	public Wage() {
	}

	/**
	 * Constructor for wage based on working hours for the given month and year.
	 *
	 * @param month               the month
	 * @param year                the year
	 * @param normalWorkingHours  number of working hours on normal days logged
	 * @param sundayWorkingHours  number of working hours on Sundays logged
	 * @param holidayWorkingHours number of working hours logged on public holidays
	 * @param numOfCallOutArea1   number of times for call out in area 1
	 * @param numOfCallOutArea2   number of times for call out in area 2
	 * @param numOfCallOutArea3   number of times for call out in area 3
	 * @param jamsostekDeduction  deduct for jamsostek
	 * @param koperasiDeduction   deduct for koperasi
	 * @param taxDeduction        deduct for tax
	 * @param tax                 the tax
	 */
	public Wage(Month month, Integer year, int normalWorkingHours, int sundayWorkingHours, int
			holidayWorkingHours, int numOfCallOutArea1, int numOfCallOutArea2, int numOfCallOutArea3,
	            boolean jamsostekDeduction, boolean koperasiDeduction, boolean taxDeduction, BigDecimal tax

	) {

		this.month = month;
		this.year = year;

		this.normalWorkingHours = normalWorkingHours;
		this.sundayWorkingHours = sundayWorkingHours;
		// working hours for incentives
		if (normalWorkingHours > 160) {
			this.incentivesHours = 160;
		} else {
			this.incentivesHours = normalWorkingHours;
		}
		this.holidayWorkingHours = holidayWorkingHours;
		this.numOfCallOutArea1 = numOfCallOutArea1;
		this.numOfCallOutArea2 = numOfCallOutArea2;
		this.numOfCallOutArea3 = numOfCallOutArea3;
		this.jamsostekDeduction = jamsostekDeduction;
		this.koperasiDeduction = koperasiDeduction;
		this.taxDeduction = taxDeduction;
		this.tax = tax;

		prepare();
	}

	/**
	 * Constructor for wage based on working hours for the given month and year.
	 *
	 * @param createTs            the created time stamp
	 * @param month               the month
	 * @param year                the year
	 * @param normalWorkingHours  number of working hours on normal days logged
	 * @param sundayWorkingHours  number of working hours on Sundays logged
	 * @param holidayWorkingHours number of working hours logged on public holidays
	 * @param numOfCallOutArea1   number of times for call out in area 1
	 * @param numOfCallOutArea2   number of times for call out in area 2
	 * @param numOfCallOutArea3   number of times for call out in area 3
	 * @param jamsostekDeduction  deduct for jamsostek
	 * @param koperasiDeduction   deduct for koperasi
	 * @param taxDeduction        deduct for tax
	 * @param tax                 the tax
	 */
	public Wage(Date createTs, Month month, Integer year, int normalWorkingHours, int sundayWorkingHours, int
			holidayWorkingHours, int numOfCallOutArea1, int numOfCallOutArea2, int numOfCallOutArea3,
	            boolean jamsostekDeduction, boolean koperasiDeduction, boolean taxDeduction, BigDecimal tax) {

		this.month = month;
		this.year = year;

		this.normalWorkingHours = normalWorkingHours;
		this.sundayWorkingHours = sundayWorkingHours;
		// working hours for incentives
		if (normalWorkingHours > 160) {
			this.incentivesHours = 160;
		} else {
			this.incentivesHours = normalWorkingHours;
		}
		this.holidayWorkingHours = holidayWorkingHours;
		this.numOfCallOutArea1 = numOfCallOutArea1;
		this.numOfCallOutArea2 = numOfCallOutArea2;
		this.numOfCallOutArea3 = numOfCallOutArea3;
		this.jamsostekDeduction = jamsostekDeduction;
		this.koperasiDeduction = koperasiDeduction;
		this.taxDeduction = taxDeduction;
		this.tax = tax;

		if (createTs != null) {
			this.createdTs = createTs;
		}

		prepare();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Wage wage = (Wage) o;

		if (month != wage.month) return false;
		return year.equals(wage.year);

	}

	@Override
	public int hashCode() {
		int result = month.hashCode();
		result = 31 * result + year.hashCode();
		return result;
	}
}
