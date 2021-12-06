class HarryThmetik extends Program{

    final String PR_NAME = "Pr. McGonnaCount";
    final String MAISON = "NombreDor";
    String prenom;
    final int TEMPS_PAUSE = 800;
    
    //Fonction principale
    void algorithm(){
	clearScreen();
	intro();
	int score = 0;
	boolean perdu=false;
	do{
	    Calcul c = creerCalculAlea();
	    profParle(toString(c) + " ?");
	    int reponse = lireEntierSecurise();
	    if (reponse==bonneReponse(c)){
		score++;
		profParle("Bonne réponse");
	    }else{
		perdu=true;
	    }
	}while(!perdu);	
	profParle("Mauvaise réponse");
	profParle(score + " point(s) pour la maison " + MAISON);
	profParle("À la prochaine ! ");
    }

    //Affichage d'un écran titre, récupération du prénom de la joueuse ou du joueur et introduction
    void intro(){
	println("===================================");
	println("Harry Thmétik à l'école des calculs");
	println("===================================");
	println();
	print("Appuyer sur entrée pour commencer à jouer");
	readString();
	clearScreen();
	print("Entrez votre prénom : ");
	prenom = readString();
	clearScreen();
	profParle("Oh, " + prenom +", tu es là.");
	profParle("Je ne t'avais pas calculé(e).");
	profParle("Mais après tout, c'est à toi de calculer !");
	profParle("Chaque bonne réponse rapportera 1 point pour la maison "+ MAISON+".");
    }

    //Fonction d'affichage des phrases dites par la ou le prof suivies d'un temps de pause
    void profParle(String phrase){
	println(PR_NAME + " : " + phrase);
	delay(TEMPS_PAUSE);
    }

    
    //Crée et retourne un calcul à partir d'un signe et 2 nombres
    Calcul creerCalcul(Signe s, int nb1, int nb2){
	Calcul c = new Calcul();
	c.signe = s;
	c.nb1 = nb1;
	c.nb2 = nb2;
	return c;
    }

    //Crée et retourne un calcul au hasard, si c'est une soustraction, on fait en sorte que le résultat soit positif ou nul
    Calcul creerCalculAlea(){
	Signe s = signeAlea();
	int c1 = chiffreAlea();
	int c2 = chiffreAlea();
	if (s==Signe.MOINS & c1<c2){
		int tmp = c2;
		c2=c1;
		c1=tmp;
	}
	return creerCalcul(s,c1,c2);
    }

    //Lit et retourne un entier en prenant en compte les éventuelles erreurs de saisie et en en redemandant un le cas échéant
    int lireEntierSecurise(){
	print(prenom + " : ");
	String reponse = readString();
	if (estNombreEntier(reponse)){
	    return stringToInt(reponse);
	}else{
	    profParle("Ce n'est pas un nombre "+ prenom +", ta langue a dû fourcher, répète");
	    return lireEntierSecurise();
	}
    }

    //Indique si une chaine est un nombre entier, positif ou négatif
    boolean estNombreEntier(String chaine){
	boolean valide = true;
	//il faut qu'il y ait au moins un caractère
    	if (length(chaine)==0){
	    valide = false;
	}else{	    
	    //que la chaine commence par - ou un chiffre
	    char premier = charAt(chaine,0);
	    if (premier != '-' && !estChiffre(premier)){
		valide = false;
	    }
	    //et que tous les éventuels caractères suivants soient des chiffres
	    int i = 1;
	    while (valide && i< length(chaine)){
		if (!estChiffre(charAt(chaine,i))){
		    valide = false;
		}
		i++;
	    }
	}
	return valide;	
    }	
	

    //Retourne vrai si le caractère est un chiffre, faux sinon
    boolean estChiffre(char c){
	return c>='0' && c<='9';
    }


    //Calcule et retourne la bonne réponse pour un calcul donné
    int bonneReponse(Calcul c){
	if (c.signe == Signe.PLUS){
	    return c.nb1 + c.nb2;
	}else{
	    return c.nb1 - c.nb2;
	}
    }

    //Renvoie le calcul sous forme d'une chaîne de caractères
    String toString(Calcul c){
	return "" + c.nb1 + toChar(c.signe) + c.nb2;
    }

    //Retourne le caractère correspondant au signe
    char toChar(Signe c){
	if (c==Signe.PLUS){
	    return '+';
	}
	else if (c==Signe.MOINS){
	    return '-';
	}
	else {
	    return ' ';
	}
    }

    //Retourne un chiffre entre 0 et 9 au hasard
    int chiffreAlea(){
	return (int) (random() * 10);
    }
    
    Signe signeAlea(){
	if (random()>0.5){
	    return Signe.PLUS;
	}else{
	    return Signe.MOINS;
	}
    }

    /*******
     *TESTS
     ******/
    //Commenter ou renommer void algorithm() pour qu'ils soient exécutés

    void testToString(){
	Calcul c = creerCalcul(Signe.PLUS,3,2);
	assertEquals("3+2",toString(c));
    }
    
    void testBonneReponse(){
	Calcul addition = creerCalcul(Signe.PLUS,3,2);
	assertEquals(5,bonneReponse(addition));

	Calcul soustraction = creerCalcul(Signe.MOINS,3,2);
	assertEquals(1,bonneReponse(soustraction));

    }
	    
    void testEstChiffre(){
	assertTrue(estChiffre('0'));
	assertTrue(estChiffre('5'));
	assertTrue(estChiffre('9'));
	assertFalse(estChiffre('A'));
	assertFalse(estChiffre('!'));
    }
    
    void testEstNombreEntier(){
	assertFalse(estNombreEntier(""));
	assertFalse(estNombreEntier("1b"));
	assertFalse(estNombreEntier("1.2"));
	assertFalse(estNombreEntier("--"));
	assertFalse(estNombreEntier("abc"));
	assertTrue(estNombreEntier("33"));
	assertTrue(estNombreEntier("-1"));
	assertTrue(estNombreEntier("0"));
    }

}
