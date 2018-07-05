import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
	
	private JTextField userText; //my message
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection; //connect two devices
	
	//constructor
	public Server() {
		super("Eugene Instant Messanger");
		userText = new JTextField();
		userText.setEditable(false); //by default, not allowed to type unless connect to somebody
		userText.addActionListener(
				new ActionListener(){
					//method called after hit "Enter"
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText(""); //reset after send message
						}
					}
				);
		
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300,150);
		setVisible(true);
	}
	
	//set up and run the server
	public void startRunning() {
		try {
			server = new ServerSocket(6789, 100); //port number, backlog(limited amount of connection)
			while(true) {
				try {
					//connect and start conversation
					waitForConnection();
					setupStreams();
					whileChatting(); //back and forth chatting
				}catch(EOFException eofException) {
					showMessage("\n Server ended the connection. ");
				}finally {
					closeChat();
				}
			}
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private void waitForConnection() throws IOException {
		showMessage(" Waiting to connect... \n");
		connection = server.accept();
		showMessage(" Now connected to " + connection.getInetAddress().getHostName()); 
	}
	
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are setup! \n");
	}
	
	//**** back and forth chatting ****
	private void whileChatting() throws IOException{
		String message = "You are connected! \n";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) input.readObject(); //make sure user sending string
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n User is not sending message in String");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	private void closeChat() {
		showMessage("\n Disabling connection... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER -  " + message); //write object to output stream
			output.flush();
			showMessage("\nSERVER - " + message);
		}catch(IOException ioException) {
			chatWindow.append("\n ERROR_101. Message can't be sent.\n");
		}
	}
	
	//**** update chatWindow ****
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatWindow.append(message);
				}
			}
		);//create a thread update GUI without creating new GUI with new update
	}
	
	//allow user to type message
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				}
			);
	}
}
