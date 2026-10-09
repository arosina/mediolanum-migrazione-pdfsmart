package prgm.pdfwebforms.publisher.display;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.common.CostantiPublisher;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfConfiguration extends DisplayCommand implements GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
		model.setUploadMaxSize(4000*1024);
		PdfAnagModel pdfAnag = model.getPdfAnag();
		
		CodDescDataList dl = pdfAnag.getCodDescDataList("pdfCodOperazionePrit");
		if(dl != null){
			if(dl.getCodDesc(""+PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE) != null)
				dl.removeCodDesc(""+PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE);
			if(!pdfAnag.getPdfCodProdottoPrit().isNull()){
				CodDescData multiOp = new CodDescData();
				multiOp.setCod(""+PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE);
				multiOp.setDescr(PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_DESCR);
				dl.addCodDescData(multiOp);
			}
		}
		
		dl = new CodDescDataList();
		CodDescData d = null;
		d = new CodDescData(); d.setCod(""); d.setDescr("Non attiva"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("D"); d.setDescr("Attiva solo in firma digitale"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("S"); d.setDescr("Attiva con tutte le modalita' di firma"); dl.addCodDescData(d);
		pdfAnag.addCodDescField("callSrvDispositivaBMED", dl);
		
		dl = new CodDescDataList();
		d = new CodDescData(); d.setCod(""); d.setDescr("Orizzontale (MOM)"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("MOM2"); d.setDescr("Verticale (MOM2)"); dl.addCodDescData(d);
		pdfAnag.addCodDescField("tipoProcessoSede", dl);

		dl = new CodDescDataList();
		d = new CodDescData(); d.setCod("S"); d.setDescr("Solo materiale"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("P"); d.setDescr("Materiale + Adeguatezza previdenza"); dl.addCodDescData(d);
		pdfAnag.addCodDescField("tipoPriips", dl);
		
		pdfAnag.initInvioInSedeFromFlags();
		
		try{
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			pdfAnag.setPdfApplReferences(dao.executeQueryAccess("loadPdfAnagApplReferences",pdfAnag).getResult());
		}catch(DAOException daoe){
			LOG.error(daoe);
		}			
		return model;			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigurationModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		
		Template template = row.getTemplate(); 
		PdfConfigurationModel confModel = (PdfConfigurationModel)template.getPageDataModel();
		PdfAnagModel pdf = (PdfAnagModel)row;
		FieldStyle fs = new FieldStyle();
		
		if(cellPropertyName.equals("pdfPublicationId")){
			if(pdf.getPdfPublicationId().intValue() == pdf.getPdfPubIdCorrente().intValue()){
				fs.innerHTML = "<b>["+pdf.getPdfPublicationId()+"]</b>";
				fs.color = "green";
			}
		}
		
		if(cellPropertyName.equals("comandi")){
			boolean archivia = false;
			if(pdf.getPdfPublicationId().intValue() != pdf.getPdfPubIdCorrente().intValue() && pdf.isPdfPassato())
				archivia = true;
			boolean elimina = false;
			if(pdf.getPdfPubStartDate().compareTo(Tools.today()) > 0)
				elimina = true;
			fs.innerHTML =  "&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='selezionaPubblicazione("+pdf.getPdfPublicationId()+");'>seleziona</span>"+
							"&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='visualizzaFilePdfPubblicato("+pdf.getPdfPublicationId()+");'>pdf</span>"+
							"&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='visualizzaAcroformPdfPubblicato("+pdf.getPdfPublicationId()+");'>acroform</span>"+
							"&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='verificaPdfPubblicato("+pdf.getPdfPublicationId()+");'>simula</span>";
			if(archivia)
				fs.innerHTML += "&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='archiviaPubblicazione("+pdf.getPdfPublicationId()+");'>archivia</span>";
			if(elimina)
				fs.innerHTML += "&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='eliminaPubblicazione("+pdf.getPdfPublicationId()+",\""+pdf.getPdfMomVersion()+"\");'>elimina</span>";
			if(confModel.getProfiloUtente().equalsIgnoreCase(CostantiPublisher.ProfiliUtente.SVILUPPO))
				fs.innerHTML += "&nbsp;&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='createPdfDataHelperPdfPubblicato("+pdf.getPdfPublicationId()+");'>helper</span>";
		}
		
		cell.setStyle(fs);
	}

}
