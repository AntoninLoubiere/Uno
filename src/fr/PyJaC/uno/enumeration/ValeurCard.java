package fr.PyJaC.uno.enumeration;

public enum ValeurCard {
	one("1", "res/1.png"), two("2", "res/2.png"), three("3", "res/3.png"), four("4", "res/4.png"), five("5", "res/5.png"), six("6", "res/6.png"), seven("7", "res/7.png"), height("8", "res/8.png"), nine("9", "res/9.png"), addTwo("+2", "res/addtwo.png")
	, pass("P", "res/P.png"), reverse("I", "res/I.png"), addFour("+4", "res/addfour.png"), multiColor("J", "res/J.png");
	
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
