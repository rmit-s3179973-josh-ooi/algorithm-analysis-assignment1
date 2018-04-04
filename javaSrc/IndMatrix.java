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
	private int[][] iMatrix;
	private Map <T, Integer> vertList;
	private Map <Edge<T>, Integer> edgeList;
	
	/**
	 * Contructs empty graph.
	 */
    public IndMatrix() 
    {
    	//2D Array stores INcidence Matrix values
    	iMatrix = new int[0][0];
    	
    	//Hashmaps store Vertices and Edges and associate with indexes for the iMatrix
    	vertList = new HashMap<T, Integer>();
    	edgeList = new HashMap<Edge<T>, Integer>();
    } // end of IndMatrix()
    
    
    public void addVertex(T vertLabel) 
    {	
    	for(T v: vertList.keySet())
    	{
    		if(v.equals(vertLabel))
    		{
    			System.err.println("Vertex already exists: " + vertLabel);
    			return;
    		}
    	}
    	
    	vertList.put(vertLabel, (iMatrix.length));
    	expandArray();
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) 
    {
    	if(!vertList.containsKey(srcLabel) || !vertList.containsKey(tarLabel))
    	{
    		System.err.println("Invalid vertex pair");
			return;
    	}
    	
    	if(edgeExists(srcLabel, tarLabel))
    	{
    		System.err.println("Edge already exists");
			return;
    	}
    	
    	Edge<T> newEdge = new Edge<T>(srcLabel, tarLabel);
    	
    	edgeList.put(newEdge, (iMatrix[0].length));
    	expandArray();
    	
    	iMatrix[vertList.get(srcLabel)][edgeList.get(newEdge)]=1;
    	iMatrix[vertList.get(tarLabel)][edgeList.get(newEdge)]=1;
    } // end of addEdge()
    
    public void removeVertex(T vertLabel) 
    {
    	if(!vertList.containsKey(vertLabel))
    	{
    		System.err.println("Invalid vertex");
    		return;
    	}
    	
    	for(T v:vertList.keySet())
    	{
    		if(edgeExists(vertLabel, v))
    		{
    			removeEdge(vertLabel, v); 
    		}
    	}
    	
    	vertList.remove(vertLabel);
    	
    	//The iMatrix must be updated once the vertex and its edges are removed
    	updateVerticies();	
    	contractArray();
    	
    } // end of removeVertex()
   
    
    //Remove an edge from both the hashmap and array
    public void removeEdge(T srcLabel, T tarLabel) 
    {
    	if(!edgeExists(srcLabel, tarLabel))
    	{
    		System.err.println("Invalid vertex pair");
    		return;
    	}
 
    	Iterator<Edge<T>> iter = edgeList.keySet().iterator();
    	
    	while(iter.hasNext())
    	{
    		Edge<T> e = iter.next();
    		if(e.getSrcVertex().equals(srcLabel) && e.getTarVertex().equals(tarLabel)
    				||e.getSrcVertex().equals(tarLabel) && e.getTarVertex().equals(srcLabel))
    		{
    			iter.remove();
    			updateEdges();
    			continue;
    		}
    	}
    	
    	updateEdges();
    	contractArray(); 	
    } // end of removeEdges()
	
    //Rebuilds edgeList to ensure iMatrix index integrity
    public void updateEdges()
    {
    	int a = 0;
		for(Edge<T> e: edgeList.keySet())
		{
			edgeList.put(e, a);
			a++;
		}
    }
    
  //Rebuilds vertList to ensure iMatrix index integrity
    public void updateVerticies()
    {
    	int a = 0;
		for(T t: vertList.keySet())
		{
			vertList.put(t, a);
			a++;
		}
    }
    
    public ArrayList<T> neighbours(T vertLabel) 
    {
    	ArrayList<T> neighbours = new ArrayList<T>();
    	
    	for(T checkVer : vertList.keySet())
    	{
	    	for(int i = 0; i<iMatrix[0].length; i++)
	    	{
	    		if(checkVer.toString().equals(vertLabel.toString()))
	    		{
	    			break;
	    		}
	    		if((iMatrix[vertList.get(vertLabel)][i])> 0 && (iMatrix[vertList.get(vertLabel)][i]) == (iMatrix[vertList.get(checkVer)][i]))
	    		{
	    			neighbours.add(checkVer);
	    			break;
	    		}
	    	}
	    	
    	}
        
        return neighbours;
    } // end of neighbours()
    
    public void printVertices(PrintWriter os) 
    {
    	for(T vertex: vertList.keySet())
    	{
    		os.println(vertex + " ");
    	}
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) 
    {
    	for(Edge<T> e: edgeList.keySet())
    	{
    		os.println(e.printEdge());
    	}
    	for(Edge<T> e: edgeList.keySet())
    	{
    		os.println(e.printEdgeReverse());
    	}
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) 
    {	
    	if(!vertList.containsKey(vertLabel1) || !vertList.containsKey(vertLabel2))
    	{
    		System.err.println("Invalid Vertecies");
    		return 0;
    	}
    	int count = 0;
    	
        Queue<T> q = new LinkedList<T>();
        boolean[] visited = new boolean[vertList.size()];
        Edge<T> edge;
        T currentV;
        
        q.add(vertLabel1);

        //Builds up a queue of vertices from target to source based on edge connections
        while(!q.isEmpty())
        {
            currentV = q.poll();
            visited[vertList.get(currentV)] = true;
            count++;
            for(int e: this.edgeList.values())
            {
            	//Checks for edges stemming from current vertex and gets their tarVertices
	            if (iMatrix[vertList.get(currentV)][e]==1)
	            {
	            	edge = getKey(e);
	            	if(edge.getSrcVertex().equals(currentV))
	            	{
	            		if(edge.getTarVertex().equals(vertLabel2))
	            		{
	            			return count;
	            		}
	            		else 
	            		{
	            			if(visited[vertList.get(edge.getTarVertex())] != true)
	            			{
	            				q.add(edge.getTarVertex());
	            			}
	            		}
	            	}
	            	else if(edge.getTarVertex().equals(currentV))
	            	{
	            		if(edge.getSrcVertex().equals(vertLabel2))
	            		{
	            			return count;
	            		}
	            		else 
	            		{
	            			if(visited[vertList.get(edge.getSrcVertex())] != true)
	            			{
	            				q.add(edge.getSrcVertex());
	            			}
	            		}
	            	}
	                
	            }
	            
	         
            }
        }
        return disconnectedDist;
    } // end of shortestPathDistance()
   
    //Method to return hashmap key from value
    private Edge<T> getKey(int value)
    {
    	for(Edge<T> e: edgeList.keySet())
    	{
    		if(edgeList.get(e) == value)
    			return e;
    	}
    	return null;
    }
    
    //Expand iMatrix after adding edge or vertex
    private void expandArray() 
    {
    	int newArray[][] = new int[vertList.size()][edgeList.size()];
    	
    	for(int i = 0; i < iMatrix.length; i++)
    	{
    		for(int j = 0; j < iMatrix[0].length; j++)
    		{
    			newArray[i][j] = iMatrix[i][j];
    		}
    	}
    	
    	iMatrix = newArray;
    	
    	newArray = null;
    }
    
    //shrink array after removing edge or vertex
    private void contractArray() 
    {
    	int newArray[][] = new int[vertList.size()][edgeList.size()];
    	
    	for(int i = 0; i < newArray.length; i++)
    	{
    		for(int j = 0; j < newArray[0].length; j++)
    		{
    			newArray[i][j] = iMatrix[i][j];
    		}
    	}
    	
    	iMatrix = newArray;
    	
    	newArray = null;
    }
    
    //Check if edge exists in iMatrix
    private boolean edgeExists(T srcLabel, T tarLabel)
    {
    	Edge<T> e = new Edge<T>(srcLabel, tarLabel);
    	for(Edge<T> i: edgeList.keySet())
    	{
    		if(i.getSrcVertex().equals(e.getSrcVertex()) && i.getTarVertex().equals(e.getTarVertex()) 
    				|| (i.getTarVertex().equals(e.getSrcVertex()) && i.getSrcVertex().equals(e.getTarVertex())))
    		{
    			return true;
    		}
    	}
    	return false;
    }
} // end of class IndMatrix

//Helper class to contain a vertex pair(edge) in an object
class Edge <T extends Object> 
{
	private T tarVertex;
	private T srcVertex;
	
	public Edge(T src, T tar)
	{
		this.srcVertex = src;
		this.tarVertex = tar;
	}

	public T getTarVertex() 
	{
		return tarVertex;
	}

	public T getSrcVertex() 
	{
		return srcVertex;
	}
	
	public String printEdge()
	{
		return(new String(srcVertex + " " + tarVertex));
	}
	
	public String printEdgeReverse()
	{
		return(new String(tarVertex + " " + srcVertex));
	}
}