package prgm.pdfwebforms.signprocess.business;

import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.signprocess.common.SignUtility;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RichiediNuovoOtp extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;
            pdf.getPersonaCorrente().getSignData().getOtpDigitato().resetTypeErrors();
            if(!pdf.isTestMode())
            	pdf.getPersonaCorrente().getSignData().setOtpGenerato(new StringType());
            SignUtility.generaOTP(csc, pdf, pdf.getPersonaCorrente());
            if(pdf.hasCommandErrors()){
            	pdf.getPersonaCorrente().getSignData().setOtpGenerato(new StringType());
            	pdf.getPersonaCorrente().getSignData().getOtpDigitato().addTypeError(pdf.getCommandErrors().get(0).toString());
            }
            setForwardDisplay(new Integer(0));
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
