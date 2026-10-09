package moke.menu2.display;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import moke.menu2.model.Application;
import moke.menu2.model.Function;
import moke.menu2.model.Group;
import moke.menu2.model.MenuModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class Menu extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			MenuModel menuModel = getMenu(csc);
			createCodes(menuModel);
			if(!menuModel.getCheckRoles().booleanValue())
				return menuModel;
			
			InputStream inputStream = this.getClass().getResourceAsStream("/appsproperties/mokemenu/users");
			Properties p = new Properties();
			p.load(inputStream);
			inputStream.close();

			String userCode = Tools.unFillSx(userSessionContext.getUserCode().toLowerCase(),'0');
			String rolesString = p.getProperty(userCode);
			if(rolesString == null || rolesString.equals(""))
				return menuModel;

			int idx = rolesString.indexOf(",");
			if(idx < 0)
				return menuModel;
			
			rolesString = rolesString.substring(idx+1);
			manageRoles(menuModel,rolesString);
			return menuModel;
			
		}catch (Exception e) {
			throw new CommandException(e);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private MenuModel getMenu(ClientSessionContext csc){
		try{
		
		 InputStream inputStream = this.getClass().getResourceAsStream("/appsproperties/mokemenu/Menu.xml");
		 if(inputStream == null)
			 return new MenuModel();
		 MenuModel menuModel = (MenuModel)Tools.modelFromXml(MenuModel.class,inputStream);
		 inputStream.close();
		 return menuModel;
		 
		}catch(Exception e){
			e.printStackTrace();
			return new MenuModel();
		}
	}
	
	/***********************************************************************************************
	 ***********************************************************************************************/
	private void createCodes(MenuModel menuModel){
		ListType groups = menuModel.getGroups();
		for(int g=0;g<groups.size();g++){
			Group group = (Group)groups.get(g);
			group.setCode(new StringType(""+g));
			ListType applications = group.getApplications();
			for(int a=0;a<applications.size();a++){
				Application appl = (Application)applications.get(a);
				appl.setCode(new StringType(""+g+"_"+a));
				ListType funzs = appl.getFunctions();
				for(int f=0;f<funzs.size();f++){
					Function funz = (Function)funzs.get(f);
					funz.setCode(new StringType(""+g+"_"+a+"_"+f));
					if(funz.getMenuLayer().equals("0") && f < (funzs.size()-1)){
						Function subfunz = (Function)funzs.get(f+1);
						if(subfunz.getMenuLayer().equals("1"))
							funz.setHasChilds(true);
					}
				}
				
			}
	    }
	}
	
	/***********************************************************************************************
	 ***********************************************************************************************/
	private void manageRoles(MenuModel menuModel, String roles){	    
	    List l = menuModel.getGroups().getElements();
	    for(int i=l.size()-1;i>=0;i--){
	        Group e = (Group)l.get(i);
	        if(manageRolesInGroup(e,roles) == 0)
	            l.remove(i);
	    }
	}

	/***********************************************************************************************
	 ***********************************************************************************************/
	private int manageRolesInGroup(Group g, String roles){
	    List l = g.getApplications().getElements();
	    for(int i=l.size()-1;i>=0;i--){
	        Application e = (Application)l.get(i);
	        if(manageRolesInAppl(e,roles) == 0)
	            l.remove(i);
	    }
	    return l.size();
	}

	/***********************************************************************************************
	 ***********************************************************************************************/
	private int manageRolesInAppl(Application a, String roles){
	    List l = a.getFunctions().getElements();
	    for(int i=l.size()-1;i>=0;i--){
	        Function e = (Function)l.get(i);
	        if(!manageRolesInFunc(e,roles))
	            l.remove(i);
	    }
	    return l.size();
	}

	/***********************************************************************************************
	 ***********************************************************************************************/
	private boolean manageRolesInFunc(Function f, String userRoles){
	    if(f.getRoles().isNull() || f.getRoles().equals("*"))
	        return true;
		StringTokenizer uRoles = new StringTokenizer(userRoles,"+");
		while(uRoles.hasMoreTokens()){
		    String uRole = uRoles.nextToken();
			StringTokenizer fRoles = new StringTokenizer(f.getRoles().toString(),"+");
			while(fRoles.hasMoreTokens()){
			    String fRole = fRoles.nextToken();
			    if(uRole.equalsIgnoreCase(fRole))
			        return true;
			}		    
		}
		return false;
	}
	
}
