import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

public class Client implements Runnable{
	private boolean connected;
	private static JPanel screenDeck = new JPanel(new CardLayout());
	private static JFrame frame;
	static Thread t;
	static DatagramSocket socket;
	static JLabel label;
	private static Socket serverSocket;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static JTextArea keystrokes;
	private static JTextArea textarea;
	private static JTextArea leaderboard;
	private static OverlaidField movementBox;


	//
	String serverIP;
	int port;
	String name;


	static int playerID;
	Player player1;

	public Client(String serverIP, int port, String name){
		try{
			socket = new DatagramSocket();		
			this.serverIP = serverIP;
			this.port = port;
			this.name = name;
			serverSocket = new Socket(serverIP, port);

			System.out.println("Just connected to " + serverSocket.getRemoteSocketAddress());

			OutputStream outToServer = serverSocket.getOutputStream();
            out = new DataOutputStream(outToServer);
            out.writeUTF(name);


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
				p.show(screenDeck, "JButton");

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
		textarea = new JTextArea("WELCOME TO THE CHAT ROOM!\n");
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

		leaderboard = new JTextArea("LEADERBOARD:");
		JScrollPane leadScroll = new JScrollPane(leaderboard);
		leadScroll.setPreferredSize(new Dimension(445, 100));
		leaderboard.setEditable(false);
		info.add(leadScroll, BorderLayout.SOUTH);

		infoBox.add(info, BorderLayout.EAST);


		JPanel gameProper = new JPanel();

		movementBox = new OverlaidField();

		movementBox.setPreferredSize(new Dimension(900, 520));
		player1 = new Player(450, 20,1);
		movementBox.add(player1);
		Arrow parrow = new Arrow(-900,-900, true);
		movementBox.add(parrow);
		Kakamora[] kaks = new Kakamora[20];
		for(int k=0;k<20;k++){
			kaks[k] = new Kakamora(50,40*k, 1);
			movementBox.add(kaks[k]);
			Thread y = new Thread(kaks[k]);
			y.start();
		}
		for(int l=0;l<20;l++){
			Kakamora kak = new Kakamora(0,50*l, 1);
			movementBox.add(kak);
			Thread y = new Thread(kak);
			y.start();
		}

		leaderboard.requestFocus();
		leaderboard.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent ke) {}
			public void keyTyped(KeyEvent ke) {
				try{
					if(ke.getKeyChar() == KeyEvent.VK_1){
						out.writeUTF("game~" + name + "~left");
						player1.moveLeft();
						send("MOVE " + name + " " + player1.getXpos() + " " + player1.getYpos());
					}else if(ke.getKeyChar() == KeyEvent.VK_0){
						out.writeUTF("game~" + name + "~right");
						player1.moveRight();
						send("MOVE " + name + " " + player1.getXpos() + " " + player1.getYpos());
					}else if(ke.getKeyChar() == KeyEvent.VK_SPACE){
						out.writeUTF("game~" + name + "~FIRE");
						parrow.setpos(player1.getYpos(),player1.getXpos());

						//send move data
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent ke) {}
		});


		gameScreen.add(movementBox, BorderLayout.NORTH);
		gameScreen.add(infoBox, BorderLayout.SOUTH);




		c.add(screenDeck);

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(message.getText() != "" || message.getText() != null){
					try{
						out.writeUTF("chat~" + name + "~" + message.getText());
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
			}
		});


		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

	}

	//
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
			InetAddress address = InetAddress.getByName(serverIP);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//

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
		String serverData;
		connected = true;
		while(connected){
			try{
				// 
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				try{
					socket.receive(packet);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				serverData = new String(buf);
				serverData = serverData.trim();

				System.out.println("serverData:" + serverData.split(":"));

				if(!connected && serverData.startsWith("JOINED")){
					connected = true;
					System.out.println("Joined");
				}else if(!connected){
					System.out.println("Joining..");
					send("JOIN " + name);
				}else if(connected){
					//UI Stuff - clear the players
					//offscreen.getGraphics().clearRect(0, 0, 640, 480);
					if (serverData.startsWith("MOVE")){
						String[] playersInfo = serverData.split(":");
						System.out.println("serverData: " + serverData);
						for (int i=0;i<playersInfo.length;i++){
							String[] playerInfo = playersInfo[i].split(" ");
							String pname =playerInfo[1];
							int x = Integer.parseInt(playerInfo[2]);
							int y = Integer.parseInt(playerInfo[3]);
							// draw again
							// offscreen.getGraphics().fillOval(x, y, 20, 20);
							// offscreen.getGraphics().drawString(pname,x-10,y+30);					
						}
						//show the changes
						//frame.repaint();
					}

				}

				//
				InputStream inFromServer = serverSocket.getInputStream();
	            DataInputStream in = new DataInputStream(inFromServer);
	            String msg = in.readUTF();

	            System.out.println(msg);

	            String[] tokens = msg.split("~");

	            if(tokens[0].equals("chat")){
	            	textarea.setText(textarea.getText() +"\n"+ tokens[1] + ": " + tokens[2]);
	            }else if(tokens[0].equals("game")){
	            	leaderboard.setText(leaderboard.getText()+"\n"+tokens[1]+" "+tokens[2]);
	            }

			}catch(SocketException e){
				e.printStackTrace();
				System.exit(1);
			}catch(IOException er){
				er.printStackTrace();
			}
		}
		try{
			serverSocket.close();	
		}catch(IOException e){

		}
	}

}