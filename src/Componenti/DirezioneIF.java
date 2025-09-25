package Componenti;

import Componenti.Posizione;

public interface DirezioneIF {

    Posizione getNewPosition(Posizione oldPosition, int passi);

}
