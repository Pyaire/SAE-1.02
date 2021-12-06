class Main extends Program {

    int capital = 0;
    double[] capital_per_second = new double[]{5, 0, 0};

    void algorithm() {

        do {
            int choix = choisir();

            int initialTemps = (int) (getTime() / 1000);
            int ecartTemps = (int) ((getTime()/1000)-initialTemps);
            println("Temps d'interaction de " + ecartTemps + " secondes");
            for (int i=0; i< length(capital_per_second); i++){
                capital += (capital_per_second[i] * ecartTemps);
            }
            println("Votre capital actuel est de : " + capital + "€");
        } while(capital>0);
    }

    boolean estNombre(String action) {
        boolean estValide = true;
        if (length(action) == 0) {
            estValide = false;
        }
        int idx = 0;
        while (estValide && idx < length(action)) {
            if (chartAt(action, idx) < '0' || charAt(action, idx) > '9') {
                estValide = false;
            }
            idx++;
        }

        return estValide;
    }

    int choisir() {
        boolean estValide = false;
        String choix;
        do {
            println("1: Récupérer l'argent");
            println("2: Améliorer la production");
            println("3: Construire");

            choix = readString();
            if (estNombre(choix)) {
                // Valide
            } else {
                println("Entrée invalide. Veuillez réessayer.");
            }
        } while (!estValide);
    }
    
}