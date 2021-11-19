class IC extends Program{

                                         /* Partie Test des fonction */
    void testuniform(){ 
    assertEquals("test",uniform("TeSt"));
    assertEquals("jemappellepi3rre",uniform("Je m'aPPellE Pi3rre"));
    assertEquals("ceciestunlongtextojemamuse",uniform("ceci est un long #text où je m'amuse"));
    assertEquals("enigmacstpss1f",uniform(" ENI,GMA c'ést  .   pàS s1 fôù "));
    }
                                        /* Partie écriture de fonction */
    String uniform (String message){
        String fin="";
        for (int i=0; i<length(message); i++){
            char enTest = charAt(message,i);
            if (enTest <='Z'&& enTest >= 'A'){
                enTest = (char) (enTest + ('a'-'A'));
                fin = fin + enTest;            
            }else if (enTest <='z'&& enTest >= 'a'){
                fin = fin + enTest;
            }else if(enTest <='9'&& enTest >= '0'){
                fin = fin + enTest;  
            }            
        }
        return fin;
    }
                                        /* Program Principal */
    // void algorithm(){

    // }
}