package fr.PyJaC.uno;

import fr.PyJaC.uno.enumeration.ColorCard;
import fr.PyJaC.uno.enumeration.ValeurCard;

public class Card {
	
	private ColorCard color;
	private ValeurCard valeur;
	
	public Card(ColorCard color, ValeurCard valeur) {
		this.color = color;
		this.valeur = valeur;
	}
	
	public ColorCard getColor() {
		return color;
	}
	
	public ValeurCard getValeur() {
		return valeur;
	}
	
	public void setColor(ColorCard color) {
		this.color = color;
	}
	
	public String toString() {
		return color.getColorString() + valeur.getValeurString();
	}

	public boolean testWithOther(Card card) {
		if (card.getColor() == getColor() || card.getValeur() == getValeur())
			return true;
		else if (card.getValeur() == Game.valeurJocker[0] || card.getValeur() == Game.valeurJocker[1])
			return true;
		return false;
	}
}
