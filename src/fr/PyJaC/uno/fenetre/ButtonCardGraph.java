package fr.PyJaC.uno.fenetre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import fr.PyJaC.uno.Card;
import fr.PyJaC.uno.Player;

public class ButtonCardGraph extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Card card;
	
	public ButtonCardGraph(Card card){
		super();
		this.card = card;
		setPreferredSize(new Dimension(75, 100));
	}
	
	public ButtonCardGraph(){
		super();
		card = null;
		setPreferredSize(new Dimension(75, 100));
	}
	
	public ButtonCardGraph(Card cardParam, Player playerCourant) {
		super();
		this.card = cardParam;
		setPreferredSize(new Dimension(75, 100));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playerCourant.poseCard(card);
			}
		});
	}
	
	public void setCard(Card card) {
		this.card = card;
		paintComponent(getGraphics());
	}

	public void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (card != null) {
			GradientPaint gp = new GradientPaint(0, 0, card.getColor().getColor(), this.getWidth() * 2, this.getHeight() * 2,
			Color.white, true);
			g2d.setPaint(gp);
			try {
				g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
				g2d.drawImage(ImageIO.read(new File(card.getValeur().getPathString())), 0, 0, getWidth(), getHeight(), this);
			} catch (IOException e) {
				System.out.println("Impossible de lire l'image: " + card.getValeur().getPathString());
			}

		} else {
			g2d.setPaint(Color.lightGray);
			g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
		}
	}

}
