package fr.PyJaC.uno.fenetre;

import java.awt.Dimension;

import javax.swing.JFrame;

public class UnoFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnoFrame() {
		setTitle("Uno");
		setSize(new Dimension(900, 500));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void updateFrame() {
		invalidate();
		validate();
		repaint();
	}
	
	public void showFrame() {
		setVisible(true);
	}
	
	public void hideFrame() {
		setVisible(false);
	}
}
