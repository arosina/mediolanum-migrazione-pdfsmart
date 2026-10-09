package prgm.pdfwebformsutil.drivers.threads;

import java.util.concurrent.Executor;

public class SyncExecutor implements Executor {
	  public void execute(Runnable r) {
	      r.run();
	  }
}