class Main extends Program {

    // /!\ à faire : tout mettre dans un csv paramètres /!\

        double capital = 50;
        double prixJouet = 25;
        int nbJouets = 0;
        int maxJouets = 2;
        Jouet[] jouets = new Jouet[maxJouets];
        final String DEVISE = "euros";

        String nomPatron;
        int tpsImpots = 0;
        int ECART_IMPOTS = 20;  // en seconde
        double TAUX_IMPOTS = 0.3;
        double taxes = 0;

    // -----

    void algorithm() {
        introduction();
        do {
            int initialTemps = (int) (getTime() / 1000);
            int choix = choisir();
            int ecartTemps = (int) ((getTime()/1000)-initialTemps);
            tpsImpots += ecartTemps;
            if (tpsImpots >= ECART_IMPOTS){
                capital = (capital - (totalParSeconde()*TAUX_IMPOTS));
                println("Vous avez payé " + (totalParSeconde()*TAUX_IMPOTS) + " " + DEVISE + " d'impôts !");
                tpsImpots = 0;
                delay(4000);
            }
            if (choix == 2) {
                achatJouet(); // Achat de nouveau jouet
            } else if (choix == 3) {
                // Amélioration
                menuAmelioration();
            } else {
                // Récupérer l'argent
                for (int i=0; i<nbJouets; i++){
                    capital += (jouets[i].revenu * ecartTemps);
                }
            }
        } while(capital>=0);
        println("Vous avez perdu !");
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
            clearConsole();
            informations();

            println("==================================================        Choix       ==================================================");
            println("                    1                             Récupérer l'argent");
            println("                    2                             Acheter un nouveau jouet");
            println("                    3                             Améliorer la production");
            println("                    4                             Construire");
            println("========================================================================================================================");
            println();

            choix = readString(5000);
            estValide = strToIntPossible(choix);
            if (!estValide) {
                println("Il semblerait que vous n'ayez pas entré un nombre ! Veuillez réessayer !");
            }
        } while (!estValide);
        choix_final = strToInt(choix);
        return choix_final;
    }

    void informations() {
        // Afficher les informations sur l'usine
        println("==================================================    Informations    ==================================================");
        println("                    Argent Total                      " + capital  + " " + DEVISE);
        println("                    Production par seconde            " + totalParSeconde() + " " + DEVISE);
        println("                    Prévision Impôts                  " + totalParSeconde() * TAUX_IMPOTS + " " + DEVISE + " (Dans " + (ECART_IMPOTS - tpsImpots) + " secondes)");
        println("========================================================================================================================");
        println();
    }

    Jouet nouveauJouet(String nom) {
        Jouet nouveau = new Jouet();
        nouveau.nom = nom;
        nouveau.revenu = 1;
        nouveau.prixAmelioration = 50;
        nbJouets++;
        return nouveau;
    }

    void menuAmelioration() {
        println("Quel jouet souhaitez vous améliorer ?");
        for (int i_jouet=0; i_jouet<nbJouets; i_jouet++) {
            println((i_jouet+1) + " : " + jouets[i_jouet].nom);
        }
        delay(2000);
    }

    double totalParSeconde() {
        double total = 0;
        for (int i_jouet=0; i_jouet<nbJouets; i_jouet++) {
            total += jouets[i_jouet].revenu;
        }
        return total;
    }

    void achatJouet() {
        println("Voulez vous acheter un nouveau jouet pour " + prixJouet + " " + DEVISE + " ?");
        print("Oui / Non : ");
        String reponse = toLowerCase(readString());
        println(reponse);
        if (equals(reponse, "oui")) {
            if (nbJouets < maxJouets) {
                println("Bravo ! Vous venez d'acheter un nouveau jouet ! Comment voulez vous l'appeller ?");
                Jouet nouveau = nouveauJouet(readString());
                jouets[(nbJouets-1)] = nouveau;
                println("Tout est bon, votre jouet produira " + jouets[(nbJouets-1)].revenu + " " + DEVISE + " par seconde !");
                capital -= prixJouet;
            } else {
                println("Mince ! Vous avez acheté tous les jouets possibles !");
            }
            delay(4000);
        }
    }

    void introduction() {
        clearConsole();
        int delay = 1500;
        String nomEmploye = "Pyaire";
        println(nomEmploye + " : Bonjour ! Bienvenue dans votre magasin patron !");
        delay(delay);
        println(nomEmploye + " : Tiens, on dirait qu'il n'y a pas beaucoup d'argent dans votre porte feuilles...");
        delay(delay);
        println("*Vous ouvrez votre porte feuille et constatez qu'il n'y a que " + capital + " " + DEVISE + "*");
        delay(delay*2);
        println(nomEmploye + " : On va arranger tout ça, comment je peux vous appeller ?");
        nomPatron = readString();
        println(nomEmploye + " : Très bien " + nomPatron + ", on y va !");
        delay(delay);
        println(nomEmploye + " : Voulez-vous que je vous explique votre mission (Oui / Non) ?");
        String answer = readString();
        if (equals(toLowerCase(answer), "oui")) {
            regles(delay);
        }
        clearConsole();
    }

    void regles(int delay) {
        delay *= 2;
        clearConsole();
        println("Voici votre mission :");
        println("-------------------------");
        delay(delay);
        println("Vous venez d'arriver dans votre magasin.");
        delay(delay);
        println("Votre but : gagner de l'argent le plus efficacement possible");
        delay(delay);
        println("Attention tout de même à bien gérer vos finances,");
        delay(delay);
        println("toutes les " + ECART_IMPOTS + " secondes, vous devrez payer " + (TAUX_IMPOTS*100) + "% de votre production par seconde !");
        delay(delay);
        println("Si vous tombez en dessous des 0 euros de capital, c'est perdu !");
        delay(delay);
        println("Vous pourrez acheter un nouveau produit qui vous rapportera de l'argent par seconde,");
        delay(delay);
        println("vous pouvez aussi en améliorer un pour qu'il vous rapporte plus !");
        delay(delay);
        println();
        println("Bonne chance à vous patron !");
        println("-------------------------");
        delay(delay*2);
    }

    void clearConsole() {
        clearScreen();
        cursor(1, 1);
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