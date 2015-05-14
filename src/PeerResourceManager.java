import java.util.*;


public class PeerResourceManager
{
	private HashMap<ID,PeerResource> resourceDirectory;
	
	private static PeerResourceManager resourcesManager;
	
	private PeerResourceManager()
	{
		this.resourceDirectory = new HashMap<ID,PeerResource>();
	}
	
	public static PeerResourceManager getInstance()
	{	
		if(resourcesManager == null)
		{
			resourcesManager = new PeerResourceManager();
		}
		return resourcesManager;
	}
	
	public static PeerResourceManager newInstance()
	{
		return getInstance();
	}
	
	public synchronized void addResource(PeerResource resource)
	{
		resourceDirectory.put(resource.getID(), resource);
	}
	
	public synchronized PeerResource getResourceFromID(ID ident)
	{
		return resourceDirectory.get(ident);
	}
}
