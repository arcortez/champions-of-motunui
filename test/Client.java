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
	private static DataOutputStream out;

	public Client(String serverIP, int port, String name){
		try{
			socket = new DatagramSocket();		
			serverSocket = new Socket(serverIP, port);

			System.out.println("Just connected to " + serverSocket.getRemoteSocketAddress());

			OutputStream outToServer = serverSocket.getOutputStream();
            out = new DataOutputStream(outToServer);
            
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
		screenDeck.add(gameScreen, "GAME");
		screenDeck.add(tutorialScreen, "TUTORIAL");
		
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
		JButton focus = new JButton("FOCUS!");
		
		lifePanel.add(new JLabel("Lives left:"), BorderLayout.WEST);
		JTextField lives = new JTextField("3");
		lives.setEditable(false);
		lifePanel.add(lives, BorderLayout.CENTER);
		lifePanel.add(focus, BorderLayout.EAST);
		info.add(lifePanel, BorderLayout.NORTH);

		JTextArea leaderboard = new JTextArea("LEADERBOARD:");
		leaderboard.setPreferredSize(new Dimension(445, 100));
		leaderboard.setEditable(false);
		info.add(leaderboard, BorderLayout.SOUTH);

		infoBox.add(info, BorderLayout.EAST);


		JPanel gameProper = new JPanel();

		JTextArea keystrokes = new JTextArea("press a key.");
		keystrokes.setEditable(false);
		JScrollPane pane1 = new JScrollPane(keystrokes);
		pane1.setPreferredSize(new Dimension(900, 520));

		keystrokes.requestFocus();
		keystrokes.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent ke) {}
			public void keyTyped(KeyEvent ke) {
				try{
					if(ke.getKeyChar() == KeyEvent.VK_1){
						keystrokes.setText(keystrokes.getText() + "\n"+name+" left");
						out.writeUTF("game " + name + " left");

					}else if(ke.getKeyChar() == KeyEvent.VK_0){
						keystrokes.setText(keystrokes.getText() + "  \n"+name+" right");
						out.writeUTF("game " + name + " right");

					}else if(ke.getKeyChar() == KeyEvent.VK_SPACE){
						keystrokes.setText(keystrokes.getText() + "  \n"+name+" FIRE");
						out.writeUTF("game " + name + " FIRE");

					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent ke) {}
		});

		gameScreen.add(pane1, BorderLayout.NORTH);
		gameScreen.add(infoBox, BorderLayout.SOUTH);


		c.add(screenDeck);

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				textarea.setText(textarea.getText()+"\n"+name+": "+message.getText());
				try{
					out.writeUTF("chat " + name + " " + message.getText());
				}catch(IOException e){
					e.printStackTrace();
				}
				message.setText("");
				keystrokes.requestFocus();
			}
		});

		focus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				keystrokes.requestFocus();
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

	public void update(String msg){

	}
}