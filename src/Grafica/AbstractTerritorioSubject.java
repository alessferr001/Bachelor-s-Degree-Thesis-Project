package Grafica;

import Grafica.TerritorioElementListener;
import Grafica.TerritorioSubject;

import java.util.LinkedList;

public abstract class AbstractTerritorioSubject implements TerritorioSubject {
    private LinkedList<TerritorioElementListener> listeners = new LinkedList<>();
    @Override
    public void addObjectListener(TerritorioElementListener tel) {
        if (listeners.contains(tel))
            return;
        listeners.add(tel);
    }
    public void notifyListeners() {
        for (TerritorioElementListener tel : listeners)
            tel.changed();
    }
}
