
public class UIControllerTester
{
  /*
   * For testing whether UIController works.
   */
	public static void main(String[] args) 
	{
//		System.out.println(Thread.activeCount());
		UIController uiControl;
		// First port is the UI listening
		// Second port is the peer listening to the UI
		uiControl = new UIController(new PortNumber(12347), new PortNumber(12346), 512);
		uiControl.start();
		
//		System.out.println(Thread.activeCount());
		
	}

}
