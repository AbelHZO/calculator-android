package com.abelhzo.calculator.services;

/**
 *
 * @autor: Abel_HZO
 * @company: AbelHZO
 * @created: 19/11/2018 12:50:27
 * @file: MemoryCalculator.java
 * @license: <i>GNU General Public License<i>
 *
 */
public interface MemoryCalculator {
	
	public void memoryClear();
	public void memoryAdd(Double result);
	public void memorySubs(Double result);
	public String memoryRead();

}
