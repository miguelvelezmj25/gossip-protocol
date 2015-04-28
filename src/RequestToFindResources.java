import java.util.ArrayList;


public class RequestToFindResources extends Request {
	
	private ArrayList<ID> responses;
	
	public RequestToFindResources(ID id) {
		super(id);
		
		this.responses = new ArrayList<ID>();
	}

	@Override
	public void updateRequest(UDPMessage udpMessage) {
	// TODO	
	}

}
