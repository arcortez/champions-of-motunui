import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client implements Runnable{
	private boolean connected;
	private static JPanel screenDeck = new JPanel(new CardLayout());
	private static JFrame frame;
	static Thread t;
	static DatagramSocket socket;
	private static Socket serverSocket;

	public Client(String serverIP, int port, String name){
		try{
			socket = new DatagramSocket();		
			serverSocket = new Socket(serverIP, port);

			System.out.println("Just connected to " + serverSocket.getRemoteSocketAddress());
		}catch(SocketException e){
			e.printStackTrace();
			System.exit(1);
		}catch(UnknownHostException e){
			e.printStackTrace();
			System.exit(1);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}

		t = new Thread(this);
		t.start();
		frame = new JFrame("Champions of Motunui : "+name);
		Container c = frame.getContentPane();
		frame.setPreferredSize(new Dimension(900,700));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JPanel tutorialScreen = new JPanel();
		JPanel gameScreen = new JPanel();

		tutorialScreen.setLayout(new BorderLayout());
		JPanel southTutorial = new JPanel();
		JButton skipTutorial = new JButton("Skip Tutorial");
		JButton prev = new JButton("< Previous");
		skipTutorial.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CardLayout p = (CardLayout)screenDeck.getLayout();
				p.show(screenDeck, "GAME");
			}
		});

		JButton next = new JButton("Next >");
		southTutorial.setLayout(new BorderLayout());
		southTutorial.add(prev, BorderLayout.WEST);
		southTutorial.add(next, BorderLayout.EAST);
		southTutorial.add(skipTutorial, BorderLayout.CENTER);

		tutorialScreen.add(new JLabel("insert tutorial here"), BorderLayout.CENTER);
		tutorialScreen.add(southTutorial, BorderLayout.SOUTH);
		screenDeck.add(tutorialScreen, "TUTORIAL");
		screenDeck.add(gameScreen, "GAME");
		
		gameScreen.setLayout(new BorderLayout());
		

		JPanel infoBox = new JPanel();
		infoBox.setLayout(new BorderLayout());
		JPanel chatBox = new JPanel();
		chatBox.setLayout(new BorderLayout());
		JTextArea textarea = new JTextArea("WELCOME TO THE CHAT ROOM!\n");
		textarea.setEditable(false);
		
		JScrollPane pane = new JScrollPane(textarea);
		pane.setPreferredSize(new Dimension(450, 100));
		chatBox.add(pane, BorderLayout.NORTH);

		JPanel messageBox = new JPanel();
		messageBox.setLayout(new BorderLayout());
		JLabel l1 = new JLabel("You:");
		JTextField message = new JTextField();
		JButton sendButton = new JButton("Send!");
		
		messageBox.add(l1, BorderLayout.WEST);
		message.setPreferredSize(new Dimension(350,20));
		messageBox.add(message, BorderLayout.CENTER);
		messageBox.add(sendButton, BorderLayout.EAST);

		chatBox.add(messageBox, BorderLayout.EAST);
		infoBox.add(chatBox, BorderLayout.WEST);

		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());
		JPanel lifePanel = new JPanel();
		lifePanel.add(new JLabel("Lives left:"), BorderLayout.WEST);
		JTextField lives = new JTextField("3");
		lives.setEditable(false);
		lifePanel.add(lives, BorderLayout.EAST);
		info.add(lifePanel, BorderLayout.NORTH);

		JTextArea leaderboard = new JTextArea("LEADERBOARD:");
		leaderboard.setPreferredSize(new Dimension(445, 100));
		leaderboard.setEditable(false);
		info.add(leaderboard, BorderLayout.SOUTH);

		infoBox.add(info, BorderLayout.EAST);


		JPanel gameProper = new JPanel();
		JLabel keystrokes = new JLabel("press a key.");

		keystrokes.requestFocus();
		keystrokes.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent ke) {}
			public void keyTyped(KeyEvent ke) {
				if(ke.getChar() == "q"){
					keystrokes.setText("left");
				}else if(ke.getChar() == "p"){
					keystrokes.setText("right");
				}else if(ke.getChar() == " "){
					keystrokes.setText("FIRE");
				}
			}
			public void keyReleased(KeyEvent ke) {}
		});

		gameScreen.add(infoBox, BorderLayout.SOUTH);

		c.add(screenDeck);

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				textarea.setText(textarea.getText()+"\n"+name+": "+message.getText());
				message.setText("");
			}
		});


		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

	}

	public static void main(String[] args){
		try{
			String serverIP = args[0];
			int port = Integer.parseInt(args[1]);
			String name = args[2];
			//usage: java Client <ip address of server> <port number> <name of player>		
			Client client = new Client(serverIP, port, name);
	        
		}catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> <your name>");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
        }

	}

	public void run(){
		connected = true;
		while(connected){

		}
		try{
			serverSocket.close();	
		}catch(IOException e){

		}
		
	}
}