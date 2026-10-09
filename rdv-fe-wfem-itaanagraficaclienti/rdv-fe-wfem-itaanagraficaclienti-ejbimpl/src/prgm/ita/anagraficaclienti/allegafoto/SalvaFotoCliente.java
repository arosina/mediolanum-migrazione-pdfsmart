package prgm.ita.anagraficaclienti.allegafoto;

import java.io.File;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SalvaFotoCliente extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			
			File input = popupClientiModel.getClienteSelezionato().getFotografia().getImmagine().getFile();
			ImageInputStream stream = ImageIO.createImageInputStream(input);
	        Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

	        if (readers.hasNext()) {
	            ImageReader reader = readers.next();
	            reader.setInput(stream);
	            
	            try {
		            IIOMetadata metadata = reader.getImageMetadata(0);
		            
		            IntegerType[] risoluzione = leggiRisoluzione(metadata);
		            
		            if (validaFotoCliente(reader.getWidth(0), reader.getHeight(0), risoluzione[0].intValue(), risoluzione[1].intValue()))  {
		            	AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
		    			ClienteModel cliente = facade.salvaFotografiaCliente(csc,popupClientiModel.getClienteSelezionato());
		    			popupClientiModel.setClienteSelezionato(cliente);
		    			setForwardDisplay(Integer.valueOf(0));
		    			popupClientiModel.addCommandMessage("Fotografia correttamente inviata in sede");
		            } else {
		            	setForwardDisplay(Integer.valueOf(0));
		            	popupClientiModel.addCommandError("Il formato della foto selezionata non rispetta i requisiti richiesti. ATTENZIONE verificare che i pixel che compongono la foto siano corretti. Per la modifica consultare la guida presente in alto a destra.");
		            }
	            } catch( IIOException ex) {
		        	setForwardDisplay(Integer.valueOf(0));
		        	popupClientiModel.addCommandError("Errore nel leggere il formato della foto selezionata. ATTENZIONE verificare che i pixel che compongono la foto siano corretti. Per la modifica consultare la guida presente in alto a destra.");
		        }
	        }
	        else {
	        	setForwardDisplay(Integer.valueOf(0));
	        	popupClientiModel.addCommandError("Errore nel leggere il formato della foto selezionata. ATTENZIONE verificare che i pixel che compongono la foto siano corretti. Per la modifica consultare la guida presente in alto a destra.");
	        }
	        
	        return popupClientiModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in SalvaFotoCliente: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}
	
	protected IntegerType[] leggiRisoluzione(IIOMetadata metadata) {
		
		int horizontalPixelSizeMM;
        int verticalPixelSizeMM;
    
        IIOMetadataNode standardTree = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
        IIOMetadataNode dimension = (IIOMetadataNode) standardTree.getElementsByTagName("Dimension").item(0);
        horizontalPixelSizeMM = getPixelSizeMM(dimension, "HorizontalPixelSize");
        verticalPixelSizeMM = getPixelSizeMM(dimension, "VerticalPixelSize");
                
        if (horizontalPixelSizeMM <0 || verticalPixelSizeMM <0) {
    	    Element tree = (Element)metadata.getAsTree("javax_imageio_jpeg_image_1.0"); 
            Element jfif = (Element)tree.getElementsByTagName("app0JFIF").item(0);
            String xDensity = jfif.getAttribute("Xdensity");
            String yDensity = jfif.getAttribute("Ydensity");
            horizontalPixelSizeMM = (int) (xDensity != null ? Math.ceil(Float.parseFloat(xDensity)) : -1);
            verticalPixelSizeMM = (int) (yDensity != null ? Math.ceil(Float.parseFloat(yDensity)) : -1);
         }
 
		return new IntegerType[] {new IntegerType(horizontalPixelSizeMM), new IntegerType(verticalPixelSizeMM)};
	}
	
	protected boolean validaFotoCliente(int width, int height, float hdpi, float vdpi) {
		
		return (width==224 && height==224 && hdpi>70 && vdpi>70);
		
	}
	
	
	private static int getPixelSizeMM(final IIOMetadataNode dimension, final String elementName) {
        double mm2inch = 25.4;
        NodeList pixelSizes = dimension.getElementsByTagName(elementName);
        IIOMetadataNode pixelSize = pixelSizes.getLength() > 0 ? (IIOMetadataNode) pixelSizes.item(0) : null;
        
        return (int) (pixelSize != null ? Math.ceil((mm2inch/Float.parseFloat(pixelSize.getAttribute("value")))) : -1);
    }

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupClientiModel.class;
	}

}
