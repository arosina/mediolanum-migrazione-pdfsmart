package prgm.ita.p.dac.sede.facade;

import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class GestoreEditabilita {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void setEditabilitaDocumenti (ListType documenti) {
		for (int i=0; i<documenti.size(); i++){
			DocumentoModel doc = (DocumentoModel)documenti.get(i);

			doc.getBarcode().setEditable(false);
			doc.getDescrProdotto().setEditable(false);
			doc.getNumeroContratto().setEditable(false);
			doc.getDescrOperazione().setEditable(false);
			doc.getAgente().getCodAgente().setEditable(false);
			doc.getCliente().getNominativo().setEditable(false);
		}
	}
	
}
