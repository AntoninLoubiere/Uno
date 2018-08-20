package fr.PyJaC.uno.enumeration;

import java.awt.Color;

public enum ColorCard {
	
	Red("R",Color.red, "red_"),Green("V", Color.green, "green_"),Blue("B", Color.cyan, "blue_"),Yellow("J", Color.yellow, "yellow_");
	
	private String colorString;
	private Color color;
	private String pathColor;
	
	private ColorCard(String colorString, Color color, String pathColor) {
		this.colorString = colorString;
		this.color = color;
		this.pathColor = pathColor;
	}
	
	public String getColorString() {
		return colorString;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getPathColor() {
		return pathColor;
	}
}
