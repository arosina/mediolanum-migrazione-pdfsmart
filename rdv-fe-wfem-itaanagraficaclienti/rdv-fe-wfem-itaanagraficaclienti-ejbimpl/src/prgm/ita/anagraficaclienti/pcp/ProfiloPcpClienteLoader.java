package prgm.ita.anagraficaclienti.pcp;

import java.util.UUID;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.ita.anagraficaclienti.model.ClienteKeyModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ProfiloPcpClienteLoader {

	private static final String DAO_PCP_XML_NAME = "ItaAnagraficaClienti.ProfiloPcpCliente";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ProfiloPcpClienteLoader() {}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProfiloPcpClienteModel loadProfiloPcp(ClientSessionContext csc, ClienteKeyModel clienteKey, String flagProvvisorio) {
		String idCliente = clienteKey.getCodMediolanum().toString();
		if(idCliente.length() > 0)
			idCliente = Tools.fillSx(idCliente,'0',11);
		else
			idCliente = Tools.fillSx(clienteKey.getCodPotenziale().toString(),'0',16);
		return loadProfiloPcp(csc, idCliente, flagProvvisorio);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static ProfiloPcpClienteModel loadProfiloPcp(ClientSessionContext csc, String idCliente, String flagProvvisorio) {
		
		ProfiloPcpClienteSrvInputModel input = new ProfiloPcpClienteSrvInputModel();
		input.setIdConversazione(new StringType(UUID.randomUUID().toString()));
		input.setTimeStamp(Tools.now());
		input.setUserId(new StringType(csc.getUserCode()));
		input.setIdCliente(new StringType(idCliente));
		input.setFlagProvvisorio(new StringType(flagProvvisorio));

		ProfiloPcpClienteModel res = new ProfiloPcpClienteModel();
		try {
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_PCP_XML_NAME).executeOSBAccess("getPcpCliente", input);
			if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK)
				return res;
			return (ProfiloPcpClienteModel)wsRes.getResult();
		}catch(DAOException daoe) {
			return res;
		}

	}
}
