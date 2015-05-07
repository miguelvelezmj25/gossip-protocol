
public class UIControllerTester
{
  /*
   * For testing whether UIController works.
   */
	public static void main(String[] args) 
	{
		System.out.println(Thread.activeCount());
		UIController uiControl;
		uiControl = new UIController(new PortNumber(33333), new PortNumber(22223), 512);
		uiControl.start();
		
		System.out.println(Thread.activeCount());
		
	}

}
