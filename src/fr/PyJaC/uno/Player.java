package fr.PyJaC.uno;

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
	private boolean wait = true;

	Player(PlayerType playerType, String name, Game game, WindowsPrincipale windowPrincipale) {
		this.playerType = playerType;
		this.name = name;
		this.game = game;
		this.windowsPrincipale = windowPrincipale;
	}
	
	public void runTurn(){
		if  (playerType == PlayerType.HUMAN) {
			
			windowsPrincipale.waitVerif();
			
			wait = true;
			
			while (wait) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			windowsPrincipale.addLog(getName() + " " + messageAction + "\n");
			
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
			
			windowsPrincipale.addLog(getName() + " " + messageAction + "\n");
			
			if (listCard.size() == 1) {
				int chance = random.nextInt(22);
				if (chance != 20) {
					windowsPrincipale.addLog(" ");
					windowsPrincipale.addLog("" + getName() + " a dit UNO !");
					windowsPrincipale.addLog(" ");
				}
				else {
					windowsPrincipale.addLog(getName() + " a oublier de dire UNO, il pioche 2 cartes !\n");
					listCard.add(game.takeCard());
					listCard.add(game.takeCard());
				}
				
			}
			
			if (listCard.size() == 0) {
				game.win(getName(), this);
			}
			
			
		}
	}
	
	public void poseCard(Card card) {
		if (game.testCard(card)) {
			listCard.remove(card);
			game.changeCenterCard(card);
			messageAction = "a posé la carte " + card.toString();
			game.lastCard = card;
			windowsPrincipale.dechargeCard();
			wait = false;
		}
	}

	public void piocheCard() {
		Card newCard = game.takeCard();
		addCard(newCard);
		messageAction = "a pioché une carte";
		windowsPrincipale.dechargeCard();
		wait = false;

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
	
	public boolean testAddFour() {
		for (Card card : listCard) {
			if (card.getValeur() != ValeurCard.addFour && game.testCard(card))
				return false;
		}
		return true;
	}
	
	public boolean testCard(Card card) {
		return game.testCard(card);
	}
}