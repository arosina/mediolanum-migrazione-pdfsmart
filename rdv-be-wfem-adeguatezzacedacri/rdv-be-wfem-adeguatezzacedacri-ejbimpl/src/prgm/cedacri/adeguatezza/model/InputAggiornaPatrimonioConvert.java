package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputAggiornaPatrimonioConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputAggiornaPatrimonioBean bean = new InputAggiornaPatrimonioBean();
		InputAggiornaPatrimonioModel internalModel = (InputAggiornaPatrimonioModel)model;
		bean.setNdgDoss (internalModel.getNdgDoss().getStringValue());
		bean.setNdgTemp (internalModel.getNdgTemp().getStringValue());
		bean.setCanVend (internalModel.getCanVend().getStringValue());
		bean.setDataOraComp(internalModel.getDataOraComp().getStringValue());
		bean.setCountry (internalModel.getCountry().getStringValue());
		bean.setUsername(internalModel.getUsername().getStringValue());
		bean.setNumSched(internalModel.getNumSched().getStringValue());
		bean.setImporto1(internalModel.getImporto1().intValue());
		bean.setImporto2(internalModel.getImporto2().intValue());
		bean.setImporto3(internalModel.getImporto3().intValue());
		bean.setImporto4(internalModel.getImporto4().intValue());
		bean.setImporto5(internalModel.getImporto5().intValue());
		bean.setImporto6(internalModel.getImporto6().intValue());
		bean.setImporto7(internalModel.getImporto7().intValue());
		bean.setImporto8(internalModel.getImporto8().intValue());
		bean.setImporto9(internalModel.getImporto9().intValue());
		
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputAggiornaPatrimonioBean internalBean = (InputAggiornaPatrimonioBean)bean;
		InputAggiornaPatrimonioModel model = new InputAggiornaPatrimonioModel();
		model.setNdgDoss (new StringType(internalBean.getNdgDoss()));
		model.setNdgTemp (new StringType(internalBean.getNdgTemp()));
		model.setCanVend (new StringType(internalBean.getCanVend()));
		model.setDataOraComp(new TimestampType(internalBean.getDataOraComp()));
		model.setCountry (new StringType(internalBean.getCountry()));
		model.setUsername(new StringType(internalBean.getUsername()));
		model.setNumSched(new StringType(internalBean.getNumSched()));
		model.setImporto1(new IntegerType(internalBean.getImporto1()));
		model.setImporto2(new IntegerType(internalBean.getImporto2()));
		model.setImporto3(new IntegerType(internalBean.getImporto3()));
		model.setImporto4(new IntegerType(internalBean.getImporto4()));
		model.setImporto5(new IntegerType(internalBean.getImporto5()));
		model.setImporto6(new IntegerType(internalBean.getImporto6()));
		model.setImporto7(new IntegerType(internalBean.getImporto7()));
		model.setImporto8(new IntegerType(internalBean.getImporto8()));
		model.setImporto9(new IntegerType(internalBean.getImporto9()));

		return (CommandDataModel)model;
	}
}
