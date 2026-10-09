package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputSalvaQuestionarioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputSalvaQuestionarioBean bean = new InputSalvaQuestionarioBean();
		InputSalvaQuestionarioModel internalModel = (InputSalvaQuestionarioModel)model;
		ElementoQuestionarioRisposteConvert convert = new ElementoQuestionarioRisposteConvert();
		ElementoQuestionarioRisposteBean[] list = new ElementoQuestionarioRisposteBean[internalModel.getRisposte().size()];
		for(int i=0;i<internalModel.getRisposte().size();i++)
			list[i] = (ElementoQuestionarioRisposteBean)convert.convertToBean(internalModel.getRisposte().get(i));
		bean.setRisposte(list);
		bean.setNdgDoss (internalModel.getNdgDoss().getStringValue());
		bean.setNdgTemp (internalModel.getNdgTemp().getStringValue());
		bean.setCanVend (internalModel.getCanVend().getStringValue());
		bean.setEta     (internalModel.getEta().intValue());
		bean.setTitStud (internalModel.getTitStud().getStringValue());
		bean.setBozza   (internalModel.getBozza().getStringValue());
		bean.setProfilo (internalModel.getProfilo().getStringValue());
		bean.setDataOraComp(internalModel.getDataOraComp().getStringValue());
		bean.setCountry (internalModel.getCountry().getStringValue());
		bean.setUsername(internalModel.getUsername().getStringValue());
		bean.setNumSched(internalModel.getNumSched().getStringValue());
		bean.setCluster (internalModel.getCluster().getStringValue());
		bean.setPG      (internalModel.getPG().getStringValue());
/*v12*/ bean.setEsigLiq (internalModel.getEsigLiq().getStringValue());
/*v12*/ bean.setForzObT (internalModel.getForzObT().getStringValue());
/*v12*/ bean.setOrigObT (internalModel.getOrigObT().getStringValue());
/*v12*/ bean.setOrigClu (internalModel.getOrigClu().getStringValue());
		/* 20140829 aggiunta Disc */
		//bean.setObbtemp(internalModel.getObbtemp().getStringValue());
		//bean.setSitfina(internalModel.getSitfina().getStringValue());
		//bean.setObbinve(internalModel.getObbinve().getStringValue());
		//bean.setEspfina(internalModel.getEspfina().getStringValue());
		/* 20140829 aggiunta Disc */

		/* 20141022 aggiunta Disc */
		//Se i parametri sono nulli vengono settati a spazio 
		if (internalModel.getObbtemp().getStringValue() != null)
			bean.setObbtemp(internalModel.getObbtemp().getStringValue());
		else
			bean.setObbtemp(" ");
		
		if (internalModel.getSitfina().getStringValue() != null)
			bean.setObbtemp(internalModel.getObbtemp().getStringValue());
		else
			bean.setSitfina(" ");

		if (internalModel.getObbinve().getStringValue()!= null)
			bean.setObbinve(internalModel.getObbinve().getStringValue());
		else
			bean.setObbinve(" ");

		if (internalModel.getEspfina().getStringValue()!= null)
			bean.setEspfina(internalModel.getEspfina().getStringValue());
		else
			bean.setEspfina(" ");
		/* 20141022 aggiunta Disc */
		
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputSalvaQuestionarioBean internalBean = (InputSalvaQuestionarioBean)bean;
		InputSalvaQuestionarioModel model = new InputSalvaQuestionarioModel();
		ElementoQuestionarioRisposteConvert convert = new ElementoQuestionarioRisposteConvert();
		ListType list = new ListType();
		if (internalBean.getRisposte() != null)
		{
			for(int i=0;i<internalBean.getRisposte().length;i++)
				list.add(convert.convertToModel(internalBean.getRisposte()[i]));
		}
		model.setRisposte(list);
		model.setNdgDoss (new StringType(internalBean.getNdgDoss()));
		model.setNdgTemp (new StringType(internalBean.getNdgTemp()));
		model.setCanVend (new StringType(internalBean.getCanVend()));
		model.setEta     (new IntegerType(internalBean.getEta()));
		model.setTitStud (new StringType(internalBean.getTitStud()));
		model.setBozza   (new StringType(internalBean.getBozza()));
		model.setProfilo (new StringType(internalBean.getProfilo()));
		model.setDataOraComp(new TimestampType(internalBean.getDataOraComp()));
		model.setCountry (new StringType(internalBean.getCountry()));
		model.setUsername(new StringType(internalBean.getUsername()));
		model.setNumSched(new StringType(internalBean.getNumSched()));
		model.setCluster (new StringType(internalBean.getCluster()));
/*v12*/ model.setEsigLiq (new StringType(internalBean.getEsigLiq()));
/*v12*/ model.setForzObT (new StringType(internalBean.getForzObT()));
/*v12*/ model.setOrigObT (new StringType(internalBean.getOrigObT()));
/*v12*/ model.setOrigClu (new StringType(internalBean.getOrigClu()));
		/* 20140829 aggiunta Disc */		
		model.setObbtemp(new StringType(internalBean.getObbtemp()));
		model.setSitfina(new StringType(internalBean.getSitfina()));
		model.setObbinve(new StringType(internalBean.getObbinve()));
		model.setEspfina(new StringType(internalBean.getEspfina()));
		/* 20140829 aggiunta Disc */


		return (CommandDataModel)model;
	}
}
