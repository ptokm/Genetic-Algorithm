package geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {
    /**
     * _population: Chromosomes (size: geneSize),
                    fitness { The evaluate value of each chromosome in population } , 
                    normalizateFitness
                    cumulativeSum     
     */
    private final ArrayList <ArrayList <Double>> _population;
    private int _countOfGeneOfChromosome = -1;
    
    Population(int populationSize, int geneSize,  String initializeGeneOption, String fitnessFunctionOption) {
        this._population = new ArrayList <>();
        this._countOfGeneOfChromosome = geneSize;
        
        this.initializePopulation(populationSize, initializeGeneOption);
        this.calculateFitness("sumation_of_genes");
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
            for (short i = 0; i < this._countOfGeneOfChromosome; i++)
                chromosome.add(initializationValue);
            
            for (short i = 0; i < populationSize; i++)
                this._population.add(chromosome);
        } else {
            // Need random numbers in [min, max]
            int min = -1;
            int max = 1;
            for (short i = 0; i < populationSize; i++) {
                ArrayList <Double> chromosome = new ArrayList <>();
                Random r = new Random();
                for (short j = 0; j < this._countOfGeneOfChromosome; j++)
                    chromosome.add(r.nextDouble((max - min) + 1) + min);
                this._population.add(chromosome);
            }
        }
        
        return true;
    }
    
    private boolean calculateFitness(String fitnessFunctionOption) {
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
    
    private boolean validateNormalizedFitnessValues() {
        double sumNormalizedFitnessValue = 0.0;
        for (ArrayList <Double> chromosome : this._population)
            sumNormalizedFitnessValue += chromosome.get(this._countOfGeneOfChromosome + 1);
        
        return (sumNormalizedFitnessValue == 1.0);
    }
    
    private boolean fitnessScaling() {
        boolean hasNegativeFitnessValue = false;
        for (ArrayList <Double> chromosome : this._population)
            if (chromosome.get(this._countOfGeneOfChromosome) < 0)
                hasNegativeFitnessValue = true;
        
        if (hasNegativeFitnessValue) {
            double minFitnessValue = this._population.get(0).get(this._countOfGeneOfChromosome);
            for (short i = 0; i < this._population.size(); i++) {
                if (this._population.get(i).get(this._countOfGeneOfChromosome) < minFitnessValue)
                    minFitnessValue = this._population.get(i).get(this._countOfGeneOfChromosome);
            }
            
            minFitnessValue = Math.abs(minFitnessValue);
            for (short i = 0; i < this._population.size(); i++) {
                double scaledFitnessValue = 1.0 * this._population.get(i).get(this._countOfGeneOfChromosome) + minFitnessValue + 1; // +1 to avoid devide by zero after
                this._population.get(i).set(this._countOfGeneOfChromosome, scaledFitnessValue);
            }
        }
        
        return true;
    }
    
    // Devide the fitness value of each chromosome
    // by the total values of all fitnesses
    private boolean normalizeFitnessValues() {
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
    
    private boolean calculateCumulativeSumOfNormalizedFitnessValues() {
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
    
    private boolean selectChromosomes(int numberOfSelection ) {
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
    
    public boolean rouletteWheel(int numberOfChromosomesToBeSelected) {
        this.fitnessScaling();
        this.normalizeFitnessValues();
        this.calculateCumulativeSumOfNormalizedFitnessValues();
        this.selectChromosomes(numberOfChromosomesToBeSelected);
        return true;
    }

}
