package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/******************************************************************************/
/******************************************************************************/
public class ServiziEsitoModel extends CommandDataModel {

	private StringType 		pdfInstanceId 	= new StringType();
	
	private TimestampType 	dataElabPrimeNuove	= new TimestampType();	
	private StringType		infoPrimeNuove				= new StringType();
	
	private TimestampType 	dataElabConfAgg	= new TimestampType();	
	private StringType		infoConfAgg				= new StringType();
	
	private TimestampType 	dataElabSwitchDis= new TimestampType();	
	private StringType		infoSwitchDis				= new StringType();
	
	private TimestampType 	dataElabSwitchInv	= new TimestampType();	
	private StringType		infoSwitchInv				= new StringType();
	
	private StringType		infoArchivia		= new StringType();
	private TimestampType 	dataElabArchivia	= new TimestampType();	
	
	private StringType		infoInvio		= new StringType();
	private TimestampType 	dataElabInvio	= new TimestampType();
	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}
	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}
	public TimestampType getDataElabPrimeNuove() {
		return dataElabPrimeNuove;
	}
	public void setDataElabPrimeNuove(TimestampType dataElabPrimeNuove) {
		this.dataElabPrimeNuove = dataElabPrimeNuove;
	}
	public StringType getInfoPrimeNuove() {
		return infoPrimeNuove;
	}
	public void setInfoPrimeNuove(StringType infoPrimeNuove) {
		this.infoPrimeNuove = infoPrimeNuove;
	}
	public TimestampType getDataElabConfAgg() {
		return dataElabConfAgg;
	}
	public void setDataElabConfAgg(TimestampType dataElabConfAgg) {
		this.dataElabConfAgg = dataElabConfAgg;
	}
	public StringType getInfoConfAgg() {
		return infoConfAgg;
	}
	public void setInfoConfAgg(StringType infoConfAgg) {
		this.infoConfAgg = infoConfAgg;
	}
	public TimestampType getDataElabSwitchDis() {
		return dataElabSwitchDis;
	}
	public void setDataElabSwitchDis(TimestampType dataElabSwitchDis) {
		this.dataElabSwitchDis = dataElabSwitchDis;
	}
	public StringType getInfoSwitchDis() {
		return infoSwitchDis;
	}
	public void setInfoSwitchDis(StringType infoSwitchDis) {
		this.infoSwitchDis = infoSwitchDis;
	}
	public TimestampType getDataElabSwitchInv() {
		return dataElabSwitchInv;
	}
	public void setDataElabSwitchInv(TimestampType dataElabSwitchInv) {
		this.dataElabSwitchInv = dataElabSwitchInv;
	}
	public StringType getInfoSwitchInv() {
		return infoSwitchInv;
	}
	public void setInfoSwitchInv(StringType infoSwitchInv) {
		this.infoSwitchInv = infoSwitchInv;
	}
	public StringType getInfoArchivia() {
		return infoArchivia;
	}
	public void setInfoArchivia(StringType infoArchivia) {
		this.infoArchivia = infoArchivia;
	}
	public TimestampType getDataElabArchivia() {
		return dataElabArchivia;
	}
	public void setDataElabArchivia(TimestampType dataElabArchivia) {
		this.dataElabArchivia = dataElabArchivia;
	}
	public StringType getInfoInvio() {
		return infoInvio;
	}
	public void setInfoInvio(StringType infoInvio) {
		this.infoInvio = infoInvio;
	}
	public TimestampType getDataElabInvio() {
		return dataElabInvio;
	}
	public void setDataElabInvio(TimestampType dataElabInvio) {
		this.dataElabInvio = dataElabInvio;
	}	
	
	


}
