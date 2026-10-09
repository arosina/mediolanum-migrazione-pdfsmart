package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class OutputCancellaQuestionarioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		OutputCancellaQuestionarioBean bean = new OutputCancellaQuestionarioBean();
		OutputCancellaQuestionarioModel internalModel = (OutputCancellaQuestionarioModel)model;
		bean.setDescErr(internalModel.getDescErr().getStringValue());
		bean.setEsito  (internalModel.getEsito().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		OutputCancellaQuestionarioBean internalBean = (OutputCancellaQuestionarioBean)bean;
		OutputCancellaQuestionarioModel model = new OutputCancellaQuestionarioModel();
		model.setDescErr(new StringType(internalBean.getDescErr()));
		model.setEsito  (new StringType(internalBean.getEsito()));
		return (CommandDataModel)model;
	}
}
