package prgm.cedacri.adeguatezza.log;

public interface UserAccessLogConstants extends java.io.Serializable {

  public static final String PKG_UA_LOG = "prgm.useraccesslog.UserAccessLog";
	
  // Costanti per il Tipo di Accesso sui LOG
  public final int AT_APPLICATION = 1;
  public final int AT_FUNCTION = 2;
  public final int AT_COMMAND = 3;
  public final int AT_LOGIN = 4;
  public final int AT_LOGIN_WITH_DELEGATE = 5;
  public final int AT_CHANGE_PASSWORD = 6;
  public final int AT_RESET_PASSWORD = 7;
  public final int AT_ENABLE_ACCOUNT = 8;
  public final int AT_DISABLE_ACCOUNT = 9;

  // Tipo Accesso per servizi Esterni 
  public final int AT_WS_EXTERNAL_COMMAND = 10;
  public final int AT_EJB_EXTERNAL_COMMAND = 11;

  public final int AT_CHANGE_REF_AGENT = 12;
  public final int AT_LOCK_USER_PRGM_SIDE = 13;
  public final int AT_UNLOCK_USER_PRGM_SIDE = 14;
  
  public final int AT_ONLINE_AUTH_CHECK = 15;
  public final int AT_LOGOUT = 16;
  
}