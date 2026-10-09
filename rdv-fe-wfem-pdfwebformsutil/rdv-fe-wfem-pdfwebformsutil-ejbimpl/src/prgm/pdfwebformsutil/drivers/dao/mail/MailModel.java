package prgm.pdfwebformsutil.drivers.dao.mail;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class MailModel extends CommandDataModel {
	public static final String DEFAULT_OBJECT_STORE = "CED088";
	public static final String DEFAULT_ISTITUTO = "088";
	public static final String DEFAULT_FILIALE = "001";
	public static final String DEFAULT_FROM = "bmed.comunicazioni@bancamediolanum.it";
	
	public static final String OBJECT_CLASS_BANCACC = "BANCACC";
	public static final String OBJECT_CLASS_DOCUMENTOGENERICO = "DOCUMENTOGENERICO";
	public static final String OBJECT_CLASS_RISPARMIOGESTITO = "RISPARMIOGESTITO";
	public static final String CONTENT_TYPE_APP_PDF = "application/pdf";
	public static final String CONTENT_ENCODING_BASE64 = "BASE64";
	
	public static final String DEFAULT_INDEX_NAME = "ID";
	public static final String DEFAULT_INDEX_OPERATOR = "=";
	
	public static class MailAddress extends CommandDataModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8152236686341669425L;
		private StringType address = new StringType();

		public StringType getAddress() {
			return address;
		}

		public void setAddress(StringType address) {
			this.address = address;
		}
	}

	public static class Index extends CommandDataModel  {
		/**
		 * 
		 */
		private static final long serialVersionUID = 815894964069867786L;
		private StringType name = new StringType(DEFAULT_INDEX_NAME);
		private StringType operator = new StringType(DEFAULT_INDEX_OPERATOR);
		private StringType attributeValue = new StringType();

		public StringType getName() {
			return name;
		}

		public void setName(StringType name) {
			this.name = name;
		}

		public StringType getOperator() {
			return operator;
		}

		public void setOperator(StringType operator) {
			this.operator = operator;
		}

		public StringType getAttributeValue() {
			return attributeValue;
		}

		public void setAttributeValue(StringType attributeValue) {
			this.attributeValue = attributeValue;
		}
	}

	public static class RequestInvioMail extends CommandDataModel  {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8157042885225725160L;
		private StringType objectStore = new StringType(DEFAULT_OBJECT_STORE);
		private StringType objectClass = new StringType();
		private Index index = new Index();
		private StringType istituto = new StringType(DEFAULT_ISTITUTO);
		private StringType matricola = new StringType();
		private StringType ruolo = new StringType();
		private StringType filiale = new StringType(DEFAULT_FILIALE);
		private StringType from = new StringType(DEFAULT_FROM);
		private StringType subject = new StringType();
		private StringType body = new StringType();
		private StringType contentType = new StringType(CONTENT_TYPE_APP_PDF);
		private StringType contentEncoding = new StringType(CONTENT_ENCODING_BASE64);
		private StringType filename = new StringType();

		private ListType toList = new ListType(MailAddress.class);
		private ListType ccList = new ListType(MailAddress.class);
		private ListType bccList = new ListType(MailAddress.class);

		public StringType getObjectStore() {
			return objectStore;
		}

		public void setObjectStore(StringType objectStore) {
			this.objectStore = objectStore;
		}

		public StringType getObjectClass() {
			return objectClass;
		}

		public void setObjectclass(StringType objectClass) {
			this.objectClass = objectClass;
		}

		public Index getIndex() {
			return index;
		}

		public void setIndex(Index index) {
			this.index = index;
		}

		public StringType getIstituto() {
			return istituto;
		}

		public void setIstituto(StringType istituto) {
			this.istituto = istituto;
		}

		public StringType getMatricola() {
			return matricola;
		}

		public void setMatricola(StringType matricola) {
			this.matricola = matricola;
		}

		public StringType getRuolo() {
			return ruolo;
		}

		public void setRuolo(StringType ruolo) {
			this.ruolo = ruolo;
		}

		public StringType getFiliale() {
			return filiale;
		}

		public void setFiliale(StringType filiale) {
			this.filiale = filiale;
		}

		public StringType getFrom() {
			return from;
		}

		public void setFrom(StringType from) {
			this.from = from;
		}

		public StringType getSubject() {
			return subject;
		}

		public void setSubject(StringType subject) {
			this.subject = subject;
		}

		public StringType getBody() {
			return body;
		}

		public void setBody(StringType body) {
			this.body = body;
		}

		public StringType getContentType() {
			return contentType;
		}

		public void setContentType(StringType contentType) {
			this.contentType = contentType;
		}

		public StringType getContentEncoding() {
			return contentEncoding;
		}

		public void setContentEncoding(StringType contentEncoding) {
			this.contentEncoding = contentEncoding;
		}

		public StringType getFilename() {
			return filename;
		}

		public void setFilename(StringType filename) {
			this.filename = filename;
		}

		public ListType getToList() {
			return toList;
		}

		public void setToList(ListType toList) {
			this.toList = toList;
		}

		public ListType getCcList() {
			return ccList;
		}

		public void setCcList(ListType ccList) {
			this.ccList = ccList;
		}

		public ListType getBccList() {
			return bccList;
		}

		public void setBccList(ListType bccList) {
			this.bccList = bccList;
		}
	}

	public static class ResponseInvioMail extends CommandDataModel  {
		/**
		 * 
		 */
		private static final long serialVersionUID = -749490802027278504L;
		private StringType subjectMail = new StringType();
		private StringType testoMail = new StringType();
		private StringType from = new StringType();
		private StringType to = new StringType();
		private StringType codEsito = new StringType();
		private StringType desEsito = new StringType();
		
		public StringType getSubjectMail() {
			return subjectMail;
		}

		public void setSubjectMail(StringType subjectMail) {
			this.subjectMail = subjectMail;
		}

		public StringType getTestoMail() {
			return testoMail;
		}

		public void setTestoMail(StringType testoMail) {
			this.testoMail = testoMail;
		}

		public StringType getFrom() {
			return from;
		}

		public void setFrom(StringType from) {
			this.from = from;
		}

		public StringType getTo() {
			return to;
		}

		public void setTo(StringType to) {
			this.to = to;
		}

		public StringType getCodEsito() {
			return codEsito;
		}

		public void setCodEsito(StringType codEsito) {
			this.codEsito = codEsito;
		}

		public StringType getDesEsito() {
			return desEsito;
		}

		public void setDesEsito(StringType desEsito) {
			this.desEsito = desEsito;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestInvioMail request = new RequestInvioMail();
	private ResponseInvioMail response = new ResponseInvioMail();
	private IntegerType resultCode = new IntegerType();
	
	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}
	
	public RequestInvioMail getRequest() {
		return request;
	}

	public void setRequest(RequestInvioMail request) {
		this.request = request;
	}

	public ResponseInvioMail getResponse() {
		return response;
	}

	public void setResponse(ResponseInvioMail response) {
		this.response = response;
	}
}
