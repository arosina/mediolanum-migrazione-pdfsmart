package com.atosorigin.wfem.controller;

/**
 * Insert the type's description here.
 * Creation date: (31/07/2002 14.27.53)
 * @author: Administrator
 */
public interface Constants {
	String NESTED_INDICATOR = "_";

	// Parameters to manage source/target model coping
	String TARGET_PROPERTY_NAME_PARAMETER  = "targetPropertyName";
	String SOURCE_PROPERTY_NAME_PARAMETER  = "sourcePropertyName";

	// Parameter with application errors (for business command) in request
	String BUSINESS_COMMAND_ERROR_PARAMETER = "errors";

	String TRACE_MODEL_ALL_PARAMETER   = "TRACE_MODEL_ALL";
	String TRACE_MODEL_PARAMETER 	   = "TRACE_MODEL_";
	String TRACE_COMMAND_ALL_PARAMETER = "TRACE_COMMAND_ALL";
	String TRACE_COMMAND_PARAMETER 	   = "TRACE_COMMAND_";
	String TRACE_REQUEST_PARAMETER	   = "TRACE_REQUEST";
	String TRACE_ENABLED               = "TRACE_ENABLED";
	
	String COMMAND_LISTENER_PARAMETER  = "commandListener";
}
