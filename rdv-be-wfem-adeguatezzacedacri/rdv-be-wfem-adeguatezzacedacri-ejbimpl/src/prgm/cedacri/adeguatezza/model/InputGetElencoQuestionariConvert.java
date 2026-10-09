package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputGetElencoQuestionariConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputGetElencoQuestionariBean bean = new InputGetElencoQuestionariBean();
		InputGetElencoQuestionariModel internalModel = (InputGetElencoQuestionariModel)model;
		bean.setNdgDoss (internalModel.getNdgDoss().getStringValue());
		bean.setNdgTemp (internalModel.getNdgTemp().getStringValue());
		bean.setCanVend (internalModel.getCanVend().getStringValue());
		bean.setCountry (internalModel.getCountry().getStringValue());
		bean.setUsername(internalModel.getUsername().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputGetElencoQuestionariBean internalBean = (InputGetElencoQuestionariBean)bean;
		InputGetElencoQuestionariModel model = new InputGetElencoQuestionariModel();
		model.setNdgDoss (new StringType(internalBean.getNdgDoss()));
		model.setNdgTemp (new StringType(internalBean.getNdgTemp()));
		model.setCanVend (new StringType(internalBean.getCanVend()));
		model.setCountry (new StringType(internalBean.getCountry()));
		model.setUsername(new StringType(internalBean.getUsername()));
		return (CommandDataModel)model;
	}
}
