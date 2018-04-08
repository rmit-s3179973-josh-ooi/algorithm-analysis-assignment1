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
    	if(vertList.containsKey(vertLabel));
    	
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
    	if(!vertList.containsKey(vertLabel))
    	{
    		System.err.println("Invalid Vertex");
    		return null;
    	}
    	
    	ArrayList<T> neighbours = new ArrayList<T>();
    	
    	for(T checkVer : vertList.keySet())
    	{
	    	for(int i = 0; i<iMatrix[0].length; i++)
	    	{
	    		if(checkVer.equals(vertLabel))
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
    
    //Breadth-first based search method for finding shortest path
    public int shortestPathDistance(T vertLabel1, T vertLabel2) 
    {	
    	if(!vertList.containsKey(vertLabel1) || !vertList.containsKey(vertLabel2))
    	{
    		System.err.println("Invalid Vertecies");
    		return 0;
    	}
    	 
    	Map<T,T> path = new HashMap<T,T>();
        Queue<T> q = new LinkedList<T>();
        T currentV;
        
        q.add(vertLabel1);

        while(!q.isEmpty())
        {
            currentV = q.poll();
            
            //This block extracts the shortest path from the hashmap and returns its length
            if(currentV.equals(vertLabel2))
            {
            	 List<T> shortestPath = new ArrayList<>();
            	 T vertex = vertLabel2;
            	 while(vertex != null) 
            	 {
            	    shortestPath.add(vertex);
            	    vertex = path.get(vertex);
            	 }
            	 
            	 //The starting vertex is not counted
            	 shortestPath.remove(vertLabel1);
            	 return shortestPath.size();
            }
            
            /*Vertices and their neighbours are added to the hashmap until the level containing the target is reached;
            	it therefore will contain the edges comprising the shortest path*/
            for(T neighbour: neighbours(currentV))
            {
            	if(path.containsKey(neighbour) || path.containsValue(neighbour))
            	{
            		continue;
            	}
            	path.put(neighbour, currentV);
            	q.add(neighbour);
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