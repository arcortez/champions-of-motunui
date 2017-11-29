import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.imageio.ImageIO;

public class Client implements Runnable{

	//variables for networking
	static boolean connected;
	static DatagramSocket socket;
	static JLabel label;

	static Socket serverSocket;

	Thread t = new Thread(this);

	private static DataOutputStream out;
	private static DataInputStream in;
	private static ChatClient cclient;

	//variables for GUI
	private static JPanel screenDeck = new JPanel(new CardLayout());
	private static JFrame frame;
	private static Container c;

	private static JPanel gameScreen; //panel with chatbox AND actual gameScreen
	/*private*/ static JTextArea textarea; // actual chatbox
	private static JPanel infoBox; // bottom panel of gameScreen
	private static JPanel chatBox; // left half of bottom panel
	private static JTextArea leaderboard;
	private static JScrollPane pane; // scrollable area for chat messages
	private static JPanel messageBox; //below textarea
	private static JTextField message; // chat textfield
	private static JButton sendButton;
	private static JPanel info;
	private static JPanel lifePanel;
	private static JButton focus;
	private static JTextField lives;
	private static JScrollPane leadScroll;
	private static JLabel tutorialImage;

	private static OverlaidField movementBox; //actual game field

	private static JPanel tutorialScreen; //tutorial screens
	private static JPanel southTutorial;
	private static JButton skipTutorial;
	private static JButton prev;
	private static JButton next;

	private static JPanel waitingScreen;
	private static JButton readyButton;

	private static int currentTutorialScreen = 0;

	static int playerID;
	static int maxPlayers;

	static int xpos;
	static int ypos;

	static Kakamora[][] kaks;
	static PlayerGUI[] players;
	static Arrow[] arrows;

	static String serverIP;
	static int port;
	static String name;
	
	public Client(String serverIP, int port, String name){
		this.serverIP = serverIP;
		this.name = name;
		this.port = port;

		try{
			socket = new DatagramSocket();
			serverSocket = new Socket(serverIP, port);

			System.out.println("Just connected to " + serverSocket.getRemoteSocketAddress());

			OutputStream outToServer = serverSocket.getOutputStream();
            out = new DataOutputStream(outToServer);
			out.writeUTF(name);
			

		}
		catch(UnknownHostException e){e.printStackTrace();System.exit(1);}
		catch(IOException e){e.printStackTrace();System.exit(1);}

		new Assets();
		t.start();
		
	}

	public void run(){

		String serverData;
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}

			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			if (!connected) {
				System.out.println("Joining...");
				send("JOIN " + name);
			} 

			try {
				socket.receive(packet);
			}catch(Exception e) {
				e.printStackTrace();
			}

			serverData = new String(packet.getData());
			// System.out.println("serverData: " + serverData.trim());
			
