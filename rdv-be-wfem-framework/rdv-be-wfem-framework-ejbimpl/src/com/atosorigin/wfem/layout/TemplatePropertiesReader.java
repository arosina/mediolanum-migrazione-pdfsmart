package com.atosorigin.wfem.layout;

import java.util.List;
import java.util.Properties;

import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandMessage;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeMessage;
import com.atosorigin.wfem.types.TypeWarning;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class TemplatePropertiesReader implements java.io.Serializable{

	protected Properties properties = new Properties();
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public TemplatePropertiesReader(){
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public TemplatePropertiesReader(Properties properties){
		this.properties = properties;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( CommandError commandError ) {
	
		String param0 = null, param1 = null, param2 = null, param3 = null;
		String propertyName = commandError.getKey();
		Object[] values     = commandError.getValues();
	
		if ( values[3] != null ) { param3 = values[3].toString(); }
		if ( values[2] != null ) { param2 = values[2].toString(); }
		if ( values[1] != null ) { param1 = values[1].toString(); }
		if ( values[0] != null ) { param0 = values[0].toString(); }
		
		return getProperty( propertyName, param0, param1, param2, param3 );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( CommandMessage commandMessage ) {
	
		String param0 = null, param1 = null, param2 = null, param3 = null;
		String propertyName = commandMessage.getKey();
		Object[] values     = commandMessage.getValues();
	
		if ( values[3] != null ) { param3 = values[3].toString(); }
		if ( values[2] != null ) { param2 = values[2].toString(); }
		if ( values[1] != null ) { param1 = values[1].toString(); }
		if ( values[0] != null ) { param0 = values[0].toString(); }
		
		return getProperty( propertyName, param0, param1, param2, param3 );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( CommandWarning commandWarning ) {
	
		String param0 = null, param1 = null, param2 = null, param3 = null;
		String propertyName = commandWarning.getKey();
		Object[] values     = commandWarning.getValues();
	
		if ( values[3] != null ) { param3 = values[3].toString(); }
		if ( values[2] != null ) { param2 = values[2].toString(); }
		if ( values[1] != null ) { param1 = values[1].toString(); }
		if ( values[0] != null ) { param0 = values[0].toString(); }
		
		return getProperty( propertyName, param0, param1, param2, param3 );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( TypeError typeError ) {
	
		String param0 = null, param1 = null, param2 = null, param3 = null;
		String propertyName = typeError.getKey();
		Object[] values     = typeError.getValues();
	
		if ( values[3] != null ) { param3 = values[3].toString(); }
		if ( values[2] != null ) { param2 = values[2].toString(); }
		if ( values[1] != null ) { param1 = values[1].toString(); }
		if ( values[0] != null ) { param0 = values[0].toString(); }
		
		return getProperty( propertyName, param0, param1, param2, param3 );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( TypeWarning typeWarning ) {
	
		String param0 = null, param1 = null, param2 = null, param3 = null;
		String propertyName = typeWarning.getKey();
		Object[] values     = typeWarning.getValues();
	
		if ( values[3] != null ) { param3 = values[3].toString(); }
		if ( values[2] != null ) { param2 = values[2].toString(); }
		if ( values[1] != null ) { param1 = values[1].toString(); }
		if ( values[0] != null ) { param0 = values[0].toString(); }
		
		return getProperty( propertyName, param0, param1, param2, param3 );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( TypeMessage typeMessage ) {
	
		String param0 = null, param1 = null, param2 = null, param3 = null;
		String propertyName = typeMessage.getKey();
		Object[] values     = typeMessage.getValues();
	
		if ( values[3] != null ) { param3 = values[3].toString(); }
		if ( values[2] != null ) { param2 = values[2].toString(); }
		if ( values[1] != null ) { param1 = values[1].toString(); }
		if ( values[0] != null ) { param0 = values[0].toString(); }
		
		return getProperty( propertyName, param0, param1, param2, param3 );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( String propertyName ) {
	
		return getProperty( propertyName, null, null, null, null );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( String propertyName, String param0 ) {
	
		return getProperty( propertyName, param0, null, null, null );
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( String propertyName, String param0, String param1 ) {
	
		return getProperty( propertyName, param0, param1, null, null );
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( String propertyName, String param0, String param1, String param2 ) {
	
		return getProperty( propertyName, param0, param1, param2, null );
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProperty( String propertyName, String param0, String param1, String param2, String param3 ) {
	
		if ( this.properties == null )
			return "";
		
		String propertyValue = properties.getProperty(propertyName);
		if(propertyValue == null){
			propertyValue = propertyName;
		}
		
		if( param0 == null && param1 == null && 
			param2 == null && param3 == null)
			return propertyValue;
			
		String translatedValue = propertyValue;
		
		if(param0 != null){
			translatedValue = replaceParamAll( translatedValue, "%1", param0 );
		}
		if(param1 != null){
			translatedValue = replaceParamAll( translatedValue, "%2", param1 );
		}
		if(param2 != null){
			translatedValue = replaceParamAll( translatedValue, "%3", param2 );
		}
		if(param3 != null){
			translatedValue = replaceParamAll( translatedValue, "%4", param3 );
		}
		
		return translatedValue;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String replaceParam( String sourceString, String oldToken, String newToken ) {
		
		String result = sourceString;
	
		int index = sourceString.indexOf( oldToken );
		if ( index != -1 ) {
			result  = sourceString.substring( 0, index );
			result += newToken;
			result += sourceString.substring( index + oldToken.length() );
		}
		return result;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String replaceParamAll( String sourceString, String oldToken, String newToken ) {
	
		String result = sourceString;
		while ( true ) {
			String prevResult = result;
			result = replaceParam( prevResult, oldToken, newToken );
			if ( result.equals( prevResult ) ) {
				break;
			}
		}
		return result;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isCommandEnabled( String commandClassName, String adminTypes ) {
	
		String propertyValue = getProperty( commandClassName, null, null, null, null );
	
		// Se il comando non esiste nel file di properties, NON E' ABILITATO
		if ( propertyValue == null ) {
			return false;
		}
		
		for ( int i = 0; i < adminTypes.length(); i++ ) {
			String oneAdminType = adminTypes.substring(i,i+1);
	
			// Se è stato trovato un AdminType dell'utente, E' ABILITATO
			if (( propertyValue.indexOf( "["+oneAdminType+"]" ) != -1 ) ||
				( propertyValue.indexOf( "["+oneAdminType+"," ) != -1 ) ||
				( propertyValue.indexOf( ","+oneAdminType+"," ) != -1 ) ||
				( propertyValue.indexOf( ","+oneAdminType+"]" ) != -1 )) {
					
				return true;
			}	
		}	
	
		// Se non è stato trovato un AdminType dell'utente, NON E' ABILITATO
		return false;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isCommandEnabled( String commandClassName, List userRoles ) {
	
		String propertyValue = getProperty( commandClassName, null, null, null, null );
	
		// Se il comando non esiste nel file di properties, NON E' ABILITATO
		if ( propertyValue == null ) {
			return false;
		}
		
		for ( int i = 0; i < userRoles.size(); i++ ) {
			Integer role = (Integer) userRoles.get(i);
			String strRole = role.toString();
	
			// Se è stato trovato uno dei Ruoli dell'utente, E' ABILITATO
			if (( propertyValue.indexOf( "["+strRole+"]" ) != -1 ) ||
				( propertyValue.indexOf( "["+strRole+"," ) != -1 ) ||
				( propertyValue.indexOf( ","+strRole+"," ) != -1 ) ||
				( propertyValue.indexOf( ","+strRole+"]" ) != -1 )) {
					
				return true;
			}	
		}	
	
		// Se non è stato trovato uno dei Ruoli dell'utente, NON E' ABILITATO
		return false;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
