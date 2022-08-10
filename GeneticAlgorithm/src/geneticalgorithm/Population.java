package geneticalgorithm;

import java.util.ArrayList;

public class Population {
    private final ArrayList <ArrayList <Integer>> _population;
    
    Population(int populationSize, int initializeGeneOption) {
        _population = new ArrayList <>();
        initializePopulation(populationSize, initializeGeneOption);
    }
    
    private boolean initializePopulation(int populationSize, int initializeGeneOption) {
        ArrayList <Integer> chromosome = new ArrayList <>();
        switch (initializeGeneOption) {
            case 0 -> {
                chromosome.add(0);
                chromosome.add(0);
                break;
            }
            case 1 -> {
                chromosome.add(1);
                chromosome.add(1);
                break;
            }
            default -> {
                chromosome.add(0);
                chromosome.add(0);
                break;
            }
        }
            
        for (short i = 0; i < populationSize; i++) {
            this._population.add(chromosome);
        }
        
        return true;
    }
}
