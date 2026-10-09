package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class ElementoQuestionarioRisposteConvert implements ToBeanConversion,ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		ElementoQuestionarioRisposteBean bean = new ElementoQuestionarioRisposteBean();
		ElementoQuestionarioRisposteModel internalModel = (ElementoQuestionarioRisposteModel)model;
		bean.setNumElem(internalModel.getNumElem().intValue());
		bean.setNumSele(internalModel.getNumSele().intValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		ElementoQuestionarioRisposteBean internalBean = (ElementoQuestionarioRisposteBean)bean;
		ElementoQuestionarioRisposteModel model = new ElementoQuestionarioRisposteModel();
		model.setNumElem(new IntegerType(internalBean.getNumElem()));
		model.setNumSele(new IntegerType(internalBean.getNumSele()));
		return (CommandDataModel)model;
	}
}
