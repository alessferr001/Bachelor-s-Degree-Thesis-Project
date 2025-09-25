package Componenti;

import java.util.*;

public class Agente{

    private final int id;
    private final int raggioAzione;
    private final int raggioVisibilita;
    private final Territorio territorio;
    private final Direzione[] direzioni = Direzione.values();
    private final Random random = new Random();
    private Posizione posizioneCorrente;
    private StatoAgenteIF statoCorrente;
    private Casella obiettivo;
    private final int numeroPassi;
    private int contatore;
    private Direzione direzionePrioritaria;
    private int caricoCorrente;

    public Agente(int id, Territorio territorio, int raggioAzione, int raggioVisibilita, Posizione posizioneIniziale){
        this.id=id;
        this.territorio = territorio;
        this.raggioAzione = raggioAzione;
        this.raggioVisibilita=raggioVisibilita;
        this.posizioneCorrente=posizioneIniziale;
        this.statoCorrente=StatoAgente.CERCA_RISORSA;
        this.caricoCorrente=0;
        this.numeroPassi=Math.max(territorio.getRighe(),territorio.getColonne())/4;
    }

    private Posizione scegliDestinazione() {
        List<Casella> vicinatoRaggioAzione = territorio.getVicinato(posizioneCorrente, raggioAzione);
        if (!vicinatoRaggioAzione.isEmpty()) {
            int probabilita = random.nextInt(3);
            if (probabilita == 0) {//33% possibilità di scegliere una direzione random
                Posizione posizioneRandom = vicinatoRaggioAzione.get(random.nextInt(vicinatoRaggioAzione.size())).getPosizione();
                if(posizioneValida(posizioneRandom)) return posizioneRandom;
            }else {
                int passi = random.nextInt(raggioAzione) + 1;
                Posizione destinazionePrioritaria = direzionePrioritaria.getNewPosition(posizioneCorrente, passi);
                if (posizioneValida(destinazionePrioritaria)) return destinazionePrioritaria;
            }
        }
        return posizioneCorrente;
    }

    private boolean posizioneValida(Posizione destinazione) {

        if(!territorio.posizioneOk(destinazione)) return false;
        Casella casella = territorio.getCasellaAtPosition(destinazione);
        return casella.getTipologiaCasella()== TipologiaCasella.BASE && casella.getAgente()==null;

    }

    private Casella cercaObiettivo(List<Casella> vicinatoRV, TipologiaCasella tipologiaCasella) {
        List<Casella> risorseInVicinato = new ArrayList<>();

        for(Casella casella : vicinatoRV){
            if(casella.getTipologiaCasella().equals(tipologiaCasella) && casella.getnRisorsa()>0) risorseInVicinato.add(casella);
        }

        //scelgo una casella random tra quelle nella lista se esiste altrimenti ritorno null
        if (!risorseInVicinato.isEmpty()) return risorseInVicinato.get(random.nextInt(risorseInVicinato.size()));

        return null;
    }

    private boolean obiettivoValido(Casella obiettivo) {
        return obiettivo.getnRisorsa()>0;
    }

    private boolean obiettivoPresenteIn(List<Casella> vicinatoRA) {
        return vicinatoRA.contains(obiettivo);
    }

    private Posizione getPosizionePiuVicina(List<Casella> vicinatoRA, Casella obiettivo) {

        //considero ad ogni passo il vicinato dell'obiettivo, ad ogni iterazione aumento il raggio,
        //la prima volta che ottengo una casella che attorno all'obiettivo e nel mio raggio d'azione ci vado

        int raggio=1;//potrei partire già dal raggio>raggio d'azione agente
        while(raggio<=raggioVisibilita){
            List<Casella> vicinatoObiettivoR=territorio.getVicinato(obiettivo.getPosizione(),raggio);
            List<Casella> matched = getCaselleComuniTraVicinati(vicinatoObiettivoR,vicinatoRA);
            if(!matched.isEmpty()){
                for(Casella casellaMatchata: matched){
                    if(posizioneValida(casellaMatchata.getPosizione())) return casellaMatchata.getPosizione();
                }
            }
            raggio++;
        }
        //mi conviene spostarmi casualmente, evito di stare fermo per non causare blocchi
        return scegliDestinazione();
    }

    private List<Casella> getCaselleComuniTraVicinati(List<Casella> vicinato1, List<Casella> vicinato2) {

        List<Casella> caselleMatchate = new ArrayList<>();
        for(Casella casella : vicinato1){
            if(vicinato2.contains(casella)) caselleMatchate.add(casella);
        }
        return caselleMatchate;
    }

    private void cambiaPrioritaDirezione() {
        Direzione direzione = direzioni[random.nextInt(direzioni.length)];
        if(direzione!=direzionePrioritaria) direzionePrioritaria=direzione;
    }

    public int[] eseguiOperazioni(){
        if(contatore==0) cambiaPrioritaDirezione();
        contatore++;
        contatore=contatore%numeroPassi;
        return statoCorrente.eseguiOperazione(this);
    }

    public int getCarico() {
        return caricoCorrente;
    }


    private interface StatoAgenteIF{
        int[] eseguiOperazione(Agente agente);
    }

    private enum StatoAgente implements StatoAgenteIF{

