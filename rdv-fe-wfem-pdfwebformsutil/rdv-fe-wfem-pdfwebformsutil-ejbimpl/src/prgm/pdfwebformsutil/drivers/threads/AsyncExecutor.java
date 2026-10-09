package prgm.pdfwebformsutil.drivers.threads;

import java.util.concurrent.Executor;

public class AsyncExecutor implements Executor {    
    public void execute(final Runnable r) {
    	new Thread(r).start();      
    }	
}