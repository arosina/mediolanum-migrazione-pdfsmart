package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import com.atosorigin.wfem.types.DoubleType;

public class InviaACopernicoFormatter {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String fromCentsToEuro(String importoInCentesimi){
		if(importoInCentesimi == null || importoInCentesimi.length() == 0)
			return "";
		double cent = Double.parseDouble(importoInCentesimi);
		double euro = cent / (double)100;
		return new DoubleType(new BigDecimal(euro).setScale(2,BigDecimal.ROUND_HALF_UP)).toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formatAsDoubleType( String value ) {
		DoubleType dblTypeValue = new DoubleType( value );
		return dblTypeValue.toScaledString(2);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formatAsDoubleNoThousands(String value) {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALY);
		Number num = null;
		try {
			num = df.parse(value);
		} catch (ParseException e) {
			return value;
		}
		df.setGroupingUsed(false);
		return df.format(num);
	}
}
