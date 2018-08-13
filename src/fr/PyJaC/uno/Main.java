package fr.PyJaC.uno;

import fr.PyJaC.uno.fenetre.WindowsPrincipale;

public class Main {

	public static void main(String[] args) {
		WindowsPrincipale win = new WindowsPrincipale();
		Game game = new Game(2, 4, win);
		game.Play();
		Player.scan.close();
		}

}
