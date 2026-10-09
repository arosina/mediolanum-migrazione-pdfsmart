package com.atosorigin.wfem.layout;

import java.util.*;

public interface LayoutFactory extends java.io.Serializable{
	public Properties getTranslation(String language, String applCode);
}
