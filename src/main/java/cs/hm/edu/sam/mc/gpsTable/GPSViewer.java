package cs.hm.edu.sam.mc.gpsTable;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cs.hm.edu.sam.mc.emergent.Emergent;
import cs.hm.edu.sam.mc.misc.CONSTANTS;

/**
 * GPSViewer shows the GPS-Table-Log.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class GPSViewer extends JInternalFrame
{
  private JButton selectFile = new JButton("Refresh");
  private JButton clear = new JButton("Clear File");
  private JTextArea tarea = new JTextArea(30, 60);

  private FileReader input = null;
  private final JScrollPane scrollPane = new JScrollPane();
  int reply;

  public GPSViewer()
  {
      setFrameIcon(new ImageIcon(Emergent.class.getResource(CONSTANTS.ICON_DIR
              + "gps_icon_mini.png")));
      setTitle("GPS Table");
      setIconifiable(true);
      setClosable(true);
      setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
      setBounds(0, 0, 800, 600);

    Container c = getContentPane();
    c.setLayout(new FlowLayout());
    c.add(selectFile);
    c.add(clear);
        
        getContentPane().add(scrollPane);
        scrollPane.setViewportView(tarea);
    
        tarea.setEditable(false);
    c.add(new JScrollPane());

    clear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
    	  reply = JOptionPane.showConfirmDialog(null, "Are you sure to delete the current GPS table?", "Clear?",  JOptionPane.YES_NO_OPTION);
    	  if (reply == JOptionPane.YES_OPTION)
    	  {
	        tarea.setText("");
	        input = null;
	        GpsTable.clearGpsTable();
    	  }

      }
    });

    selectFile.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
        	selectGPSFile();
        	tarea.setCaretPosition(0);
        }
      });
    
    selectGPSFile();
    tarea.setCaretPosition(0);
  }



  private void selectGPSFile()
  {
      File file = new File( CONSTANTS.GPS_TABLE_LOG );

		if(!file.exists()){
			return;
		}
      
      try {
        if (input != null)
          input.close();

        input = new FileReader(file);
        Scanner sc = new Scanner(input);

        StringBuffer buff = new StringBuffer();

        while(sc.hasNextLine())
          buff.append(sc.nextLine() + "\n"); // append line to buff

        tarea.setText(buff.toString());

        sc.close();
        input.close();
        input = null;

      } catch(IOException ex) {
        JOptionPane.showMessageDialog(GPSViewer.this,
          "File does not exist.", "Invalid file name",
          JOptionPane.ERROR_MESSAGE);
      }
  }
}