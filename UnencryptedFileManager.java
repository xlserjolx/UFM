package ua.serhiyshkurin.UFM;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class UnencryptedFileManager extends JFrame{

    static ArrayList<Character> tempArray = new ArrayList <Character>();
    static String password = "myPassword";
    static String encryptedFileName = "encryptedFile.txt";
    static InputStream in = null;
    static OutputStream out = null;
    static int len = 0;
    static File encryptedFile = new File(encryptedFileName);

    static ArrayList<Character> beforeEncryptionCharacters = new ArrayList<Character>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ',', '.', '?', ':', ';', '\"', '\'', '!', '-', '+'));
    static ArrayList<Character> afterEncryptionCharacters = new ArrayList<Character>(Arrays.asList('q','a','z','x','s','w','e','d','c','v','f','r','t','g','b','n','h','y','u','j','m','p','o','l','i','k','Q','A','Z','X','S','W','E','D','C','V','F','R','T','G','B','N','H','Y','U','J','M','P','O','L','I','K','0','3','8','1','6','5','4','7','2','9','+','-','!','\'','\"',';',':','?','.',','));

    public static void main(String[] args) {
        GUI ufm = new GUI();
        ufm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ufm.setSize(300,500);
        ufm.setVisible(true);
    }

    public static ArrayList<String> findRecord(String searchString) {

        StringBuilder result = new StringBuilder(tempArray.size()-(len+1));
        for (int i=(len+1); i<tempArray.size(); i++) {
            result.append(tempArray.get(i));
        }
        String[] strings = result.toString().split("\n");
        ArrayList<String> output = new ArrayList<String>();
        for(String o:strings){
            if(o.contains(searchString)){
                output.add(o);
                output.add("\n");
            }
        }
        return output;
    }

    public static void addText(String s) {

        //Encrypting array
        char tempChar;
        tempArray.add('\n');
        for(int t = 0; t<s.length(); t++) {
            tempChar = s.charAt(t);
            tempArray.add(tempChar);
        }
    }

    public static void addToEncryptedFile() {

        // Encrypting array
        char tempChar;
        ArrayList<Character> tempArray2 = new ArrayList<Character>();
        for (Character aTempArray : tempArray) {
            tempChar = aTempArray;
            if (beforeEncryptionCharacters.contains(tempChar)) {
                tempArray2.add(afterEncryptionCharacters.get(beforeEncryptionCharacters.indexOf(tempChar)));
            } else tempArray2.add(tempChar);
        }

        //Adding array to our new file
        try{
            out = new BufferedOutputStream(new FileOutputStream(encryptedFile));
            for (Character aTempArray2 : tempArray2) {
                out.write(aTempArray2);
            }
            out.flush();
        }catch(Exception e){JOptionPane.showMessageDialog(new JLabel(),"Error with writing to the file!");}
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {JOptionPane.showMessageDialog(new JLabel(), "Error with writing to the file!");}
            }
        }
    }

    public static boolean checkPassword(File eFile) {

        // Cutting password length from array, converting character to integer and setting it to the new int
        readEncryptedFile(eFile);
        len = (int) tempArray.get(0);

      // Checking password from array
        char tempArray2[] = new char[len];
        for(int y = 1;y<=len;y++){
            tempArray2[y-1] = (tempArray.get(y));
        }
        char tempChar2;
        for(int t = 0; t<len; t++){
            tempChar2 = tempArray2[t];
            if(afterEncryptionCharacters.contains(tempChar2)){
                tempArray2[t]=beforeEncryptionCharacters.get(afterEncryptionCharacters.indexOf(tempChar2));
            }
        }
        return password.equals(String.valueOf(tempArray2));
    }

    public static void decryptTempArray() {
        //Decrypting array
        char tempChar;
        for(int t = 1; t<tempArray.size(); t++){
            tempChar = tempArray.get(t);
            if(afterEncryptionCharacters.contains(tempChar)){
                tempArray.set(t, beforeEncryptionCharacters.get(afterEncryptionCharacters.indexOf(tempChar)));
            }
        }
    }

    private static void readEncryptedFile(File encryptedFile3) {

        try{
            in = new BufferedInputStream(new FileInputStream(encryptedFile3));
            int ch;
           // System.out.println("Previous tempArray text:");
            while ((ch = in.read()) != -1) {
                tempArray.add((char)ch);
            }

            // Checking for tempArray filled
            for(char s:tempArray){
                System.out.print(s);
            }

        }catch(Exception e){JOptionPane.showMessageDialog(new JLabel(), "Error with opening file!");}
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {JOptionPane.showMessageDialog(new JLabel(), "Error with opening file!");}
            }
        }


    }

    static void readUnencryptedFile(File unencryptedFile6) {
        try{
            in = new BufferedInputStream(new FileInputStream(unencryptedFile6));
            int ch;
            tempArray.add((char) password.length());

            for(char c: password.toCharArray()){
                tempArray.add(c);
            }

            while ((ch = in.read()) != -1) {
                tempArray.add((char)ch);
            }

            // Checking for tempArray filled
            for(char s:tempArray){
                System.out.print(s);
            }


        }catch(Exception e){
            JOptionPane.showMessageDialog(new JLabel(),"Error with reading file!");
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeNewFile(String absolutePath, String fileName) {
        // Encrypting array
        char tempChar;
        ArrayList<Character> tempArray2 = new ArrayList<Character>();
        for (Character aTempArray : tempArray) {
            tempChar = aTempArray;
            if (beforeEncryptionCharacters.contains(tempChar)) {
                tempArray2.add(afterEncryptionCharacters.get(beforeEncryptionCharacters.indexOf(tempChar)));

            } else tempArray2.add(tempChar);
        }

        //Adding array to our new file
        try{
            out = new BufferedOutputStream(new FileOutputStream(new File(absolutePath)+"\\"+fileName));
            for (Character aTempArray2 : tempArray2) {
                out.write(aTempArray2);
            }
            out.flush();
        }catch(Exception e){JOptionPane.showMessageDialog(new JLabel(), "Error with writing to the file!");}
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {JOptionPane.showMessageDialog(new JLabel(), "Error with writing to the file!");}
            }
        }
    }
}

