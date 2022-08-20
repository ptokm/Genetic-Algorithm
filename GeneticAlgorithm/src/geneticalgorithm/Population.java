package geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;

public class Population {
    /**
     * _population: Chromosomes (size: geneSize),
                    fitness { The evaluate value of each chromosome in population } , 
                    normalizateFitness
                    cumulativeSum     
     */
    private final ArrayList <ArrayList <Double>> _population;
    private int _countOfGeneOfChromosome = -1;
    
    Population(int populationSize, int geneSize,  String initializeGeneOption) {
        this._population = new ArrayList <>();
        this._countOfGeneOfChromosome = geneSize;
        initializePopulation(populationSize, initializeGeneOption);
    }
    
    // Initialization can be either all genes in same specific double value
    // or in ramdom values
    private boolean initializePopulation(int populationSize, String initializeGeneOption) {
        if (!initializeGeneOption.equals("random")) {
            double initializationValue;
            try {
                initializationValue = Double.parseDouble(initializeGeneOption);
            } catch(NumberFormatException e) {
                 initializationValue = 0.0;
            }
            
            ArrayList <Double> chromosome = new ArrayList <>();
            for (short i = 0; i < this._countOfGeneOfChromosome; i++) {
                chromosome.add(initializationValue);
            }
            
            for (short i = 0; i < populationSize; i++)
                this._population.add(chromosome);
        } else {
            for (short i = 0; i < populationSize; i++) {
                ArrayList <Double> chromosome = new ArrayList <>();
                double random1 = Math.random();
                double random2 = Math.random();
                chromosome.add((random1 > 0.5) ? 1.0 : 0.0);
                chromosome.add((random2 > 0.5) ? 1.0 : 0.0);
                this._population.add(chromosome);
            }
        }
        
        return true;
    }
    
    public boolean calculateFitness(String fitnessFunctionOption) {
        switch (fitnessFunctionOption) {
                case "sumation_of_genes" ->  {
                    for (short i = 0; i < this._population.size(); i++) {
                        ArrayList <Double> temp = new ArrayList <> (this._population.get(i));
                        double sum = 0.0;
                        for (short j = 0; j < temp.size(); j++) {
                            sum += temp.get(j);
                        }
                        temp.add(sum);
                        this._population.set(i, temp);
                    }
                    
                    break;
                }
                default ->  { // sumation_of_genes
                    for (short i = 0; i < this._population.size(); i++) {
                        ArrayList <Double> temp = new ArrayList <> (this._population.get(i));
                        double sum = 0.0;
                        for (short j = 0; j < temp.size(); j++) {
                            sum += temp.get(j);
                        }
                        temp.add(sum);
                        this._population.set(i, temp);
                    }
                    break;
                }
            }
                       
        return true;
    }
    
    public boolean validateNormalizedFitnessValues() {
        double sumNormalizedFitnessValue = 0.0;
        for (ArrayList <Double> chromosome : this._population)
            sumNormalizedFitnessValue += chromosome.get(this._countOfGeneOfChromosome + 1);
        
        return (sumNormalizedFitnessValue == 1.0);
    }
    
    // Devide the fitness value of each chromosome
    // by the total values of all fitnesses
    public boolean normalizeFitnessValues() {
        double sumFitnessValue = 0.0;
        for (ArrayList <Double> chromosome : this._population) {
            sumFitnessValue += chromosome.get(this._countOfGeneOfChromosome);
        }

        for (short i = 0; i < this._population.size(); i++) {
            ArrayList <Double> inner = new ArrayList <> (this._population.get(i));
            inner.add(inner.get(this._countOfGeneOfChromosome) / sumFitnessValue);
            this._population.set(i, inner);
        }
             
        if (this.validateNormalizedFitnessValues()) {
            Collections.sort(this._population, Collections.reverseOrder((a, b) -> Double.compare(a.get(this._countOfGeneOfChromosome + 1), b.get(this._countOfGeneOfChromosome + 1))));
            return true;
        }
        
        return false;
    }
    
    public boolean calculateCumulativeSumOfNormalizedFitnessValues() {
        for (int i = 0; i < this._population.size(); i++) {
            ArrayList <Double> temp = new ArrayList <>(this._population.get(i));
            double sum = temp.get(this._countOfGeneOfChromosome + 1);
            for (int j = (i + 1); j < this._population.size(); j++) {
                sum += this._population.get(j).get(this._countOfGeneOfChromosome + 1);
            }
            temp.add(sum);
            this._population.set(i, temp);
        }
        
        return (this._population.get(0).get(this._countOfGeneOfChromosome + 2) >= 1.0);
    }
    
    public boolean selectChromosomes(int numberOfSelection ) {
        ArrayList <ArrayList <Double>> selectedChromosomes = new ArrayList <>();
        ArrayList <ArrayList <Double>> tempPopulation = new ArrayList <>(this._population);
        
        for (short i = 0; i < numberOfSelection; i++) {
            Double random = Math.random(); // Random value in [0,1]
            
            for (short j = 0; j < tempPopulation.size(); j++) {
                if (random > tempPopulation.get(j).get(this._countOfGeneOfChromosome + 1)) {
                    selectedChromosomes.add(tempPopulation.get(j));
                    tempPopulation.remove(j);
                    break;
                }     
            }
        }
        
        return (!selectedChromosomes.isEmpty());
    }
    
}
