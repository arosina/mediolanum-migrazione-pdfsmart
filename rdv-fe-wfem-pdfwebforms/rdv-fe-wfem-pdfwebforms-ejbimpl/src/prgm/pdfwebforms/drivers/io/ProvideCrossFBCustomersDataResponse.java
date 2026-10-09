package prgm.pdfwebforms.drivers.io;

import java.util.ArrayList;
import java.util.List;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideCrossFBCustomersDataResponse{
	
	List<String> crossFBCustomerIndexes = new ArrayList<>();

	public List<String> getCrossFBCustomerIndexes() {
		return crossFBCustomerIndexes;
	}

	public void setCrossFBCustomerIndexes(List<String> crossFBCustomerIndexes) {
		this.crossFBCustomerIndexes = crossFBCustomerIndexes;
	}


}
