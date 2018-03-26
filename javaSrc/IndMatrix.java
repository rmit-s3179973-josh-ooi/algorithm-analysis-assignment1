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
    	iMatrix = new int[0][0];
    	vertList = new HashMap<T, Integer>();
    	edgeList = new HashMap<Edge<T>, Integer>();
    } // end of IndMatrix()
    
    
    public void addVertex(T vertLabel) 
    {	
    	vertList.put(vertLabel, (iMatrix.length));

    	expandArray();
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) 
    {
    	Edge<T> newEdge = new Edge<T>(srcLabel, tarLabel);
    	
    	edgeList.put(newEdge, (iMatrix[0].length));
    	
    	expandArray();
    	
    	iMatrix[vertList.get(srcLabel)][edgeList.get(newEdge)]=1;
    	iMatrix[vertList.get(tarLabel)][edgeList.get(newEdge)]=1;
    	
    	for(int i = 0; i < iMatrix.length; i++)
    	{
    		for(int j = 0; j < iMatrix[0].length; j++)
    		{
    			System.out.print(iMatrix[i][j]);
    		}
    		System.out.print('\n');
    	}
    } // end of addEdge()
    
    public void removeVertex(T vertLabel) 
    {
    	if(!vertList.containsKey(vertLabel))
    	{
    		return;
    	}
    	
    	Iterator<Edge<T>> iter = edgeList.keySet().iterator();
    	
    	while(iter.hasNext())
    	{
    		Edge<T> e = iter.next();
    		if(edgeExists(vertLabel, e.getTarVertex()))
    		{
    			iter.remove();
    			updateEdges();
    			continue;
    		}
    		else if(edgeExists(e.getSrcVertex(),vertLabel))
    		{
    			iter.remove();
    			updateEdges();
    			continue;
    		}
    	}
    	
    	
    	vertList.remove(vertLabel);
    	
    	updateVerticies();
    	
    	contractArray();
        // Implement me!
    } // end of removeVertex()
   
    
    //Remove an edge from both the hashmap and array
    public void removeEdge(T srcLabel, T tarLabel) 
    {
    	if(!edgeExists(srcLabel, tarLabel))
    	{
    		return;
    	}
    	
    	int newArray[][] = new int[vertList.size()][edgeList.size()-1];

    	Edge<T> edgeToRemove = null;
    	
    	for(int i = 0; i <newArray.length; i++)
    	{
    		int a = 0;
    		for(Edge<T> e: edgeList.keySet())
    		{
    			if(e.getSrcVertex().equals(srcLabel) && e.getTarVertex().equals(tarLabel) && edgeToRemove == null || e == edgeToRemove)
    			{
    				edgeToRemove = e;
    				continue;
    			}
    			
    			newArray[i][a] = iMatrix[i][edgeList.get(e)];
    			a++;
    		}  		
    	}
    	edgeList.remove(edgeToRemove);
    	
    	updateEdges();
		
    	iMatrix = newArray;  	
    } // end of removeEdges()
	
    
    public void updateEdges()
    {
    	int a = 0;
		for(Edge<T> e: edgeList.keySet())
		{
			edgeList.put(e, a);
			a++;
		}
    }
    
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
    	os.println(vertList.keySet());
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) 
    {
    	for(Edge<T> e: edgeList.keySet())
    	{
    		os.println(e.printEdge());
    	}
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) 
    {	
    	if(!vertList.containsKey(vertLabel1) || !vertList.containsKey(vertLabel2))
    	{
    		System.err.println("Invalid Vertecies");
    		return 0;
    	}
    	
        Queue<T> q = new LinkedList<T>();
        Map<T, Boolean> visited = new HashMap<T, Boolean>();
        List<Edge<T>> route = new ArrayList<Edge<T>>();
        
        T currentV = vertLabel1;
        
        q.add(currentV);
        
        visited.put(currentV, true);
    	
        while(!q.isEmpty())
        {
            currentV = q.remove();
            if (currentV.equals(vertLabel2))
            {
                break;
            }
            else
            {
                for(T nextVert : vertList.keySet())
                {
                	for(Edge<T> e: edgeList.keySet())
                	{
	                    if(!visited.containsKey(nextVert))
	                    {
	                    	visited.put(nextVert, true);
	                    	if(e.getSrcVertex().equals(currentV) && e.getTarVertex().equals(nextVert))
	                    	{
	                    		q.add(nextVert); 
	   		                    route.add(e);
	                    	}
		                 
	                    }
                	}
                }
            }
        }	
    	
        if (!currentV.equals(vertLabel2))
        {
        	 return disconnectedDist;
        }  	
        else
        {
        	return route.size();
        }
    } // end of shortestPathDistance()
    
    private void expandArray() 
    {
    	int newArray[][] = new int[vertList.size()][edgeList.size()];
    	System.out.println("SIZES:"+vertList.size() + " " + edgeList.size());
    	
    	for(int i = 0; i < iMatrix.length; i++)
    	{
    		for(int j = 0; j < iMatrix[0].length; j++)
    		{
    			newArray[i][j] = iMatrix[i][j];
    		}
    	}
    	
    	iMatrix = newArray;
    	System.out.println("SIZES:"+iMatrix.length + " " + iMatrix[0].length);
    	
    	newArray = null;
    }
    
    private void contractArray() 
    {
    	int newArray[][] = new int[vertList.size()][edgeList.size()];
    	System.out.println("SIZES:"+vertList.size() + " " + edgeList.size());
    	
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
    
    private boolean edgeExists(T srcLabel, T tarLabel)
    {
    	Edge<T> e = new Edge<T>(srcLabel, tarLabel);
    	for(Edge<T> i: edgeList.keySet())
    	{
    		if(i.getSrcVertex().equals(e.getSrcVertex()) && i.getTarVertex().equals(e.getTarVertex()))
    		{
    			return true;
    		}
    	}
    	return false;
    }
} // end of class IndMatrix

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
		return(new String(srcVertex + ""+tarVertex));
	}
}