package util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestClient implements KeyListener{
	
	Client client;
	
	public TestClient(){
	}

	
	public void keyReleased(KeyEvent arg0) {
		client.send("H"); 
	}

	
	public void keyTyped(KeyEvent arg0) {}
	public void keyPressed(KeyEvent arg0) {}	

}
