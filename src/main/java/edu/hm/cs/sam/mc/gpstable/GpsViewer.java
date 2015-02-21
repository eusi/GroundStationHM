package edu.hm.cs.sam.mc.gpstable;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Window;

/**
 * GPSViewer shows the GPS-Table-Log.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class GpsViewer extends Window {
    private static final Logger LOG = Logger.getLogger(GpsViewer.class.getName());
    private final JButton selectFile = new JButton("Refresh");
    private final JButton clear = new JButton("Clear File");
    private final JTextArea tarea = new JTextArea(28, 90);
    private final JScrollPane scrollPane = new JScrollPane();

    private FileReader input = null;
    int reply;

    /**
     * @param title
     *            title of the frame.
     * @param icon
     *            icon of the frame.
     */
    public GpsViewer(final String title, final Icon icon) {
        super(title, icon);
        
        setSize(780, 600);

        final Container c = getMainPanel();
        getMainPanel().setLayout(new MigLayout("", "[71px][77px][]", "[][]"));
        c.add(selectFile, "cell 0 0,alignx left,aligny center");

        selectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                selectGPSFile();
                tarea.setCaretPosition(0);
            }
        });
        c.add(clear, "cell 2 0,alignx left,aligny center");

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                reply = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete the current GPS table?", "Clear?",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    tarea.setText("");
                    input = null;
                    GpsTable.clearGpsTable();
                }

            }
        });

        getContentPane().add(scrollPane, "cell 0 1 4 1,alignx left,aligny top");
        scrollPane.setViewportView(tarea);

        tarea.setEditable(false);

        selectGPSFile();
        tarea.setCaretPosition(0);
    }

    @Override
    public void loadProperties() {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveProperties() {
        // TODO Auto-generated method stub

    }

    // select and load gps file
    private void selectGPSFile() {
        final File file = new File(Constants.GPS_TABLE_LOG);

        if (!file.exists()) {
            return;
        }

        try {
            if (input != null) {
                input.close();
            }

            input = new FileReader(file);
            final Scanner sc = new Scanner(input);

            final StringBuilder buff = new StringBuilder();

            while (sc.hasNextLine()) {
                buff.append(sc.nextLine() + "\n"); // append line to buff
            }

            tarea.setText(buff.toString());

            sc.close();
            input.close();
            input = null;

        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(GpsViewer.this, "File does not exist.",
                    "Invalid file name", JOptionPane.ERROR_MESSAGE);
            LOG.error("File does not exist.", ex);
        }
    }
}