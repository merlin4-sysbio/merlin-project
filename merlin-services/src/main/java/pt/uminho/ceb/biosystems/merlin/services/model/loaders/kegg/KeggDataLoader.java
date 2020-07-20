package pt.uminho.ceb.biosystems.merlin.services.model.loaders.kegg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceData;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceInitialData;
import pt.uminho.ceb.biosystems.merlin.services.WorkspaceInitialDataServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.loaders.LoadMetabolicData;

public class KeggDataLoader {
	public static AtomicBoolean cancel = new AtomicBoolean(false);

	
	/**
	 * @param connection
	 * @return
	 */
	public static boolean loadData(String databaseName, WorkspaceData workspaceMetabolicData, AtomicBoolean cancel) {

		System.out.println("load...");
		
		try  {

			boolean error = false;
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			int numberOfProcesses =  Runtime.getRuntime().availableProcessors();
			List<Thread> threads = new ArrayList<Thread>();

			List<Runnable> runnables = new ArrayList<Runnable>();

			WorkspaceInitialData databaseInitialData = null;

			if(workspaceMetabolicData.getResultGenes()!=null && workspaceMetabolicData.getResultGenes().size()>0)
				databaseInitialData = WorkspaceInitialDataServices.retrieveAnnotationData(databaseName);
			else
				databaseInitialData = WorkspaceInitialDataServices.retrieveAllData(databaseName);

			int id = loadDefaultCompartmentsAndGetInternal(databaseName);
			
			databaseInitialData.setCompartmentIdentifier(id);

			AtomicInteger dataSize = new AtomicInteger(0);
			if(workspaceMetabolicData.getResultMetabolites()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getResultMetabolites().size());
			if(workspaceMetabolicData.getResultEnzymes()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getResultEnzymes().size());
			if(workspaceMetabolicData.getResultReactions()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getResultReactions().size());
			if(workspaceMetabolicData.getResultGenes()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getResultGenes().size());
			if(workspaceMetabolicData.getResultModules()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getResultModules().size());
			if(workspaceMetabolicData.getKeggPathwaysHierarchy()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getKeggPathwaysHierarchy().size());
			if(workspaceMetabolicData.getOrthologueEntities()!=null)
				dataSize = new AtomicInteger( dataSize.get() + workspaceMetabolicData.getOrthologueEntities().size());

			long startTime = System.currentTimeMillis();

			//loading prepared statements
			
			System.out.println("Number of processes: " + numberOfProcesses);

			for(int i=0; i<numberOfProcesses; i++) {

				Runnable loadKeggData = new LoadMetabolicData(databaseName, databaseInitialData, workspaceMetabolicData, cancel, dataSize);
				runnables.add(loadKeggData);
				Thread thread = new Thread(loadKeggData);
				threads.add(thread);
				thread.start();

				if(((LoadMetabolicData) loadKeggData).isError())					
					error = true;
			}

			for(Thread thread :threads)				
				thread.join();

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			numberOfProcesses =  Runtime.getRuntime().availableProcessors();///2;
			threads = new ArrayList<Thread>();


			//loading normal statments (remove after setting all loaders to prepared statements

//			for(int i=0; i<numberOfProcesses; i++) {
//
//				Runnable loadMetabolicData = new LoadMetabolicData(databaseName, databaseInitialData, workspaceMetabolicData, cancel, dataSize);
//				runnables.add(loadMetabolicData);
//				Thread thread = new Thread(loadMetabolicData);
//				threads.add(thread);
//				thread.start();
//
//				if(((LoadMetabolicData) loadMetabolicData).isError())					
//					error = true;
//			}
//
//			for(Thread thread :threads)				
//				thread.join();

			long endTime2 = System.currentTimeMillis();

			long startTime1 = System.currentTimeMillis();

			long endTime1 = System.currentTimeMillis();

			long endTime = System.currentTimeMillis();

			System.out.println("Total elapsed time in execution of method Load_kegg is :"+ String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(endTime2-startTime),TimeUnit.MILLISECONDS.toSeconds(endTime2-startTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime2-startTime))));

			System.out.println("Total elapsed time in execution of method build view is :"+ String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(endTime1-startTime1),TimeUnit.MILLISECONDS.toSeconds(endTime1-startTime1) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime1-startTime1))));

			System.out.println("Total elapsed time in execution of method TOTAL is :"+ String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(endTime-startTime),TimeUnit.MILLISECONDS.toSeconds(endTime-startTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime-startTime))));

			if(error) 
				return false;
			else
				return true;
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}
	
	public static int loadDefaultCompartmentsAndGetInternal(String databaseName) throws Exception {
		
		int id = ModelCompartmentServices.getCompartmentID(databaseName, "inside");

		if(id < 0)
			id = ModelCompartmentServices.insertNameAndAbbreviation(databaseName, "inside", "in");
		
		int id2 = ModelCompartmentServices.getCompartmentID(databaseName, "outside");
		
		if(id < 0)
			ModelCompartmentServices.insertNameAndAbbreviation(databaseName, "outside", "out");
		
		return id;
	}
	
	
	public void setCancel() {
		cancel.set(true);
	}
	
	public AtomicBoolean isCancel() {
		return cancel;
	}
	
	
}
