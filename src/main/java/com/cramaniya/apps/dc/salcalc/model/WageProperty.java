package com.cramaniya.apps.dc.salcalc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Citra Ramaniya
 */
@Getter
@Setter
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WageProperty implements Serializable {

	private static final long serialVersionUID = 7725849001983324677L;

	@XmlAttribute
	private BigDecimal workingHoursRate;
	@XmlAttribute
	private BigDecimal mealsRate;
	@XmlAttribute
	private BigDecimal incentivesRate;

	@XmlAttribute
	private BigDecimal callOutAreaOneRate;
	@XmlAttribute
	private BigDecimal callOutAreaTwoRate;
	@XmlAttribute
	private BigDecimal callOutAreaThreeRate;

	@XmlAttribute
	private BigDecimal jamsostekDeduction;
	@XmlAttribute
	private BigDecimal koperasiDeduction;

	@XmlAttribute
	private int amPmShiftWorkingHours;
	@XmlAttribute
	private int nightShiftWorkingHours;
	@XmlAttribute
	private int sundayNightShiftWorkingHours;

	public void setToDefaultValue() {
		this.workingHoursRate = new BigDecimal("18750");
		this.mealsRate = new BigDecimal("2500");
		this.incentivesRate = new BigDecimal("12500");

		this.callOutAreaOneRate = new BigDecimal("400000");
		this.callOutAreaTwoRate = new BigDecimal("900000");
		this.callOutAreaThreeRate = new BigDecimal("1500000");

		this.jamsostekDeduction = new BigDecimal("60000");
		this.koperasiDeduction = new BigDecimal("15000");

		this.amPmShiftWorkingHours = 7;
		this.nightShiftWorkingHours = 10;
		this.sundayNightShiftWorkingHours = 11;
	}


}
