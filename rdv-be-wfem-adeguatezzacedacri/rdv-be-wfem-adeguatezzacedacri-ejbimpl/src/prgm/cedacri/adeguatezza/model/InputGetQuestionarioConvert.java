package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputGetQuestionarioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputGetQuestionarioBean bean = new InputGetQuestionarioBean();
		InputGetQuestionarioModel internalModel = (InputGetQuestionarioModel)model;
		bean.setNdgDoss (internalModel.getNdgDoss().getStringValue());
		bean.setNdgTemp (internalModel.getNdgTemp().getStringValue());
		bean.setCanVend (internalModel.getCanVend().getStringValue());
		bean.setUserKey (internalModel.getUserKey().getStringValue());
		bean.setCountry (internalModel.getCountry().getStringValue());
		bean.setUsername(internalModel.getUsername().getStringValue());
		bean.setPG      (internalModel.getPG().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputGetQuestionarioBean internalBean = (InputGetQuestionarioBean)bean;
		InputGetQuestionarioModel model = new InputGetQuestionarioModel();
		model.setNdgDoss (new StringType(internalBean.getNdgDoss()));
		model.setNdgTemp (new StringType(internalBean.getNdgTemp()));
		model.setCanVend (new StringType(internalBean.getCanVend()));
		model.setUserKey (new StringType(internalBean.getUserKey()));
		model.setCountry (new StringType(internalBean.getCountry()));
		model.setUsername(new StringType(internalBean.getUsername()));
		model.setPG      (new StringType(internalBean.getPG()));
		return (CommandDataModel)model;
	}
}
