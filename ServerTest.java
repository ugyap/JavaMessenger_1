import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args) {
		Server eugene = new Server();
		eugene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		eugene.startRunning();
	}

}
