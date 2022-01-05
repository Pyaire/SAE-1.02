import extensions.CSVFile;
import extensions.File;

class Main extends Program {

    final int MAXJOUETS = 3;
    final String DEVISE = "euros";
    final int ECART_IMPOTS = 20;  // en seconde
    final double TAUX_IMPOTS = 5; // Part de revenue par seconde
    final double TAUX_REVENU = 0.05;
    final double TAUX_AMELIORATION = 1.5;
    final int MULT_NOUVEAU_ACHAT = 100;
    final double BASE_PRIX_ACHAT = 50;

    void algorithm() {
        boolean fin = false;

        double capital = 100;
        String nomPatron = "";
        int nbJouets = 0;
        Jouet[] jouets = new Jouet[MAXJOUETS];
        int tpsImpots = 0;

        clearConsole();
        print("Souhaitez vous charger une sauvegarde ? (Oui / Non) : ");
        String load_save = toLowerCase(readString());

        if (equals("oui", load_save)) {
            int saveNb = loadSave(capital, nomPatron, nbJouets, tpsImpots, jouets);
            if (saveNb != -1) {
                String[] allFiles = getAllFilesFromDirectory("Saves");
                CSVFile fichierSauvegarde = loadCSV("Saves/" + allFiles[(saveNb-1)]);
                capital = strToDouble(getCell(fichierSauvegarde, 0, 0));
                nomPatron = getCell(fichierSauvegarde, 0, 1);
                tpsImpots = strToInt(getCell(fichierSauvegarde, 0, 2));

                for (int row=1; row<rowCount(fichierSauvegarde); row++) {
                    jouets[(row-1)] = nouveauJouet(
                        getCell(fichierSauvegarde, row, 0),
                        strToDouble(getCell(fichierSauvegarde, row, 1)),
                        strToInt(getCell(fichierSauvegarde, row, 2))
                    );
                    nbJouets++;
                }
            } else {
                nomPatron = introduction(capital);
            }
        } else {
            nomPatron = introduction(capital);
        }

        do {
            capital = round(capital);
            int initialTemps = (int) (getTime() / 1000);
            int choix = choisir(jouets, nbJouets, capital, tpsImpots);
            if (choix == 2) {
                nbJouets += achatJouet(jouets, nbJouets, capital); // Achat de nouveau jouet
                delay(2500);
            } else if (choix == 3) {
                // Amélioration
                menuAmelioration(jouets, nbJouets, capital);
            } else if (choix == 4) {
                infos_jouets(jouets, nbJouets);
            } else if (choix == 5) {
                // Sauvegarde
                sauvegarde(capital, nomPatron, nbJouets, tpsImpots, jouets);
            } else if (choix == 6) {
                println("Cette fonctionnalité n'est pas encore disponible...");
                delay(2500);
            } else if (choix == 9) {
                fin = true;
            } else {
                // Récupérer l'argent
                int ecartTemps = (int) ((getTime()/1000)-initialTemps);
                capital += totalParSeconde(jouets, nbJouets) * ecartTemps;
                tpsImpots += ecartTemps;
                if (tpsImpots >= ECART_IMPOTS){
                    capital = (capital - (totalParSeconde(jouets, nbJouets)*TAUX_IMPOTS));
                    println("Vous avez payé " + (totalParSeconde(jouets, nbJouets)*TAUX_IMPOTS) + " " + DEVISE + " d'impôts !");
                    tpsImpots = 0;
                    delay(2500);
                }
            }
        } while(capital>=0 && !fin);
        if (!fin) {
            println("Vous avez perdu ! Votre capital est de " + capital + " " + DEVISE + "...");
            println("Pensez à mieux gérer vos finances !");
        } else {
            println("A la prochaine !");
        }
    }

