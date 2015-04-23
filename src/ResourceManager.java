import java.io.File;
import java.util.HashMap;


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
		return resourcesManager;
	}
	
	public static ResourceManager newInstance()
	{
		return getInstance();
	}
	
	public synchronized void loadResourcesFrom(File file)
	{
		String description;
		String location;
		String mimeType;
		ID resourceID;
		String data;
		
		location = file.getAbsolutePath();
		
		
		
		
	}
	public synchronized Resource[] getResourcesThatMatch(String searchString)
	{
		return null;
	}
	
}
