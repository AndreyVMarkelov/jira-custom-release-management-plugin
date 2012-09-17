package com.ideotechnologies.jira.plugin.utils.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class UtilsHelper {
	
	/**
	 * Get a formatted value as pattern "dd/MMM/yy"
	 * if the value is instance of Date
	 * @param customFieldValue to format
	 * @return formatted value
	 */
	public static String getFormattedValue(Object customFieldValue){
		String cfValue = "";
		if (customFieldValue instanceof Date){
			SimpleDateFormat sdf = new SimpleDateFormat( "dd/MMM/yy" );
			Date date = (Date) customFieldValue;
			cfValue=sdf.format(date).toString() ;}
		else  {cfValue = customFieldValue.toString();}
		return cfValue;
	}
	/**
	 * Check if the value is a digit
	 * @param value value expected
	 * @return the test result
	 */
	public  static boolean isNumber(String value){
		return Pattern.matches("[0-9].*", value);
	}
}
