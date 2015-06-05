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
	@FieldOrder(value = 4)
	private Integer sundayWorkingHours;

	/**
	 * Number of working hours on public holiday logged.
	 */
	@FieldOrder(value = 5)
	private Integer holidayWorkingHours;

	/**
	 * Number of days working the AM/PM shift.
	 * AM shift: 8:00 - 15:00 (7 hours).
	 * PM shift: 15:00 - 22:00 (7 hours).
	 */
//	@FieldOrder(value = 5)
	private Integer daysShiftAmPm = 0;

	/**
	 * Number of days working the night shift.
	 * Night shift: 22:00 - 8:00 (10 hours)
	 */
//	@FieldOrder(value = 6)
	private Integer daysShiftNight = 0;


	/**
	 * Number of times got called out in area 1.
	 */
	@FieldOrder(value = 6)
	private Integer numOfCallOutArea1;

	/**
	 * Number of times got called out in area 2.
	 */
	@FieldOrder(value = 7)
	private Integer numOfCallOutArea2;

	/**
	 * Number of times got called out in area 3.
	 */
	@FieldOrder(value = 8)
	private Integer numOfCallOutArea3;

	/**
	 * Jamsostek deduction.
	 */
	@FieldOrder(value = 9)
	private Boolean jamsostekDeduction;

	/**
	 * Koperasi deduction.
	 */
	@FieldOrder(value = 10)
	private Boolean koperasiDeduction;

	/**
	 * Tax deduction.
	 */
	@FieldOrder(value = 11)
	private Boolean taxDeduction;

	/**
	 * Tax.
	 */
	@FieldOrder(value = 12)
	private BigDecimal tax;

	/**
	 * Creation date.
	 */
	@FieldOrder(value = 13)
	private Date createdTs;

	@FieldOrder(value = 14)
	private BigDecimal currentAdjustment;

	@FieldOrder(value = 15)
	private BigDecimal prevAdjustment;

	/**
	 * Number of working hours for incentives.
	 * Maximum is always 160.
	 */
	private transient BigDecimal incentivesHours;

	private transient double extraHours;

	/**
	 * The normal working hours without extra hours.
	 */
	private transient BigDecimal decPlainWorkingHours;

	/**
	 * The extra hours.
	 * On Sundays the hours weight is 1.5 of normal hours.
	 * If user logged 2 hours on Sunday then the system recognizes it as 3 hours (2 * 1.5)
	 */
	private transient BigDecimal decWithExtraWorkingHours;

	/**
	 * Holidays working hours.
	 */
	private transient BigDecimal decHolidayWorkingHours;

	private void prepare() {
		// find the total of Sunday working hours.
		double totalSundays = sundayWorkingHours * 1.5; // 3 hours (2 * 1.5)

		// find the normal hours excluding Sundays' extra.
		extraHours = totalSundays - sundayWorkingHours; // 1 extra hours (3 - 2)

		double totalWithoutExtra = (normalWorkingHours + holidayWorkingHours) - extraHours;

		// set BigDecimal values
		decPlainWorkingHours = BigDecimal.valueOf(totalWithoutExtra);
		decWithExtraWorkingHours = BigDecimal.valueOf(normalWorkingHours);
		decHolidayWorkingHours = BigDecimal.valueOf(holidayWorkingHours);

		// working hours for incentives

		if ((normalWorkingHours - extraHours) > 160) {
			this.incentivesHours = BigDecimal.valueOf(160);
		} else {
			this.incentivesHours = BigDecimal.valueOf(normalWorkingHours - extraHours);
		}

	}

	public BigDecimal getTotalWorkingHours() {
		return decWithExtraWorkingHours.add(decHolidayWorkingHours);
	}

	public BigDecimal getTotalAllowanceHours() {
		return getTotalWorkingHours().subtract(decPlainWorkingHours);
	}

	/**
	 * Returns base income.
	 */
	public BigDecimal getWageWorkingHours() {
		return currentWageProperty.getWorkingHoursRate().multiply(decWithExtraWorkingHours);
	}

	/**
	 * Returns income with public holiday calculation.
	 */
	public BigDecimal getWageWorkingHoursHolidays() {
		return currentWageProperty.getWorkingHoursRate().multiply(decHolidayWorkingHours);
	}

	/**
	 * Returns base wage.
	 */
	public BigDecimal getWageWorkingHoursTotal() {
		return getWageWorkingHours().add(getWageWorkingHoursHolidays());
	}

	/**
	 * Returns wage for meals.
	 */
	public BigDecimal getWageMeals() {
		return currentWageProperty.getMealsRate().multiply(decPlainWorkingHours);
	}

	/**
	 * Returns wage for incentives.
	 */
	public BigDecimal getWageIncentives() {
		return currentWageProperty.getIncentivesRate().multiply(incentivesHours);
	}

	/**
	 * Returns wage allowance total.
	 */
	public BigDecimal getWageCallOutTotal() {
		BigDecimal callOut1 = currentWageProperty.getCallOutAreaOneRate().multiply(BigDecimal.valueOf
				(numOfCallOutArea1));
		BigDecimal callOut2 = currentWageProperty.getCallOutAreaTwoRate().multiply(BigDecimal.valueOf
				(numOfCallOutArea2));
		BigDecimal callOut3 = currentWageProperty.getCallOutAreaThreeRate().multiply(BigDecimal.valueOf
				(numOfCallOutArea3));
		return callOut1.add(callOut2).add(callOut3);
	}

	/**
	 * Returns wage allowance total.
	 */
	public BigDecimal getWageAllowanceTotal() {
		return getWageMeals().add(getWageIncentives()).add(getWageCallOutTotal());
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
	public BigDecimal getWageGross() {
		// Wage without deduction
		return getWageWorkingHoursTotal()
				.add(getWageAllowanceTotal())
				.add(currentAdjustment)
				.add(prevAdjustment);
	}

	/**
	 * Returns total net wage.
	 */
	public BigDecimal getWageNet() {
		return getWageGross().subtract(getWageDeduction());
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
	 * @param currentAdjustment   current month adjustment
	 * @param prevAdjustment      previous month adjustment
	 */
	public Wage(Month month, Integer year, int normalWorkingHours, int sundayWorkingHours, int
			holidayWorkingHours, int numOfCallOutArea1, int numOfCallOutArea2, int numOfCallOutArea3,
	            boolean jamsostekDeduction, boolean koperasiDeduction, boolean taxDeduction, BigDecimal tax,
	            BigDecimal currentAdjustment, BigDecimal prevAdjustment) {

		this.month = month;
		this.year = year;

		this.normalWorkingHours = normalWorkingHours;
		this.sundayWorkingHours = sundayWorkingHours;
		this.holidayWorkingHours = holidayWorkingHours;
		this.numOfCallOutArea1 = numOfCallOutArea1;
		this.numOfCallOutArea2 = numOfCallOutArea2;
		this.numOfCallOutArea3 = numOfCallOutArea3;
		this.jamsostekDeduction = jamsostekDeduction;
		this.koperasiDeduction = koperasiDeduction;
		this.taxDeduction = taxDeduction;
		this.tax = tax;
		this.currentAdjustment = currentAdjustment;
		this.prevAdjustment = prevAdjustment;

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
	 * @param currentAdjustment   current month adjustment
	 * @param prevAdjustment      previous month adjustment
	 */
	public Wage(Date createTs, Month month, Integer year, int normalWorkingHours, int sundayWorkingHours, int
			holidayWorkingHours, int numOfCallOutArea1, int numOfCallOutArea2, int numOfCallOutArea3,
	            boolean jamsostekDeduction, boolean koperasiDeduction, boolean taxDeduction, BigDecimal tax,
	            BigDecimal currentAdjustment, BigDecimal prevAdjustment) {

		this.month = month;
		this.year = year;

		this.normalWorkingHours = normalWorkingHours;
		this.sundayWorkingHours = sundayWorkingHours;
		this.holidayWorkingHours = holidayWorkingHours;
		this.numOfCallOutArea1 = numOfCallOutArea1;
		this.numOfCallOutArea2 = numOfCallOutArea2;
		this.numOfCallOutArea3 = numOfCallOutArea3;
		this.jamsostekDeduction = jamsostekDeduction;
		this.koperasiDeduction = koperasiDeduction;
		this.taxDeduction = taxDeduction;
		this.tax = tax;
		this.currentAdjustment = currentAdjustment;
		this.prevAdjustment = prevAdjustment;

		if (createTs != null) {
			this.createdTs = createTs;
		}

		prepare();
	}

	/**
	 * Objects is considered the same when they have the same month and year.
	 *
	 * @param o other Wage object
	 * @return true if objects have the same month and year
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Wage wage = (Wage) o;

		return month == wage.month && year.equals(wage.year);
	}

	@Override
	public int hashCode() {
		int result = month.hashCode();
		result = 31 * result + year.hashCode();
		return result;
	}
}
