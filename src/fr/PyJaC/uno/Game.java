package fr.PyJaC.uno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
			
	public Game(int numberPlayerHuman, int numberPlayerCPU, WindowsPrincipale windowsPrincipale) {
		this.windowPrincipale = windowsPrincipale;
		// init listCard
		for (int x = 0; x < (numberPlayerHuman + numberPlayerCPU) / 6 + 1; x++) {
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
		String reponse;
		for (int x = 0; x < numberPlayerHuman; x++) {
			while (true) {
				System.out.println("Choisissez un pseudo");
				reponse  = Player.scan.nextLine();
				if (!listPseudo.contains(reponse)) {
					listPseudo.add(reponse);
					break;
				}
				
				System.out.println("Saisie incorecte");
			}
			listPlayer.add(new Player(PlayerType.HUMAN, reponse, this, windowPrincipale));
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
				System.out.println("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " passe son tour\n");
				if (listPlayer.get(playerIdinProgress).getPlayerType() == PlayerType.HUMAN) {
					System.out.println("Appuyer sur entrer pour valider");
					Player.scan.nextLine();
				}
				playerIdinProgress++;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.reverse && playerBefore != null) {
				System.out.println("Le joueur " + playerBefore.getName() + " a inversée le sens de jeu");
				Collections.reverse(listPlayer);
				for (playerIdinProgress = 0 ;playerIdinProgress < listPlayer.size(); playerIdinProgress++) {
					if (listPlayer.get(playerIdinProgress) == playerBefore)
						break;
				}
				playerIdinProgress++;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.addTwo && playerBefore != null) {
				System.out.println("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " pioche 2 cartes et passe son tour\n");
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				if (listPlayer.get(playerIdinProgress).getPlayerType() == PlayerType.HUMAN) {
					System.out.println("Appuyer sur entrer pour valider");
					Player.scan.nextLine();
				}
				playerIdinProgress++;
			}
			
			if (lastCard != null && lastCard.getValeur() == ValeurCard.addFour && playerBefore != null) {
				System.out.println("Le joueur " + listPlayer.get(playerIdinProgress).getName() + " pioche 4 cartes et passe son tour\n");
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				listPlayer.get(playerIdinProgress).addCard(takeCard());
				if (listPlayer.get(playerIdinProgress).getPlayerType() == PlayerType.HUMAN) {
					System.out.println("Appuyer sur entrer pour valider");
					Player.scan.nextLine();
				}
				playerIdinProgress++;
			}
			
			if (playerIdinProgress >= listPlayer.size()) {
				playerIdinProgress = 0;
			}
			lastCard = null;
			Player playerCourant = listPlayer.get(playerIdinProgress);
			System.out.println("C'est au tour de: " + playerCourant.getName());
			
			try {
				playerCourant.runTurn();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (listPlayer.size() == 1) {
				partyInProgress  = false;
			}
			
			playerBefore = playerCourant;
			
			playerIdinProgress++;
		}
		
		listWinner.add(listPlayer.get(0));
		
		System.out.println("\nFin de la partie, classement: ");
		for (int x = 0; x < listWinner.size(); x++) {
			if (listWinner.get(x).getPlayerType() == PlayerType.HUMAN)
				System.out.println(String.valueOf(x + 1) + ". " + listWinner.get(x).getName() + " (Player)");
			else
				System.out.println(String.valueOf(x + 1) + ". " + listWinner.get(x).getName() + " (IA)");

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
		System.out.println("\n\n" + name + " a terminé !\n");
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

}

	