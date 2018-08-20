package fr.PyJaC.uno.fenetre;

import java.awt.Dimension;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class UnoFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnoFrame() {
		setTitle("Uno");
		setSize(new Dimension(1200, 500));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			setIconImage(ImageIO.read(getClass().getResource("res/Logo.png")));
		} catch (IOException e) {
			System.out.println("Impossible de charger le logo");
			System.exit(1);
		}
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
