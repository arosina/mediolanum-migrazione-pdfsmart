package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail.model.InviaMailInputModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiceResponse;

public class InviaMailService {
	
	public static ServiceResponse chiamaInvioMail(ClientSessionContext csc, InviaMailInputModel input)  {
		// RFC #141104 - Archiviazione pdf su filenet PAPF. La mail viene inviata sempre dall'icw "signed"
		return ServiceResponse.esitoOK();
	}
}
