package ua.serhiyshkurin.UFM;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JFrame {

    private JTextField searchAddTextField;
    private JTextField passwordField;
    private JButton fileChooseButton;
    private JButton encryptAndSave;
    private JButton saveAs;
    private JButton newEncryptedFileChooseButton;
    private JButton searchTextButton;
    private JButton addTextButton;
    private final JLabel label;
    public JTextArea fileTextView;

    public GUI() {
        super("Unencrypted File Manager");
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        fileChooseButton = new JButton("Open encrypted file");
        add(fileChooseButton);

        newEncryptedFileChooseButton = new JButton("Encrypt new file");
        add(newEncryptedFileChooseButton);

        searchAddTextField = new JTextField(20);
        searchAddTextField.setVisible(false);
        add(searchAddTextField);

        addTextButton = new JButton("Add record");
        addTextButton.setVisible(false);
        add(addTextButton);

        searchTextButton = new JButton("Find record");
        searchTextButton.setVisible(false);
        add(searchTextButton);

        label = new JLabel("Chosen file");
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);

        passwordField = new JPasswordField(20);
        passwordField.setText("myNewPassword");
        add(passwordField);

        fileTextView = new JTextArea(15,20);
        fileTextView.setVisible(false);
        fileTextView.setBackground(Color.CYAN);
        fileTextView.setCaretColor(Color.BLUE);
        add(fileTextView);

        encryptAndSave = new JButton("Encrypt and save");
        encryptAndSave.setVisible(false);
        add(encryptAndSave);

        saveAs = new JButton("Save as...");
        saveAs.setVisible(false);
        add(saveAs);

        TheHandler handler = new TheHandler();
        passwordField.addActionListener(handler);
        fileChooseButton.addActionListener(handler);
        newEncryptedFileChooseButton.addActionListener(handler);
        searchTextButton.addActionListener(handler);
        addTextButton.addActionListener(handler);
        encryptAndSave.addActionListener(handler);
        saveAs.addActionListener(handler);

    }

    private class TheHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == fileChooseButton) {
                UnencryptedFileManager.tempArray.clear();
                JFileChooser fileOpen = new JFileChooser();
                int ret = fileOpen.showDialog(null, "Open file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    UnencryptedFileManager.password = passwordField.getText();
                    if (UnencryptedFileManager.checkPassword(fileOpen.getSelectedFile())) {
                        label.setText(fileOpen.getSelectedFile().getName());
                        UnencryptedFileManager.encryptedFile = fileOpen.getSelectedFile();
                        UnencryptedFileManager.encryptedFileName = fileOpen.getName();
                        UnencryptedFileManager.len = passwordField.getText().length();
                        addTextButton.setVisible(true);
                        searchAddTextField.setVisible(true);
                        searchTextButton.setVisible(true);
                        fileTextView.setVisible(true);
                        passwordField.setVisible(false);
                        encryptAndSave.setVisible(true);
                        UnencryptedFileManager.decryptTempArray();
                        for (int i = passwordField.getText().length()+1;i<UnencryptedFileManager.tempArray.size(); i++) {
                            char s = UnencryptedFileManager.tempArray.get(i);
                            fileTextView.append(String.valueOf(s));
                        }
                        saveAs.setVisible(true);

                        JOptionPane.showMessageDialog(new JLabel("Done"), "Password correct");
                    } else {
                        JOptionPane.showMessageDialog(new JLabel("Error"), "Incorrect password");
                    }
                }
            } else if (event.getSource() == addTextButton) {
                UnencryptedFileManager.addText(searchAddTextField.getText());
                fileTextView.setText("");
                for (int i = passwordField.getText().length()+1;i<UnencryptedFileManager.tempArray.size(); i++) {
                    char s = UnencryptedFileManager.tempArray.get(i);
                    fileTextView.append(String.valueOf(s));
                }
            } else if (event.getSource() == searchTextButton) {
                fileTextView.setText("");
                for(String o:UnencryptedFileManager.findRecord(searchAddTextField.getText())){
                        fileTextView.append(o);
                }
            } else if (event.getSource() == newEncryptedFileChooseButton) {
                UnencryptedFileManager.tempArray.clear();
                JFileChooser fileOpen2 = new JFileChooser();
                    int ret2 = fileOpen2.showDialog(null, "Open file");
                    if (ret2 == JFileChooser.APPROVE_OPTION) {
                        UnencryptedFileManager.password = passwordField.getText();
                        try {
                            UnencryptedFileManager.readUnencryptedFile(fileOpen2.getSelectedFile());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        UnencryptedFileManager.len = passwordField.getText().length();
                        label.setText(fileOpen2.getSelectedFile().getName());
                        addTextButton.setVisible(true);
                        searchAddTextField.setVisible(true);
                        searchTextButton.setVisible(true);
                        fileChooseButton.setVisible(false);
                        passwordField.setVisible(false);
                        fileTextView.setVisible(true);
                        for (int i = passwordField.getText().length()+1;i<UnencryptedFileManager.tempArray.size(); i++) {
                            char s = UnencryptedFileManager.tempArray.get(i);
                            fileTextView.append(String.valueOf(s));
                        }
                        encryptAndSave.setVisible(true);
                        saveAs.setVisible(true);
                    }

            } else if(event.getSource() == encryptAndSave){
                    UnencryptedFileManager.addToEncryptedFile();
            }else if(event.getSource() == saveAs){
                JFileChooser fileSaveAs = new JFileChooser();
                int ret3 = fileSaveAs.showSaveDialog(null);
                if (ret3 == JFileChooser.APPROVE_OPTION) {
                    try {
                        UnencryptedFileManager.writeNewFile(fileSaveAs.getCurrentDirectory().getAbsolutePath(), fileSaveAs.getSelectedFile().getName());
                        label.setText(fileSaveAs.getSelectedFile().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else JOptionPane.showMessageDialog(new JLabel("Error"), "Unknown command");

        }
    }
}
