package com.atosorigin.wfem.layout;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.AbstractType;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public interface GridDecorator {
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell,  
						  int rowIndex, int cellIndex);
}
