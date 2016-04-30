package com.nikola.srtshifter.shifter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

import org.apache.commons.lang3.StringUtils;

public class SrtShifter {

	public void shift(SrtFileShiftAction shiftAction) throws IOException {
		FileWriter fw = new FileWriter(getShiftedFilePath(shiftAction.getFilePath()));
		try (Stream<String> lines = Files.lines(Paths.get(shiftAction.getFilePath()))) {
			lines.forEachOrdered(value -> {
				try {
					writeNewLine(fw, value, shiftAction.getMillisecondShift());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			});
		}
		fw.close();
	}

	private String getShiftedFilePath(String filePath) {
		String[] tokens = filePath.split("\\.(?=[^\\.]+$)");
		tokens[0] = tokens[0] + "_shifted";
		return StringUtils.join(tokens, ".");
	}

	private void writeNewLine(FileWriter writer, String value, int shift) throws IOException {
		if (value.contains("-->")) {
			String startTimeValue = value.split("-->")[0].trim();
			String endTimeValue = value.split("-->")[1].trim();
			Calendar startTime = createNewStartTime(startTimeValue, shift);
			Calendar endTime = createNewStartTime(endTimeValue, shift);
			writer.write(formatNewTimingLine(startTime,endTime));

		} else {
			writer.write(value);
		}
		writer.write(System.lineSeparator());
	}


	private Calendar createNewStartTime(String timeValue, int shift) {
		Calendar time = Calendar.getInstance();
		time.setTime(getStartOfDay(new Date()));
		List<Integer> values = Arrays.stream(timeValue.split(":|,")).map(Integer::parseInt).collect(toList());
		// null and index check to be added
		time.set(Calendar.HOUR_OF_DAY, values.get(0));
		time.set(Calendar.MINUTE, values.get(1));
		time.set(Calendar.SECOND, values.get(2));
		time.set(Calendar.MILLISECOND, values.get(3));
		time.add(Calendar.MILLISECOND, shift);
		return time;
	}

	private Date getStartOfDay(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return localDateTimeToDate(startOfDay);
	}

	private static Date localDateTimeToDate(LocalDateTime startOfDay) {
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

	private static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
	}
	

	private String formatNewTimingLine(Calendar startTime, Calendar endTime) {
		return formatSrtTiming(startTime) + " --> " + formatSrtTiming(endTime);
	}

	private String formatSrtTiming(Calendar time) {
		return String.format("%02d", time.get(Calendar.HOUR))
				+":"+String.format("%02d", time.get(Calendar.MINUTE))
				+":"+String.format("%02d", time.get(Calendar.SECOND))
				+","+String.format("%03d", time.get(Calendar.MILLISECOND));
	}
	

}
