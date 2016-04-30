package com.nikola.srtshifter.shifter;

public class SrtFileShiftAction {
	
	private final String filePath;
	private final int millisecondShift;
	
	public SrtFileShiftAction(String filePath, int millisecondShift) {
		super();
		this.filePath = filePath;
		this.millisecondShift = millisecondShift;
	}

	public String getFilePath() {
		return filePath;
	}

	public int getMillisecondShift() {
		return millisecondShift;
	}
	
	

}
