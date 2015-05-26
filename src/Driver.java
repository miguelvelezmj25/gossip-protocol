
public class Driver
{

	public static void main(String[] args) 
	{
		UIController uiControl;
		// First port is the UI listening
		// Second port is the peer listening to the UI
		uiControl = new UIController(new PortNumberUIPeer(54321), new PortNumberPeerUI(12346), 512);
		uiControl.start();
		
	}

}
