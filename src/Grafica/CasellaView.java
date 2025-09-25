package Grafica;

import Componenti.Agente;
import Componenti.Casella;
import Componenti.TerritorioElement;
import Componenti.TipologiaCasella;

import java.awt.*;

public class CasellaView implements TerritorioElementView{

    @Override
    public void drawGraphicObject(TerritorioElement te, Graphics2D g) {
        Casella casella = (Casella) te;
        int cellaSize = PannelloTerritorio.getCasellaSize();
        Rectangle rettangolo_casella = new Rectangle(casella.getPosizione().getColonna()*cellaSize, casella.getPosizione().getRiga()*cellaSize,cellaSize,cellaSize);
        casella.setRectangle(rettangolo_casella);

        g.setColor(Color.white);
        g.fill(rettangolo_casella);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(1));
        g.draw(rettangolo_casella);

        double x = rettangolo_casella.getCenterX();
        double y = rettangolo_casella.getCenterY();

        if(casella.getAgente()!=null){
            Agente agente = casella.getAgente();
            Font font = new Font("Arial", Font.PLAIN, 100);
            if(agente.getCarico()>0){
                g.setColor(Color.GREEN);
            }else{
                g.setColor(Color.BLUE);
            }

            g.setFont(font);

            int textWidth = g.getFontMetrics().stringWidth(".");
            int textHeight = g.getFontMetrics().getHeight();
            int textX = (int) (x - textWidth / 2.0);
            int textY = (int) (y + textHeight / 15.0);
            g.drawString(".", textX, textY);

        }

        int risorse = casella.getnRisorsa();
        if(risorse>0){

            Font font = new Font("Arial", Font.PLAIN, 20);
            if(casella.getTipologiaCasella()== TipologiaCasella.BUCA){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.GREEN);
            }
            g.setFont(font);

            int textWidth = g.getFontMetrics().stringWidth("" + risorse);
            int textHeight = g.getFontMetrics().getHeight();
            int textX = (int) (x - textWidth / 2.0);
            int textY = (int) (y + textHeight / 4.0);
            g.drawString("" + risorse, textX, textY);
        }

    }

}
