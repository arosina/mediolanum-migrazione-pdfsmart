package com.atosorigin.wfem.controller;

import java.util.UUID;

import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.CsrfWatchedCommand;
import com.atosorigin.wfem.controller.ControllerServlet.CommandsStack;

/**************************************************************************************************/
/**************************************************************************************************/
public class BrowserInstanceGenerator {
	
	private static final String WFEM_COMMAND_STACK_CONTEXT = "WfemCommandStackContext_";
	private static final String BROWSER_INSTANCE = "BrowserInstance";
	private static final String CRSF_DETECTED_ERR = "\n\nCSRF detected\n\n";
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private BrowserInstanceGenerator(){}
	
	/*
	 * From: https://stackoverflow.com/questions/2178992/how-to-generate-unique-id-in-java-integer
	 */
    public static Integer generateUniqueBrowserInstance(RequestManager requestManager) throws CommandException {   
    	for(int i=0;i<50;i++) {
	        UUID idOne = UUID.randomUUID();
	        String bi=""+idOne;       
	        int uid=myHashCode(bi);
	        String filterStr=""+uid;
	        bi=filterStr.replaceAll("-", "");
	        if(requestManager.getSession().getAttribute(WFEM_COMMAND_STACK_CONTEXT+bi) == null)
	        	return Integer.parseInt(bi);
    	}
        throw new CommandException("\n\nNot able to generate unique Browser Instance\n\n");
    }
	
    public static void watchCsrfCommand(RequestManager requestManager, Command command) throws CommandException {   
		if(command instanceof CsrfWatchedCommand ||
		   Configuration.getInstance().getCrossSiteRequestForgeryWatchList().get(command.getClass().getName()) != null) {
			String browserInstance = (String)requestManager.getAttribute(BROWSER_INSTANCE);
			if(browserInstance == null || browserInstance.equals("0"))
				throw new CommandException(CRSF_DETECTED_ERR);
			CommandsStack stack = (CommandsStack)requestManager.getSession().getAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance);
			if(stack == null || stack.getCurrentCommand() == null)
				throw new CommandException(CRSF_DETECTED_ERR);
		}
    }
    
    /*
     * From: https://medium.com/@vaibhav0109/https-medium-com-vaibhav0109-java-hashcode-collision-how-uniform-is-its-distribution-ee4e5e8dc894
     */
    private static int myHashCode(String input){
    	final int prime = 37;
	    int result = 17;
	    for (char character : input.toCharArray()){
	    	result = prime * result + character;
	    }
	    return result;
    }
  
}
