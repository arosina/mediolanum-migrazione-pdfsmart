package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputCalcoloProfiloConvert implements ToBeanConversion,ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputCalcoloProfiloBean bean = new InputCalcoloProfiloBean();
		InputCalcoloProfiloModel internalModel = (InputCalcoloProfiloModel)model;
		ElementoQuestionarioRisposteConvert convert = new ElementoQuestionarioRisposteConvert();
		ElementoQuestionarioRisposteBean[] list = new ElementoQuestionarioRisposteBean[internalModel.getRisposte().size()];
		for(int i=0;i<internalModel.getRisposte().size();i++)
			list[i] = (ElementoQuestionarioRisposteBean)convert.convertToBean(internalModel.getRisposte().get(i));
		bean.setRisposte(list);
		bean.setNdgDoss (internalModel.getNdgDoss().getStringValue());
		bean.setNdgTemp (internalModel.getNdgTemp().getStringValue());
		bean.setCanVend (internalModel.getCanVend().getStringValue());
		bean.setEta     (internalModel.getEta().intValue());
		bean.setTitStud (internalModel.getTitStud().getStringValue());
		bean.setCountry (internalModel.getCountry().getStringValue());
		bean.setUsername(internalModel.getUsername().getStringValue());
		bean.setPG      (internalModel.getPG().getStringValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputCalcoloProfiloBean internalBean = (InputCalcoloProfiloBean)bean;
		InputCalcoloProfiloModel model = new InputCalcoloProfiloModel();
		ElementoQuestionarioRisposteConvert convert = new ElementoQuestionarioRisposteConvert();
		ListType list = new ListType();
		if (internalBean.getRisposte() != null)
		{
			for(int i=0;i<internalBean.getRisposte().length;i++)
				list.add(convert.convertToModel(internalBean.getRisposte()[i]));
		}
		model.setRisposte(list);
		model.setNdgDoss (new StringType(internalBean.getNdgDoss()));
		model.setNdgTemp (new StringType(internalBean.getNdgTemp()));
		model.setCanVend (new StringType(internalBean.getCanVend()));
		model.setEta     (new IntegerType(internalBean.getEta()));
		model.setTitStud (new StringType(internalBean.getTitStud()));
		model.setCountry (new StringType(internalBean.getCountry()));
		model.setUsername(new StringType(internalBean.getUsername()));
		model.setPG      (new StringType(internalBean.getPG()));
		return (CommandDataModel)model;
	}
}
