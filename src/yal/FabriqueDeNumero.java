package yal;

public final class FabriqueDeNumero {
    private static FabriqueDeNumero instance;
    private int numeroLabelCondition;
    private int numeroLabelBoucle;

    private FabriqueDeNumero(){
        numeroLabelCondition = 0;
        numeroLabelBoucle = 0;
    }

    public static FabriqueDeNumero getInstance(){
        if(instance == null){
            instance = new FabriqueDeNumero();
        }
        return instance;
    }

    public int getNumeroLabelCondition() {
        return numeroLabelCondition++;
    }

    public int getNumeroLabelBoucle() {
        return numeroLabelBoucle++;
    }
}
