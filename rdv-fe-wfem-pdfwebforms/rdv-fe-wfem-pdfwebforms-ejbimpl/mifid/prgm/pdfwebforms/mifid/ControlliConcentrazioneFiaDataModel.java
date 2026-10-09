package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.drivers.io.mifid.ElementoControlliConcentrazioneFiaModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlliConcentrazioneFiaDataModel extends CommandDataModel {
	
    private ListType elementiControlloConcentrazioneFia = new ListType(ElementoControlliConcentrazioneFiaModel.class);

    public ListType getElementiControlloConcentrazioneFia() {
		return elementiControlloConcentrazioneFia;
	}

	public void setElementiControlloConcentrazioneFia(ListType elementiControlloConcentrazioneFia) {
		this.elementiControlloConcentrazioneFia = elementiControlloConcentrazioneFia;
	}
    
}
