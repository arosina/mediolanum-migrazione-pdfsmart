package com.atosorigin.wfem.bo;

import java.util.Vector;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BoParameters extends Vector {
	
	private boolean useParNameAsIndex = false;

	public boolean isUseParNameAsIndex() {
		return useParNameAsIndex;
	}

	public void setUseParNameAsIndex(boolean useParNameAsIndex) {
		this.useParNameAsIndex = useParNameAsIndex;
	}

}
