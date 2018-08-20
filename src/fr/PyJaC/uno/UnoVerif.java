package fr.PyJaC.uno;

import java.util.Timer;
import java.util.TimerTask;

import fr.PyJaC.uno.fenetre.WindowsPrincipale;

public class UnoVerif extends TimerTask{
	
	private Player playerToVerif;
	private Game game;
	private WindowsPrincipale windowsPrincipale;
	private boolean sayUno = false;
	
	public UnoVerif(Player playerToVerif, Game game, WindowsPrincipale windowsPrincipale) {
		this.playerToVerif = playerToVerif;
		this.game = game;
		this.windowsPrincipale = windowsPrincipale;
		
		Timer timer = new Timer();
		timer.schedule(this, 10000);
	}
	
	public void sayUno() {
		if (playerToVerif.getCard().size() > 1) {
			return; // verif it is in uno
		}
		sayUno = true;
		windowsPrincipale.addLog(" ");
		windowsPrincipale.addLog(playerToVerif.getName() + " a dit UNO !");
		windowsPrincipale.addLog(" ");
		windowsPrincipale.listPseudoInfoChange(playerToVerif, "(UNO)");
	}

	@Override
	public void run() {
		if (!sayUno) {
			if (playerToVerif.getCard().size() > 1) {
				return; // verif it is in uno
			}
			windowsPrincipale.addLog(" ");
			windowsPrincipale.addLog(playerToVerif.getName() + " a oublier de dire Uno, il pioche 2 cartes");
			windowsPrincipale.addLog(" ");
			playerToVerif.addCard(game.takeCard());
			playerToVerif.addCard(game.takeCard());
			windowsPrincipale.removeUnoVerif();
			windowsPrincipale.listPseudoInfoChange(playerToVerif, "");


		}
	}
	
}
