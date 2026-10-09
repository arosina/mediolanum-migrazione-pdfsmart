package prgm.pdfwebformsdrivers.postcompletioncewutility.riepilogoprimenuove;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import com.atosorigin.wfem.types.DoubleType;

public class PrimeNuoveFondiFormatter {
	
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
