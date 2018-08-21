package fr.PyJaC.uno.fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import fr.PyJaC.uno.Card;
import fr.PyJaC.uno.Game;
import fr.PyJaC.uno.Main;
import fr.PyJaC.uno.Player;
import fr.PyJaC.uno.UnoVerif;
import fr.PyJaC.uno.enumeration.ValeurCard;


public class WindowsPrincipale extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel center = new JPanel();
	private JPanel textLogPanel = new JPanel();
	private JPanel north = new JPanel();
	private JPanel cardPanel = new JPanel();
	private JPanel verifPanel = new JPanel();
	private JPanel cardCenterPanel = new JPanel();
	private JPanel bonusValidatePanel = new JPanel();
	private JPanel south = new JPanel();
	private JPanel southButtonPanel = new JPanel();
	private JPanel listPseudoPanel = new JPanel();
	
	private JScrollPane scrollPaneEast = new JScrollPane(textLogPanel);
	private JScrollPane scrollCard = new JScrollPane(cardPanel);
	private JScrollPane scrollPseudo = new JScrollPane(listPseudoPanel);
	
	private JButton pioche = new JButton("Piocher");
	private JButton unoButton = new JButton("UNO !");
	private JButton verifButton = new JButton("Valider");
	private JButton bonusValidateButton = new JButton("Ok");
	
	private JLabel verifLabel = new JLabel("C'est au tour de: ");
	private JLabel bonusValidateLabel = new JLabel("joueur 1 passe son tour");
	private JLabel labelProgressBar = new JLabel("Avancement de la partie: ");
	
	private JProgressBar progressBar = new JProgressBar();
	
	private Player playerCourant;
	
	private JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center, scrollPaneEast);
	private JSplitPane splitLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPseudo, splitRight);
	
	private ButtonCardGraph centerCard = new ButtonCardGraph();
	
	private UnoVerif unoVerif = null;

	private Game game;
	
	private UnoFrame frame;

	private int progressBarInt = 0;
	private int numberMaxLog = 500;
	
	private ArrayList<JLabel> listLogLabel = new ArrayList<>();
	
	private HashMap<Player, JLabel> listPseudoInfo = new HashMap<>();

	protected boolean stopUpdate;
	private boolean needUpdateScrollBar = false;
	
	private Thread threadUpdateFrame = new Thread(new Runnable() {
		
		@Override
		public void run() {
			setName("updateThread - UNO");
			int numberPerSeconds = 0;
			long time1 = System.currentTimeMillis();
			while (!stopUpdate) {
				updateFrame();
				numberPerSeconds++;
				
				if (time1 + 1000 < System.currentTimeMillis() || numberPerSeconds == 60) {
					numberPerSeconds = 0;
					time1 = System.currentTimeMillis();
				}
				try {
					Thread.sleep(((time1 + 1000) - System.currentTimeMillis()) / (60 - numberPerSeconds));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	
	
	public WindowsPrincipale(Game game, UnoFrame frame) {
		this.game = game;
		this.frame = frame;
		
		createJPanel();
		creatWidget();

		
		progressBar.setMaximum(game.getNumberPlayer() - 1);
		progressBar.setValue(0);
		
		//updateFrame();
		
		frame.showFrame();
		
		startUpdate();
	}
	
	// == Methode Create Widget == //
	
	private void creatWidget() {
		addLog("Bienvenue dans l'application \"UNO\"");
		addLog("Distribution des cartes merci de patienter...");
				
		centerCard.setEnabled(false);
		centerCard.setJockerColorVisibility(true);

		pioche.setEnabled(false);
		
		progressBar.setStringPainted(true);
		
		for (Player player : game.getPlayerList()) {
			listPseudoPanel.add(new JLabel("  " + player.getName()));
			listPseudoInfo.put(player, new JLabel("  7c  "));
			listPseudoPanel.add(listPseudoInfo.get(player));
		}
		
		// action Listener
		
		bonusValidateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				center.removeAll();
				center.add(cardCenterPanel, BorderLayout.CENTER);
				//updateFrame();
				game.setWaitBonus(false);
			}
		});
		
		pioche.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				testUno();
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
				validerVerif();
			}
		});
		
	}

	private void createJPanel() {
		// layout
		
		this.setLayout(new BorderLayout());
		textLogPanel.setLayout(new BoxLayout(textLogPanel, BoxLayout.Y_AXIS));
		center.setLayout(new BorderLayout());
		verifPanel.setLayout(new BorderLayout());
		bonusValidatePanel.setLayout(new BorderLayout());
		south.setLayout(new BorderLayout());
		listPseudoPanel.setLayout(new GridLayout(game.getNumberPlayer(), 2));
		
		scrollCard.setPreferredSize(new Dimension(10, 130));
		center.setPreferredSize(new Dimension(500, 300));
		scrollPseudo.setPreferredSize(new Dimension(300, 300));

		splitRight.resetToPreferredSizes();
		splitLeft.resetToPreferredSizes();
		
		// add
		this.add(splitLeft, BorderLayout.CENTER); // splitRight is in the left
		this.add(south, BorderLayout.SOUTH);
		this.add(north, BorderLayout.NORTH);
		
		verifPanel.add(verifLabel, BorderLayout.CENTER);
		verifPanel.add(verifButton, BorderLayout.SOUTH);
		
		bonusValidatePanel.add(bonusValidateLabel, BorderLayout.CENTER);
		bonusValidatePanel.add(bonusValidateButton, BorderLayout.SOUTH);
		
		//north.add(pioche);
		//north.add(unoButton);
		north.add(labelProgressBar);
		north.add(progressBar);
		
		center.add(cardCenterPanel, BorderLayout.CENTER);
		
		cardCenterPanel.add(centerCard);
		
		south.add(southButtonPanel);
		south.add(scrollCard, BorderLayout.SOUTH);
		
		southButtonPanel.add(pioche);
		southButtonPanel.add(unoButton);
		}
	
	// == Methode Card == //
	
	public void chargeCard() {
		updateCardCenterGraph();		
		cardPanel.removeAll();
		pioche.setEnabled(true);
		
		boolean canPlaceAddFour = playerCourant.testAddFour();
		ArrayList<Card> listCard = playerCourant.getCard();
		for (Card card : listCard) {
			ButtonCardGraph button = new ButtonCardGraph(card, playerCourant, this);
			cardPanel.add(button);
			if (card.getValeur() == ValeurCard.pickFour && !canPlaceAddFour) {
				button.setEnabled(false);
			}
			else
				button.setEnabled(playerCourant.testCard(card));
		}
		//updateFrame();
	}
	
	public void dechargeCard() {
		pioche.setEnabled(false);
		cardPanel.removeAll();
		//updateFrame();
		disableCenterCard(); // the center is not update
	}
	
	// == Ui update & more == //
	
	public void addLog(String log) {
		JLabel jlabel = new JLabel(log);
		textLogPanel.add(jlabel);
		listLogLabel.add(jlabel);
		needUpdateScrollBar = true;
	}
	
	private void updateScrollBarPaneEast() {
		try {
			JScrollBar vertical = scrollPaneEast.getVerticalScrollBar(); // possible error
			vertical.setValue(vertical.getMaximum()); // set the sidebar in the botom
		} catch (NullPointerException e) {
			Main.printExceptionNotExit(e); 
		}
		
		while (listLogLabel.size() > numberMaxLog) {
			textLogPanel.remove(listLogLabel.get(0));
			listLogLabel.remove(0);
		}
	}
	
	public void updateFrame() {
		frame.updateFrame();
		if (needUpdateScrollBar) {
			needUpdateScrollBar = false; // to no repeat update scrollbar
			updateScrollBarPaneEast();
		}
	}
	
	public void setPlayer(Player player) {
		playerCourant = player;
	}
	
	public void waitVerif() {
		if (game.getNumberPlayerHumanNotFinish() > 1) {
			center.removeAll();
			center.add(verifPanel, BorderLayout.CENTER);
			verifLabel.setText("C'est au tour de: " + playerCourant.getName());
			//updateFrame();
		} else
			validerVerif();
	}
	
	private void validerVerif() {
		center.removeAll();
		center.add(cardCenterPanel, BorderLayout.CENTER);
		//updateFrame();
		chargeCard();
		}

	private void updateCardCenterGraph() {
		centerCard.setCard(game.getCenterCard());
	}
	
	public void showDialogBonusInfo(String message) {
		game.setWaitBonus(true);
		center.removeAll();
		center.add(bonusValidatePanel, BorderLayout.CENTER);
		bonusValidateLabel.setText(message);
	}
	
	public void removeUnoVerif() {
		unoVerif = null;
	}

	public void addUnoVerif(Player playerCourant2) {
		unoVerif = new UnoVerif(playerCourant2, game, this);
	}
	
	public void updateProgressBar() {
		progressBarInt++;
		progressBar.setValue(progressBarInt );
	}
	
	public void listPseudoInfoChange(Player player, String message) {
		JLabel infoLabel = listPseudoInfo.get(player);
		if (infoLabel.getText() != message) { // to optimize
			if (!message.contains("Terminé"))
				infoLabel.setText("  " + player.getCard().size() +"c " + message + "  ");
			else
				infoLabel.setText("  " + message + "  ");
		}
	}

	public void disableCard() {
		disableCenterCard(); // the center is not update
		pioche.setEnabled(false);
		cardPanel.removeAll();
		for (Card card : playerCourant.getCard()) {
			ButtonCardGraph button = new ButtonCardGraph(card, playerCourant, this);
			button.setEnabled(false);
			cardPanel.add(button);
			
		}
		//updateFrame();
	}
	
	public void disableCenterCard() {
		centerCard.setCard(null);
	}
	
	public void testUno() {
		if (unoVerif != null) { // If they are one player the program not wait
			unoVerif.cancel();
			unoVerif.run();
		}
	}
	
	public void startUpdate() {
		stopUpdate = false;
		threadUpdateFrame.start();
	}
	
	public void stopUpdate() {
		stopUpdate = true;
	}
	
	// == getter and setter == //
	
	public void setMaxLog(int max) {
		numberMaxLog = max;
	}
	
}
