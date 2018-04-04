package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class DataGen 
{
	private static int maxVertex;
	public static void main(String args[])
	{
		//update later
		maxVertex = 4038;
		 BufferedWriter writer = null;
	        try 
	        {
	            File genFile = new File("testCommands.txt");

	            writer = new BufferedWriter(new FileWriter(genFile));
	            if(args[0].equals("addition"))
	            {
	            	writer.write(buildTest("AV", "AE", false));
	            	System.out.println("A testing file for graph addition operations has been created");
	            }
	            else if(args[0].equals("connection"))
	            {
	            	writer.write(buildTest("N", "S", true));
	            	System.out.println("A testing file for graph connection operations has been created");
	            }
	            else if(args[0].equals("removal"))
	            {
	            	writer.write(buildTest("RV", "RE", true));
	            	System.out.println("A testing file for graph removal operations has been created");
	            }
	        } 
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        } 
	        finally 
	        {
	        	try 
	        	{
					writer.close();
				} 
	        	catch (IOException e)
	        	{
					e.printStackTrace();
				}
	        }
	        
	        System.exit(1);
	}
	
	public static String buildTest(String commandA, String commandB, boolean notAddingVerts)
	{
		StringBuilder testBuilder = new StringBuilder();
		int length = ThreadLocalRandom.current().nextInt(50, 100 + 1);
		
		for(int i = 0; i<length; i++)
		{
			int coinToss = ThreadLocalRandom.current().nextInt(1, 2 + 1);
			if(coinToss == 2)
			{
				testBuilder.append(commandA+" "+getVertex(!notAddingVerts));
				testBuilder.append(System.getProperty("line.separator"));
			}
			else
			{
				testBuilder.append(commandB +" "+getVertex(true) + " "+getVertex(true));
				testBuilder.append(System.getProperty("line.separator"));
			}
		}
		
		testBuilder.append("Q");
		return testBuilder.toString();
	}
	
	//The parameter indicates whether  an already existing vertex is desired
	public static int getVertex(boolean wantExtant)
	{
		if(wantExtant)
		{
			return ThreadLocalRandom.current().nextInt(0, maxVertex+1);
		}
		else
		{
			return ThreadLocalRandom.current().nextInt(maxVertex, 9999);
		}
	}
}

