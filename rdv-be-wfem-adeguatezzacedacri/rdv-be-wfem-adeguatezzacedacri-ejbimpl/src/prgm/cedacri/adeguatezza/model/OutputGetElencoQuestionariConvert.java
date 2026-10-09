package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class OutputGetElencoQuestionariConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		OutputGetElencoQuestionariBean bean = new OutputGetElencoQuestionariBean();
		OutputGetElencoQuestionariModel internalModel = (OutputGetElencoQuestionariModel)model;
		ElementoStoricoConvert convert = new ElementoStoricoConvert();
		ElementoStoricoBean[] storico = new ElementoStoricoBean[internalModel.getStorico().size()];
		for(int i=0;i<internalModel.getStorico().size();i++)
			storico[i] = (ElementoStoricoBean)convert.convertToBean(internalModel.getStorico().get(i));
		bean.setStorico(storico);
		bean.setDescErr(internalModel.getDescErr().getStringValue());
		bean.setEsito  (internalModel.getEsito().getStringValue());
		bean.setSeCompi(internalModel.getSeCompi().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		OutputGetElencoQuestionariBean internalBean = (OutputGetElencoQuestionariBean)bean;
		OutputGetElencoQuestionariModel model = new OutputGetElencoQuestionariModel();
		ElementoStoricoConvert convert = new ElementoStoricoConvert();
		ListType storico = new ListType();
		if (internalBean.getStorico() != null)
		{
			for(int i=0;i<internalBean.getStorico().length;i++)
				storico.add(convert.convertToModel(internalBean.getStorico()[i]));
		}
		model.setStorico(storico);
		model.setDescErr(new StringType(internalBean.getDescErr()));
		model.setEsito  (new StringType(internalBean.getEsito()));
		model.setSeCompi(new StringType(internalBean.getSeCompi()));
		return (CommandDataModel)model;
	}
}
