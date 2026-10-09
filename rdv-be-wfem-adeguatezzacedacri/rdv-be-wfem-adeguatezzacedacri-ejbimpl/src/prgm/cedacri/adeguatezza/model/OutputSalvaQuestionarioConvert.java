package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class OutputSalvaQuestionarioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		OutputSalvaQuestionarioBean bean = new OutputSalvaQuestionarioBean();
		OutputSalvaQuestionarioModel internalModel = (OutputSalvaQuestionarioModel)model;
		bean.setDescErr(internalModel.getDescErr().getStringValue());
		bean.setEsito(internalModel.getEsito().getStringValue());
		bean.setNumElem(internalModel.getNumElem().intValue());
		bean.setDfinval(internalModel.getDfinval().intValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		OutputSalvaQuestionarioBean internalBean = (OutputSalvaQuestionarioBean)bean;
		OutputSalvaQuestionarioModel model = new OutputSalvaQuestionarioModel();
		model.setDescErr(new StringType(internalBean.getDescErr()));
		model.setEsito(new StringType(internalBean.getEsito()));
		model.setNumElem(new IntegerType(internalBean.getNumElem()));
		model.setDfinval(new IntegerType(internalBean.getDfinval()));
		return (CommandDataModel)model;
	}
}
