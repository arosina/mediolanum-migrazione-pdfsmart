package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
import prgm.cedacri.adeguatezza.base.*;

public class InputCtrlAdeguatezzaConvert implements ToBeanConversion, ToModelConversion 
{
	public Object convertToBean(CommandDataModel model) {
		InputCtrlAdeguatezzaBean bean = new InputCtrlAdeguatezzaBean();
		InputCtrlAdeguatezzaModel internalModel = (InputCtrlAdeguatezzaModel)model;
		bean.setNdgDoss1  (internalModel.getNdgDoss1().getStringValue());
		bean.setNdgDoss2  (internalModel.getNdgDoss2().getStringValue());
		bean.setNdgDoss3  (internalModel.getNdgDoss3().getStringValue());
		bean.setNdgDoss4  (internalModel.getNdgDoss4().getStringValue());
		bean.setNdgTemp1  (internalModel.getNdgTemp1().getStringValue());
		bean.setNdgTemp2  (internalModel.getNdgTemp2().getStringValue());
		bean.setNdgTemp3  (internalModel.getNdgTemp3().getStringValue());
		bean.setNdgTemp4  (internalModel.getNdgTemp4().getStringValue());
		bean.setFilDoss   (internalModel.getFilDoss().getStringValue());
		bean.setContDoss  (internalModel.getContDoss().getStringValue());
		bean.setProgDoss  (internalModel.getProgDoss().getStringValue());
		bean.setCanVend   (internalModel.getCanVend().getStringValue());
		bean.setCountry   (internalModel.getCountry().getStringValue());
		bean.setUsername  (internalModel.getUsername().getStringValue());
		
		bean.setDataRif   (internalModel.getDataRif().getStringValue());
		bean.setSegnOrd   (internalModel.getSegnOrd().getStringValue());
		bean.setNumOrdg   (internalModel.getNumOrdg().intValue());
		bean.setTipStruStr(internalModel.getTipStruStr().getStringValue()); 
		bean.setSotTipoStr(internalModel.getSotTipoStr().getStringValue()); 
		bean.setObbInveStr(internalModel.getObbInveStr().getStringValue()); 
		bean.setRiscStrStr(internalModel.getRiscStrStr().getStringValue()); 
		bean.setObbTempStr(internalModel.getObbTempStr().getStringValue()); 
		bean.setModVersStr(internalModel.getModVersStr().getStringValue()); 
		bean.setCompAziStr(internalModel.getCompAziStr().getStringValue()); 
		bean.setCodMercStr(internalModel.getCodMercStr().getStringValue()); 
		bean.setObbInveCli(internalModel.getObbInveCli().getStringValue()); 
		bean.setObbTempCli(internalModel.getObbTempCli().getStringValue()); 
		bean.setModVersCli(internalModel.getModVersCli().getStringValue()); 
		bean.setSitFinaCli(internalModel.getSitFinaCli().getStringValue()); 
		bean.setCtvlord   (internalModel.getCtvlord().intValue()); 
		bean.setDivisOt   (internalModel.getDivisOt().getStringValue());
		bean.setPosDisiCli(internalModel.getPosDisiCli().getStringValue());
		bean.setAttCons(internalModel.getAttCons().getStringValue());
		bean.setTitCons(internalModel.getTitCons().getStringValue());		
		
		return bean;
	}

	public CommandDataModel convertToModel(Object bean) {
		InputCtrlAdeguatezzaBean internalBean = (InputCtrlAdeguatezzaBean)bean;
		InputCtrlAdeguatezzaModel model = new InputCtrlAdeguatezzaModel();
		model.setNdgDoss1 (new StringType(internalBean.getNdgDoss1()));
		model.setNdgDoss2 (new StringType(internalBean.getNdgDoss2()));
		model.setNdgDoss3 (new StringType(internalBean.getNdgDoss3()));
		model.setNdgDoss4 (new StringType(internalBean.getNdgDoss4()));
		model.setNdgTemp1 (new StringType(internalBean.getNdgTemp1()));
		model.setNdgTemp2 (new StringType(internalBean.getNdgTemp2()));
		model.setNdgTemp3 (new StringType(internalBean.getNdgTemp3()));
		model.setNdgTemp4 (new StringType(internalBean.getNdgTemp4()));
		model.setFilDoss  (new StringType(internalBean.getFilDoss()));
		model.setContDoss (new StringType(internalBean.getContDoss()));
		model.setProgDoss (new StringType(internalBean.getProgDoss()));
		model.setCanVend  (new StringType(internalBean.getCanVend()));
		model.setCountry  (new StringType(internalBean.getCountry()));
		model.setUsername (new StringType(internalBean.getUsername()));

		model.setDataRif   (new DateType(internalBean.getDataRif()));
		model.setSegnOrd   (new StringType(internalBean.getSegnOrd()));
		model.setNumOrdg   (new IntegerType(internalBean.getNumOrdg()));
		model.setTipStruStr(new StringType(internalBean.getTipStruStr())); 
		model.setSotTipoStr(new StringType(internalBean.getSotTipoStr())); 
		model.setObbInveStr(new StringType(internalBean.getObbInveStr())); 
		model.setRiscStrStr(new StringType(internalBean.getRiscStrStr())); 
		model.setObbTempStr(new StringType(internalBean.getObbTempStr())); 
		model.setModVersStr(new StringType(internalBean.getModVersStr())); 
		model.setCompAziStr(new StringType(internalBean.getCompAziStr())); 
		model.setCodMercStr(new StringType(internalBean.getCodMercStr())); 
		model.setObbInveCli(new StringType(internalBean.getObbInveCli())); 
		model.setObbTempCli(new StringType(internalBean.getObbTempCli())); 
		model.setModVersCli(new StringType(internalBean.getModVersCli())); 
		model.setSitFinaCli(new StringType(internalBean.getSitFinaCli())); 
		model.setCtvlord   (new IntegerType(internalBean.getCtvlord())); 
		model.setDivisOt   (new StringType(internalBean.getDivisOt()));
		model.setPosDisiCli(new StringType(internalBean.getPosDisiCli()));
		model.setAttCons(new StringType(internalBean.getAttCons()));
		model.setTitCons(new StringType(internalBean.getTitCons()));
		
		return (CommandDataModel)model;
	}
}
