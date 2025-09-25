package Grafica;

import Componenti.Territorio;
import Componenti.TerritorioElement;

import java.awt.*;

public interface TerritorioElementView {
    void drawGraphicObject(TerritorioElement te, Graphics2D g);
}
