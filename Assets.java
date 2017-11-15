public class Assets {
    static BufferedImage imMoana;
    static BufferedImage imMaui;
    static BufferedImage imPig;
    static BufferedImage imRooster;

    static BufferedImage background;
    static BufferedImage gameScreen;
    static BufferedImage tutorial1;
    static BufferedImage tutorial2;
    static BufferedImage tutorial3;
    static BufferedImage tutorial4;

    static BufferedImage countdown_1;
    static BufferedImage countdown_2;
    static BufferedImage countdown_3;
    
    static BufferedImage title;

    // start = new JButton(new ImageIcon(filename_source));

    Assets() {
       imMoana = loadImage("moana");


       background = loadImage("screen");
       gameScreen = loadImage("gamescreen");
       title = loadImage("title");

       tutorial1 = loadImage("tutorial1");
       tutorial2 = loadImage("tutorial2");
       tutorial3 = loadImage("tutorial3");
       tutorial4 = loadImage("tutorial4");

       countdown_1 = loadImage("countdown_1");
       countdown_2 = loadImage("countdown_2");
       countdown_3 = loadImage("countdown_3");

    }

    public static BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(new File("../assets"))
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}

