package Simulazione;

import Componenti.Agente;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GestoreAvanzamento {

    private List<ConcurrentLinkedQueue<Agente>> primaBatteria;
    private List<ConcurrentLinkedQueue<Agente>> secondaBatteria;
    private ConcurrentLinkedQueue<Agente> codaCorrente;
    private int contatore;
    private final int numeroPassi;

    public GestoreAvanzamento(List<ConcurrentLinkedQueue<Agente>> primaBatteria, List<ConcurrentLinkedQueue<Agente>> secondaBatteria, int numeroPassi){
        this.primaBatteria=primaBatteria;
        this.secondaBatteria=secondaBatteria;
        this.numeroPassi=numeroPassi;
        codaCorrente=primaBatteria.get(contatore);
    }

    public ConcurrentLinkedQueue<Agente> getCodaCorrente(){
        return codaCorrente;
    }

    public void azioneEffettuata(Agente agente, int cfn){
        secondaBatteria.get(cfn).add(agente);
    }

    public void passoSuccessivo() {
        contatore++;
        contatore = contatore%numeroPassi;
        if(contatore==0) swapBatterie();
        codaCorrente=primaBatteria.get(contatore);
    }

    private void swapBatterie(){
        var temp = primaBatteria;
        primaBatteria = secondaBatteria;
        secondaBatteria=temp;
    }


}
