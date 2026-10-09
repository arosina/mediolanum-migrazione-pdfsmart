package com.atosorigin.wfem.util;

import java.io.Serializable;

/* *******************************************************************************
 * This element expires only if is not used by applications
   ******************************************************************************* */
public abstract class RefreshableCacheElement implements Serializable{
	private int currentTime = 0;

	public abstract int getRefreshTime();

	public void resetElementTime(){
		currentTime = getRefreshTime();
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

}
