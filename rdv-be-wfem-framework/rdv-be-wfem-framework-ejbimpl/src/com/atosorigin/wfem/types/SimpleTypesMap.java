package com.atosorigin.wfem.types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class SimpleTypesMap {
    
    public static final     int STRING          = 1;
    public static final     int INTEGER         = 2;
    public static final     int LONG            = 3;
    public static final     int DOUBLE          = 4;
    public static final     int BOOLEAN         = 5;
    public static final     int DATE            = 6;
    public static final     int STRINGTYPE      = 7;
    public static final     int INTEGERTYPE     = 8;
    public static final     int LONGTYPE        = 9;
    public static final     int DOUBLETYPE      = 10;
    public static final     int BOOLEANTYPE     = 11;
    public static final     int DATETYPE        = 12;
    public static final     int TIMESTAMPTYPE   = 13;
    public static final     int BYTEARRAYTYPE   = 14;
    public static final     int FILETYPE        = 15;
        
    private static final Map<Class<?>, Integer> simpleTypes = new HashMap<Class<?>, Integer>(); 
    static{
        simpleTypes.put(String.class,           new Integer(STRING));
        simpleTypes.put(Integer.class,          new Integer(INTEGER));
        simpleTypes.put(Long.class,             new Integer(LONG));
        simpleTypes.put(Double.class,           new Integer(DOUBLE));
        simpleTypes.put(Boolean.class,          new Integer(BOOLEAN));
        simpleTypes.put(Date.class,             new Integer(DATE));
        simpleTypes.put(StringType.class,       new Integer(STRINGTYPE));
        simpleTypes.put(IntegerType.class,      new Integer(LONGTYPE));
        simpleTypes.put(DoubleType.class,       new Integer(DOUBLETYPE));
        simpleTypes.put(BooleanType.class,      new Integer(BOOLEANTYPE));
        simpleTypes.put(DateType.class,         new Integer(DATETYPE));
        simpleTypes.put(TimestampType.class,    new Integer(TIMESTAMPTYPE));
        simpleTypes.put(ByteArrayType.class,    new Integer(BYTEARRAYTYPE));
        simpleTypes.put(FileType.class,         new Integer(FILETYPE));
    }
        
    /*************************************************************************************************/
    /*************************************************************************************************/
    public static int getClassCode(Class<?> c){
        Integer classCode = simpleTypes.get(c);
        return classCode == null ? -1 : classCode.intValue();
    }
}
