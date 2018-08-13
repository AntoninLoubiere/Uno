package fr.PyJaC.uno.enumeration;

import java.awt.Color;

public enum ColorCard {
	
	Red("R",Color.red),Green("V", Color.green),Blue("B", Color.cyan),Yellow("J", Color.yellow);
	
	private String colorString;
	private Color color;
	
	private ColorCard(String colorString, Color color) {
		this.colorString = colorString;
		this.color = color;
	}
	
	public String getColorString() {
		return colorString;
	}
	
	public Color getColor() {
		return color;
	}
}
