package Componenti;

import java.awt.*;

public class Casella implements TerritorioElement{

    private int cfn;
    private Agente agente;
    private int nRisorsa;
    private TipologiaCasella tipologiaCasella;
    private final Posizione posizione;
    private Rectangle rettangoloCasella;


    public Casella(Posizione posizione, int cfn, TipologiaCasella tipologiaCasella){
        this.posizione=posizione;
        this.cfn=cfn;
        this.tipologiaCasella=tipologiaCasella;
    }

    public void setCFN(int cfn) {
        this.cfn=cfn;
    }

    public void setRectangle(Rectangle rettangoloCasella) {
        this.rettangoloCasella=rettangoloCasella;
    }

    public TipologiaCasella getTipologiaCasella(){
        return tipologiaCasella;
    }
    public Posizione getPosizione() {
        return posizione;
    }
    public int getCfn() {
        return cfn;
    }
    public Agente getAgente() {
        return agente;
    }
    public void setAgente(Agente agente) {
        this.agente = agente;
    }
    public int getnRisorsa() {
        return nRisorsa;
    }
    public void incrementaRisorsa(int caricoScelto) {
        nRisorsa+=caricoScelto;
    }
    public void decrementaRisorsa(int caricoScelto) {
        nRisorsa-=caricoScelto;
        if(nRisorsa == 0) tipologiaCasella=TipologiaCasella.BASE;
    }


    public String toString(){
        return ""+cfn;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Casella casella = (Casella) o;
        return this.posizione.equals(casella.posizione);
    }

}
