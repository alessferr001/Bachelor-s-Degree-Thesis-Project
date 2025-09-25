package Componenti;

import java.util.*;

public class Territorio {

    private final Casella[][] territorio;
    private final int righe;
    private final int colonne;
    private List<Casella> sorgentiEbuche;

    public Territorio(int dimensioneRighe,int dimensioneColonne){
        territorio = new Casella[dimensioneRighe][dimensioneColonne];
        this.righe=dimensioneRighe;
        this.colonne=dimensioneColonne;
    }

    public int getRighe() {
        return righe;
    }

    public int getColonne() {
        return colonne;
    }

    public Casella[][] getTerritorio() {
        return territorio;
    }

    public void setCasella(Casella casella){
        territorio[casella.getPosizione().getRiga()][casella.getPosizione().getColonna()]=casella;
    }

    public void setSorgentiAndBuche(List<Casella> sorgentiAndBuche) {
        this.sorgentiEbuche = sorgentiAndBuche;
    }

    public List<Casella> getSorgentiAndBuche(){ return sorgentiEbuche;}

    public Casella getCasellaAtPosition(Posizione posizione) {
        return territorio[posizione.getRiga()][posizione.getColonna()];
    }

    public void spostaAgente(Agente agente, Posizione posizioneNuova, Posizione posizioneVecchia){
        Casella vecchiaCasella = territorio[posizioneVecchia.getRiga()][posizioneVecchia.getColonna()];
        vecchiaCasella.setAgente(null);
        Casella nuovaCasella = territorio[posizioneNuova.getRiga()][posizioneNuova.getColonna()];
        nuovaCasella.setAgente(agente);
    }

    public boolean posizioneOk(Posizione posizione) {

        //controllo se è all'interno del territorio
        //controllo sulla colonna
        if(posizione.getColonna()<0  || posizione.getColonna() >= territorio[0].length) return false;
        //controllo sulla riga
        return posizione.getRiga() >= 0 && posizione.getRiga() < territorio.length;
    }

    public List<Casella> getVicinato(Posizione posizionePartenza, int raggio){
        List<Casella> vicinato = new ArrayList<>();
        int nIterazioni = 1;
        Posizione posizioneCorrente = posizionePartenza;

        while (nIterazioni<=raggio){
            posizioneCorrente = Direzione.NORD_OVEST.getNewPosition(posizioneCorrente,1);
            int iterazioniLato = nIterazioni*2;
            vicinato.addAll(eseguiQuadrato(posizioneCorrente,iterazioniLato));
            nIterazioni++;

        }
        return vicinato;
    }

    private Collection<? extends Casella> eseguiQuadrato(Posizione posizionePartenza, int iterazioniLato) {
        List<Casella> caselle = new ArrayList<>();
        Posizione posizioneCorrente = posizionePartenza;

        Direzione[] ordineDirezioni = {Direzione.SUD,Direzione.EST,Direzione.NORD,Direzione.OVEST};
        int nLati=0;

        while(nLati<4){
            Direzione direzioneCorrente = ordineDirezioni[nLati];

            int i=0;
            while(i<iterazioniLato){
                posizioneCorrente=direzioneCorrente.getNewPosition(posizioneCorrente,1);
                if(posizioneOk(posizioneCorrente)) caselle.add(getCasellaAtPosition(posizioneCorrente));
                i++;
            }
            nLati++;

        }
        return caselle;

    }

    public List<Casella> getCaselleByTipologia(TipologiaCasella tipologiaCasella) {

        List<Casella> caselle = new ArrayList<>();
        for(int i=0; i<righe; i++){
            for(int j=0; j<colonne; j++){
                if(territorio[i][j].getTipologiaCasella()==tipologiaCasella) caselle.add(territorio[i][j]);
            }
        }
        return caselle;
    }

    public int getRisorseRimanenti() {
        int risorse = 0;
        for(int i=0; i<righe; i++){
            for(int j=0; j<colonne; j++){
                if(territorio[i][j].getTipologiaCasella()==TipologiaCasella.RISORSA) risorse+=territorio[i][j].getnRisorsa();
                if(territorio[i][j].getAgente()!=null) {
                    Agente agente = territorio[i][j].getAgente();
                    risorse+=agente.getCarico();
                }
            }
        }
        return risorse;
    }

}
