package Util;

import java.util.ArrayList;
import java.util.Collections;

public class GeneratoreCFN {

    private ArrayList<Integer> etichette;

    public GeneratoreCFN(int n){
        etichette = new ArrayList<>(n);
        for(int i=0; i<n; i++){
            etichette.add(i);
        }
    }

    public int getCFN(int x,int y,int r){

        int coeffX = x % ((2*r)+1);
        int coeffY = (y % ((2*r)+1)) * ((2*r)+1);

        return etichette.get(coeffX+coeffY);
    }

    public void shuffle(){
        Collections.shuffle(etichette);
    }

}
