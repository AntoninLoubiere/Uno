package fr.PyJaC.uno.fenetre;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import fr.PyJaC.uno.Card;
import fr.PyJaC.uno.Game;
import fr.PyJaC.uno.Main;
import fr.PyJaC.uno.Player;
import fr.PyJaC.uno.enumeration.ColorCard;

public class ButtonCardGraph extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Card card;
	private boolean showColorJocker = false;
	private WindowsPrincipale windowsPrincipale;
	
	
	public ButtonCardGraph(Card cardParam, Player playerCourant, WindowsPrincipale win) {
		super();
		this.card = cardParam;
		this.windowsPrincipale = win;
		setPreferredSize(new Dimension(75, 100));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				windowsPrincipale.testUno();
				if (card.getValeur() == Game.valeurJocker[0] || card.getValeur() == Game.valeurJocker[1]) {
					// dialog choose color
					Object[] possibilities = {"Rouge", "Vert", "Bleu", "Jaune"};
					String s = (String)JOptionPane.showInputDialog(
							windowsPrincipale,
							"Vous avez mis un jocker,\n"
									+ "Choisissez une couleur",
							"Choix Couleur",
							JOptionPane.PLAIN_MESSAGE,
							null,
							possibilities,
							"Rouge");
					
					if (s == null) {
						return;
					}
					
					switch (s) {
					case "Rouge":
						card.setColor(ColorCard.Red);
						break;
						
					case "Vert":
						card.setColor(ColorCard.Green);
						break;
						
					case "Bleu":
						card.setColor(ColorCard.Blue);
						break;
						
					case "Jaune":
						card.setColor(ColorCard.Yellow);
						break;

					default:
						break;
					}

				}
				windowsPrincipale.testUno();
				playerCourant.poseCard(card);
				if (playerCourant.getCard().size() == 1) {
					windowsPrincipale.addUnoVerif(playerCourant);
					windowsPrincipale.listPseudoInfoChange(playerCourant, "");
				}
			}
		});
	}
	
	public ButtonCardGraph() {
		super();
		setPreferredSize(new Dimension(75, 100));
		card = null;
	}

	public void setCard(Card card) {
		this.card = card;
		paintComponent(getGraphics());
	}
	
	public void setJockerColorVisibility(boolean bool) {
		showColorJocker = bool;
	}

	public void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (card != null) {
			try {
				if ((card.getValeur() == Game.valeurJocker[0] || card.getValeur() == Game.valeurJocker[1]) && !showColorJocker) {
					g2d.drawImage(ImageIO.read(getClass().getResource("res/card/" + card.getValeur().getPathString())), 0, 0, getWidth(),
							getHeight(), this);
				} else {
					g2d.drawImage(ImageIO.read(getClass().getResource("res/card/" + card.getColor().getPathColor() + card.getValeur().getPathString())), 0, 0, getWidth(),
							getHeight(), this);
				}
			} catch (IOException e) {
				System.out.println("Impossible de lire l'image: " + card.getValeur().getPathString());
			}

		} else {
			try {
				g2d.drawImage(ImageIO.read(getClass().getResource("res/card/card_back_alt.png")), 0, 0, getWidth(), getHeight(), this);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Main.printExceptionNotExit(e);
			}
		}
	}

}
