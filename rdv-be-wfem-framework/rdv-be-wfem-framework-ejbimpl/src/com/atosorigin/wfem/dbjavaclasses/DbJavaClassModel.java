package com.atosorigin.wfem.dbjavaclasses;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DbJavaClassModel extends DbJavaProjectModel {
	
	private boolean newClass = false;
	private StringType classesToDelete = new StringType();
	private StringType classesToEnable = new StringType();
	private StringType classesToDisable = new StringType();
	
	private StringType    nameOfClass = new StringType();
	private StringType 	  source = new StringType();
	private ByteArrayType bytecode = new ByteArrayType();
	private TimestampType compilationTime = new TimestampType();
	private TimestampType backupTime = new TimestampType();
	private StringType 	  backupTimeString = new StringType();
	private BooleanType	  enabled = new BooleanType();
	
	private boolean 	compilationErrors = false;
	private StringType  compilerOut = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DbJavaClassModel(){
		addCodDescField("backupTimeString","BACKUP_TIME");
	}
	
	public boolean isCompilationErrors() {
		return compilationErrors;
	}
	public void setCompilationErrors(boolean compilationErrors) {
		this.compilationErrors = compilationErrors;
	}
	public StringType getCompilerOut() {
		return compilerOut;
	}
	public void setCompilerOut(StringType compilerOut) {
		this.compilerOut = compilerOut;
	}
	public StringType getSource() {
		return source;
	}
	public void setSource(StringType source) {
		this.source = source;
	}
	public ByteArrayType getBytecode() {
		return bytecode;
	}
	public void setBytecode(ByteArrayType bytecode) {
		this.bytecode = bytecode;
	}
	public boolean isNewClass() {
		return newClass;
	}
	public void setNewClass(boolean newClass) {
		this.newClass = newClass;
	}
	public StringType getNameOfClass() {
		return nameOfClass;
	}
	public void setNameOfClass(StringType nameOfClass) {
		this.nameOfClass = nameOfClass;
	}
	public StringType getClassesToDelete() {
		return classesToDelete;
	}
	public void setClassesToDelete(StringType classesToDelete) {
		this.classesToDelete = classesToDelete;
	}
	public TimestampType getBackupTime() {
		return backupTime;
	}
	public void setBackupTime(TimestampType backupTime) {
		this.backupTime = backupTime;
	}

	public StringType getBackupTimeString() {
		return backupTimeString;
	}

	public void setBackupTimeString(StringType backupTimeString) {
		this.backupTimeString = backupTimeString;
	}

	public BooleanType getEnabled() {
		return enabled;
	}

	public void setEnabled(BooleanType enabled) {
		this.enabled = enabled;
	}

	public StringType getClassesToDisable() {
		return classesToDisable;
	}

	public void setClassesToDisable(StringType classesToDisable) {
		this.classesToDisable = classesToDisable;
	}

	public StringType getClassesToEnable() {
		return classesToEnable;
	}

	public void setClassesToEnable(StringType classesToEnable) {
		this.classesToEnable = classesToEnable;
	}

	public TimestampType getCompilationTime() {
		return compilationTime;
	}

	public void setCompilationTime(TimestampType compilationTime) {
		this.compilationTime = compilationTime;
	}
	
}
