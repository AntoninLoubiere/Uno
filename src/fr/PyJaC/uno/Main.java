package fr.PyJaC.uno;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import fr.PyJaC.uno.fenetre.WindowsPrincipale;

public class Main {
	
	private static int numberPlayer;
	private static int numberCPU;

	public static void main(String[] args) {
		WindowsPrincipale win = new WindowsPrincipale();
		// DIALOG NUMBER PLAYER
		showNumberPlayerDialog(win);
				
		Game game = new Game(numberPlayer, numberCPU, win);
		win.setGame(game);
		game.Play();
		Player.scan.close();
		}
	
	private static void showNumberPlayerDialog(WindowsPrincipale win) {
		// def panel
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(5, 2));
		
		// def widget
		
		SpinnerModel spinerModelPlayer =
				new SpinnerNumberModel(1, //initial value
						0, //min
						10, //max
						1);
		
		SpinnerModel spinerModelCPU =
				new SpinnerNumberModel(1, //initial value
						0, //min
						10000, //max
						1);
		
		JSpinner spinerPlayer = new JSpinner(spinerModelPlayer);
		JSpinner spinerCPU = new JSpinner(spinerModelCPU);
		
		JLabel labelPlayer = new JLabel("Nombre de joueur");
		JLabel labelCPU = new JLabel("Nombre d'ordinateur");
		JLabel labelError = new JLabel("");
		
		// add
		
		contentPane.add(labelPlayer);
		contentPane.add((Component) spinerPlayer);
		contentPane.add(labelCPU);
		contentPane.add((Component) spinerCPU);
		
		// show dialog
		String[] options = {"OK"};
		while (true) {
			int i = JOptionPane.showOptionDialog(win, contentPane, "Nombre de Joueur", JOptionPane.NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
			
			if (i == 0) {
				if ((int) spinerPlayer.getValue() + (int) spinerCPU.getValue() <= 2) {
					labelError.setText("Le nombre de joueur est trop peu important");
				}
				else
					break;
			}
		}
		
		numberPlayer = (int) spinerPlayer.getValue();
		numberCPU = (int) spinerCPU.getValue();
	}

}
