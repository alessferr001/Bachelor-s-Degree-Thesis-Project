package Grafica;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;

public class FinestraPrincipale extends JFrame{

    public FinestraPrincipale(PannelloTerritorio pannelloTerritorio) {

        setLayout(new BorderLayout());

        add(pannelloTerritorio, BorderLayout.CENTER);
        pack();

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

}