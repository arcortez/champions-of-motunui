import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Font;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

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

    static BufferedImage arrow_down;
    static BufferedImage arrow_up;
    
    static BufferedImage title;

    // start = new JButton(new ImageIcon(filename_source));

    Assets(){
        System.out.print("Loading assets: [");
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

       arrow_up = loadImage("arrow_up");
       arrow_down = loadImage("arrow_down");
       System.out.println("] 100%");
    }

    public static BufferedImage loadImage(String filename) {
        try {
          System.out.print("#");
            return ImageIO.read(new File("../assets/"+filename+".png"));
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.print("!");
        return null;
    }
    public static Font createFont(String filename, float size){
      try{
          FileInputStream input = new FileInputStream(new File("../fonts/" + filename));
          Font font = Font.createFont(Font.TRUETYPE_FONT, input);
          Font namesFont = font.deriveFont(size);
          return namesFont;
      }catch(Exception e){
        System.exit(1);
      };
      return null;
    }

    public static void playSound(String filename, boolean repeat) {
      try {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename));
        DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(ais);
        if(repeat)
          clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
          clip.start();
      } catch(Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    public static String addZeroes(String str, int zerocount){
      int len = zerocount - str.length();
      for(int i=0;i<len;i++){
        str = "0" + str;
      }
      return str;
    }
}

