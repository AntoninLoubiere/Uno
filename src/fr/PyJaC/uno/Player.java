package fr.PyJaC.uno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.PyJaC.uno.enumeration.ColorCard;
import fr.PyJaC.uno.enumeration.PlayerType;
import fr.PyJaC.uno.enumeration.ValeurCard;

public class Player {

	
	private ArrayList<Card> listCard = new ArrayList<>();
	private PlayerType playerType;
	private String name;
	private Game game;
	private String messageAction;
	private Random random = new Random();
	private boolean wait = true;

	Player(PlayerType playerType, String name, Game game) {
		this.playerType = playerType;
		this.name = name;
		this.game = game;
	}
	
	public void runTurn(){
		if  (playerType == PlayerType.HUMAN) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			game.winWaitVerif();
			
			wait = true;
			
			while (wait) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			game.addLog(getName() + " " + messageAction + "\n");
			
			if (listCard.size() == 0) {
				game.win(getName(), this);
			}
			
		} else if (playerType == PlayerType.CPU) {
			
			// CPU turn
			
			Card cardChoose = null;
			for (Card card : listCard) {
				if (card.getValeur() == ValeurCard.piçkTwo) {
					if (game.testCard(card)) {
						cardChoose = card;
						break;
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() == ValeurCard.skip || card.getValeur() == ValeurCard.reverse) {
						if (game.testCard(card)) {
							cardChoose = card;
							break;
						}
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() != ValeurCard.changeColor && card.getValeur() != ValeurCard.pickFour) {
						if (game.testCard(card)) {
							cardChoose = card;
							break;
						}
					}
				}
			}
			
			if (cardChoose == null) {// aucune carte trouver dans la boucle precedente
				for (Card card : listCard) {
					if (card.getValeur() == ValeurCard.changeColor) {
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
					if (card.getValeur() == ValeurCard.pickFour) {
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
			
			game.addLog(getName() + " " + messageAction + "\n");
			
			if (listCard.size() == 1) {
				int chance = random.nextInt(22);
				if (chance != 20) {
					game.addLog(" ");
					game.addLog("" + getName() + " a dit UNO !");
					game.addLog(" ");
					game.winChangeListPlayer(this, "(UNO)");
				}
				else {
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
			if (game.getNumberHumanPlayer() > 1 && playerType == PlayerType.HUMAN) // if there are multiple player on one pc
				game.winDechargeCard();
			else if (playerType == PlayerType.HUMAN)
				game.winDisableCard();
			wait = false;
			testChangeListPseudo();
		}
	}

	public void piocheCard() {
		Card newCard = game.takeCard();
		addCard(newCard);
		messageAction = "a pioché une carte";
		if (game.getNumberHumanPlayer() > 1 && playerType == PlayerType.HUMAN) { // if there are multiple player on one pc
			game.winDechargeCard();
		} else if (playerType == PlayerType.HUMAN) {
			game.winDisableCard();
		}
		wait = false;
		testChangeListPseudo();
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
			if (card.getValeur() == ValeurCard.changeColor || card.getValeur() == ValeurCard.pickFour) {
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
			if (card.getValeur() != ValeurCard.pickFour && game.testCard(card))
				return false;
		}
		return true;
	}
	
	public boolean testCard(Card card) {
		return game.testCard(card);
	}
	
	public void testChangeListPseudo() {
		if (listCard.size() > 1) {
			game.winChangeListPlayer(this, " ");
		}
	}
}
