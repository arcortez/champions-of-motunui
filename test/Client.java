import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

public class Client implements Runnable{

	//variables for networking
	static boolean connected;
	static DatagramSocket socket;
	static JLabel label;
	static Socket serverSocket;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static OutputStream outToServer;

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

	private static int currentTutorialScreen = 0;

	//variables for gameplay & scoring system
	static int playerID;
	static Kakamora[][] kaks;
	static int playerScore;

	public Client(String serverIP, int port, String name){
		try{
			socket = new DatagramSocket();		
			serverSocket = new Socket(serverIP, port);

			System.out.println("Just connected to " + serverSocket.getRemoteSocketAddress());

			outToServer = serverSocket.getOutputStream();
            out = new DataOutputStream(outToServer);
            out.writeUTF(name);
		}
		catch(SocketException e){e.printStackTrace();System.exit(1);}
		catch(UnknownHostException e){e.printStackTrace();System.exit(1);}
		catch(IOException e){e.printStackTrace();System.exit(1);}


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
		// skipTutorial = new JButton(new ImageIcon("../assets/skip-n.png"));
		// skipTutorial.setRolloverIcon(new ImageIcon("../assets/skip-h.png"));
		// skipTutorial.setPressedIcon(new ImageIcon("../assets/skip-c.png"));
		// skipTutorial.setContentAreaFilled(false);
		// skipTutorial.setBorderPainted(false);
		// skipTutorial.setMargin(new Insets(0,0,0,0));

		prev = new JButton("< Prev");
		// prev = new JButton(new ImageIcon("../assets/prev-n.png"));
		// prev.setRolloverIcon(new ImageIcon("../assets/prev-h.png"));
		// prev.setPressedIcon(new ImageIcon("../assets/prev-c.png"));
		// prev.setContentAreaFilled(false);
		// prev.setBorderPainted(false);
		// prev.setMargin(new Insets(0,0,0,0));


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
		screenDeck.add(tutorialScreen, "TUTORIAL");
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
		sendButton = new JButton("Send!");
		
		messageBox.add(new JLabel("You:"), BorderLayout.WEST);
		message.setPreferredSize(new Dimension(350,20));
		messageBox.add(message, BorderLayout.CENTER);
		messageBox.add(sendButton, BorderLayout.EAST);


		chatBox.add(messageBox, BorderLayout.EAST);
		infoBox.add(chatBox, BorderLayout.WEST);

		info = new JPanel();
		info.setLayout(new BorderLayout());
		lifePanel = new JPanel();
		focus = new JButton("FOCUS!");
		
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

		// movementBox.add(new ImageIcon("../assets/gameScreen.png"));

		movementBox.setPreferredSize(new Dimension(900, 520));
		Player player1 = new Player(450, 20, 1);
		movementBox.add(player1);
		kaks = new Kakamora[4][19];

		for(int i=0;i<4;i++){
			for (int j=0;j<19;j++) {
				kaks[i][j] = new Kakamora(i*40, (j*40)+70, i*j, (i%3)+1);
				movementBox.add(kaks[i][j]);
			}
		}
		playerScore = 0;

		Arrow parrow = new Arrow(-900,-900, true, kaks);
		movementBox.add(parrow);		

		gameScreen.add(movementBox, BorderLayout.NORTH);
		gameScreen.add(infoBox, BorderLayout.SOUTH);

		c.add(screenDeck);

		skipTutorial.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CardLayout p = (CardLayout)screenDeck.getLayout();
				p.show(screenDeck, "GAME");
			}
		});

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(message.getText().length() > 0){
					try{
						out.writeUTF(name + ": " + message.getText());
					}catch(IOException er){
						er.printStackTrace();
					}
					message.setText("");
				}
				leaderboard.requestFocus();
			}
		});

		focus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				leaderboard.requestFocus();
				for(int i=0;i<4;i++){
					for (int j=0;j<19;j++) {
						Thread t = new Thread(kaks[i][j]);
						t.start();
					}
				}
			}
		});

		leaderboard.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent ke) {}
			public void keyTyped(KeyEvent ke) {
				try{
					if(ke.getKeyChar() == KeyEvent.VK_1){
						player1.moveLeft();
						out.writeUTF(name+"> left");

					}else if(ke.getKeyChar() == KeyEvent.VK_0){
						player1.moveRight();
						out.writeUTF(name+"> right");

					}else if(ke.getKeyChar() == KeyEvent.VK_SPACE){
						parrow.setpos(player1.ypos,player1.xpos);
						out.writeUTF(name+"> FIRE");
					}
				}catch(IOException e){}
			}
			public void keyReleased(KeyEvent ke) {}
		});


		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(currentTutorialScreen >= 4){
					CardLayout p = (CardLayout)screenDeck.getLayout();
					p.show(screenDeck, "GAME");
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
		
		Thread t = new Thread(this);
		t.start();

		
	}

	public void run(){
		leaderboard.requestFocus();
		System.out.println(playerScore);
		try{
			//int playerScore_old = playerScore;
			//Thread.sleep(1);
			//if(playerScore_old != playerScore){
			//}
		}catch(Exception e){}

	}

	public static void incrementScore(int addPoint){ // increments this player's score by addPoint
		playerScore = playerScore + addPoint;
	}

	public static void main(String[] args){
		try{
			String serverIP = args[0];
			int port = Integer.parseInt(args[1]);
			String name = args[2];
			//usage: java Client <ip address of server> <port number> <name of player>		
			Client client = new Client(serverIP, port, name);
	        ChatListener listen = new ChatListener();

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

class ChatListener implements Runnable{
	public ChatListener(){
		Thread t = new Thread(this);
		t.start();
	}

	public void run(){
		boolean connected = true;
		while(connected){
			try{
				InputStream inFromServer = Client.serverSocket.getInputStream();
	            DataInputStream in = new DataInputStream(inFromServer);
	            String msg = in.readUTF();

	            System.out.println(msg);

            	Client.textarea.setText("\n"+Client.textarea.getText()+msg);

			}catch(SocketException e){
				e.printStackTrace();
				System.exit(1);
			}catch(IOException er){
				er.printStackTrace();
				System.exit(1);
			}
		}
		try{
			Client.serverSocket.close();	
		}catch(IOException e){}
	}
}