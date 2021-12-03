class Main extends Program {

    int total = 0;
    double[] total_per_second = new double[]{5, 0, 0};

    void algorithm() {
        println("1: Miner");
        println("2: Construire");
        int choix = readInt();

        if (choix == 1) {
            // Miner
            mine();
        } else {
            // Construire
        }
    }

    void mine() {
        while (true) {
            for (int i=0; i<length(total_per_second); i++) {
                total += total_per_second[i];
            }
            delay(1000);
            println(total);
        }
    }
    
}