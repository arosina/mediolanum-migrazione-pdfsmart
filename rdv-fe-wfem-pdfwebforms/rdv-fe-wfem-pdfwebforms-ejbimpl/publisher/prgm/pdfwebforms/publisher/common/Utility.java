package prgm.pdfwebforms.publisher.common;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class Utility {

	private static String sPublic = "		public static final String ";
	private static String sUguale = " = \"";
	private static String sPuntoVirgola = "\";\n";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private Utility() {
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String generatePdfDataHelperSource(PdfAnagModel pdfAnag, byte[] pdfContent){

		PdfInfos pdfInfos = null;
		try{
			pdfInfos = PdfEngine.inspectPdfInfos(pdfContent);
		}catch(Throwable t){
			return "Problems on pdf: "+t.toString();
		}

		StringBuilder source = new StringBuilder();

		/* import, definizione delle classe, costruttore */
		source.append("package prgm.pdfwebformsdrivers."+pdfAnag.getPdfDriverName()+"."+pdfAnag.getPdfDriverVersion()+";\n\n");
		source.append("import prgm.pdfwebforms.drivers.PdfAcroFieldNotFoundException;\n");
		source.append("import prgm.pdfwebforms.drivers.PdfBaseDataHelper;\n");
		source.append("import prgm.pdfwebforms.model.PdfDataModel;\n");
		source.append("import com.atosorigin.wfem.types.*;\n\n");
		source.append("public class PdfDataHelper extends PdfBaseDataHelper{\n");
		
		source.append("	public PdfDataHelper(PdfDataModel pdfData){\n");
		source.append("		super(pdfData);\n");
		source.append("	}\n");

		/* getters e setters dei campi */ 
		generateGetterSetter(pdfInfos, source);

		/* sottoclasse con le costanti dei nomi dei campi */
		generateFieldNames(pdfInfos, source);

		/* sottoclasse con le costanti dei valori possibili dei campi */
		generateFieldValues(pdfInfos, source);

		/* chiusura classe */
		source.append("}\n");
		return source.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void generateGetterSetter(PdfInfos pdfInfos, StringBuilder source) {
		ArrayList<String> managedFields = new ArrayList<String>();
		for(PdfFieldInfos fi : pdfInfos.getFieldInfos()){
			
			if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			
			if(PdfPredefinedFields.AGENTE_NOME_FIRMA_N_PATTERN.matcher(fi.htmlFieldName).matches() ||
			   PdfPredefinedFields.CLIENTE_NOME_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName).matches())
				continue;
			
			if(fi.mainField != null)
				continue;
			
			String fname = fi.htmlFieldName.substring(0,1).toUpperCase()+fi.htmlFieldName.substring(1);
			if(managedFields.contains(fname))
				continue;
			
			String dataType = fi.dataType;
			
			source.append("	public "+dataType+" get"+fname+"() throws PdfAcroFieldNotFoundException{\n");
			source.append("		return ("+dataType+")read(FieldNames."+fi.htmlFieldName.toUpperCase()+");\n");
			source.append("	}\n");
			source.append("	public void set"+fname+"("+dataType+" value){\n");
			source.append("		write(FieldNames."+fi.htmlFieldName.toUpperCase()+",value);\n");
			source.append("	}\n");
			managedFields.add(fname);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void generateFieldNames(PdfInfos pdfInfos, StringBuilder source) {
		source.append("\n\tpublic class FieldNames {\n");

		ArrayList<String> managedFields = new ArrayList<String>();
		for(PdfFieldInfos fi : pdfInfos.getFieldInfos()){

			if(fi.mainField != null)
				continue;

			String fname = fi.htmlFieldName;
			if(managedFields.contains(fname))
				continue;

			source.append(sPublic+fname.toUpperCase()+sUguale+fname+sPuntoVirgola);
			managedFields.add(fname);

			/* se finisce con un numero tratto anche la base, per utilizzarlo nei cicli */
			Pattern pat = Pattern.compile("\\d+$");
			Matcher mat = pat.matcher(fi.htmlFieldName);
			if(mat.find()){
				fname = fi.htmlFieldName.substring(0, mat.start());
				if(managedFields.contains(fname))
					continue;
				source.append(sPublic+fname.toUpperCase()+sUguale+fname+sPuntoVirgola);
				managedFields.add(fname);
			}
		}
		source.append("\n");
		source.append("		private FieldNames() {\n");
		source.append("		}\n");
		source.append("	}\n");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void generateFieldValues(PdfInfos pdfInfos, StringBuilder source) {
		source.append("\n\tpublic class FieldValues {\n");

		for(PdfFieldInfos fi : pdfInfos.getFieldInfos()){

			if(fi.mainField != null)
				continue;

			String fname = fi.htmlFieldName;
			if(fi.fieldType == AcroFields.FIELD_TYPE_COMBO && fi.codDescDataList != null) {
				manageCombo(fi, fname, source);
			}else if(fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON && fi.expValue != null && fi.expValue.length()>0) {
				source.append(sPublic+fname.toUpperCase()+"_"+fi.expValue.toUpperCase().replace(" ", "_")+sUguale+fi.expValue+sPuntoVirgola);
			}
		}

		source.append("\n");
		source.append("		private FieldValues() {\n");
		source.append("		}\n");
		source.append("	}\n");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void manageCombo(PdfFieldInfos fi, String fname, StringBuilder source) {
		for(int i=0;i<fi.codDescDataList.getCodDescCount();i++) {
			CodDescData codDesc = fi.codDescDataList.getCodDesc(i);
			if(!codDesc.getCod().equals("") && !codDesc.getCod().equals(" ")) {
				source.append(sPublic+fname.toUpperCase()+"_"+codDesc.getCod().toUpperCase().replace(" ", "_")+sUguale+codDesc.getCod()+sPuntoVirgola);
			}
		}
	}
}
