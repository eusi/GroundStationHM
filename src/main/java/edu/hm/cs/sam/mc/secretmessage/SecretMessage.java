package edu.hm.cs.sam.mc.secretmessage;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Window;

@SuppressWarnings("serial")
public class SecretMessage extends Window implements Observer {

    private String letters;
    private static final Logger LOG = Logger.getLogger(SecretMessage.class.getName());
    private final JTextField lettersTextField;
    private final JLabel suggestedWord;

    private final SecretMessageController secretMessageController = SecretMessageController
            .getInstance();

    public SecretMessage(final String title, final Icon icon) {
        super(title, icon);

        secretMessageController.addObserver(this);

        final GridLayout gridLayout = new GridLayout(5, 0);

        setLayout(gridLayout);

        lettersTextField = new JTextField();
        lettersTextField.setFont(new Font("Helvetica", Font.BOLD, 30));
        lettersTextField.setBounds(20, 20, this.getBounds().width - 60,
                (this.getBounds().height / 2) - 180);
        getMainPanel().add(lettersTextField);

        final JButton btnSuggest2 = new JButton("find possible Words");
        btnSuggest2.setBounds(70, 120, 320, 50);
        btnSuggest2.setBackground(Color.white);
        btnSuggest2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    suggestWord(lettersTextField.getText());
                } catch (final Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ungültige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungültige Eingabe", ex);
                }

            }
        });

        getMainPanel().add(btnSuggest2);

        final JLabel suggestedWordHeader = new JLabel();
        suggestedWordHeader.setFont(new Font("Helvetica", Font.BOLD, 30));
        suggestedWordHeader.setBounds(70, 220, 320, 50);
        suggestedWordHeader.setText("Suggested Words:");
        getMainPanel().add(suggestedWordHeader);

        suggestedWord = new JLabel();
        suggestedWord.setFont(new Font("Helvetica", Font.BOLD, 50));
        suggestedWord.setBounds(70, 290, 320, 50);
        suggestedWord.setText("");
        getMainPanel().add(suggestedWord);

        setLetters("efir");

    }

    @Override
    public void loadProperties() {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveProperties() {
        // TODO Auto-generated method stub

    }

    public void setLetters(final String letters) {
        this.letters = letters;

        lettersTextField.setText(this.letters);
    }

    private void suggestWord(final String letters) {
        final ArrayList<String> possibleWords = secretMessageController.searchWords(letters);

        suggestedWord.setText(possibleWords.toString());
    }

    @Override
    public void update(final Observable o, final Object arg) {
        // TODO Auto-generated method stub
        final String newWord = (String) arg;
        setLetters(newWord);
    }
}
