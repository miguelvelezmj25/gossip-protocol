
/**
 * @author James Jensen
 * Funkadelic Division Multiplexing
 * ~~Free Your Mind, and Your Packets Will Follow~~
 * Indicates a file that we have and are able to send out if requested.
 */
public class PeerResource 
{
	private String 	description;
	private int 	length;
	private String 	mimeType;
	private ID 		resourceID;
	private byte[] 	fileData;
	
	/**
	 * Constructor. Calls other constructor.
	 * @param id The element's ID
	 * @param data A string containing the description, location, and mime type of the resource.
	 */
	public PeerResource (ID id, String data)
	{
		this(id,data,data.charAt(0));
	}
	
	/**
	 * Constructor. Splits the data by the delimiter. If data is split up incorrectly, throws an error.
	 * @param id The element's ID.
	 * @param data A string containing the description, location, and mime type of the resource.
	 * @param delimiter A character meant to differentiate between the different variables contained in data.
	 */
	public PeerResource(ID id, String data, char delimiter)
	{
		int loc;
		int loc2;
		this.resourceID = id;
		loc = data.indexOf(delimiter,1);
		loc2 = data.indexOf(delimiter, loc + 1);
		mimeType = data.substring(1, loc);
		length = Integer.parseInt(data.substring(loc + 1, loc2));
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
	public int getLength()
	{
		return this.length;
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
	 * Checks whether searchString matches anything involving this resource.
	 * @param searchString a string that says something about the resource.
	 * @return true if searchString is included in the description, mime type, resource ID, or location. False otherwise.
	 */
	public boolean matches(String searchString)
	{
		String otherString;
		otherString = searchString.toLowerCase().trim();
		return description.toLowerCase().contains(otherString) || mimeType.toLowerCase().contains(otherString) || resourceID.toString().toLowerCase().contains(otherString);
	}
	
	public byte[] getBytesForPart(int part)
	{
		int start;
		int end;
		start = part * (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - PartNumbers.getLengthInBytes());
		end = start + (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - PartNumbers.getLengthInBytes());
		
		return getBytes(start,end);
	}
	public byte[] getBytes(int start, int end)
	{
		byte[] result;
		result = new byte[Math.min(end - start, fileData.length - start)];
		System.arraycopy(fileData, start, result, 0, result.length);
		return result;
	}
	
}
