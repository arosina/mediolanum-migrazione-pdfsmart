package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class OutputCalcoloProfiloConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) 
	{
		OutputCalcoloProfiloBean bean = new OutputCalcoloProfiloBean();
		OutputCalcoloProfiloModel internalModel = (OutputCalcoloProfiloModel)model;
		ElementoQuestionarioRisposteConvert risposteConvert = new ElementoQuestionarioRisposteConvert();
		ElementoQuestionarioRisposteBean[] risposte = new ElementoQuestionarioRisposteBean[internalModel.getRisposte().size()];
		for(int i=0;i<internalModel.getRisposte().size();i++)
			risposte[i] = (ElementoQuestionarioRisposteBean)risposteConvert.convertToBean(internalModel.getRisposte().get(i));
		bean.setRisposte(risposte);
		ElementoPunteggiConvert punteggiConvert = new ElementoPunteggiConvert();
		ElementoPunteggiBean[] punteggi = new ElementoPunteggiBean[internalModel.getPunteggi().size()];
		for(int i=0;i<internalModel.getPunteggi().size();i++)
			punteggi[i] = (ElementoPunteggiBean)punteggiConvert.convertToBean(internalModel.getPunteggi().get(i));
		bean.setPunteggi(punteggi);
		bean.setDescErr(internalModel.getDescErr().getStringValue());
		bean.setEsito  (internalModel.getEsito().getStringValue());
		bean.setRifiuto(internalModel.getRifiuto().getStringValue());
		bean.setSeCompi(internalModel.getSeCompi().getStringValue());
		bean.setSeInCon(internalModel.getSeInCon().getStringValue());
		bean.setProfilo(internalModel.getProfilo().getStringValue());
		bean.setNumElem(internalModel.getNumElem().intValue());
		bean.setCluster(internalModel.getCluster().getStringValue());
		bean.setDesCluster(internalModel.getDesCluster().getStringValue());
/*v12*/ bean.setEsigLiq(internalModel.getEsigLiq().getStringValue());
/*v12*/ bean.setForzObT(internalModel.getForzObT().getStringValue());
/*v12*/ bean.setOrigObT(internalModel.getOrigObT().getStringValue());
/*v12*/ bean.setOrigClu(internalModel.getOrigClu().getStringValue());
/*v12*/ bean.setDesOrigClu(internalModel.getDesOrigClu().getStringValue());
		bean.setDfinval(internalModel.getDfinval().intValue());
		/* 20140829 aggiunta Disc */
		bean.setObbtemp(internalModel.getObbtemp().getStringValue());
		bean.setSitfina(internalModel.getSitfina().getStringValue());
		bean.setObbinve(internalModel.getObbinve().getStringValue());
		bean.setEspfina(internalModel.getEspfina().getStringValue());
		/* 20140829 aggiunta Disc */
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) 
	{
		OutputCalcoloProfiloBean internalBean = (OutputCalcoloProfiloBean)bean;
		OutputCalcoloProfiloModel model = new OutputCalcoloProfiloModel();
		ElementoQuestionarioRisposteConvert risposteConvert = new ElementoQuestionarioRisposteConvert();
		ListType risposte = new ListType();
		if (internalBean.getRisposte() != null)
		{
			for(int i=0;i<internalBean.getRisposte().length;i++)
				risposte.add(risposteConvert.convertToModel(internalBean.getRisposte()[i]));
		}
		model.setRisposte(risposte);
		ElementoPunteggiConvert punteggiConvert = new ElementoPunteggiConvert();
		ListType punteggi = new ListType();
		if (internalBean.getPunteggi() != null)
		{
			for(int i=0;i<internalBean.getPunteggi().length;i++)
				punteggi.add(punteggiConvert.convertToModel(internalBean.getPunteggi()[i]));
		}
		model.setPunteggi(punteggi);
		model.setDescErr(new StringType(internalBean.getDescErr()));
		model.setEsito  (new StringType(internalBean.getEsito()));
		model.setRifiuto(new StringType(internalBean.getRifiuto()));
		model.setSeCompi(new StringType(internalBean.getSeCompi()));
		model.setSeInCon(new StringType(internalBean.getSeInCon()));
		model.setProfilo(new StringType(internalBean.getProfilo()));
		model.setNumElem(new IntegerType(internalBean.getNumElem()));
		model.setCluster(new StringType(internalBean.getCluster()));
		model.setDesCluster(new StringType(internalBean.getDesCluster()));
/*v12*/ model.setEsigLiq(new StringType(internalBean.getEsigLiq()));
/*v12*/ model.setForzObT(new StringType(internalBean.getForzObT()));
/*v12*/ model.setOrigObT(new StringType(internalBean.getOrigObT()));
/*v12*/ model.setOrigClu(new StringType(internalBean.getOrigClu()));
/*v12*/ model.setDesOrigClu(new StringType(internalBean.getDesOrigClu()));
		model.setDfinval(new IntegerType(internalBean.getDfinval()));
		/* 20140829 aggiunta Disc */
		model.setObbtemp(new StringType(internalBean.getObbtemp()));
		model.setSitfina(new StringType(internalBean.getSitfina()));
		model.setObbinve(new StringType(internalBean.getObbinve()));
		model.setEspfina(new StringType(internalBean.getEspfina()));
		/* 20140829 aggiunta Disc */
		return (CommandDataModel)model;
	}
}
