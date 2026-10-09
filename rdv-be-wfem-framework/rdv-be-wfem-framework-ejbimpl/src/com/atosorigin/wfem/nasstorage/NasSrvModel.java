package com.atosorigin.wfem.nasstorage;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class NasSrvModel extends CommandDataModel {

    private StringType  destinationPath = new StringType();
    private StringType  nameApp = new StringType();
    private StringType  nameFile = new StringType();
    private StringType  source = new StringType();
    private StringType  type = new StringType();
    private StringType  userId = new StringType();
    private StringType  idFile = new StringType();
    private StringType  checkIdFile = new StringType();
    private BooleanType deleteFileNASResponse = new BooleanType();
    
	public StringType getDestinationPath() {
		return destinationPath;
	}
	public void setDestinationPath(StringType destinationPath) {
		this.destinationPath = destinationPath;
	}
	public StringType getNameApp() {
		return nameApp;
	}
	public void setNameApp(StringType nameApp) {
		this.nameApp = nameApp;
	}
	public StringType getNameFile() {
		return nameFile;
	}
	public void setNameFile(StringType nameFile) {
		this.nameFile = nameFile;
	}
	public StringType getSource() {
		return source;
	}
	public void setSource(StringType source) {
		this.source = source;
	}
	public StringType getType() {
		return type;
	}
	public void setType(StringType type) {
		this.type = type;
	}
	public StringType getUserId() {
		return userId;
	}
	public void setUserId(StringType userId) {
		this.userId = userId;
	}
	public StringType getIdFile() {
		return idFile;
	}
	public void setIdFile(StringType idFile) {
		this.idFile = idFile;
	}
	public StringType getCheckIdFile() {
		return checkIdFile;
	}
	public void setCheckIdFile(StringType checkIdFile) {
		this.checkIdFile = checkIdFile;
	}
	public BooleanType getDeleteFileNASResponse() {
		return deleteFileNASResponse;
	}
	public void setDeleteFileNASResponse(BooleanType deleteFileNASResponse) {
		this.deleteFileNASResponse = deleteFileNASResponse;
	}
	
}
