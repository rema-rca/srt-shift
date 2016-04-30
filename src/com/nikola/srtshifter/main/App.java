package com.nikola.srtshifter.main;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.nikola.srtshifter.shifter.SrtFileShiftAction;
import com.nikola.srtshifter.shifter.SrtShifter;

public class App {
	
	public static void main(String[] args) {
		if(args.length < 2 || StringUtils.isBlank(args[0]) || StringUtils.isBlank(args[1])) {
			System.err.println("First argument must be full path to .srt file, second one must be +/- number of milliseconds");
			System.exit(0);
		}
		int milliseconds = 0;
		try {
			milliseconds = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException ex) {
			System.err.println("Second argument must be integer value e.g. 500,-1000...");
			System.exit(0);
		}
		SrtShifter shifter = new SrtShifter();
		try {
			shifter.shift(new SrtFileShiftAction(args[0], milliseconds));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
