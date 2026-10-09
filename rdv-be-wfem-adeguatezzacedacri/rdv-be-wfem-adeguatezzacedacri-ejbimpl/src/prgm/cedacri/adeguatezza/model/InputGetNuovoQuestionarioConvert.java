package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputGetNuovoQuestionarioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputGetNuovoQuestionarioBean bean = new InputGetNuovoQuestionarioBean();
		InputGetNuovoQuestionarioModel internalModel = (InputGetNuovoQuestionarioModel)model;
		bean.setNdgDoss (internalModel.getNdgDoss().getStringValue());
		bean.setNdgTemp (internalModel.getNdgTemp().getStringValue());
		bean.setCanVend (internalModel.getCanVend().getStringValue());
		bean.setCountry (internalModel.getCountry().getStringValue());
		bean.setUsername(internalModel.getUsername().getStringValue());
		bean.setPG      (internalModel.getPG().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputGetNuovoQuestionarioBean internalBean = (InputGetNuovoQuestionarioBean)bean;
		InputGetNuovoQuestionarioModel model = new InputGetNuovoQuestionarioModel();
		model.setNdgDoss (new StringType(internalBean.getNdgDoss()));
		model.setNdgTemp (new StringType(internalBean.getNdgTemp()));
		model.setCanVend (new StringType(internalBean.getCanVend()));
		model.setCountry (new StringType(internalBean.getCountry()));
		model.setUsername(new StringType(internalBean.getUsername()));
		model.setPG      (new StringType(internalBean.getPG()));
		return (CommandDataModel)model;
	}
}
