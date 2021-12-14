class Main extends Program {

    // /!\ à faire : tout mettre dans un csv paramètres /!\

        double capital = 25;
        double prixMagasin = 1;
        Magasin[] magasins = new Magasin[3];
        int nbMagasins = 0;
        int maxMagasins = 2;
        final String DEVISE = "euros";

        String nomPatron;
        int tpsImpots = 0;
        int ecartImpot = 20;  // en seconde
        double taxes = 0;

    // -----

    void algorithm() {
        introduction();
        do {
            int initialTemps = (int) (getTime() / 1000);
            int choix = choisir();
            int ecartTemps = (int) ((getTime()/1000)-initialTemps);
            tpsImpots += ecartTemps;
            if (tpsImpots >= ecartImpot){
                capital = (capital - (capital*0.3));
                println("Vous avez payé " + (capital*0.3) + " " + DEVISE + " d'impôts !");
                delay(2500);
                tpsImpots = 0;
            }
            if (choix == 2) {
                achatMagasin();       // Achat de magasin
            } else if (choix == 3) {
                // Amélioration
                menuAmelioration();
            } else {
                // Récupérer l'argent
                for (int i=0; i<nbMagasins; i++){
                    capital += (magasins[i].revenu * ecartTemps);
                }
            }
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
                int power = (int) (pow(10, (length(action) - 1 - i)));
                retour += (int) (charAt(action, i) - '0') * power;
            }
        }
        return retour ;
    }

    int choisir() {
        boolean estValide = false;
        String choix;
        int choix_final;
        do {
            clearScreen();
            cursor(1, 1);
            println("Votre captial est de " + capital + " " + DEVISE);
            println("Vous produisez " + totalParSeconde() + " " + DEVISE + " par seconde.");
            println("1: Récupérer l'argent");
            println("2: Acheter un nouveau magasin");
            println("3: Améliorer la production");
            println("4: Construire");

            choix = readString(5000);
            estValide = strToIntPossible(choix);
            if (!estValide) {
                println("Il semblerait que vous n'ayez pas entré un nombre ! Veuillez réessayer !");
            }
        } while (!estValide);
        choix_final = strToInt(choix);
        return choix_final;
    }

    Magasin nouveauMagasin(String nom) {
        Magasin nouveau = new Magasin();
        nouveau.nom = nom;
        nouveau.revenu = 5;
        nouveau.prixAmelioration = 50;
        nbMagasins++;
        return nouveau;
    }

    void menuAmelioration() {
        println("Quel magasin souhaitez vous améliorer ?");
        for (int i_magasin=0; i_magasin<nbMagasins; i_magasin++) {
            println((i_magasin+1) + " : " + magasins[i_magasin].nom);
        }
        delay(2000);
    }

    double totalParSeconde() {
        double total = 0;
        for (int i_magasin=0; i_magasin<nbMagasins; i_magasin++) {
            total += magasins[i_magasin].revenu;
        }
        return total;
    }

    void achatMagasin() {
        println("Voulez vous acheter un nouveau magasin pour " + prixMagasin + " " + DEVISE + " ?");
        print("Oui / Non : ");
        String reponse = toLowerCase(readString());
        println(reponse);
        if (equals(reponse, "oui")) {
            if (nbMagasins < maxMagasins) {
                println("Bravo ! Vous venez d'acheter un nouveau magasin ! Comment voulez vous l'appeller ?");
                Magasin nouveau = nouveauMagasin(readString());
                magasins[(nbMagasins-1)] = nouveau;
                println("Tout est bon, votre magasin produira " + magasins[(nbMagasins-1)].revenu + " " + DEVISE + " par seconde !");
            } else {
                println("Mince ! Vous avez acheté tous les magasins possibles !");
            }
            delay(2000);
        }
    }

    void introduction() {
        clearScreen();
        cursor(1, 1);
        int delay = 1500;
        String nomEmploye = "Pyaire";
        println(nomEmploye + " : Bonjour ! Bienvenue dans votre usine patron !");
        delay(delay);
        println(nomEmploye + " : Tiens, on dirait qu'il n'y a pas beaucoup d'argent dans votre porte feuilles...");
        delay(delay);
        println("*Vous ouvrez votre porte feuille et constatez qu'il n'y a que " + capital + " " + DEVISE + "*");
        delay(delay*2);
        println(nomEmploye + " : On va arranger tout ça, comment je peux vous appeller ?");
        nomPatron = readString();
        println(nomEmploye + " : Très bien " + nomPatron + ", on y va !");
        delay(delay);
    }

    //##################################################################     TESTS     #######################################################################//

    void testEstValide(){
        assertTrue(strToIntPossible("674265"));
        assertFalse(strToIntPossible("a5642"));
        assertFalse(strToIntPossible("395678687G"));
        assertFalse(strToIntPossible(" 6"));
        assertTrue(strToIntPossible(""));
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