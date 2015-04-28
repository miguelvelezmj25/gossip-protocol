import java.io.File;


/**
 * @author James Jensen
 * Funkadelic Division Multiplexing
 * ~~Free Your Mind, and Your Packets Will Follow~~
 * Indicates a file that we have and are able to send out if requested.
 */
public class Resource 
{
	private String description;
	private File location;
	private String mimeType;
	private ID resourceID;
	
	/**
	 * Constructor. Calls other constructor.
	 * @param id The element's ID
	 * @param data A string containing the description, location, and mime type of the resource.
	 */
	public Resource (ID id, String data)
	{
		this(id,data,data.charAt(0));
	}
	
	/**
	 * Constructor. Splits the data by the delimiter. If data is split up incorrectly, throws an error.
	 * @param id The element's ID.
	 * @param data A string containing the description, location, and mime type of the resource.
	 * @param delimiter A character meant to differentiate between the different variables contained in data.
	 */
	public Resource(ID id, String data, char delimiter)
	{
		int loc;
		int loc2;
		this.resourceID = id;
		loc = data.indexOf(delimiter,1);
		loc2 = data.indexOf(delimiter, loc + 1);
		mimeType = data.substring(1, loc);
		location = new File(data.substring(loc + 1, loc2));
		description = data.substring(loc2 + 1);
		
	}
	
	/**
	 * Returns the resource's description.
	 * @return the resource's description.
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns the file this resource represents.
	 * @return the file this resource represents.
	 */
	public File getLocation()
	{
		return this.location;
	}
	
	/**
	 * Returns the resource's mime type.
	 * @return the resource's mime type.
	 */
	public String getMimeType()
	{
		return this.mimeType;
	}
	
	/**
	 * Returns the resource's ID.
	 * @return the resource's ID.
	 */
	public ID getID()
	{
		return this.resourceID;
	}
	
	/**
	 * Returns the size of the file the resource represents.
	 * @return the size of the file the resource represents.
	 */
	public long getSizeInBytes()
	{
		return getLocation().length();
	}
	
	/**
	 * Checks whether searchString matches anything involving this resource.
	 * @param searchString a string that says something about the resource.
	 * @return true if searchString is included in the description, mime type, resource ID, or location. False otherwise.
	 */
	public boolean matches(String searchString)
	{
		String otherString;
		otherString = searchString.toLowerCase();
		return description.contains(otherString) || mimeType.contains(otherString) || resourceID.toString().contains(otherString) || location.getAbsolutePath().contains(otherString);
	}
	
	
}