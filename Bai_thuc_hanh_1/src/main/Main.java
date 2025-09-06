package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.StoreManager;

public class Main {
    public static void main(String[] args) {
    	try {
    	    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception ex) {
    	    System.err.println("Failed to initialize Nimbus LaF");
    	}


        SwingUtilities.invokeLater(() -> new StoreManager().setVisible(true));
    }
}
