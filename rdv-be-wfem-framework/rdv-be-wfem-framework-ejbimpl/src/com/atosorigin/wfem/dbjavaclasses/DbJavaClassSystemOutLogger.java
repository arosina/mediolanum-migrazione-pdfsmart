package com.atosorigin.wfem.dbjavaclasses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/***********************************************************************************************/
/***********************************************************************************************/
public class DbJavaClassSystemOutLogger extends PrintStream {

    protected PrintStream systemOut = null;
    ByteArrayOutputStream baos = null;
    PrintStream ps = null;

	/***********************************************************************************************/
	/***********************************************************************************************/
    public DbJavaClassSystemOutLogger() {
    	super(System.out);
        this.systemOut = System.out;
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
    }

	/***********************************************************************************************/
	/***********************************************************************************************/
    public void startLog(){
    	System.setOut(this); 
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
    public String stopLog(){
    	System.setOut(systemOut);
        return baos.toString();    	
    }

    /***********************************************************************************************/
	/***********************************************************************************************/
    protected PrintStream findStream() {
        return ps;
    }

    public void flush() {
        findStream().flush();
    }

    public void close() {
        findStream().close();
    }

    public boolean checkError() {
        return findStream().checkError();
    }

    protected void setError() {
        //findStream().setError();
    }

    public void write(int b) {
        findStream().write(b);
    }

    public void write(byte[] b)
        throws IOException {
        findStream().write(b);
    }

    public void write(byte[] buf, int off, int len) {
        findStream().write(buf, off, len);
    }

    public void print(boolean b) {
        findStream().print(b);
    }

    public void print(char c) {
        findStream().print(c);
    }

    public void print(int i) {
        findStream().print(i);
    }

    public void print(long l) {
        findStream().print(l);
    }

    public void print(float f) {
        findStream().print(f);
    }

    public void print(double d) {
        findStream().print(d);
    }

    public void print(char[] s) {
        findStream().print(s);
    }

    public void print(String s) {
        findStream().print(s);
    }

    public void print(Object obj) {
        findStream().print(obj);
    }

    public void println() {
        findStream().println();
    }

    public void println(boolean x) {
        findStream().println(x);
    }

    public void println(char x) {
        findStream().println(x);
    }

    public void println(int x) {
        findStream().println(x);
    }

    public void println(long x) {
        findStream().println(x);
    }

    public void println(float x) {
        findStream().println(x);
    }

    public void println(double x) {
        findStream().println(x);
    }

    public void println(char[] x) {
        findStream().println(x);
    }

    public void println(String x) {
        findStream().println(x);
    }

    public void println(Object x) {
        findStream().println(x);
    }

}
