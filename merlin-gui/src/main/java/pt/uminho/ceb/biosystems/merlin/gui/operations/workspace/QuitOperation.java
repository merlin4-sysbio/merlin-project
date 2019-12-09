/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.operations.workspace;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;


/**
 */
@Operation()
public class QuitOperation {	
	
	@Port(name="projects",direction=Direction.INPUT,order=1)
	public void quit() throws IOException{
		
		System.out.println("op");
	}
	
}