			if (!connected && serverData.startsWith("ID")){
				connected = true;
				String[] tk = serverData.split(" ");
				playerID = Integer.parseInt(tk[1].trim());
				maxPlayers = Integer.parseInt(tk[2].trim());
					
				
				initGUI(maxPlayers);
				
				System.out.println("You have joined the game.");
			} else if (connected) {
				if (serverData.startsWith("GAME START")){
					System.out.println("\nGAME START!\n");
					CardLayout p = (CardLayout)screenDeck.getLayout();
					p.show(screenDeck, "GAME");
					leaderboard.requestFocus();
					for(int i=0;i<4;i++){
						for (int j=0;j<19;j++) {
							Thread t = new Thread(kaks[i][j]);
							t.start();
						}
					}
				} else if (serverData.startsWith("MOVE")){
					String[] playerInfo = serverData.split(" ");

					int pID =  Integer.parseInt(playerInfo[1].trim());
					int x = Integer.parseInt(playerInfo[2].trim());
					int y = Integer.parseInt(playerInfo[3].trim());
				

					System.out.println("MOVE " + pID + " " + x + " " + y);
					players[pID].setPos(x,y);
					// change UI
				} else if (serverData.startsWith("FIRE")){
					String[] playerInfo = serverData.split(" ");

					int pID =  Integer.parseInt(playerInfo[1].trim());
					int x = Integer.parseInt(playerInfo[2].trim());
					int y = Integer.parseInt(playerInfo[3].trim());
				

					System.out.println("FIRE " + pID + " " + x + " " + y);
					arrows[pID].setPos(y,x);
					// change UI
				} else{
					System.out.println(serverData.trim());
				}
			}
		}
	}

	public void initGUI(int maxPlayers){
		frame = new JFrame("Champions of Motunui : "+name);
		c = frame.getContentPane();
		frame.setPreferredSize(new Dimension(900,700));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		tutorialScreen = new JPanel();
		gameScreen = new JPanel();

		tutorialScreen.setLayout(new BorderLayout());
		southTutorial = new JPanel();
		southTutorial.setOpaque(false);
		skipTutorial = new JButton("SKIP TUTORIAL");
		skipTutorial = new JButton(new ImageIcon("../assets/skip-n.png"));
		skipTutorial.setRolloverIcon(new ImageIcon("../assets/skip-h.png"));
		skipTutorial.setPressedIcon(new ImageIcon("../assets/skip-c.png"));
		skipTutorial.setContentAreaFilled(false);
		skipTutorial.setBorderPainted(false);
		skipTutorial.setMargin(new Insets(0,0,0,0));

		prev = new JButton(new ImageIcon("../assets/prev-n.png"));
		prev.setRolloverIcon(new ImageIcon("../assets/prev-h.png"));
		prev.setPressedIcon(new ImageIcon("../assets/prev-c.png"));
		prev.setContentAreaFilled(false);
		prev.setBorderPainted(false);
		prev.setMargin(new Insets(0,0,0,0));


		next = new JButton(new ImageIcon("../assets/next-n.png"));
		next.setRolloverIcon(new ImageIcon("../assets/next-h.png"));
		next.setPressedIcon(new ImageIcon("../assets/next-c.png"));
		next.setContentAreaFilled(false);
		next.setBorderPainted(false);
		next.setMargin(new Insets(0,0,0,0));

		southTutorial.setLayout(new BorderLayout());
		southTutorial.setOpaque(false);
		southTutorial.add(prev, BorderLayout.WEST);
		southTutorial.add(next, BorderLayout.EAST);
		southTutorial.add(skipTutorial, BorderLayout.CENTER);

		tutorialImage = new JLabel(new ImageIcon("../assets/title.png"));
		prev.setEnabled(false);
		tutorialScreen.add(tutorialImage, BorderLayout.CENTER);
		tutorialScreen.add(southTutorial, BorderLayout.SOUTH);
		
		waitingScreen = new JPanel();
		waitingScreen.setLayout(new BorderLayout());
		waitingScreen.add(new JLabel(new ImageIcon("../assets/waiting.png")), BorderLayout.NORTH);
		readyButton = new JButton("READY!!");
		readyButton = new JButton(new ImageIcon("../assets/ready-n.png"));
		readyButton.setRolloverIcon(new ImageIcon("../assets/ready-h.png"));
		readyButton.setPressedIcon(new ImageIcon("../assets/ready-c.png"));
		readyButton.setContentAreaFilled(false);
		readyButton.setBorderPainted(false);
		readyButton.setMargin(new Insets(0,0,0,0));
		
		waitingScreen.add(readyButton, BorderLayout.SOUTH);

		readyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				send("READY " + playerID);
				readyButton.setEnabled(false);
			}
		});

		screenDeck.add(tutorialScreen, "TUTORIAL");		
		screenDeck.add(waitingScreen, "WAITING");
		screenDeck.add(gameScreen, "GAME");
		gameScreen.setLayout(new BorderLayout());
		

		infoBox = new JPanel();
		infoBox.setLayout(new BorderLayout());
		chatBox = new JPanel();
		chatBox.setLayout(new BorderLayout());
		textarea = new JTextArea("WELCOME TO THE CHAT ROOM!\n");
		textarea.setEditable(false);
		
		pane = new JScrollPane(textarea);
		pane.setPreferredSize(new Dimension(450, 100));
		chatBox.add(pane, BorderLayout.NORTH);

		messageBox = new JPanel();
		messageBox.setLayout(new BorderLayout());
		message = new JTextField();
		sendButton = new JButton(new ImageIcon("../assets/send-n.png"));
		sendButton.setRolloverIcon(new ImageIcon("../assets/send-h.png"));
		sendButton.setPressedIcon(new ImageIcon("../assets/send-c.png"));
		sendButton.setContentAreaFilled(false);
		sendButton.setBorderPainted(false);
		sendButton.setMargin(new Insets(0,0,0,0));
		
		messageBox.add(new JLabel("You:"), BorderLayout.WEST);
		message.setPreferredSize(new Dimension(350,20));
		messageBox.add(message, BorderLayout.CENTER);
		messageBox.add(sendButton, BorderLayout.EAST);


		chatBox.add(messageBox, BorderLayout.EAST);
		infoBox.add(chatBox, BorderLayout.WEST);

		info = new JPanel();
		info.setLayout(new BorderLayout());
		lifePanel = new JPanel();
		focus = new JButton(new ImageIcon("../assets/focus-n.png"));
		focus.setRolloverIcon(new ImageIcon("../assets/focus-h.png"));
		focus.setPressedIcon(new ImageIcon("../assets/focus-c.png"));
		focus.setContentAreaFilled(false);
		focus.setBorderPainted(false);
		focus.setMargin(new Insets(0,0,0,0));
		
		lifePanel.add(new JLabel("Lives left:"), BorderLayout.WEST);
		lives = new JTextField("3");
		lives.setEditable(false);
		lifePanel.add(lives, BorderLayout.WEST);
		lifePanel.add(focus, BorderLayout.EAST);
		info.add(lifePanel, BorderLayout.NORTH);

		leaderboard = new JTextArea("LEADERBOARD:");
		leadScroll = new JScrollPane(leaderboard);
		leadScroll.setPreferredSize(new Dimension(445, 100));
		leaderboard.setEditable(false);
		info.add(leadScroll, BorderLayout.SOUTH);

		infoBox.add(info, BorderLayout.EAST);

		movementBox = new OverlaidField();
		movementBox.setPreferredSize(new Dimension(900, 520));
		
		// Player player1 = new Player(450, 20,1);
		
		players = new PlayerGUI[maxPlayers];
		arrows = new Arrow[maxPlayers];

		System.out.print("Adding Players: [");
		for(int i=0;i<maxPlayers;i++){
			System.out.print("#");

			players[i] = new PlayerGUI(name, 450, 20*(i+1), i);
			movementBox.add(players[i]);
				
			arrows[i] = new Arrow(i, -900,-900, true);
			movementBox.add(arrows[i]);

			xpos = 450;
			ypos = 20*(i+1);
		}
		System.out.println("] 100%");


		kaks = new Kakamora[4][19];

		for(int i=0;i<4;i++){
			for (int j=0;j<19;j++) {
				kaks[i][j] = new Kakamora(i*40, (j*40)+70, i*j, (i%3)+1);
				movementBox.add(kaks[i][j]);
			}
		}
		
		movementBox.setOpaque(false);
		infoBox.setOpaque(false);
		gameScreen.add(movementBox, BorderLayout.NORTH);
		gameScreen.add(infoBox, BorderLayout.SOUTH);

		c.add(screenDeck);

		skipTutorial.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CardLayout p = (CardLayout)screenDeck.getLayout();
				p.show(screenDeck, "WAITING");
			}
		});

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(message.getText().length() > 0){
					cclient.sendMessage(new ChatMessage(message.getText()));
					message.setText("");
				}
				leaderboard.requestFocus();
			}
		});

		focus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				leaderboard.requestFocus();
			}
		});

		leaderboard.requestFocus();
		leaderboard.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent ke) {}
			public void keyTyped(KeyEvent ke) {
				if(ke.getKeyChar() == KeyEvent.VK_1){
					moveLeft();
					send("MOVE " + playerID + " " + xpos + " " + ypos);
				}else if(ke.getKeyChar() == KeyEvent.VK_0){
					moveRight();
					send("MOVE " + playerID + " " + xpos + " " + ypos);
				}else if(ke.getKeyChar() == KeyEvent.VK_SPACE){
					send("FIRE " + playerID + " " + xpos + " " + ypos);
				}
			}
			public void keyReleased(KeyEvent ke) {}
		});


		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(currentTutorialScreen >= 4){
					CardLayout p = (CardLayout)screenDeck.getLayout();
					p.show(screenDeck, "WAITING");
				}
				currentTutorialScreen += 1;
				tutorialImage.setIcon(new ImageIcon("../assets/tutorial"+ currentTutorialScreen +".png"));
				prev.setEnabled(true);
			}
		});

		prev.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(currentTutorialScreen == 1){
					tutorialImage.setIcon(new ImageIcon("../assets/title.png"));	
					prev.setEnabled(false);
				}else{
					currentTutorialScreen -= 1;
					tutorialImage.setIcon(new ImageIcon("../assets/tutorial"+ currentTutorialScreen +".png"));	
				}
				
			}
		});

		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void send(String msg) {
		try {
			byte[] buf = msg.getBytes();
			InetAddress address = InetAddress.getByName(serverIP);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void moveLeft(){
		if(ypos >= 45)
			ypos -= 20;
	}	

	 public static void moveRight(){
	 	if(ypos <= 790)
	 		ypos += 20;
	}

	public static void main(String[] args){
		try{
			String serverIP = args[0];
			int port = Integer.parseInt(args[1]);
			String name = args[2];
			//usage: java Client <ip address of server> <port number> <name of player>		
			Client client = new Client(serverIP, port, name);
			cclient = new ChatClient(serverIP, port, name);


		}catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> <your name>");
            System.exit(1);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
            System.exit(1);
        }

	}

}
