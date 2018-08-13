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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import fr.PyJaC.uno.Card;
import fr.PyJaC.uno.Game;
import fr.PyJaC.uno.Player;
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
	
	private JScrollPane scrollPaneEast = new JScrollPane(textLogPanel);
	private JScrollPane south = new JScrollPane(cardPanel);
	
	private JButton pioche = new JButton("Piocher");
	private JButton unoButton = new JButton("UNO !");
	private JButton verifButton = new JButton("Valider");
	
	private JLabel verifLabel = new JLabel("C'est au tour de: ");
	
	private Player playerCourant;
	
	private JSplitPane JSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center, scrollPaneEast);
	
	private ButtonCardGraph centerCard = new ButtonCardGraph();

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
		
		pioche.setEnabled(false);
		pioche.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playerCourant.piocheCard();
			}
		});
		
		verifLabel.setPreferredSize(new Dimension(50, 5));
		
		verifButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				center.removeAll();
				center.add(cardCenterPanel);
				updateFrame();
				center.setSize(verifPanel.getSize());
				chargeCard();
			}
		});
		
		centerCard.setEnabled(false);
	}

	private void createJPanel() {
		this.setContentPane(contentPane);
		
		// layout
		
		verifPanel.setLayout(new BorderLayout());
		contentPane.setLayout(new BorderLayout());
		textLogPanel.setLayout(new BoxLayout(textLogPanel, BoxLayout.Y_AXIS));
		
		south.setPreferredSize(new Dimension(10, 130));
		center.setPreferredSize(new Dimension(500, 300));
		verifPanel.setPreferredSize(new Dimension(500, 300));
		
		// add
		contentPane.add(JSP, BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);
		contentPane.add(north, BorderLayout.NORTH);
		
		verifPanel.add(verifLabel, BorderLayout.CENTER);
		verifPanel.add(verifButton, BorderLayout.SOUTH);
		
		north.add(pioche);
		north.add(unoButton);
		
		center.add(cardCenterPanel);
		
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
			ButtonCardGraph button = new ButtonCardGraph(card, playerCourant);
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
		center.add(verifPanel);
		verifLabel.setText("C'est au tour de: " + playerCourant.getName());
		updateFrame();
	}
	
	private void updateCardCenterGraph() {
		updateFrame();
		centerCard.setCard(game.getCenterCard());
		updateFrame();
	}
	
	public void showDialogBonusInfo(String message) {
		JOptionPane.showMessageDialog(this,
				message,
				"Information - Bonus utilisée",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
