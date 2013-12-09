

import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JApplet;



public class ImageViewer extends JApplet{
  Image aang;
  JButton startButton;
  public void init() {
    try {
      URL pic = new URL(getDocumentBase(), "images.png");
      aang = ImageIO.read(pic);
      
    } catch(Exception e) {
        e.printStackTrace();
    }
  }
  public void paint(Graphics g) {
    if (aang!=null) {
      g.drawImage(aang, 80, 80, this);
    }
  }
}