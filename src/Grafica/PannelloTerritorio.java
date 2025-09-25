package Grafica;

import Componenti.TerritorioElement;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class PannelloTerritorio extends JPanel implements TerritorioElementListener{
    private final LinkedList<TerritorioElement> elements = new LinkedList<>();
    private static int casellaSize;
    public static int getCasellaSize(){
        return casellaSize;
    }
    public PannelloTerritorio(int dimensioneRighe,int dimensioneColonne){
        setBackground(Color.WHITE);
        casellaSize = 40;
        setPreferredSize(new Dimension(dimensioneColonne*casellaSize, dimensioneRighe*casellaSize));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (TerritorioElement te : elements) {
            TerritorioElementView view = TerritorioElementViewFactory.FACTORY.createView(te);
            view.drawGraphicObject(te, g2);
        }
    }
    public void add(TerritorioElement go) {
        elements.add(go);
    }
    @Override
    public void changed() {
        repaint();
    }
}