        CERCA_RISORSA{
            @Override
            public int[] eseguiOperazione(Agente agente) {
                int scelta = agente.random.nextInt(2);//50% si mette a cercare la risorsa
                if(scelta == 0){//cerca la risorsa
                    List<Casella> vicinatoRV = agente.territorio.getVicinato(agente.posizioneCorrente,agente.raggioVisibilita);
                    Casella casellaObiettivo = agente.cercaObiettivo(vicinatoRV, TipologiaCasella.RISORSA);
                    if(casellaObiettivo!=null){//esiste una sorgente
                        agente.obiettivo=casellaObiettivo;
                        agente.statoCorrente=RAGGIUNGI_RISORSA;
                    }
                }

                else{//si muove casualmente o resta fermo
                    Posizione newPosizione = agente.scegliDestinazione();
                    agente.territorio.spostaAgente(agente, newPosizione, agente.posizioneCorrente);
                    agente.posizioneCorrente = newPosizione;
                }
                return new int[]{agente.territorio.getCasellaAtPosition(agente.posizioneCorrente).getCfn(),0};
            }
        },
        RAGGIUNGI_RISORSA{
            @Override
            public int[] eseguiOperazione(Agente agente) {
                //Sono sicuro che l'obiettivo è != null e che è valido, in questo stato posso arrivare solo da CERCA_RISORSA
                //e prima di arrivare qui ho cercato un nuovo obiettivo valido.
                if(agente.obiettivoValido(agente.obiettivo)) {

                    List<Casella> vicinatoRA = agente.territorio.getVicinato(agente.posizioneCorrente, agente.raggioAzione);
                    //ho le caselle direttamente raggiungibili

                    //se l'obiettivo è nel mio raggio d'azione prelevo N risorse e cambio stato
                    //non esiste possibilità di race condition sulla risorsa, sono l'unico con il mio cfn a poter
                    //toccare la risorsa
                    if(agente.obiettivoPresenteIn(vicinatoRA)){
                        int caricoScelto = 1;////prendo un numero N di risorse
                        while(caricoScelto>agente.obiettivo.getnRisorsa()) caricoScelto--;

                        agente.caricoCorrente = caricoScelto;
                        agente.obiettivo.decrementaRisorsa(caricoScelto);

                        agente.obiettivo=null;
                        agente.statoCorrente=CERCA_BUCA;
                    }

                    else{//altrimenti tra le caselle direttamente raggiungibili mi sposto verso quella più vicino all'obiettivo
                        Posizione newPosizione = agente.getPosizionePiuVicina(vicinatoRA,agente.obiettivo);
                        agente.territorio.spostaAgente(agente, newPosizione, agente.posizioneCorrente);
                        agente.posizioneCorrente = newPosizione;
                    }

                }
                else{
                    agente.obiettivo=null;
                    agente.statoCorrente=CERCA_RISORSA;
                }
                return new int[]{agente.territorio.getCasellaAtPosition(agente.posizioneCorrente).getCfn(),0};
            }
        },
        CERCA_BUCA{
            @Override
            public int[] eseguiOperazione(Agente agente) {
                List<Casella> vicinatoRV = agente.territorio.getVicinato(agente.posizioneCorrente,agente.raggioVisibilita);
                    Casella casellaObiettivo = agente.cercaObiettivo(vicinatoRV, TipologiaCasella.BUCA);
                    if(casellaObiettivo!=null){//esiste una buca nel vicinatoRV
                        agente.obiettivo=casellaObiettivo;
                        agente.statoCorrente=RAGGIUNGI_BUCA;
                    }
                    else{
                        Posizione newPosizione = agente.scegliDestinazione();
                        agente.territorio.spostaAgente(agente, newPosizione, agente.posizioneCorrente);
                        agente.posizioneCorrente = newPosizione;
                    }
                return new int[]{agente.territorio.getCasellaAtPosition(agente.posizioneCorrente).getCfn(),0};
            }
        },
        RAGGIUNGI_BUCA{
            @Override
            public int[] eseguiOperazione(Agente agente) {
                int depositoEffettuato=0;
                if(agente.obiettivoValido(agente.obiettivo)) {

                    List<Casella> vicinatoRA = agente.territorio.getVicinato(agente.posizioneCorrente, agente.raggioAzione);
                    //ho le caselle direttamente raggiungibili

                    //se l'obiettivo è nel mio raggio d'azione cerco di depositare N risorse e cambio stato
                    if(agente.obiettivoPresenteIn(vicinatoRA)){

                        int numeroDepositi = agente.caricoCorrente;//cerco di depositare tutto
                        while(numeroDepositi>agente.obiettivo.getnRisorsa()) numeroDepositi--;

                        depositoEffettuato=numeroDepositi;

                        agente.caricoCorrente -= numeroDepositi;
                        agente.obiettivo.decrementaRisorsa(numeroDepositi);
                        agente.obiettivo=null;
                        if(agente.caricoCorrente==0) agente.statoCorrente=CERCA_RISORSA;
                        else agente.statoCorrente=CERCA_BUCA;
                    }

                    else{//altrimenti tra le caselle direttamente raggiungibili mi sposto verso quella più vicino all'obiettivo
                        Posizione newPosizione = agente.getPosizionePiuVicina(vicinatoRA,agente.obiettivo);
                        agente.territorio.spostaAgente(agente, newPosizione, agente.posizioneCorrente);
                        agente.posizioneCorrente = newPosizione;
                    }

                }
                else{
                    agente.obiettivo=null;
                    agente.statoCorrente=CERCA_BUCA;
                }
                return new int[]{agente.territorio.getCasellaAtPosition(agente.posizioneCorrente).getCfn(),depositoEffettuato};
            }
        }

    }


}
