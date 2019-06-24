package Visualisation;

public class Simulation_Update_Thread implements Runnable{

	public void run() {
		while(true){
		Main.Main.simulation.update_text();
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
