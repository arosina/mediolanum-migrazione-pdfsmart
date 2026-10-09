package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class ElementoQuestionarioConvert implements ToModelConversion,ToBeanConversion 
{
	public Object convertToBean(CommandDataModel model) {
		ElementoQuestionarioBean bean = new ElementoQuestionarioBean();
		ElementoQuestionarioModel internalModel = (ElementoQuestionarioModel)model;
		bean.setNumElem(internalModel.getNumElem().intValue());
		bean.setNumSele(internalModel.getNumSele().intValue());
		bean.setSelSele(internalModel.getSelSele().getStringValue());
		bean.setTestEle(internalModel.getTestEle().getStringValue());
		bean.setTipElem(internalModel.getTipElem().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		ElementoQuestionarioBean internalBean = (ElementoQuestionarioBean)bean;
		ElementoQuestionarioModel model = new ElementoQuestionarioModel();
		model.setNumElem(new IntegerType(internalBean.getNumElem()));
		model.setNumSele(new IntegerType(internalBean.getNumSele()));
		model.setSelSele(new StringType(internalBean.getSelSele()));
		model.setTestEle(new StringType(internalBean.getTestEle()));
		model.setTipElem(new StringType(internalBean.getTipElem()));
		return (CommandDataModel)model;
	}
}
