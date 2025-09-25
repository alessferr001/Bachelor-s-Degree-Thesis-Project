package Util;

import Componenti.Casella;
import Componenti.Posizione;
import Componenti.Territorio;
import Componenti.TipologiaCasella;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Configurazione {

    public static final boolean visualizzaGrafica = false;
    public static final boolean tempoEsecuzione = true;
    public static final String percorsoFileTempoEsecuzione = "C:\\Users\\Alessandro\\Desktop\\Università\\tesi\\Sperimentazioni\\FileTempiSimulazioni.csv";
    public static CSVWriter writer;
    public static final String idCorrente = generaID();
    public static final int numeroEsecutori = 8;
    public static final int numeroAgenti = 100_000;
    public static final int dimensioneTerritorioRighe = 1000;
    public static final int dimensioneTerritorioColonne = 1000;
    public static final int nSorgenti = 100_000;
    public static final int nBuche = 100_000;
    public static final int risorse=100_000_000;
    public static final int raggioAzione=1;
    public static final int raggioVisibilita=1;
    public static final int numeroEtichette = getNEtichette(raggioAzione);
    public static final double percentualeCompletamento = 0.5;
    public static String istanteInizio,istanteFine,durata,durataSecondi,conservazione;


    public static void scriviRisultatiFile(){
        try{


/*           //Scrittura header
            String[] header = {"ID", "numeroEsecutori", "numeroAgenti", "dimensioneTerritorioRighe", "dimensioneTerritorioColonne",
                                "nSorgenti", "nBuche", "risorse", "raggioAzione", "raggioVisibilita", "numeroEtichette",
                                "visualizzaGrafica", "tempoEsecuzione", "percentualeCompletamento","istanteInizio","istanteFine","durata","durataSecondi","Conservazione"};

            writer.writeNext(header);*/


            String[] configurazione = {idCorrente,Integer.toString(numeroEsecutori), Integer.toString(numeroAgenti),
                    Integer.toString(dimensioneTerritorioRighe), Integer.toString(dimensioneTerritorioColonne),
                    Integer.toString(nSorgenti), Integer.toString(nBuche), Integer.toString(risorse),
                    Integer.toString(raggioAzione), Integer.toString(raggioVisibilita),
                    Integer.toString(numeroEtichette), Boolean.toString(visualizzaGrafica),
                    Boolean.toString(tempoEsecuzione),Double.toString(percentualeCompletamento),istanteInizio,istanteFine,durata,durataSecondi,conservazione};

            writer.writeNext(configurazione);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("Errore scrittura file");
        }
    }

    public static void scriviRisultatiConsole(){
        System.out.println("Durata totale: "+durata);
    }


    public static String generaID() {

        String parteUUID = UUID.randomUUID().toString().split("-")[0];

        Random random = new Random();
        int valoreCasuale = random.nextInt(10000);

        return parteUUID + "_" + valoreCasuale;
    }

    public static String formattaIstanteDiTempo(LocalDateTime istanteDiTempo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return istanteDiTempo.format(formatter);
    }

    public static int getNEtichette(int raggioAzione){
        int nEtichette=0;
        for(int i=1; i<=raggioAzione; i++){
            nEtichette+=8*i;
        }
        return nEtichette+1;
    }

    public static Map<Posizione, TipologiaCasella> assegnaSorgentiEBuche(int dimensioneTerritorioRighe, int dimensioneTerritorioColonne, int nSorgenti, int nBuche) {

        Random random = new Random();
        Map<Posizione,TipologiaCasella> sorgentiEbuche=new HashMap<>();

        int sorgentiAssegnate=0;
        while(sorgentiAssegnate<nSorgenti){
            int posRiga = random.nextInt(dimensioneTerritorioRighe);
            int posColonna = random.nextInt(dimensioneTerritorioColonne);
            Posizione posizioneSorgente = new Posizione(posRiga,posColonna);
            if(!sorgentiEbuche.containsKey(posizioneSorgente)){
                sorgentiEbuche.put(posizioneSorgente,TipologiaCasella.RISORSA);
                sorgentiAssegnate++;
            }
        }

        int bucheAssegnate=0;
        while(bucheAssegnate<nBuche){
            int posRiga = random.nextInt(dimensioneTerritorioRighe);
            int posColonna = random.nextInt(dimensioneTerritorioColonne);
            Posizione posizioneBuca = new Posizione(posRiga,posColonna);
            if(!sorgentiEbuche.containsKey(posizioneBuca)){
                sorgentiEbuche.put(posizioneBuca,TipologiaCasella.BUCA);
                bucheAssegnate++;
            }
        }

        return sorgentiEbuche;

    }

    public static void distribuisciCarico(List<Casella> sorgentiObuche, int risorseIniziali) {

        Random random = new Random();
        int n = sorgentiObuche.size();

        int caricoSingolo = risorseIniziali/n;
        for (Casella casella : sorgentiObuche) {
            casella.incrementaRisorsa(caricoSingolo);
        }

        int resto = risorseIniziali-(caricoSingolo*n);
        Casella casella = sorgentiObuche.get(random.nextInt(sorgentiObuche.size()));
        casella.incrementaRisorsa(resto);

    }

    public static void settaSorgentiEBuche(Territorio territorio, Map<Posizione,TipologiaCasella> sorgentiEbuche, GeneratoreCFN generatoreCFN, int raggioAzione) {

        for(Posizione p : sorgentiEbuche.keySet()){
            Casella casella = new Casella(p, generatoreCFN.getCFN(p.getColonna(), p.getRiga(), raggioAzione), sorgentiEbuche.get(p));
            territorio.setCasella(casella);
        }

    }

    public static boolean isCasellaSorgente(Posizione posizioneCasella, Map<Posizione,TipologiaCasella> sorgentiEbuche) {
        return sorgentiEbuche.get(posizioneCasella)!=null;
    }

}
