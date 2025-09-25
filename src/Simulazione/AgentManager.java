package Simulazione;

import Componenti.Agente;

import java.util.concurrent.*;

public class AgentManager implements Runnable{

    private final CyclicBarrier barriera;
    private final GestoreAvanzamento gestoreAvanzamento;
    private int correnteCaricoDepositato;

    public AgentManager(CyclicBarrier barriera, GestoreAvanzamento gestoreAvanzamento){
        this.gestoreAvanzamento=gestoreAvanzamento;
        this.barriera=barriera;
    }

    public int getCorrenteCaricoDepositato(){
        return this.correnteCaricoDepositato;
    }

    @Override
    public void run() {

        while (true){
            correnteCaricoDepositato=0;
            ConcurrentLinkedQueue<Agente> codaCorrente = gestoreAvanzamento.getCodaCorrente();
            boolean more=true;
            //int i=0;
            while(more){
                Agente agente = codaCorrente.poll();
                if(agente!=null) {
                    try {
                        //i++;
                        int[] result = agente.eseguiOperazioni();
                        int newCfn = result[0];
                        correnteCaricoDepositato += result[1];
                        gestoreAvanzamento.azioneEffettuata(agente, newCfn);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else more = false;
            }

            //System.out.println(Thread.currentThread().getName()+" "+i);

            try {
                barriera.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("Errore");
            }
        }
    }

}
