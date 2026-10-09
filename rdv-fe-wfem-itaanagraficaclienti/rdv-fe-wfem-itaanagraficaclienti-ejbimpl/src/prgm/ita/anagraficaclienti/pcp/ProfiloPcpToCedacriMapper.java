package prgm.ita.anagraficaclienti.pcp;

import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class ProfiloPcpToCedacriMapper {

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private ProfiloPcpToCedacriMapper() {}
	
	/*****************************************************************************************************/
	private static Map<String, String> codesClusterCedacri = new HashMap<String, String>();
	static{
		codesClusterCedacri.put("intraprendente lungo", 	"CLUSTER1");
		codesClusterCedacri.put("intraprendente medio", 	"CLUSTER2");
		codesClusterCedacri.put("intraprendente breve",		"CLUSTER3");
		codesClusterCedacri.put("equilibrato lungo", 		"CLUSTER4");
		codesClusterCedacri.put("equilibrato medio", 		"CLUSTER5");
		codesClusterCedacri.put("equilibrato breve", 		"CLUSTER6");
		codesClusterCedacri.put("conservatore lungo",		"CLUSTER7");
		codesClusterCedacri.put("conservatore medio", 		"CLUSTER8");
		codesClusterCedacri.put("conservatore breve",	 	"CLUSTER9");
	}
	/*****************************************************************************************************/
	public static OutputGetQuestionarioModel fromProfiloPcpToProfiloCedacri(ProfiloPcpClienteModel pcp) {
		OutputGetQuestionarioModel res = new OutputGetQuestionarioModel();
		
		String profilo = pcp.getProfilo().toString();
		res.setProfilo(new StringType(profilo));
		if(profilo.equalsIgnoreCase("CONSERVATORE"))
			res.setProfilo(new StringType("CON"));
		else if(profilo.equalsIgnoreCase("EQUILIBRATO"))
			res.setProfilo(new StringType("EQU"));
		else if(profilo.equalsIgnoreCase("INTRAPRENDENTE"))
			res.setProfilo(new StringType("INT"));
		
		String espfina = pcp.getEsposizioneFinanziaria().toString();
		res.setEspfina(new StringType(espfina));
		if(espfina.equalsIgnoreCase("ALTA"))
			res.setEspfina(new StringType("ALT"));
		else if(espfina.equalsIgnoreCase("MEDIA"))
			res.setEspfina(new StringType("MED"));
		else if(espfina.equalsIgnoreCase("BASSA"))
			res.setEspfina(new StringType("BAS"));
		
		String desClusterCedacri = pcp.getProfilo()+" "+pcp.getOrizzonteTemporale();
		res.setDesCluster(new StringType(desClusterCedacri));
		
		String codClusterCedacri = codesClusterCedacri.get(desClusterCedacri.toLowerCase());
		if(codClusterCedacri == null)
			codClusterCedacri = "NA";
		res.setCluster(new StringType(codClusterCedacri));

		DateType dtComp = pcp.getDataInizioValiditaPCP();
		if(!dtComp.isNull())
			res.setDatComp(new StringType(dtComp.getAA()+dtComp.getMM()+dtComp.getGG()));		
		
		DateType dtScad = pcp.getDataFineValiditaPCP();
		if(!dtScad.isNull())
			res.setDfinval(new IntegerType(dtScad.getAA()+dtScad.getMM()+dtScad.getGG()));
		
		res.setSeValid(new StringType("S"));
		res.setRelease(new IntegerType(99));
		return res;
	}
	
}
