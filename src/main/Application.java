package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import vue.FenetreApplication;

public class Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
       java.awt.EventQueue.invokeLater(() -> new FenetreApplication().setVisible(true));
    }	
}
