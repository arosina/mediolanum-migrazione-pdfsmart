package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

import prgm.cedacri.adeguatezza.base.*;

public class ElementoStoricoConvert implements ToModelConversion,ToBeanConversion 
{
	public Object convertToBean(CommandDataModel model) {
		ElementoStoricoBean bean = new ElementoStoricoBean();
		ElementoStoricoModel internalModel = (ElementoStoricoModel)model;
		bean.setProfilo(internalModel.getProfilo().getStringValue());
		bean.setSeBanca(internalModel.getSeBanca().getStringValue());
		bean.setDatComp(internalModel.getDatComp().getStringValue());
		bean.setOraComp(internalModel.getOraComp().getStringValue());
		bean.setCanVend(internalModel.getCanVend().getStringValue());
		bean.setFiliale(internalModel.getFiliale().getStringValue());
		bean.setUserKey(internalModel.getUserKey().getStringValue());
		bean.setRelease(internalModel.getRelease().intValue());
		bean.setCluster(internalModel.getCluster().getStringValue()); 
//20111002:seValid
		bean.setSeValid(internalModel.getSeValid().getStringValue());
 		bean.setDfinval(internalModel.getDfinval().intValue());
 		/* 20140829 aggiunta Disc */
		bean.setObbtemp(internalModel.getObbtemp().getStringValue());
		bean.setSitfina(internalModel.getSitfina().getStringValue());
		bean.setObbinve(internalModel.getObbinve().getStringValue());
		bean.setEspfina(internalModel.getEspfina().getStringValue());
		/* 20140829 aggiunta Disc */
		bean.setDesCluster(new String("   "));
		
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER1"))
 		{
 			bean.setDesCluster(new String("Intraprendente Lungo"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER2"))
 		{
 			bean.setDesCluster(new String("Intraprendente Medio"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER3"))
 		{
 			bean.setDesCluster(new String("Intraprendente Breve"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER4"))
 		{
 			bean.setDesCluster(new String("Equilibrato Lungo"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER5"))
 		{
 			bean.setDesCluster(new String("Equilibrato Medio"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER6"))
 		{
 			bean.setDesCluster(new String("Equilibrato Breve"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER7"))
 		{
 			bean.setDesCluster(new String("Conservatore Lungo"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER8"))
 		{
 			bean.setDesCluster(new String("Conservatore Medio"));
 		}
 		if (internalModel.getCluster().equalsIgnoreCase("CLUSTER9"))
 		{
 			bean.setDesCluster(new String("Conservatore Breve"));
 		}

		
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		ElementoStoricoBean internalBean = (ElementoStoricoBean)bean;
		ElementoStoricoModel model = new ElementoStoricoModel();
		model.setProfilo(new StringType(internalBean.getProfilo()));
		model.setSeBanca(new StringType(internalBean.getSeBanca()));
		model.setDatComp(new StringType(internalBean.getDatComp()));
		model.setOraComp(new StringType(internalBean.getOraComp()));
		model.setCanVend(new StringType(internalBean.getCanVend()));
		model.setFiliale(new StringType(internalBean.getFiliale()));
		model.setUserKey(new StringType(internalBean.getUserKey()));
		model.setRelease(new IntegerType(internalBean.getRelease()));
		model.setCluster(new StringType(internalBean.getCluster()));
		model.setDfinval(new IntegerType(internalBean.getDfinval())); 
//20111002:seValid
		model.setSeValid(new StringType(internalBean.getSeValid()));
		model.setDesCluster(new StringType(internalBean.getDesCluster()));
		/* 20140829 aggiunta Disc */
		model.setObbtemp(new StringType(internalBean.getObbtemp()));
		model.setSitfina(new StringType(internalBean.getSitfina()));
		model.setObbinve(new StringType(internalBean.getObbinve()));
		model.setEspfina(new StringType(internalBean.getEspfina()));
		/* 20140829 aggiunta Disc */
		
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER1"))
 		{
 			model.setDesCluster(new StringType("Intraprendente Lungo"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER2"))
 		{
 			model.setDesCluster(new StringType("Intraprendente Medio"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER3"))
 		{
 			model.setDesCluster(new StringType("Intraprendente Breve"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER4"))
 		{
 			model.setDesCluster(new StringType("Equilibrato Lungo"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER5"))
 		{
 			model.setDesCluster(new StringType("Equilibrato Medio"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER6"))
 		{
 			model.setDesCluster(new StringType("Equilibrato Breve"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER7"))
 		{
 			model.setDesCluster(new StringType("Conservatore Lungo"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER8"))
 		{
 			model.setDesCluster(new StringType("Conservatore Medio"));
 		}
 		if (internalBean.getCluster().equalsIgnoreCase("CLUSTER9"))
 		{
 			model.setDesCluster(new StringType("Conservatore Breve"));
 		}

		
		return (CommandDataModel)model;
	}
}
