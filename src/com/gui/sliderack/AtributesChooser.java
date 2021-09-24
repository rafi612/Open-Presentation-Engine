package com.gui.sliderack;

import javax.swing.JDialog;

public class AtributesChooser extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	RackElement rackelement;
	
	public AtributesChooser(RackElement rackelement)
	{
		this.rackelement = rackelement;
	}
}
