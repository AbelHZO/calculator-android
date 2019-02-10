package com.abelhzo.calculator.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @autor: Abel_HZO
 * @company: AbelHZO
 * @created: 19/11/2018 14:02:32
 * @file: MemoryCalculatorImpl.java
 * @license: <i>GNU General Public License<i>
 *
 */
public class MemoryCalculatorImpl implements MemoryCalculator {
	
	private double resultInMemory;

	public void memoryClear() {
		resultInMemory = 0.0;
	}

	public void memoryAdd(Double result) {
		this.resultInMemory += result;
	}

	public void memorySubs(Double result) {
		this.resultInMemory -= result;
	}

	public String memoryRead() {
		DecimalFormat df = new DecimalFormat("#.########");
		df.setRoundingMode(RoundingMode.CEILING);
		
		if(resultInMemory == 0.0)
			return "";
		
		return df.format(resultInMemory);
	}

}
