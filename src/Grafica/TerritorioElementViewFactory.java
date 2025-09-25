package Grafica;

import Componenti.TerritorioElement;

import java.util.HashMap;
import java.util.Map;

public enum TerritorioElementViewFactory {
    FACTORY;
    private final Map<Class<? extends TerritorioElement>, TerritorioElementView> viewMap = new HashMap<>();
    TerritorioElementView createView(TerritorioElement se) {
        return viewMap.get(se.getClass());
    }
    public void installView(Class<? extends TerritorioElement> clazz, TerritorioElementView view) {
        viewMap.put(clazz, view);
    }
}
