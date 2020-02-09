package com.slimtrade.core.observing.improved;

import java.util.ArrayList;

import com.slimtrade.enums.ColorTheme;
import com.slimtrade.core.managers.ColorManager;
import com.slimtrade.gui.options.ISaveable;

public class EventManager {

    private ArrayList<ISaveable> saveListenerList = new ArrayList<>();
    private ArrayList<IColorable> colorListenerList = new ArrayList<>();

    public void addColorListener(IColorable listener) {
        if (colorListenerList.contains(listener)) {
            return;
        }
//        int i = colorListenerList.size();
        colorListenerList.add(listener);
//        System.out.print("IColor");
//        if (i == colorListenerList.size()) {
//            System.out.print("!");
//        }
//        System.out.println("++(" + colorListenerList.size() + "):" + listener);
    }

    public void removeColorListener(IColorable listener) {
        colorListenerList.remove(listener);
    }

    public void addSaveListener(ISaveable listener) {
        saveListenerList.add(listener);
    }

    // Probably won't be used as ISaveable objects shouldn't be discarded before the program ends
    public void removeSaveListener(ISaveable listener) {
        saveListenerList.add(listener);
    }

    public void saveAll() {
        for(ISaveable s : saveListenerList) {
            s.save();
        }
    }

    public void loadAll() {
        for(ISaveable s : saveListenerList) {
            s.load();
        }
    }

    public void updateAllColors(ColorTheme theme) {
        System.out.println("Updaing All Colors");
        ColorManager.setTheme(theme);
        for (IColorable l : colorListenerList) {
            l.updateColor();
        }
    }

}