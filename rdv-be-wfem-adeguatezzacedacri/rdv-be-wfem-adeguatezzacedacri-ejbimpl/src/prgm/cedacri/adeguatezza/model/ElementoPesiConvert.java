package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class ElementoPesiConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		ElementoPesiBean bean = new ElementoPesiBean();
		ElementoPesiModel internalModel = (ElementoPesiModel)model;
		bean.setDominio(internalModel.getDominio().getStringValue());
		bean.setNumDoma(internalModel.getNumDoma().intValue());
		bean.setNumRisp(internalModel.getNumRisp().intValue());
		bean.setPesoRis(internalModel.getPesoRis().intValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		ElementoPesiBean internalBean = (ElementoPesiBean)bean;
		ElementoPesiModel model = new ElementoPesiModel();
		model.setDominio(new StringType(internalBean.getDominio()));
		model.setNumDoma(new IntegerType(internalBean.getNumDoma()));
		model.setNumRisp(new IntegerType(internalBean.getNumRisp()));
		model.setPesoRis(new IntegerType(internalBean.getPesoRis()));
		return (CommandDataModel)model;
	}
}
