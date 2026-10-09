package com.atosorigin.wfem.dbjavaclasses;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DbJavaProjectModel extends CommandDataModel {
	
	private boolean newProject = false;
	private StringType projectsToDelete = new StringType();
	
	private StringType projectName = new StringType();
	private StringType jarFiles = new StringType();
	private ListType   classes = new ListType(DbJavaClassModel.class);
	
	public StringType getJarFiles() {
		return jarFiles;
	}

	public void setJarFiles(StringType jarFiles) {
		this.jarFiles = jarFiles;
	}

	public StringType getProjectName() {
		return projectName;
	}

	public void setProjectName(StringType projectName) {
		this.projectName = projectName;
	}

	public ListType getClasses() {
		return classes;
	}

	public void setClasses(ListType classes) {
		this.classes = classes;
	}

	public boolean isNewProject() {
		return newProject;
	}

	public void setNewProject(boolean newProject) {
		this.newProject = newProject;
	}

	public StringType getProjectsToDelete() {
		return projectsToDelete;
	}

	public void setProjectsToDelete(StringType projectsToDelete) {
		this.projectsToDelete = projectsToDelete;
	}

}