    int choisir(Jouet[] jouets, int nbJouets, double capital, int tpsImpots) {
        boolean estValide = false;
        String choix;
        int choix_final;
        do {
            clearConsole();
            informations(jouets, nbJouets, capital, tpsImpots);
            println("==================================================        Choix       ==================================================");
            println("                    1                             Récupérer l'argent");
            println("                    2                             Acheter un nouveau jouet");
            println("                    3                             Améliorer la production");
            println("                    4                             Informations sur les jouets");
            println("                    5                             Sauvegarder la partie");
            println("                    9                             Quitter le jeu (N'oubliez pas de sauvegarder)");
            if (nbJouets == MAXJOUETS) {
                println("                    6                          ===   Terminer le jeu  ===                                               ");
            }
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

    void informations(Jouet[] jouets, int nbJouets, double capital, int tpsImpots) {
        // Afficher les informations sur l'usine
        println("==================================================    Informations    ==================================================");
        println("                    Argent Total                      " + capital  + " " + DEVISE);
        println("                    Production par seconde            " + totalParSeconde(jouets, nbJouets) + " " + DEVISE);
        println("                    Prévision Impôts                  " + totalParSeconde(jouets, nbJouets) * TAUX_IMPOTS + " " + DEVISE + " (Dans " + (ECART_IMPOTS - tpsImpots) + " secondes)");
        println("========================================================================================================================");
        println();
    }

    Jouet nouveauJouet(String nom, double prixAchat, int niveau) {
        Jouet nouveau = new Jouet();
        nouveau.nom = nom;
        nouveau.prixAchat = prixAchat;
        nouveau.niveau = niveau;
        return nouveau;
    }

    int achatJouet(Jouet[] jouets, int nbJouets, double capital) {
        clearConsole();
        if (nbJouets < MAXJOUETS) {
            double prixJouet;
            if (nbJouets == 0) {
                prixJouet = BASE_PRIX_ACHAT;
            } else {
                prixJouet = jouets[(nbJouets-1)].prixAchat * MULT_NOUVEAU_ACHAT;
            }
            println("==================================================   Achat de Jouet   ==================================================");
            println("                    Prix du jouet                    " + prixJouet  + " " + DEVISE);
            println("                                              Confirmer l'achat ? (Oui / Non) ");
            print("Confirmation : ");
            String answer = readString();
            println("========================================================================================================================");
            if (equals("oui", toLowerCase(answer))) {
                // Confirmé
                if (capital >= prixJouet) {
                    print("Nom du jouet (ex: Ours en peluche) : ");
                    String nomJouet = sansVirgule(readString());
                    jouets[nbJouets] = nouveauJouet(nomJouet, prixJouet, 1);
                    capital -= prixJouet;
                    println("Bravo ! Vous venez d'acheter un nouveau jouet !");
                    return 1;
                } else {
                    println("Vous ne pouvez pas vous le permettre... Revenez plus tard !");
                }
            } else {
                // Abandonné
                println("Tant pis ! Revenez vite !");
            }
        } else {
            println("Vous ne pouvez pas acheter plus de jouets, mais vous pouvez terminer le jeu !");
        }
        return 0;
    }

    void menuAmelioration(Jouet[] jouets, int nbJouets, double capital) {
        clearConsole();
        // Lister les jouets
        int choix = -1;
        for (int iJouet=0; iJouet<nbJouets; iJouet++) {
            double prixAmelio = (jouets[iJouet].prixAchat * pow(TAUX_AMELIORATION, (jouets[iJouet].niveau)));
            println((iJouet+1) + " : " + jouets[iJouet].nom + " - Niveau " + jouets[iJouet].niveau + " (" + prixAmelio + " " + DEVISE + ")");
        }
        do {
            print("Jouet à améliorer : ");
            String reponse = readString();
            int reponse_int = strToInt(reponse);
            if (reponse_int > 0 && reponse_int <= nbJouets) {
                choix = reponse_int;
            } else {
                println("Le choix n'est pas dans la liste !");
            }
        } while (choix == -1);
        choix--; // Le convertir pour un array
        double prixAmelioration = (jouets[choix].prixAchat * pow(TAUX_AMELIORATION, (jouets[choix].niveau))); // Pas -1 au niveau car on cherche le prochain prix
        println("==============================================   Amélioration de Jouet   ===============================================");
        println("                    Prix de l'amélioration       " + prixAmelioration  + " " + DEVISE);
        println("                                         Confirmer l'amélioration ? (Oui / Non) ");
        print("Confirmation : ");
        String answer = readString();
        println("========================================================================================================================");
        if (equals("oui", toLowerCase(answer))) {
            // Confirmé
            if (capital >= prixAmelioration) {
                jouets[choix].niveau++;
                capital -= prixAmelioration;
                println("Bravo ! Vous venez d'améliorer un jouet !");
            } else {
                println("Vous ne pouvez pas vous le permettre... Revenez plus tard !");
            }
        } else {
            // Abandonné
            println("Tant pis ! Revenez vite !");
        }
        delay(2500);
    }

    void infos_jouets(Jouet[] jouets, int nbJouets) {
        clearConsole();
        for (int iJouet=0; iJouet<nbJouets; iJouet++) {
            println("==============================================      Jouet numéro " + (iJouet+1) + "      ===============================================");
            println("               Nom :                                " + jouets[iJouet].nom);
            println("               Niveau :                             " + jouets[iJouet].niveau);
            println("               Revenu par seconde :                 " + (jouets[iJouet].prixAchat * pow(TAUX_AMELIORATION, (jouets[iJouet].niveau-1))) * TAUX_REVENU + " " + DEVISE);
            println("               Prix de passage au niveau suivant :  " + (jouets[iJouet].prixAchat * pow(TAUX_AMELIORATION, (jouets[iJouet].niveau))));
        }
        println("=========================================================================================================================================");
        println("Pour quitter, saisissez quelque chose.");
        readString();
    }

    String introduction(double capital) {
        clearConsole();
        int delay = 1500;
        String nomEmploye = "Pyaire";
        String nomPatron;
        println(nomEmploye + " : Bonjour ! Bienvenue dans votre magasin patron !");
        delay(delay);
        println(nomEmploye + " : Tiens, on dirait qu'il n'y a pas beaucoup d'argent dans votre porte feuilles...");
        delay(delay);
        println("*Vous ouvrez votre porte feuille et constatez qu'il n'y a que " + capital + " " + DEVISE + "*");
        delay(delay*2);
        println(nomEmploye + " : On va arranger tout ça, comment je peux vous appeller ?");
        nomPatron = sansVirgule(readString());
        println(nomEmploye + " : Très bien " + nomPatron + ", on y va !");
        delay(delay);
        println(nomEmploye + " : Voulez-vous que je vous explique votre mission (Oui / Non) ?");
        String answer = readString();
        if (equals(toLowerCase(answer), "oui")) {
            regles(delay);
        }
        clearConsole();
        return nomPatron;
    }

    void regles(int delay) {
        delay *= 2; // Phrases longues
        clearConsole();
        println("Voici votre mission :");
        println("-------------------------");
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
        // Problème sur windows : Le clearscreen ne repositionne pas le curseur
        clearScreen();
        cursor(1, 1);
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
        } else {
            println(action + " n'est pas un nombre.");
        }
        return retour ;
    }

    double totalParSeconde(Jouet[] jouets, int nbJouets) {
        double total = 0;
        for (int iJouet=0; iJouet<nbJouets; iJouet++) {
            double dernierPrix = (jouets[iJouet].prixAchat * pow(TAUX_AMELIORATION, (jouets[iJouet].niveau-1))); // niveau-1 car dès qu'on l'achète, le jouet est niveau 1
            total += dernierPrix * TAUX_REVENU;
        }
        return total;
    }

    boolean filenameDejaPris(String filename) {
        String[] allFiles = getAllFilesFromDirectory("Saves");
        int iFile = 0;
        boolean pris = false;
        while (iFile < length(allFiles) && !pris) {
            if (allFiles[iFile] == filename) {
                pris = true;
            }
            iFile++;
        }
        if (pris) {
            print("Le fichier existe déjà. Voulez-vous l'écraser ? (Oui / Non) : ");
            String ecraser = toLowerCase(readString());
            if (!equals(ecraser, "oui")) {
                return true;
            }
        }
        return false;
    }

    String avecExtensionCsv(String filename) {

        if (length(filename)<5) {
            return filename + ".csv";
        } else {
            String extension = substring(filename, (length(filename)-4), length(filename));
            if (equals(extension, ".csv")) {
                return filename;
            } else {
                return filename + ".csv";
            }
        }
    }

    void sauvegarde(double capital, String nomPatron, int nbJouets, int tpsImpots, Jouet[] jouets) {
        /*
        Exemple de fichier de sauvegarde

        // Informations
        A1 : capital
        B1 : nomPatron
        C1 : nbJouets
        D1 : tpsImpots

        // Jouets
        A2 : nom du jouet
        B2 : prix d'achat
        C2 : Niveau

        ...
        ...
        ...
        */

        clearConsole();

        String[][] save = new String[(nbJouets+1)][3];
        save[0][0] = "" + capital + "";
        save[0][1] = "" + nomPatron + "";
        save[0][2] = "" + tpsImpots + "";

        for (int iJouet=0; iJouet<nbJouets; iJouet++) {
            save[(iJouet+1)][0] = jouets[iJouet].nom;
            save[(iJouet+1)][1] = "" + jouets[iJouet].prixAchat + "";
            save[(iJouet+1)][2] = "" + jouets[iJouet].niveau + "";
        }

        println("==================================================     Sauvegarde     ==================================================");
        print("           Souhaitez vous sauvergarder ? (Oui / Non) : ");
        String sauvegarde_y_n = toLowerCase(readString());
        if (equals(sauvegarde_y_n, "oui")) {
            boolean filename_est_valide = false;
            String filename = "";
            while (!filename_est_valide) {
                print("           Nom du fichier : ");
                filename = sansCaracteresSpeciaux(readString());
                if (length(filename) > 0 && !filenameDejaPris(filename)) {
                    filename_est_valide = true;
                } else {
                    println("           Veuillez entrer un autre nom de fichier");
                }
            }
            
            filename = avecExtensionCsv(filename);
            saveCSV(save, "Saves/" + filename);
            println("           La sauvegarde a été effectuée !");
            delay(2500);
        }
    }

    int loadSave(double capital, String nomPatron, int nbJouets, int tpsImpots, Jouet[] jouets) {
        String[] allFiles = getAllFilesFromDirectory("Saves");
        if (length(allFiles) == 0) {
            println("Aucun fichier de sauvegarde n'a été trouvé...");
            delay(2500);
            return -1;
        } else {
            println("Liste des sauvegardes :");
            for (int i=0; i<length(allFiles); i++) {
                println((i+1) + " : " + allFiles[i]);
            }
            boolean saiseValide = false;
            int saveNb = 0;
            while (!saiseValide) {
                print("Sauvegarde à charger : ");
                String aCharger = readString();
                saveNb = strToInt(aCharger);
                if (saveNb >= 1 && saveNb <= length(allFiles)) {
                    saiseValide = true;
                } else {
                    println("La sauvegarde demandée n'est pas dans la liste.");
                }
            }
            return saveNb;
        }
    }

    String sansCaracteresSpeciaux(String phrase) {
        String result = "";
        char carac;
        for (int i=0; i<length(phrase); i++) {
            carac = charAt(phrase, i);
            if (carac >= 'A' && carac <= 'Z' || carac >= 'a' && carac <= 'z' || carac >= 0 && carac <= 9) {
                result += carac;
            }
        }
        return result;
    }

    String sansVirgule(String phrase) {
        String result = "";
        for (int i=0; i<length(phrase); i++) {
            if (charAt(phrase, i) != ',') {
                result += charAt(phrase, i);
            }
        }
        return result;
    }

    double strToDouble(String number) {
        double retour = 0;
        int positionVirgule = posVirgule(number);
        if (positionVirgule == -1) {
            retour = strToInt(number);
        } else {
            int partieEntiere = strToInt(substring(number, 0, positionVirgule));
            if (positionVirgule == length(number)) {
                // Pas de décimale car virgule est le dernier caractère
                retour = partieEntiere;
            } else {
                String decimales = substring(number, (positionVirgule+1), length(number)); // PositionVirgule + 1 pour ne pas prendre la virgule
                int partieDecimale = strToInt(decimales);
                retour = partieEntiere + partieDecimale * pow(0.1, length(decimales));
            }
        }
        return retour;
    }

    int posVirgule(String number) {
        for (int i=0; i<length(number); i++) {
            if (charAt(number, i) == '.') {
                return i;
            }
        }
        return -1;
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

    void testStrToDouble() {
        assertEquals(4.33, strToDouble("4.33"));
        assertEquals(4.0, strToDouble("4."));
        assertEquals(0.355, strToDouble("0.355"));
        assertEquals(0.355, strToDouble(".355"));
    }
    
}