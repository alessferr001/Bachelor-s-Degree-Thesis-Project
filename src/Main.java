import Componenti.*;
import Grafica.CasellaView;
import Grafica.FinestraPrincipale;
import Grafica.PannelloTerritorio;
import Grafica.TerritorioElementViewFactory;
import Simulazione.AfterStep;
import Simulazione.AgentManager;
import Simulazione.GestoreAvanzamento;
import Util.Configurazione;
import Util.GeneratoreCFN;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Util.Configurazione.*;

public class Main {

    public static void main(String[] args) throws IOException {


        try {
            Random random = new Random();

            GeneratoreCFN generatoreCFN = new GeneratoreCFN(numeroEtichette);
            Territorio territorio =new Territorio(dimensioneTerritorioRighe,dimensioneTerritorioColonne);


            Map<Posizione,TipologiaCasella> sorgentiEbuche = Configurazione.assegnaSorgentiEBuche(dimensioneTerritorioRighe,dimensioneTerritorioColonne,nSorgenti,nBuche);
            Configurazione.settaSorgentiEBuche(territorio,sorgentiEbuche,generatoreCFN,raggioAzione);

            System.out.println("Sorgenti e Buche Assegnate");

            for(int i=0; i<dimensioneTerritorioRighe; i++){
                for(int j=0; j<dimensioneTerritorioColonne; j++){
                    Posizione posizioneCasella = new Posizione(i,j);
                    if(!Configurazione.isCasellaSorgente(posizioneCasella,sorgentiEbuche)) {
                        Casella casella = new Casella(posizioneCasella, generatoreCFN.getCFN(j, i, raggioAzione), TipologiaCasella.BASE);
                        territorio.setCasella(casella);
                    }
                }
            }

            System.out.println("Caselle Base assegnate");

            List<Casella> sorgenti = territorio.getCaselleByTipologia(TipologiaCasella.RISORSA);
            List<Casella> buche = territorio.getCaselleByTipologia(TipologiaCasella.BUCA);

            int sorgentiEBuche=sorgenti.size()+buche.size();
            System.out.println("sorgenti + buche = "+sorgentiEBuche);

            Configurazione.distribuisciCarico(sorgenti,risorse);
            Configurazione.distribuisciCarico(buche,risorse);

            System.out.println("Carico distribuito");

            sorgenti.addAll(buche);
            List<Casella> sorgentiAndBuche = sorgenti;
            territorio.setSorgentiAndBuche(sorgentiAndBuche);

            System.out.println("Territorio Costruito");

            //Costruzione Simulazione.GestoreAvanzamento
            List<ConcurrentLinkedQueue<Agente>> primaBatteria = new ArrayList<>();
            List<ConcurrentLinkedQueue<Agente>> secondaBatteria = new ArrayList<>();
            for(int i=0; i<numeroEtichette; i++){
                primaBatteria.add(new ConcurrentLinkedQueue<>());
                secondaBatteria.add(new ConcurrentLinkedQueue<>());
            }



            GestoreAvanzamento gestoreAvanzamento = new GestoreAvanzamento(primaBatteria,secondaBatteria,numeroEtichette);


            //Costruzione agenti
            for(int i=0; i<numeroAgenti; i++){
                boolean posizioneValida=false;
                while(!posizioneValida){
                    Posizione posizioneIniziale = new Posizione(random.nextInt(dimensioneTerritorioRighe),random.nextInt(dimensioneTerritorioColonne));
                    Casella casellaIniziale = territorio.getCasellaAtPosition(posizioneIniziale);
                    if(casellaIniziale.getTipologiaCasella()==TipologiaCasella.BASE && casellaIniziale.getAgente()==null){
                        Agente agente = new Agente(i,territorio,raggioAzione,raggioVisibilita,posizioneIniziale);
                        casellaIniziale.setAgente(agente);
                        primaBatteria.get(casellaIniziale.getCfn()).add(agente);
                        posizioneValida=true;
                    }
                }
            }

            System.out.println("Agenti Posizionati");

            PannelloTerritorio pannelloTerritorio = new PannelloTerritorio(dimensioneTerritorioRighe,dimensioneTerritorioColonne);

            for(int i=0; i<dimensioneTerritorioRighe; i++){
                for(int j=0; j<dimensioneTerritorioColonne; j++){
                    pannelloTerritorio.add(territorio.getTerritorio()[i][j]);
                }
            }

            LocalDateTime istanteInizio=LocalDateTime.now();

            if(tempoEsecuzione) {
                Configurazione.writer = new CSVWriter(new FileWriter(percorsoFileTempoEsecuzione,true));
                Configurazione.istanteInizio = formattaIstanteDiTempo(istanteInizio);
            }

            List<AgentManager> esecutori=new ArrayList<>();
            AfterStep afterStep = new AfterStep(gestoreAvanzamento,esecutori,istanteInizio,territorio);
            CyclicBarrier barriera = new CyclicBarrier(numeroEsecutori,afterStep);

            System.out.println("Start");

            ExecutorService executorService = Executors.newFixedThreadPool(numeroEsecutori);
            for (int i=0; i<numeroEsecutori; i++){
                AgentManager manager = new AgentManager(barriera,gestoreAvanzamento);
                esecutori.add(manager);
                executorService.submit(manager);
            }


            if(Configurazione.visualizzaGrafica) {
                TerritorioElementViewFactory.FACTORY.installView(Casella.class, new CasellaView());
                afterStep.addObjectListener(pannelloTerritorio);
                FinestraPrincipale finestraPrincipale = new FinestraPrincipale(pannelloTerritorio);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}