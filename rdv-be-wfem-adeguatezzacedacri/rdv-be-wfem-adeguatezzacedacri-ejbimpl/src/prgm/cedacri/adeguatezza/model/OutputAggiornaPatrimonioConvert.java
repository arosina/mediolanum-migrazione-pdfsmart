package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class OutputAggiornaPatrimonioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		OutputAggiornaPatrimonioBean bean = new OutputAggiornaPatrimonioBean();
		OutputAggiornaPatrimonioModel internalModel = (OutputAggiornaPatrimonioModel)model;
		bean.setDescErr(internalModel.getDescErr().getStringValue());
		bean.setEsito(internalModel.getEsito().getStringValue());
		bean.setNumElem(internalModel.getNumElem().intValue());
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		OutputAggiornaPatrimonioBean internalBean = (OutputAggiornaPatrimonioBean)bean;
		OutputAggiornaPatrimonioModel model = new OutputAggiornaPatrimonioModel();
		model.setDescErr(new StringType(internalBean.getDescErr()));
		model.setEsito(new StringType(internalBean.getEsito()));
		model.setNumElem(new IntegerType(internalBean.getNumElem()));
		return (CommandDataModel)model;
	}
}
