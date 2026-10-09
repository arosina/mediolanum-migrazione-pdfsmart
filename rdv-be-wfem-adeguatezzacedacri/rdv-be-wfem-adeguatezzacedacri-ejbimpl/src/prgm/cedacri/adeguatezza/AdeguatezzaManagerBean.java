package prgm.cedacri.adeguatezza;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioBean;
import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioConvert;
import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloBean;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloConvert;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaBean;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaConvert;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaModel;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariBean;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariConvert;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariModel;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioBean;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioConvert;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloBean;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloConvert;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaBean;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaConvert;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaModel;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariBean;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariConvert;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariModel;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioConvert;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioModel;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

@Stateless(name = "AdeguatezzaManager", mappedName = "AdeguatezzaManager")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
/********************************************************************************************************/
/********************************************************************************************************/
public class AdeguatezzaManagerBean extends ManagerObject implements AdeguatezzaManager
{
	private static final long serialVersionUID = 0;
	private final String channelDef  = new String("P");
	private final String countryDef  = new String("ITA");
	
	/*
	 * Servizio 1
	 * Servizio Compilazione Nuovo Questionario
	 */
	public OutputGetNuovoQuestionarioModel getNuovoQuestionario (ClientSessionContext csc, InputGetNuovoQuestionarioModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.getNuovoQuestionario(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	public OutputGetNuovoQuestionarioBean getNuovoQuestionarioWS (InputGetNuovoQuestionarioBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputGetNuovoQuestionarioConvert inputConvert = new InputGetNuovoQuestionarioConvert();
		OutputGetNuovoQuestionarioConvert outputConvert = new OutputGetNuovoQuestionarioConvert();
		InputGetNuovoQuestionarioModel inputModel = (InputGetNuovoQuestionarioModel)inputConvert.convertToModel(input);
		OutputGetNuovoQuestionarioModel outputModel = AdeguatezzaManagerImpl.getNuovoQuestionario(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputGetNuovoQuestionarioBean)outputConvert.convertToBean(outputModel);
	}
	
	/*
	 * Servizio 2
	 * Servizio di controllo congruenza domande/risposte e calcolo del profilo dell'investitore
	 */
	public OutputCalcoloProfiloModel calcoloProfilo (ClientSessionContext csc, InputCalcoloProfiloModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.calcoloProfilo(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	public OutputCalcoloProfiloBean calcoloProfiloWS (InputCalcoloProfiloBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputCalcoloProfiloConvert inputConvert = new InputCalcoloProfiloConvert();
		OutputCalcoloProfiloConvert outputConvert = new OutputCalcoloProfiloConvert();
		InputCalcoloProfiloModel inputModel = (InputCalcoloProfiloModel)inputConvert.convertToModel(input);
		OutputCalcoloProfiloModel outputModel = AdeguatezzaManagerImpl.calcoloProfilo(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputCalcoloProfiloBean)outputConvert.convertToBean(outputModel);
	}
	
	/*
	 * Servizio 4
	 * Servizio per la restituzione dell'elenco questionari
	 */
	public OutputGetElencoQuestionariModel getElencoQuestionari (ClientSessionContext csc, InputGetElencoQuestionariModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.getElencoQuestionari(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	public OutputGetElencoQuestionariBean getElencoQuestionariWS (InputGetElencoQuestionariBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputGetElencoQuestionariConvert inputConvert = new InputGetElencoQuestionariConvert();
		OutputGetElencoQuestionariConvert outputConvert = new OutputGetElencoQuestionariConvert();
		InputGetElencoQuestionariModel inputModel = (InputGetElencoQuestionariModel)inputConvert.convertToModel(input);
		OutputGetElencoQuestionariModel outputModel = AdeguatezzaManagerImpl.getElencoQuestionari(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputGetElencoQuestionariBean)outputConvert.convertToBean(outputModel);
	}
	
	/*
	 * Servizio 5
	 * Servizio Lettura Ultimo Questionario 
	 */
	public OutputGetQuestionarioModel getQuestionario (ClientSessionContext csc, InputGetQuestionarioModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.getQuestionario(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	public OutputGetQuestionarioBean getQuestionarioWS (InputGetQuestionarioBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputGetQuestionarioConvert inputConvert = new InputGetQuestionarioConvert();
		OutputGetQuestionarioConvert outputConvert = new OutputGetQuestionarioConvert();
		InputGetQuestionarioModel inputModel = (InputGetQuestionarioModel)inputConvert.convertToModel(input);
		OutputGetQuestionarioModel outputModel = AdeguatezzaManagerImpl.getQuestionario(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputGetQuestionarioBean)outputConvert.convertToBean(outputModel);
	}
	
	/*
	 * Servizio 6
	 * Servizio di memorizzazione/storicizzazione dei dati
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutputSalvaQuestionarioModel salvaQuestionario (ClientSessionContext csc, InputSalvaQuestionarioModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.salvaQuestionario(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutputSalvaQuestionarioBean salvaQuestionarioWS (InputSalvaQuestionarioBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputSalvaQuestionarioConvert inputConvert = new InputSalvaQuestionarioConvert();
		OutputSalvaQuestionarioConvert outputConvert = new OutputSalvaQuestionarioConvert();
		InputSalvaQuestionarioModel inputModel = null;
		try
		{
			inputModel = (InputSalvaQuestionarioModel)inputConvert.convertToModel(input);
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			OutputSalvaQuestionarioBean outputError = new OutputSalvaQuestionarioBean();
			outputError.setEsito("999");
			outputError.setDescErr("<![CDATA["+sw.toString()+"]]>");
			return outputError;
		}
		OutputSalvaQuestionarioModel outputModel = AdeguatezzaManagerImpl.salvaQuestionario(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputSalvaQuestionarioBean)outputConvert.convertToBean(outputModel);
	}
	
	/*
	 * Servizio 7
	 * Servizio di cancellazione questionario in bozza
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutputCancellaQuestionarioModel cancellaQuestionario (ClientSessionContext csc, InputCancellaQuestionarioModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.cancellaQuestionario(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutputCancellaQuestionarioBean cancellaQuestionarioWS (InputCancellaQuestionarioBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputCancellaQuestionarioConvert inputConvert = new InputCancellaQuestionarioConvert();
		OutputCancellaQuestionarioConvert outputConvert = new OutputCancellaQuestionarioConvert();
		InputCancellaQuestionarioModel inputModel = (InputCancellaQuestionarioModel)inputConvert.convertToModel(input);
		OutputCancellaQuestionarioModel outputModel = AdeguatezzaManagerImpl.cancellaQuestionario(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputCancellaQuestionarioBean)outputConvert.convertToBean(outputModel);
	}

	/*
	 * Servizio 9
	 * Servizio Controllo Adeguatezza Operazione
	 */
	public OutputCtrlAdeguatezzaModel ctrlAdeguatezza  (ClientSessionContext csc, InputCtrlAdeguatezzaModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.ctrlAdeguatezza(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	public OutputCtrlAdeguatezzaBean  ctrlAdeguatezzaWS(InputCtrlAdeguatezzaBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputCtrlAdeguatezzaConvert inputConvert = new InputCtrlAdeguatezzaConvert();
		OutputCtrlAdeguatezzaConvert outputConvert = new OutputCtrlAdeguatezzaConvert();
		InputCtrlAdeguatezzaModel inputModel = null;
		try
		{
			inputModel = (InputCtrlAdeguatezzaModel)inputConvert.convertToModel(input);
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			OutputCtrlAdeguatezzaBean outputError = new OutputCtrlAdeguatezzaBean();
			outputError.setEsito("999");
			outputError.setDescErr("<![CDATA["+sw.toString()+"]]>");
			return outputError;
		}
		OutputCtrlAdeguatezzaModel outputModel = AdeguatezzaManagerImpl.ctrlAdeguatezza(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputCtrlAdeguatezzaBean)outputConvert.convertToBean(outputModel);
	}

	/*
	 * Servizio 10
	 * Servizio Lettura Ultimo Profilo 
	 */
	public OutputGetQuestionarioModel getProfilo (ClientSessionContext csc, InputGetQuestionarioModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.getProfilo(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	public OutputGetQuestionarioBean getProfiloWS (InputGetQuestionarioBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputGetQuestionarioConvert inputConvert = new InputGetQuestionarioConvert();
		OutputGetQuestionarioConvert outputConvert = new OutputGetQuestionarioConvert();
		InputGetQuestionarioModel inputModel = (InputGetQuestionarioModel)inputConvert.convertToModel(input);
		OutputGetQuestionarioModel outputModel = AdeguatezzaManagerImpl.getProfilo(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputGetQuestionarioBean)outputConvert.convertToBean(outputModel);
	}
	
	/*
	 * Servizio 11
	 * Servizio Aggiornamento Patrimonio
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutputAggiornaPatrimonioModel aggiornaPatrimonio (ClientSessionContext csc, InputAggiornaPatrimonioModel input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		return AdeguatezzaManagerImpl.aggiornaPatrimonio(csc,input,nomeMetodo,AdeguatezzaManagerImpl.falseType);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutputAggiornaPatrimonioBean aggiornaPatrimonioWS (InputAggiornaPatrimonioBean input) throws EJBException
	{
		StringType nomeMetodo = new StringType(this.getClass().getPackage().getName() + "." + (new Throwable()).getStackTrace()[0].getMethodName());
		ClientSessionContext csc = new ClientSessionContext();
		csc.setChannelCode(channelDef);
		csc.setCountryCode(countryDef);
		InputAggiornaPatrimonioConvert inputConvert = new InputAggiornaPatrimonioConvert();
		OutputAggiornaPatrimonioConvert outputConvert = new OutputAggiornaPatrimonioConvert();
		InputAggiornaPatrimonioModel inputModel = null;
		try
		{
			inputModel = (InputAggiornaPatrimonioModel)inputConvert.convertToModel(input);
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			OutputAggiornaPatrimonioBean outputError = new OutputAggiornaPatrimonioBean();
			outputError.setEsito("999");
			outputError.setDescErr("<![CDATA["+sw.toString()+"]]>");
			return outputError;
		}
		OutputAggiornaPatrimonioModel outputModel = AdeguatezzaManagerImpl.aggiornaPatrimonio(csc,inputModel,nomeMetodo,AdeguatezzaManagerImpl.falseType);
		return (OutputAggiornaPatrimonioBean)outputConvert.convertToBean(outputModel);
	}
	
}
