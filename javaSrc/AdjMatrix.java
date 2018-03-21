import java.io.*;
import java.util.*;


/**
 * Adjacency matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class AdjMatrix <T extends Object> implements FriendshipGraph<T>
{

	private int[][] aMatrix;
	private Map <T, Integer> verIndecies;
	
	
	/**
	 * Contructs empty graph.
	 */
    public AdjMatrix() 
    {
    	aMatrix = new int[0][0];
    	verIndecies = new HashMap<T, Integer>();
    	
    } // end of AdjMatrix()
    
    
    public void addVertex(T vertLabel) 
    {
    	verIndecies.put(vertLabel, (aMatrix.length+1));
    	expandArray();
    	
        // Implement me!
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) 
    {
    	aMatrix[verIndecies.get(srcLabel)][verIndecies.get(tarLabel)]++;
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
    	verIndecies.remove(vertLabel);
        // Implement me!
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) 
    {
    	aMatrix[verIndecies.get(srcLabel)][verIndecies.get(tarLabel)]--;
        // Implement me!
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) 
    {
        // Implement me!
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) 
    {
    	for(int i = 0; i < verIndecies.size(); i++)
    	{
    		for(int j = 0; j < verIndecies.size(); j++)
    		{
    			os.print(" " + aMatrix[i][j]);
    		}
    		os.print('\n');
    	}
        // Implement me!
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	// Implement me!
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
    private void expandArray() 
    {
    	int newArray[][] = new int[verIndecies.size()][verIndecies.size()];
    	
    	for(int i = 0; i < verIndecies.size(); i++)
    	{
    		for(int j = 0; j < verIndecies.size(); j++)
    		{
    			newArray[i][j] = aMatrix[i][j];
    		}
    	}
    	
    	aMatrix = newArray;
    	
    	newArray = null;
    }
} // end of class AdjMatrix