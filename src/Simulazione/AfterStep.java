package Simulazione;


import Componenti.Territorio;
import Grafica.AbstractTerritorioSubject;
import Util.Configurazione;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static Util.Configurazione.*;

public class AfterStep extends AbstractTerritorioSubject implements Runnable{

    private final GestoreAvanzamento gestoreAvanzamento;
    private final List<AgentManager> esecutori;
    private final LocalDateTime istanteInizio;
    private int risorseTotaliDepositate;

    private final Territorio territorio;


    public AfterStep(GestoreAvanzamento gestoreAvanzamento,List<AgentManager> esecutori,LocalDateTime istanteInizio,Territorio territorio){
        this.gestoreAvanzamento=gestoreAvanzamento;
        this.istanteInizio=istanteInizio;
        this.esecutori=esecutori;
        this.territorio = territorio;
    }

    @Override
    public void run() {
        try {

            for(AgentManager manager: esecutori){
                risorseTotaliDepositate += manager.getCorrenteCaricoDepositato();
            }

            boolean soglia = ((double) risorseTotaliDepositate / Configurazione.risorse) >= Configurazione.percentualeCompletamento;

            if(soglia) gestisciTerminazione();

            if(Configurazione.visualizzaGrafica) {
                notifyListeners();
                Thread.sleep(2);
            }

            gestoreAvanzamento.passoSuccessivo();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void gestisciTerminazione() {
        LocalDateTime istanteFine = LocalDateTime.now();
        Configurazione.istanteFine = Configurazione.formattaIstanteDiTempo(LocalDateTime.now());
        Configurazione.durata = Duration.between(istanteInizio,istanteFine).toString();
        Configurazione.durataSecondi =  Duration.between(istanteInizio,istanteFine).getSeconds()+"";

        int risorseRimanenti = territorio.getRisorseRimanenti();
        int sommaRisorse = risorseTotaliDepositate+risorseRimanenti;

        System.out.println("secondi: "+durataSecondi);

        Configurazione.conservazione = Boolean.toString(sommaRisorse==Configurazione.risorse);

        System.out.println("conservazione = "+conservazione);

        if(Configurazione.tempoEsecuzione) Configurazione.scriviRisultatiFile();
        Configurazione.scriviRisultatiConsole();

        System.exit(0);
    }

}
