package yal;

public final class FabriqueDeNumero {
    private static FabriqueDeNumero instance;
    private int numeroLabelCondition;

    private FabriqueDeNumero(){
        numeroLabelCondition = 0;
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
}
