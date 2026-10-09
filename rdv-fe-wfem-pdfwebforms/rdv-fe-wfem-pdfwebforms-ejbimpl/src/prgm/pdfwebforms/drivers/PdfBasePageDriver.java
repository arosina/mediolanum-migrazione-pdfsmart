package prgm.pdfwebforms.drivers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.core.PdfActionInfos;
import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.drivers.io.PageEventOutputData;
import prgm.pdfwebforms.drivers.io.PageLoadInputData;
import prgm.pdfwebforms.drivers.io.PageLoadOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class PdfBasePageDriver extends AbstractDriver implements PdfPageDriverIntf{

	private String webApp = "";
	private PdfInfos pdfInfos = null; 
	private PdfDriverIntf pdfDriver = null;
	private Map<String, ArrayList<String>> fieldGroups = new HashMap<String, ArrayList<String>>();
	private Map<String, ArrayList<PdfPageDriverAction>> actions = new HashMap<String, ArrayList<PdfPageDriverAction>>();

	/**************************************************************************************************
	 * Creazione di gruppi di campi. I gruppi possono essere utilizzati come target della "callEvent"
	**************************************************************************************************/
	protected void createGroup(String groupName, String[] fieldNames){
		for(int i=0;i<fieldNames.length;i++){
			ArrayList<String> groups = fieldGroups.get(fieldNames[i]);
			if(groups == null){
				groups = new ArrayList<String>();
				fieldGroups.put(fieldNames[i], groups);
			}
			if(!groups.contains(groupName))
				groups.add(groupName);
		}
	}
	
	/**************************************************************************************************
	 * Creazione di più copie di un elemento azione
	**************************************************************************************************/
	protected void createAction(String actionName, PdfPageDriverAction[] actions){
		for(int i=0;i<actions.length;i++)
			createAction(actionName, actions[i]);
	}
	
	/**************************************************************************************************
	 * Creazione di un elemento di azione
	**************************************************************************************************/
	protected void createAction(String actionName, PdfPageDriverAction action){
		action.name = actionName;
		if(action.name == null || action.name.length() == 0)
			return;
		if(action.page <= 0)
			return;
		if(action.x < 0 || action.y < 0 || action.html == null)
			return;
		if(action.html.length() == 0)
			return;
		ArrayList<PdfPageDriverAction> actionList = actions.get(actionName);
		if(actionList == null){
			actionList = new ArrayList<PdfPageDriverAction>();
			actions.put(actionName, actionList);
		}
		actionList.add(action);
	}

	/**************************************************************************************************
	 * Inizializzazione di un campo azione (bottone sul pdf)
	 * L'html input verrà applicato a tutte le eventuali copie con lo stesso nome 
	**************************************************************************************************/
	protected void initAction(String actionName, String html){
		ArrayList<PdfActionInfos> actions = getPdfInfos().findActions(actionName);
		for(PdfActionInfos action : actions)
			action.html = html;
	}

	/**************************************************************************************************
	 * Inizializzazione di più campi azione con lo stesso nome (bottoni sul pdf)
	 * Gli html in input verranno applicati a ciascuna copia in ordine posizionale
	**************************************************************************************************/
	protected void initAction(String actionName, String[] htmls){
		ArrayList<PdfActionInfos> actions = getPdfInfos().findActions(actionName);
		int lastIdx = htmls.length < actions.size() ? htmls.length : actions.size();
		for(int i=0; i<lastIdx; i++){
			PdfActionInfos action = actions.get(i);
			action.html = htmls[i];
		}
	}
	
	/**************************************************************************************************
	 * Richiamato all'esecuzione del display del pdf e dopo il richiamo di un evento tramite "submitEvent"
	**************************************************************************************************/
	public PageLoadOutputData onLoad(ClientSessionContext csc, PageLoadInputData input) throws Exception{
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato nel "disegnare" lo header della pagina del pdf.
	 * In questo modo è possibile "iniettare" del codice js dinamico in pagina
	**************************************************************************************************/
	public String drawHeader(PdfModel pdf, PdfDataModel pdfData, PdfAnagModel pdfAnag){
		return "";
	}

	/**************************************************************************************************
	 * Richiamato all'esecuzione di un evento
	 * Se non sovrascritto viene richiamato un metodo con il nome dell'evento
	 * Il metodo in input avrà: ClientSessionContext, PageEventInputData, PageEventOutputData e ritornerà void
	**************************************************************************************************/
	public PageEventOutputData onEvent(ClientSessionContext csc, String eventName, PageEventInputData input) throws Exception{
		return fireEvent(this.getClass(), csc, eventName, input);
	}
	
	/**************************************************************************************************
	 * Per ottenere il driver e potrne richiamare i controlli
	**************************************************************************************************/
	public PdfDriverIntf getPdfDriver(){
		return pdfDriver;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String fieldGroups(String fieldName){
		ArrayList<String> groups = fieldGroups.get(fieldName);
		if(groups == null || groups.size() == 0)
			return "";
		String res = "";
		for(String group: groups)
			res += group+" ";
		if(res.length() > 0)
			res = res.substring(0,res.length()-1);
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String drawPageActions(int pageNum){
		StringBuffer res = new StringBuffer();
		int count=1;
	    Iterator it = actions.keySet().iterator();
	    while(it.hasNext()){
	    	String actionName = (String)it.next();
	    	ArrayList<PdfPageDriverAction> actionList = actions.get(actionName);
		    for(PdfPageDriverAction action : actionList){
		    	if(action.page != pageNum)
		    		continue;
				
				String title = "";
				if(action.helpText != null && action.helpText.length() > 0)
					title = " title=\""+action.helpText.replaceAll("\\\"","'")+"\"";

				String actGroups = fieldGroups(action.name);
				if(actGroups.length() > 0)
					actGroups = " groups='"+actGroups+"'";
				
				res.append("<div class='pdfAction' id='"+action.name+pageNum+count+"' name='"+action.name+"Cont'"+actGroups+
								"style='left:"+action.x+"px;top:"+action.y+"px;'"+title+">");
				res.append(action.html);
				res.append("</div>\n");
				count++;
		    }
	    }
		return res.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PageEventOutputData fireEvent(Class<?> clazz, ClientSessionContext csc, String eventName, PageEventInputData input) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException   {           
        try {
            Method  method = clazz.getDeclaredMethod(eventName, new Class[]{csc.getClass(), input.getClass()});
            method.setAccessible(true);
            PageEventOutputData result = (PageEventOutputData)method.invoke(this, new Object[]{csc, input});
            return result;
        }catch (NoSuchMethodException e) {  
        	if (clazz == Object.class) {
        		return null;
        	}
        	return fireEvent(clazz.getSuperclass(), csc, eventName, input);
        }       
	}
	
	public String getWebApp() {
		return webApp;
	}

	public void setWebApp(String webApp) {
		this.webApp = webApp;
	}

	public PdfInfos getPdfInfos() {
		return pdfInfos;
	}

	public void setPdfInfos(PdfInfos pdfInfos) {
		this.pdfInfos = pdfInfos;
	}

	public void setPdfDriver(PdfDriverIntf pdfDriver) {
		this.pdfDriver = pdfDriver;
	}
}
