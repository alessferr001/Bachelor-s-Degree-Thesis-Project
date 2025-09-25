package Componenti;

import Componenti.Posizione;

public enum Direzione implements DirezioneIF {

    NORD_OVEST{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga()-passi, oldPosition.getColonna()-passi);
        }
    },
    NORD{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga()-passi, oldPosition.getColonna());
        }
    },
    NORD_EST{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga()-passi, oldPosition.getColonna()+passi);
        }
    },
    EST{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga(), oldPosition.getColonna()+passi);
        }
    },
    OVEST{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga(), oldPosition.getColonna()-passi);
        }
    },
    SUD_OVEST{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga()+passi, oldPosition.getColonna()-passi);
        }
    },
    SUD{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga()+passi, oldPosition.getColonna());
        }
    },
    SUD_EST{
        @Override
        public Posizione getNewPosition(Posizione oldPosition,int passi) {
            return new Posizione(oldPosition.getRiga()+passi, oldPosition.getColonna()+passi);
        }
    },
}
