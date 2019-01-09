package com.zrmiller.slimtrade.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import com.zrmiller.slimtrade.ColorManager;
import com.zrmiller.slimtrade.windows.StashGridOverlay;

public class StashHelperContainer extends BasicDialog{
	//TODO : Should probably get spacing X directly from stash overlay left buffer
	private static final long serialVersionUID = 1L;
	public static int height = StashHelper.height;
	private int offsetY = 10;
	private int spacingX = 5;
	
	public StashHelperContainer(){
		this.setBackground(ColorManager.CLEAR);
		this.setBounds(0, 0, height, height);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, spacingX, 0));
	}
	
	public void updateBounds(){
		int posX = StashGridOverlay.getGridPos().x-spacingX;
		int posY = StashGridOverlay.getWinPos().y-height-offsetY;
		int width = StashGridOverlay.getGridSize().width+spacingX;
		this.setBounds(posX, posY, width, height);
	}

//	public void updateBounds(int posX, int posY, int width){
//		int x = posX-spacingX;
//		int y = posY-height-offsetY;
//		int w = width+spacingX;
//		this.setBounds(x, y, w, height);
//	}
	
	public void updateCellSize(int cellWidth, int cellHeight){
		
	}
	
	public void refresh(){
//		this.getContentPane().revalidate();
//		this.getContentPane().repaint();
		this.revalidate();
		this.repaint();
	}
	
}
