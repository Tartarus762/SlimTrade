package com.zrmiller.slimtrade;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.zrmiller.slimtrade.datatypes.WindowType;

public class GlobalMouseListener implements NativeMouseInputListener{

//	private WindowType lastWindow = null;
	private String lastWindow;
	
	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {		
		byte[] windowText = new byte[512];
		PointerType hwnd = User32.INSTANCE.GetForegroundWindow();
		User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
		String curWindow = Native.toString(windowText);
		if(lastWindow != null && !lastWindow.equals(curWindow) && curWindow.equals("Path of Exile")){
			FrameManager.forceAllToTop();
			System.out.println("REFRESH");
		}
		lastWindow = curWindow;
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {	
		
	}
	
	private WindowType getWindowType(String win){
		if(win.equals("Path of Exile")){
			return WindowType.POE;
		}else if(win.contains("SlimTrade")){
			return WindowType.SLIMTRADE;
		}else{
			return WindowType.OTHER;
		}
		
	}


}

