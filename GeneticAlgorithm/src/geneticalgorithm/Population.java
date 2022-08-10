package geneticalgorithm;

import java.util.ArrayList;

public class Population {
    private final ArrayList <ArrayList <Integer>> _population;
    private final ArrayList <Double> _fitness; // The evaluate value of each chromosome in population
    
    Population(int populationSize, int initializeGeneOption) {
        this._population = new ArrayList <>();
        this._fitness = new ArrayList <>();
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
            
        for (short i = 0; i < populationSize; i++)
            this._population.add(chromosome);
        
        return true;
    }
    
    
    public boolean calculateFitness(String fitnessFunctionOption) {
        for (short i = 0; i < this._population.size(); i++) {
            switch (fitnessFunctionOption) {
                case "step_function" ->  {
                    double sum = 0.0;
                    for (short j = 0; j < this._population.get(i).size(); j++)
                        sum += this._population.get(i).get(j);
                    this._fitness.add((sum >= 0 ? 1.0 : 0.0));
                }
                default ->  { // Step_function
                    double sum = 0.0;
                    for (short j = 0; j < this._population.get(i).size(); j++)
                        sum += this._population.get(i).get(j);
                    this._fitness.add((sum >= 0 ? 1.0 : 0.0));
                }
            }
        }
  
        return true;
    }
    
}
