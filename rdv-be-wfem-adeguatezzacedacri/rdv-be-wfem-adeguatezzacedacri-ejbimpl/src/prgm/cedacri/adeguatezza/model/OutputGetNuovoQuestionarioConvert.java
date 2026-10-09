package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class OutputGetNuovoQuestionarioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		OutputGetNuovoQuestionarioBean bean = new OutputGetNuovoQuestionarioBean();
		OutputGetNuovoQuestionarioModel internalModel = (OutputGetNuovoQuestionarioModel)model;
		ElementoQuestionarioConvert convert = new ElementoQuestionarioConvert();
		ElementoQuestionarioBean[] questionario = new ElementoQuestionarioBean[internalModel.getQuestionario().size()];
		for(int i=0;i<internalModel.getQuestionario().size();i++)
			questionario[i] = (ElementoQuestionarioBean)convert.convertToBean(internalModel.getQuestionario().get(i));
		bean.setQuestionario(questionario);
		bean.setDescErr(internalModel.getDescErr().getStringValue());
		bean.setEsito  (internalModel.getEsito().getStringValue());
		bean.setSeCompi(internalModel.getSeCompi().getStringValue());
		bean.setRelease(internalModel.getRelease().intValue());
		bean.setDatComp(internalModel.getDatComp().getStringValue());
		bean.setOraComp(internalModel.getOraComp().getStringValue());
		bean.setProfilo(internalModel.getProfilo().getStringValue());
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

	public CommandDataModel convertToModel(Object bean) {
		OutputGetNuovoQuestionarioBean internalBean = (OutputGetNuovoQuestionarioBean)bean;
		OutputGetNuovoQuestionarioModel model = new OutputGetNuovoQuestionarioModel();
		ElementoQuestionarioConvert convert = new ElementoQuestionarioConvert();
		ListType questionario = new ListType();
		if (internalBean.getQuestionario() != null)
		{
			for(int i=0;i<internalBean.getQuestionario().length;i++)
				questionario.add(convert.convertToModel(internalBean.getQuestionario()[i]));
		}
		model.setQuestionario(questionario);
		model.setDescErr(new StringType(internalBean.getDescErr()));
		model.setEsito  (new StringType(internalBean.getEsito()));
		model.setSeCompi(new StringType(internalBean.getSeCompi()));
		model.setRelease(new IntegerType(internalBean.getRelease()));
		model.setDatComp(new StringType(internalBean.getDatComp()));
		model.setOraComp(new StringType(internalBean.getOraComp()));
		model.setProfilo(new StringType(internalBean.getProfilo()));
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
