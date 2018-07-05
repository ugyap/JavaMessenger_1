import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		Client fabian = new Client("127.0.0.1"); //constructor need server ip
		fabian.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fabian.startRunning();
	}

}
