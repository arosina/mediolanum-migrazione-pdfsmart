package prgm.pdfwebformsutil.drivers.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XmlFormatter {
	private static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String DD_MM_YYYY = "dd-MM-yyyy";

	private XmlFormatter() {
	}

	public static String formatOSBdate(String param) throws Exception {

		SimpleDateFormat inputFormat = new SimpleDateFormat(YYYY_MM_DD);
		SimpleDateFormat outputFormat = new SimpleDateFormat(DD_MM_YYYY);
		Date x = inputFormat.parse(param.substring(0, 10));
		return outputFormat.format(x);
	}

	public static String formatXmlDate(String param) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY);
		SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS);
		Date x = inputFormat.parse(param.substring(0, 10));
		return outputFormat.format(x);
	}

	public static String formatDateyyyyMMdd(String param) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat(DD_MM_YYYY);
		SimpleDateFormat outputFormat = new SimpleDateFormat(YYYY_MM_DD);
		Date x = inputFormat.parse(param.substring(0, 10));
		return outputFormat.format(x);
	}
}
