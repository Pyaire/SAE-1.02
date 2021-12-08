class Main extends Program {

    int capital = 0;
    double[] capital_per_second = new double[]{5, 0, 0};

    void algorithm() {

        do {
            int initialTemps = (int) (getTime() / 1000);
            int choix = choisir();

            int ecartTemps = (int) ((getTime()/1000)-initialTemps);
            println("Temps d'interaction de " + ecartTemps + " secondes");
            for (int i=0; i< length(capital_per_second); i++){
                capital += (capital_per_second[i] * ecartTemps);
            }
            println("Votre capital actuel est de : " + capital + "€");
        } while(capital>0);
    }

    boolean strToIntPossible(String action){
        boolean fin = true;
        int retour = 0;     //mais pas de retour car la fonction et un boolean !
        int i = 0;
        while (fin && i<length(action)) {
            if (charAt(action, i) < '0' || charAt(action, i) > '9') {
                fin = false;
            }
            i++;
        }
        return fin;
    }

    int strToInt(String action) {
        int retour = 0;
        if(strToIntPossible(action)){
            for (int i = 0; i<length(action); i++){
                retour += (int) (charAt(action, i) - '0') * pow(10, (length(action) - i));
            }
        }
        return retour;
    }

    int choisir() {
        boolean estValide = false;
        String choix;
        int choix_final;
        do {
            clearScreen();
            println("1: Récupérer l'argent");
            println("2: Améliorer la production");
            println("3: Construire");

            choix = readString(5000);
            estValide = strToIntPossible(choix);
            if (!estValide) {
                println("Il semblerait que vous n'ayez pas entré un nombre ! Veuillez réessayer !");
            }
        } while (!estValide);
        choix_final = strToInt(choix);
        return choix_final;
    }

    void testEstValide(){
        assertTrue(strToIntPossible("674265"));
        assertFalse(strToIntPossible("a5642"));
        assertFalse(strToIntPossible("395678687G"));
        assertFalse(strToIntPossible(" 6"));
        assertFalse(strToIntPossible(""));
    }

    void teststrToInt(){
        assertEquals(674265,strToInt("674265"));
        assertEquals(5642,strToInt("5642"));
        assertEquals(395678687,strToInt("395678687"));
        assertEquals(5395,strToInt("5395"));
        assertEquals(32245,strToInt("32245"));
        assertNotEquals(10,strToInt("38"));
    }
    
}