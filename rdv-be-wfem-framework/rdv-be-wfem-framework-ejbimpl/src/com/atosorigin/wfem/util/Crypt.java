package com.atosorigin.wfem.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Crypt implements java.io.Serializable {

  transient private static com.atosorigin.wfem.loggers.UtilLogger LOG = com.atosorigin.wfem.loggers.UtilLogger.getInstance();

  public static void main(String[] args) {
  	try{
		Crypt crypt = new Crypt();
		System.out.println(crypt.Encrypt(args[0]));
  	}catch(Exception e){
  		e.printStackTrace();
  	}
  }

  public Crypt() throws Exception {
    try {
      LOG.info(errHeader + ".Crypt() Start ");
      String genKey = "mediolanum";
      DESKeySpec desKeySpec = new DESKeySpec(genKey.getBytes());
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      keyGen = keyFactory.generateSecret(desKeySpec);
      this.initialize(keyGen);
      LOG.info(errHeader + ".Crypt() Stop ");
    }
    catch (NoSuchAlgorithmException ex) {
      String errorMsg = errHeader + ".Crypt() - NoSuchAlgorithmException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (InvalidKeyException ex) {
      String errorMsg = errHeader + ".Crypt() - InvalidKeyException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (InvalidKeySpecException ex) {
      String errorMsg = errHeader + ".Crypt() - InvalidKeySpecException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (Exception ex) {
      String errorMsg = errHeader + ".Crypt() - Exception : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }

  }

  public Crypt(String algorithm, String genKey) throws Exception {
    try {
      LOG.info(errHeader + ".Crypt(String,String) Start ");
      DESKeySpec desKeySpec = new DESKeySpec(genKey.getBytes());
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
      keyGen = keyFactory.generateSecret(desKeySpec);
      this.initialize(keyGen);
      this.initialize(keyGen);
      LOG.info(errHeader + ".Crypt(String,String) Stop ");
    }
    catch (NoSuchAlgorithmException ex) {
     String errorMsg = errHeader + ".Crypt(String,String) - NoSuchAlgorithmException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
     Exception ne = new Exception(errorMsg);
     LOG.error(ne);
    }
    catch (InvalidKeyException ex) {
      String errorMsg = errHeader + ".Crypt(String,String) - InvalidKeyException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (InvalidKeySpecException ex) {
      String errorMsg = errHeader + ".Crypt(String,String) - InvalidKeySpecException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (Exception ex){
      String errorMsg = errHeader + ".Crypt(String,String) - Exception : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
      throw ne;
    }

  }


  public String Encrypt(String str) throws Exception {

    try {
      LOG.info(errHeader + ".Encrypt(String) Start ");
      byte[] utf8 = str.getBytes("UTF8");
      byte[] enc = ecipher.doFinal(utf8);
      LOG.info(errHeader + ".Encrypt(String) Stop ");
      return new String(Tools.encodeBase64(enc));
    }
    catch (BadPaddingException ex) {
      String errorMsg = errHeader + ".Encrypt(String) - BadPaddingException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (IllegalBlockSizeException ex) {
      String errorMsg = errHeader + ".Encrypt(String) - IllegalBlockSizeException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (IllegalStateException ex) {
      String errorMsg = errHeader + ".Encrypt(String) - IllegalStateException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (UnsupportedEncodingException ex) {
      String errorMsg = errHeader + ".Encrypt(String) - UnsupportedEncodingException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (Exception ex){
      String errorMsg = errHeader + ".Encrypt(String) - Exception : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
      throw ne;
    }

    return null;
  }

  public String Decrypt(String str) throws Exception {

    try {
      LOG.info(errHeader + ".Decrypt(String) Start ");
      LOG.debug(errHeader + ".Decrypt(String) keyGen=" + keyGen.toString());
      this.initialize(keyGen);
      LOG.debug(errHeader + ".Decrypt(String) str=" + str);
      byte[] dec = Tools.decodeBase64(str.getBytes());
      byte[] utf8 = dcipher.doFinal(dec);
      LOG.info(errHeader + ".Decrypt(String) Stop ");
      return new String(utf8, "UTF8");
    }
    catch (BadPaddingException ex) {
      String errorMsg = errHeader + ".Decrypt(String) - BadPaddingException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (IllegalBlockSizeException ex) {
      String errorMsg = errHeader + ".Decrypt(String) - IllegalBlockSizeException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (IllegalStateException ex) {
      String errorMsg = errHeader + ".Decrypt(String) - IllegalStateException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (IOException ex) {
      String errorMsg = errHeader + ".Decrypt(String) - IOException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (Exception ex) {
      String errorMsg = errHeader + ".Decrypt(String) - Exception : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
      throw ne;
    }

    return null;

  }

  private void initialize(SecretKey keyGen) throws Exception {

    try {
      LOG.info(errHeader + ".initialize(SecretKey) Start ");
      LOG.debug(errHeader + ".initialize(SecretKey) --> keyGen.getAlgorithm() = " + keyGen.getAlgorithm());
      ecipher = Cipher.getInstance(keyGen.getAlgorithm());
      dcipher = Cipher.getInstance(keyGen.getAlgorithm());
      ecipher.init(Cipher.ENCRYPT_MODE, keyGen);
      dcipher.init(Cipher.DECRYPT_MODE, keyGen);
      LOG.info(errHeader + ".initialize(SecretKey) Stop ");
    }
    catch (InvalidKeyException ex) {
      String errorMsg = errHeader + ".initialize(SecretKey) - InvalidKeyException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (NoSuchPaddingException ex) {
      String errorMsg = errHeader + ".initialize(SecretKey)  - NoSuchPaddingException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (NoSuchAlgorithmException ex) {
      String errorMsg = errHeader + ".initialize(SecretKey)  - NoSuchAlgorithmException : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
    }
    catch (Exception ex) {
      String errorMsg = errHeader + ".initialize(SecretKey)  - Exception : (" + ex.getMessage() + ") " + " ( " + ex.toString() + " )";
      Exception ne = new Exception(errorMsg);
      LOG.error(ne);
      throw ne;
    }

  }

  SecretKey keyGen = null;
  Cipher ecipher;
  Cipher dcipher;
  String errHeader =  this.getClass().getName();

}