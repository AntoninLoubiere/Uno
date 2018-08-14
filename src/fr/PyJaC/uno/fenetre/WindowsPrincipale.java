package fr.PyJaC.uno.fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import fr.PyJaC.uno.Card;
import fr.PyJaC.uno.Game;
import fr.PyJaC.uno.Player;
import fr.PyJaC.uno.UnoVerif;
import fr.PyJaC.uno.enumeration.ValeurCard;


public class WindowsPrincipale extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane = new JPanel();
	private JPanel center = new JPanel();
	private JPanel textLogPanel = new JPanel();
	private JPanel north = new JPanel();
	private JPanel cardPanel = new JPanel();
	private JPanel verifPanel = new JPanel();
	private JPanel cardCenterPanel = new JPanel();
	private JPanel bonusValidatePanel = new JPanel();
	
	private JScrollPane scrollPaneEast = new JScrollPane(textLogPanel);
	private JScrollPane south = new JScrollPane(cardPanel);
	
	private JButton pioche = new JButton("Piocher");
	private JButton unoButton = new JButton("UNO !");
	private JButton verifButton = new JButton("Valider");
	private JButton bonusValidateButton = new JButton("Ok");
	
	private JLabel verifLabel = new JLabel("C'est au tour de: ");
	private JLabel bonusValidateLabel = new JLabel("joueur 1 passe son tour");
	
	private Player playerCourant;
	
	private JSplitPane JSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center, scrollPaneEast);
	
	private ButtonCardGraph centerCard = new ButtonCardGraph();
	
	private UnoVerif unoVerif = null;

	private Game game;
	
	
	
	public WindowsPrincipale() {
		this.setTitle("UNO");
		this.setSize(800, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		creatWidget();
		createJPanel();
		this.setVisible(true);

	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	private void creatWidget() {
		addLog("Bienvenue dans l'application \"UNO\"");
				
		centerCard.setEnabled(false);
		centerCard.setJockerColorVisibility(true);

		pioche.setEnabled(false);
		
		// action Listener
		
		bonusValidateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				center.removeAll();
				center.add(cardCenterPanel, BorderLayout.CENTER);
				updateFrame();
				game.setWaitBonus(false);
			}
		});
		
		pioche.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playerCourant.piocheCard();
			}
		});
		
		unoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unoVerif != null) {
					unoVerif.sayUno();
					unoVerif = null;
				}
			}
		});
		verifButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				center.removeAll();
				center.add(cardCenterPanel, BorderLayout.CENTER);
				updateFrame();
				
				// verif Uno 
				
				if (unoVerif != null) {
					unoVerif.cancel();
					unoVerif.run();
				}
				
				chargeCard();
				
			}
		});
		
	}

	private void createJPanel() {
		this.setContentPane(contentPane);
		
		// layout
		
		verifPanel.setLayout(new BorderLayout());
		bonusValidatePanel.setLayout(new BorderLayout());
		contentPane.setLayout(new BorderLayout());
		textLogPanel.setLayout(new BoxLayout(textLogPanel, BoxLayout.Y_AXIS));
		center.setLayout(new BorderLayout());
		
		south.setPreferredSize(new Dimension(10, 130));
		center.setPreferredSize(new Dimension(500, 300));
		
		JSP.resetToPreferredSizes();
		
		// add
		contentPane.add(JSP, BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);
		contentPane.add(north, BorderLayout.NORTH);
		
		verifPanel.add(verifLabel, BorderLayout.CENTER);
		verifPanel.add(verifButton, BorderLayout.SOUTH);
		
		bonusValidatePanel.add(bonusValidateLabel, BorderLayout.CENTER);
		bonusValidatePanel.add(bonusValidateButton, BorderLayout.SOUTH);
		
		north.add(pioche);
		north.add(unoButton);
		
		center.add(cardCenterPanel, BorderLayout.CENTER);
		
		cardCenterPanel.add(centerCard);
		}
	
	public void addLog(String log) {
		JLabel jlabel = new JLabel(log);
		textLogPanel.add(jlabel);
		
		updateFrame();
		
		JScrollBar vertical = scrollPaneEast.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum() + 10);
		updateFrame();
	}
	
	public void chargeCard() {
		updateCardCenterGraph();		
		dechargeCard();
		
		pioche.setEnabled(true);
		
		boolean canPlaceAddFour = playerCourant.testAddFour();
		ArrayList<Card> listCard = playerCourant.getCard();
		for (Card card : listCard) {
			ButtonCardGraph button = new ButtonCardGraph(card, playerCourant, this);
			cardPanel.add(button);
			if (card.getValeur() == ValeurCard.addFour && !canPlaceAddFour) {
				button.setEnabled(false);
			}
			else
				button.setEnabled(playerCourant.testCard(card));
		}
		updateFrame();
	}
	
	public void dechargeCard() {
		pioche.setEnabled(false);
		cardPanel.removeAll();
		updateFrame();
	}
	
	public void setPlayer(Player player) {
		playerCourant = player;
	}
	
	public void updateFrame() {
		invalidate();
		validate();
		repaint();
	}
	public void waitVerif() {
		center.removeAll();
		center.add(verifPanel, BorderLayout.CENTER);
		verifLabel.setText("C'est au tour de: " + playerCourant.getName());
		updateFrame();
	}
	
	private void updateCardCenterGraph() {
		updateFrame();
		centerCard.setCard(game.getCenterCard());
		updateFrame();
	}
	
	public void showDialogBonusInfo(String message) {
		game.setWaitBonus(true);
		center.removeAll();
		center.add(bonusValidatePanel, BorderLayout.CENTER);
		bonusValidateLabel.setText(message);
		updateFrame();
	}
	
	public void removeUnoVerif() {
		unoVerif = null;
	}

	public void addUnoVerif(Player playerCourant2) {
		unoVerif = new UnoVerif(playerCourant2, game, this);
	}

}
