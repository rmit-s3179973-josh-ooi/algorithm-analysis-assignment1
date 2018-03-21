import java.io.*;
import java.util.*;


/**
 * Incidence matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class IndMatrix <T extends Object> implements FriendshipGraph<T>
{
	private boolean edgesExist;
	private int[][] iMatrix;
	private Map <T, Integer> vertList;
	private Map <T, Integer> edgeList;
	
	/**
	 * Contructs empty graph.
	 */
    public IndMatrix() 
    {
    	iMatrix = new int[0][0];
    	vertList = new HashMap<T, Integer>();
    	edgeList = new HashMap<T, Integer>();
    } // end of IndMatrix()
    
    
    public void addVertex(T vertLabel) 
    {
    	vertList.put(vertLabel, (iMatrix.length+1));
    	expandArray();
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) 
    {
    	edgesExist = true;
    	
    	edgeList.put(srcLabel, (iMatrix[0].length+1));
    	expandArray();
    	iMatrix[vertList.get(srcLabel)][edgeList.get(tarLabel)]++;
        // Implement me!
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) 
    {
        ArrayList<T> neighbours = new ArrayList<T>();
        
        // Implement me!
        
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) 
    {
        // Implement me!
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) 
    {
        // Implement me!
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) 
    {
        // Implement me!
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) 
    {
        // Implement me!
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	// Implement me!
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
    private void expandArray() 
    {
    	if(!edgesExist)
    	{
    		return;
    	}
    	
    	int newArray[][] = new int[vertList.size()][edgeList.size()];
    	
    	for(int i = 0; i < vertList.size(); i++)
    	{
    		for(int j = 0; j < edgeList.size(); j++)
    		{
    			newArray[i][j] = iMatrix[i][j];
    		}
    	}
    	
    	iMatrix = newArray;
    	
    	newArray = null;
    }
    
    
} // end of class IndMatrix