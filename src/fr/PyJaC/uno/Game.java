package fr.PyJaC.uno;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.PyJaC.uno.enumeration.ColorCard;
import fr.PyJaC.uno.enumeration.PlayerType;
import fr.PyJaC.uno.enumeration.ValeurCard;
import fr.PyJaC.uno.fenetre.WindowsPrincipale;

public class Game {

	private ArrayList<Player> listPlayer = new ArrayList<Player>();
	private Card centerCard;
	private ArrayList<Card> listCard = new ArrayList<Card>();
	public static ValeurCard[] valeurJocker = {ValeurCard.multiColor, ValeurCard.addFour};
	private int numberPlayerHuman;
	private int numberPlayerCPU;
	private boolean partyInProgress = true;
	public Card lastCard = null;
	private ArrayList<String> listPseudo = new ArrayList<>();
	private ArrayList<String> listPseudoOrdi;
	private ArrayList<Player> listWinner = new ArrayList<>();
	private int playerIdinProgress = 0;
	private WindowsPrincipale windowPrincipale;
	private boolean waitBonus = false;
			
	public Game(int numberPlayerHuman, int numberPlayerCPU, WindowsPrincipale windowsPrincipale) {
		this.windowPrincipale = windowsPrincipale;
		// init listCard
		
		int numberPackageCard = (numberPlayerHuman + numberPlayerCPU) / 6 + 1;
		if ((numberPlayerHuman + numberPlayerCPU) % 6 == 0)
			numberPackageCard--;
		for (int x = 0; x < numberPackageCard; x++) {
			for (ColorCard color : ColorCard.values()) {
				for (ValeurCard valeur : ValeurCard.values()) {
					if (valeur != valeurJocker[0] && valeur != valeurJocker[1]) {
						listCard.add(new Card(color, valeur));
						listCard.add(new Card(color, valeur));
					} else {
						listCard.add(new Card(color, valeur));
					}
					
				}
			}
		}
		Collections.shuffle(listCard);
		
		this.numberPlayerHuman = numberPlayerHuman;
		this.numberPlayerCPU = numberPlayerCPU;
		
	
	}
	
	
	public void Play() {

		listPseudoOrdi = createListOrdiPseudo();
		
		centerCard = takeCard();
		
		// add Player
		for (int x = 0; x < numberPlayerHuman; x++) {
			String[] options = {"OK"};
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			JLabel lblError = new JLabel("");
			JLabel lbl = new JLabel("Entrez votre pseudo: ");
			panel.add(lbl);
			JTextField txt = new JTextField(10);
			panel.add(txt);
			
			panel.add(lblError);
			while (true) {
				int selectedOption = JOptionPane.showOptionDialog(windowPrincipale, panel, "Choix Pseudo", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
	
				if(selectedOption == 0)
				{
					String text = txt.getText();
					if (text.length() > 3) {
						if (listPseudo.contains(text))
							lblError.setText("Le nom existe déja");
						else {
							listPlayer.add(new Player(PlayerType.HUMAN, text, this, windowPrincipale));
							listPseudo.add(text);
							break;
						}
					}
					else {
						lblError.setText("Le nom est trop petit");
					}
				}
				
			}
		}
		
		for (int x = 0; x < numberPlayerCPU; x++) {
			if (listPseudoOrdi.size() == 0 || listPseudo.contains(listPseudoOrdi.get(0)))
				listPlayer.add(new Player(PlayerType.CPU, "Ordinateur" + String.valueOf(x+ 1), this, windowPrincipale));
			else {
				listPlayer.add(new Player(PlayerType.CPU, listPseudoOrdi.get(0), this, windowPrincipale));
				listPseudoOrdi.remove(0);
			}
		}
		
		// distribuer les cartes
		for (Player player : listPlayer) {
			for(int x = 0; x < 7; x++)
				player.addCard(takeCard());
		}
		
		Collections.shuffle(listPlayer);
		
		Player playerBefore = null;
		while (partyInProgress) {
			
			if (playerIdinProgress >= listPlayer.size()) {
				playerIdinProgress = 0;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.pass) {
				windowPrincipale.addLog("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " passe son tour\n");
				if (listPlayer.get(playerIdinProgress).getPlayerType() == PlayerType.HUMAN) {
					windowPrincipale.showDialogBonusInfo("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " passe son tour\n");
	
				}
				playerIdinProgress++;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.reverse && playerBefore != null) {
				windowPrincipale.addLog("Le joueur " + playerBefore.getName() + " a inversée le sens de jeu");
				Collections.reverse(listPlayer);
				for (playerIdinProgress = 0 ;playerIdinProgress < listPlayer.size(); playerIdinProgress++) {
					if (listPlayer.get(playerIdinProgress) == playerBefore)
						break;
				}
				playerIdinProgress++;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.addTwo && playerBefore != null) {
				windowPrincipale.addLog("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " pioche 2 cartes et passe son tour\n");
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				if (listPlayer.get(playerIdinProgress).getPlayerType() == PlayerType.HUMAN) {
					windowPrincipale.showDialogBonusInfo("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " pioche 2 cartes et passe son tour\n");
				}
				playerIdinProgress++;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.addFour && playerBefore != null) {
				windowPrincipale.addLog("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " pioche 4 cartes et passe son tour\n");
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				if (listPlayer.get(playerIdinProgress).getPlayerType() == PlayerType.HUMAN) {
					windowPrincipale.showDialogBonusInfo("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " pioche 4 cartes et passe son tour\n");
				}
				playerIdinProgress++;
			}
			
			while (waitBonus) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (playerIdinProgress >= listPlayer.size()) {
				playerIdinProgress = 0;
			}
			lastCard = null;
			Player playerCourant = listPlayer.get(playerIdinProgress);
			windowPrincipale.setPlayer(playerCourant);
			System.out.println("C'est au tour de: " + playerCourant.getName());
			
			playerCourant.runTurn();
			
			if (listPlayer.size() == 1) {
				partyInProgress  = false;
			}
			
			playerBefore = playerCourant;
			
			playerIdinProgress++;
		}
		
		listWinner.add(listPlayer.get(0));
		
		windowPrincipale.addLog(" ");
		windowPrincipale.addLog("\nFin de la partie, classement: ");
		for (int x = 0; x < listWinner.size(); x++) {
			if (listWinner.get(x).getPlayerType() == PlayerType.HUMAN)
				windowPrincipale.addLog(String.valueOf(x + 1) + ". " + listWinner.get(x).getName() + " (Humain)");
			else
				windowPrincipale.addLog(String.valueOf(x + 1) + ". " + listWinner.get(x).getName() + " (IA)");

		}
		
	}
	
	public Card takeCard() {
		Card card = listCard.get(0);
		listCard.remove(card);
		return card;
	}
	
	public void poseCard(Card card) {
		listCard.add(card);
	}
	
	public void changeCenterCard(Card card) {
		poseCard(centerCard);
		centerCard = card;
	}
	
	public Card getCenterCard() {
		return centerCard;
	}


	public boolean testCard(Card card) {
		return centerCard.testWithOther(card);
	}


	public int getNumberHumanPlayer() {
		return numberPlayerHuman;
	}


	public void win(String name, Player player) {
		windowPrincipale.addLog(" ");
		windowPrincipale.addLog("\n\n" + name + " a terminé !\n");
		windowPrincipale.addLog(" ");
		listWinner.add(player);
		listPlayer.remove(player);
		playerIdinProgress--;
	}
	
	private ArrayList<String> createListOrdiPseudo() {
		
		ArrayList<String> array = new ArrayList<>();
		
		array.add("Ordi-naire");
		array.add("Calculator");
		array.add("Patrick Calcul");
		array.add("I.A");
		array.add("P.C");
		array.add("Isabelle Ananas");
		array.add("IDE");
		array.add("Eclipse");
		array.add("I'm programmed by Antonin");
		array.add("I'm a program");
		array.add("Joueur Rapide");
		
		Collections.shuffle(array);
		
		return array;
	}
	
	public void setWaitBonus(boolean bool) {
		waitBonus = bool;
	}

}

	