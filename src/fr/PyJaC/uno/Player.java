package fr.PyJaC.uno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import fr.PyJaC.uno.enumeration.ColorCard;
import fr.PyJaC.uno.enumeration.PlayerType;
import fr.PyJaC.uno.enumeration.ValeurCard;
import fr.PyJaC.uno.fenetre.WindowsPrincipale;

public class Player {

	public static Scanner scan = new Scanner(System.in);

	
	private ArrayList<Card> listCard = new ArrayList<>();
	private PlayerType playerType;
	private String name;
	private Game game;
	private String messageAction;
	private Random random = new Random();
	private WindowsPrincipale windowsPrincipale;

	Player(PlayerType playerType, String name, Game game, WindowsPrincipale windowPrincipale) {
		this.playerType = playerType;
		this.name = name;
		this.game = game;
		this.windowsPrincipale = windowPrincipale;
	}
	
	public void runTurn() throws IOException {
		if  (playerType == PlayerType.HUMAN) {
			
			windowsPrincipale.setPlayer(this);
			
			ArrayList<String> stringListCard = getCardsStringList();
			System.out.println(getName() + " appuyer sur entrée pour continuer");
			scan.nextLine();
			windowsPrincipale.chargeCard();
			
			// boucle input	
			boolean saisieIncorecte = true;
			boolean cardFind;
			do {
				cardFind = false;
				System.out.println("\nVos cartes :");
				System.out.println(String.join(", ", stringListCard));
				System.out.println("La carte de milieu est :");
				System.out.println(game.getCenterCard());
				System.out.println("\nSaisissez une carte ou piochez (p)");
				String reponse = scan.nextLine();
				reponse = reponse.toUpperCase();
				// test Human reponse
				if (reponse.equals("P")) {
					piocheCard();
					saisieIncorecte = false;
				} else if (stringListCard.contains(reponse)){
					// input in in list
					try {
						for (int x = 0; x < stringListCard.size(); x++) {
							if (reponse.equalsIgnoreCase(stringListCard.get(x))) {
								cardFind = true;
								Card cardChoose = listCard.get(x);
								if (game.testCard(cardChoose)) {
									boolean breakBool = false;
									if (cardChoose.getValeur() == ValeurCard.addFour) {
										for (Card card : listCard) {
											if (card.getValeur() != ValeurCard.addFour && game.testCard(card)) {
												System.out.println("Vous pouvez jouer donc vous ne pouvez pas poser de +4");
												breakBool = true;
												break;
											}
										}
									}
									
									if (breakBool) {
										break;
									}
									
									// choose color jocker
									if (cardChoose.getValeur() == ValeurCard.addFour || cardChoose.getValeur() == ValeurCard.multiColor) {
										while (true) {
											breakBool = false;
											System.out.println("\nVous avez choisis un jocker merci de saisir la couleur a selectionner (r, b, v, j)");
											String reponseColor = scan.nextLine();
											reponseColor = reponseColor.toUpperCase();
											switch (reponseColor) {
											case "R":
												cardChoose.setColor(ColorCard.Red);
												breakBool = true;
												break;
												
											case "V":
												cardChoose.setColor(ColorCard.Green);
												breakBool = true;
												break;
												
											case "B":
												cardChoose.setColor(ColorCard.Blue);
												breakBool = true;
												break;
												
											case "J":
												cardChoose.setColor(ColorCard.Yellow);
												breakBool = true;
												break;

											default:
												System.out.println("Erreur de saisie");
												break;
											}
											if (breakBool) {
												poseCard(cardChoose);
												break;
											}
										}
									} else
										poseCard(cardChoose);
									saisieIncorecte = false;
									break;
								} else {
									System.out.println("La carte " + cardChoose.toString() + " ne peut pas etre possé");
								}
							}
						}
					} catch (IndexOutOfBoundsException e) {
						cardFind = false;
						System.out.println("Erreur veuillez ressaisir");
					}
				}
				if (saisieIncorecte && !cardFind) {
					System.out.println("Je n'ai pas compris pouvez vous répétez ?");
				}
			} while (saisieIncorecte);
			
			if (game.getNumberHumanPlayer() >= 2) {
				for (int x = 0; x < 100; x++) {
					System.out.print("\n");
				}
			}
			
			System.out.println(getName() + " " + messageAction + "\n");
			
			if (listCard.size() == 1) {
				int chance = random.nextInt(51);
				if (chance != 50)
						System.out.println(getName() + " a dit UNO !\n");
				else {
					System.out.println(getName() + " a oublier de dire UNO, il pioche 2 cartes !\n");
					listCard.add(game.takeCard());
					listCard.add(game.takeCard());
				}
				
			}
			
			if (listCard.size() == 0) {
				game.win(getName(), this);
			}
			
		} else if (playerType == PlayerType.CPU) {
			
			// CPU turn
			
			Card cardChoose = null;
			for (Card card : listCard) {
				if (card.getValeur() == ValeurCard.addTwo) {
					if (game.testCard(card)) {
						cardChoose = card;
						break;
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() == ValeurCard.pass || card.getValeur() == ValeurCard.reverse) {
						if (game.testCard(card)) {
							cardChoose = card;
							break;
						}
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() != ValeurCard.multiColor && card.getValeur() != ValeurCard.addFour) {
						if (game.testCard(card)) {
							cardChoose = card;
							break;
						}
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() == ValeurCard.multiColor) {
						if (game.testCard(card)) {
							cardChoose = card;
							ArrayList<Integer> countColor = new ArrayList<>(); // Red Green Blue Yellow
							countColor.add(0);
							countColor.add(0);
							countColor.add(0);
							countColor.add(0);
							for (Card card2 : listCard) {
								switch (card2.getColor()) {
								case Red:
									countColor.set(0, countColor.get(0) + 1 );
									break;
									
								case Green:
									countColor.set(1, countColor.get(1) + 1 );
									break;
									
								case Blue:
									countColor.set(2, countColor.get(2) + 1 );
									break;
									
								case Yellow:
									countColor.set(3, countColor.get(3) + 1 );
									break;

								default:
									break;
								}
								
								ArrayList<Integer> countColor2 = countColor;
								Collections.sort(countColor);
								
								for (int x = 0; x < countColor2.size() ; x++) {
									if (countColor.get(0) == countColor2.get(x)) {
										switch (x) {
										case 0:
											cardChoose.setColor(ColorCard.Red);
											break;
											
										case 1:
											cardChoose.setColor(ColorCard.Green);
											break;
											
										case 2:
											cardChoose.setColor(ColorCard.Blue);
											break;
											
										case 3:
											cardChoose.setColor(ColorCard.Yellow);
											break;

										default:
											break;
										}
									}
								}
							}
							break;
						}
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() == ValeurCard.addFour) {
						if (game.testCard(card)) {
							cardChoose = card;
							ArrayList<Integer> countColor = new ArrayList<>(); // Red Green Blue Yellow
							countColor.add(0);
							countColor.add(0);
							countColor.add(0);
							countColor.add(0);
							for (Card card2 : listCard) {
								switch (card2.getColor()) {
								case Red:
									countColor.set(0, countColor.get(0) + 1 );
									break;
									
								case Green:
									countColor.set(1, countColor.get(1) + 1 );
									break;
									
								case Blue:
									countColor.set(2, countColor.get(2) + 1 );
									break;
									
								case Yellow:
									countColor.set(3, countColor.get(3) + 1 );
									break;

								default:
									break;
								}
								
								ArrayList<Integer> countColor2 = countColor;
								Collections.sort(countColor);
								
								for (int x = 0; x < countColor2.size() ; x++) {
									if (countColor.get(0) == countColor2.get(x)) {
										switch (x) {
										case 0:
											cardChoose.setColor(ColorCard.Red);
											break;
											
										case 1:
											cardChoose.setColor(ColorCard.Green);
											break;
											
										case 2:
											cardChoose.setColor(ColorCard.Blue);
											break;
											
										case 3:
											cardChoose.setColor(ColorCard.Yellow);
											break;

										default:
											break;
										}
									}
								}
							}
							break;
						}
					}
				}
			}
			
			if (cardChoose != null) {
				poseCard(cardChoose);
			} else {
				piocheCard();
			}
			
			System.out.println(getName() + " " + messageAction + "\n");
			
			if (listCard.size() == 1) {
				int chance = random.nextInt(51);
				if (chance != 50)
						System.out.println(getName() + " a dit UNO !\n");
				else {
					System.out.println(getName() + " a oublier de dire UNO, il pioche 2 cartes !\n");
					listCard.add(game.takeCard());
					listCard.add(game.takeCard());
				}
				
			}
			
			if (listCard.size() == 0) {
				game.win(getName(), this);
			}
			
			
		}
	}
	
	private void poseCard(Card card) {
		listCard.remove(card);
		game.changeCenterCard(card);
		messageAction = "a posé la carte " + card.toString();
		game.lastCard = card;
		windowsPrincipale.dechargeCard();
	}

	private void piocheCard() {
		Card newCard = game.takeCard();
		addCard(newCard);
		if (playerType == PlayerType.HUMAN)
			System.out.println("Vous avez piochez la carte " + newCard.toString());
		messageAction = "a pioché une carte";
		windowsPrincipale.dechargeCard();

	}

	public void addCard(Card card) {
		listCard.add(card);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getCardsStringList() {
		ArrayList<String> cardsString = new ArrayList<>();
		
		for (Card card : listCard) {
			if (card.getValeur() == ValeurCard.multiColor || card.getValeur() == ValeurCard.addFour) {
				cardsString.add(card.getValeur().getValeurString());
			} else
				cardsString.add(card.toString());
		}
		
		return cardsString;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public ArrayList<Card> getCard() {
		return this.listCard;
	}
}
