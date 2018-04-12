import java.io.*;
import java.util.*;
import java.util.Map.Entry;



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
    	if(!verIndecies.containsKey(vertLabel)) {
    		verIndecies.put(vertLabel, verIndecies.size());
    		expandArray();
    	}
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) 
    {
    	
    	Integer srcIndex = verIndecies.get(srcLabel);
    	Integer tarIndex = verIndecies.get(tarLabel);
    	
    	if(srcIndex != null && tarIndex != null)
    	{
        	this.aMatrix[srcIndex][tarIndex]= 1;
        	this.aMatrix[tarIndex][srcIndex]= 1;
    	}
    	
    	
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) 
    {
        ArrayList<T> neighbours = new ArrayList<T>();
        
        int index = this.verIndecies.get(vertLabel);
        
        for(int i = 0; i < this.aMatrix.length; i++)
        {
        	if(this.aMatrix[index][i] == 1)
        	{
        		neighbours.add(getVertexByValue(i));
        	}
        }
        
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) 
    {
    	if(this.verIndecies.containsKey(vertLabel))
    	{
    		int removevalue = this.verIndecies.get(vertLabel);
        	
        	this.verIndecies.remove(vertLabel);
        	
        	for(Map.Entry<T, Integer> entry : this.verIndecies.entrySet()) {
        	
        		T key = entry.getKey();
        	    int value = entry.getValue();
        	    
        	    if(value > removevalue)
        	    {
        	    	this.verIndecies.replace(key, value-1);
        	    }
        	}
        	
        	removeVertexFromMatrix(removevalue);
    	}
    	
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) 
    {
    	Integer srcIndex = verIndecies.get(srcLabel);
    	Integer tarIndex = verIndecies.get(tarLabel);
    	if(srcIndex != null && tarIndex != null)
    	{
    		aMatrix[srcIndex][tarIndex] = 0;
        	aMatrix[tarIndex][srcIndex] = 0;
    	}
    	

    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) 
    {
        // Implement me!
    		String output = "";
        for(T vertex : this.verIndecies.keySet())
        {
        		output +=vertex.toString()+" "; 
        }
        
        os.println(output);
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) 
    {
    		String holder = "";
    		
    		for(int i = 0; i < this.aMatrix.length; i++) 
    		{
    			
    			for(int j = 0; j < this.aMatrix.length; j++)
    			{
    				
    				if(this.aMatrix[i][j] == 1)
    				{
    					holder += getVertexByValue(i).toString() + " " + getVertexByValue(j).toString() + "\n";
    				}
    			}
    		}
    		os.println(holder);
        // Implement me!
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	// Implement me!
    	Map<T,T> path = new HashMap<T,T>();
    	LinkedList<T> queue = new LinkedList<T>();
    	List<T> shortestPath;
    	T vertex, currentV;
    	
    	queue.add(vertLabel1);
    	
    	while(!queue.isEmpty())
    	{
    		currentV = queue.poll();
    		
    		if(currentV.equals(vertLabel2))
            {
            	 shortestPath = new ArrayList<>();
            	 vertex = vertLabel2;
            	 while(vertex != null) 
            	 {
            	    shortestPath.add(vertex);
            	    vertex = path.get(vertex);
            	 }
            	 //The starting vertex is not counted
            	 shortestPath.remove(vertLabel1);
            	 return shortestPath.size();
            }
    		
    		for(T neighbour : neighbours(currentV))
    		{
    			if(path.containsKey(neighbour) || path.containsValue(neighbour))
            	{
            		continue;
            	}
            	path.put(neighbour, currentV);
            	queue.add(neighbour);
    		}
    		
    	}
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
    private void expandArray() 
    {
	    	int max = verIndecies.size();
	    	int newArray[][] = new int[max][max];
	
	    	for(int i = 0; i < this.aMatrix.length; i++)
	    	{
	    		for(int j = 0; j <= i; j++)
	    		{
	    			newArray[i][j] = this.aMatrix[i][j];
	    			newArray[j][i] = this.aMatrix[j][i];
	    		}
	    	}
	    	for(int k = 0; k < max;k++)
	    	{
	    		newArray[max-1][k] = 0;
	    		newArray[k][max-1] = 0;
	    	}
	    	
	    	this.aMatrix = newArray;
	    	
	    	newArray = null;
    }
    
    private void removeVertexFromMatrix(int value)
    {
    	int size = this.verIndecies.size();
    	int[][] newArray = new int[size][size];
    	int m = 0;
    	int n = 0;
    	
    	for(int i = 0; i < this.aMatrix.length; i++)
    	{
    		n = 0;
    		if(i != value && m <= size)
    		{
    			for(int j = 0; j < this.aMatrix.length; j++)
        		{
        			
        			if(j != value && n <= size)
        			{
        				newArray[m][n] = this.aMatrix[i][j];
        				n++;
        			}
        			
        		}
    			
    			m++;
    		}
    	}
    	
    	this.aMatrix = newArray;
    	
    	newArray = null;
    }
    
    private T getVertexByValue(int value)
    {
    		
    		for(Entry<T, Integer> entry : this.verIndecies.entrySet()) {
    		    T key = entry.getKey();
    		    int currentvalue = entry.getValue();

    		    if(currentvalue == value)
    		    {
    		    	return key;
    		    }
    		}
    		
    		return null;
    }
} // end of class AdjMatrix