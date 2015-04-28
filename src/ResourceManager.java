import java.io.*;
import java.util.*;


public class ResourceManager
{
	private HashMap<ID,Resource> resourceDirectory;
	
	private static ResourceManager resourcesManager;
	
	public ResourceManager()
	{
		this.resourceDirectory = new HashMap<ID,Resource>();
		this.resourcesManager = this;
	}
	
	public static ResourceManager getInstance()
	{
		ResourceManager result;
		result = resourcesManager;
		if(resourcesManager == null)
		{
			result = new ResourceManager();
		}
		return result;
	}
	
	public static ResourceManager newInstance()
	{
		return getInstance();
	}
	
	public synchronized void loadResourcesFrom(File file)
	{
		ID resourceID;
		Resource resource;
		BufferedReader bf;
		String line;
		
		try
		{
			bf = new BufferedReader(new FileReader(file));
			line = bf.readLine();
			while(line != null)
			{
				resourceID = new ID(new byte[16]);
				resource = new Resource(resourceID,line);
				resourceDirectory.put(resourceID, resource);
				
				
			}
		}
		catch(FileNotFoundException fnfe)
		{
			System.out.println(fnfe.getMessage());
		}
		catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
		
	}
	public synchronized Resource[] getResourcesThatMatch(String searchString)
	{
		Object[] values;
		int count;
		Resource[] result;
		
		
		values = resourceDirectory.values().toArray();
		count = 0;
		
		for(int i = 0; i < values.length; i = i + 1)
		{
			if(((Resource)values[i]).matches(searchString))
			{
				count = count + 1;
			}
		}
		
		result = new Resource[count];
		count = 0;
		for(int i = 0; i < values.length; i = i + 1)
		{
			if(((Resource)values[i]).matches(searchString))
			{
				result[count] = (Resource)values[i];
				count = count + 1;
			}
		}		
		
		
		return result;
	}
	
}
