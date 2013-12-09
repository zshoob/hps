

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JApplet;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;



public class FileDownloader extends JApplet implements ActionListener{
  Image aang;
  JButton startButton;
  public void init() {
    try {
      setLayout(new FlowLayout());
      startButton = new JButton("Strat");
      startButton.addActionListener(this);
      add("South", startButton);
      // URL pic = new URL(getDocumentBase(), "images.png");
      // aang = ImageIO.read(pic);
      
    } catch(Exception e) {
        e.printStackTrace();
    }
  }
  public void paint(Graphics g) {
    // if (aang!=null) {
      
    //   g.drawImage(aang, 80, 80, this);
    // }
  }

  public void actionPerformed(ActionEvent event){
    if(event.getSource() == startButton){
        try{
          File file = new File("test.txt");
          FileInputStream fis = new FileInputStream(file);
          BufferedInputStream bis = new BufferedInputStream(fis);

          File tmpDir = new File(System.getProperty("user.home"));
          File pageOutput = new File(tmpDir, "tmp.txt");
          FileOutputStream fos = new FileOutputStream(file);
          BufferedOutputStream bout = new BufferedOutputStream(fos);

          byte data[] = new byte[1024];
          while(bis.read(data, 0, 1024) >= 0){
            bout.write(data);
          }
          bout.close();
          bis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }


    }
  }
}