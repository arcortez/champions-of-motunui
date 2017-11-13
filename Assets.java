public class Assets {
    static BufferedImage imMoana;
    static BufferedImage imMaui;
    static BufferedImage imPig;
    static BufferedImage imRooster;

    static BufferedImage background;
    static BufferedImage title;

    // start = new JButton(new ImageIcon(filename_source));

    Assets() {
       imMoana = loadImage("moana");


       background = loadImage("screen");
       title = loadImage("title");
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

