package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class ElementoPunteggiConvert implements ToBeanConversion,ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		ElementoPunteggiBean bean = new ElementoPunteggiBean();
		ElementoPunteggiModel internalModel = (ElementoPunteggiModel)model;
		bean.setDominio(internalModel.getDominio().getStringValue());
		bean.setValDomi(internalModel.getValDomi().intValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		ElementoPunteggiBean internalBean = (ElementoPunteggiBean)bean;
		ElementoPunteggiModel model = new ElementoPunteggiModel();
		model.setDominio(new StringType(internalBean.getDominio()));
		model.setValDomi(new IntegerType(internalBean.getValDomi()));
		return (CommandDataModel)model;
	}
}
