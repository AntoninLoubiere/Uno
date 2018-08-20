package fr.PyJaC.uno.enumeration;

public enum ValeurCard {
	one("1", "1.png"), two("2", "2.png"), three("3", "3.png"), four("4", "4.png"), five("5", "5.png"), six("6", "6.png"), 
	seven("7", "7.png"), height("8", "8.png"), nine("9", "9.png"), piçkTwo("+2", "picker.png"),
	skip("P", "skip.png"), reverse("I", "reverse.png"), pickFour("+4", "wild_pick_four.png"), changeColor("J", "wild_color_changer.png");
	
	private String valeurString;
	private String pathImage;
	
	private ValeurCard(String valeurString, String pathImage) {
		this.valeurString = valeurString;
		this.pathImage = pathImage;
	}
	
	public String getValeurString() {
		return valeurString;
	}
	
	public String getPathString() {
		return pathImage;
	}
}
