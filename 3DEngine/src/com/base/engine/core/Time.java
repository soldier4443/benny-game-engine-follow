package com.base.engine.core;

/**
 * Wrapper for time functions
 * @author soldi
 */
public class Time {
	private static final long SECOND = 1000000000L;
	
	public static double getTime() {
		return System.nanoTime() / (double)SECOND;
	}
}