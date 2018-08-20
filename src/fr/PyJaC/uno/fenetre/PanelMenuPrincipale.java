package fr.PyJaC.uno.fenetre;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class PanelMenuPrincipale extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3431559038877443170L;
	
	public PanelMenuPrincipale() {
		configurePanel();
		configureWidget();
		paint(getGraphics());
	}

	private void configureWidget() {
		// TODO Auto-generated method stub
		
	}

	private void configurePanel() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Image img;
		try {
			img = ImageIO.read(getClass().getResource("res/MainMenu/Title.png"));
			g2d.drawImage(img, getWidth() / 2 - 300 / 2, 150 / 6, 300, 150, this);
		} catch (IOException e) {
			System.out.println("Impossible de lire \"res/MainMenu/title\"");
			System.exit(1);
		}
		
	}

}
