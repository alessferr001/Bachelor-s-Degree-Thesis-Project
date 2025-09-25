package Componenti;

public class Posizione {

    private final int riga;
    private final int colonna;

    public Posizione(int riga,int colonna){
        this.riga=riga;
        this.colonna=colonna;
    }

    public int getRiga() {
        return riga;
    }

    public int getColonna() {
        return colonna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posizione posizione = (Posizione) o;
        return riga == posizione.riga && colonna == posizione.colonna;
    }

    @Override
    public int hashCode() {
        int hashRiga = Integer.hashCode(riga);
        int hashColonna = Integer.hashCode(colonna);
        return hashRiga ^ hashColonna;
    }

    @Override
    public String toString() {
        return "Componenti.Posizione: <"+riga+">"+",<"+colonna+">";
    }
}
