package optimizationAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Genetic_Algorithm {
    /**
     * _population: Chromosomes (size: geneSize),
                    fitness { The evaluate value of each chromosome in population } , 
                    normalizateFitness
                    cumulativeSum     
     */
    private ArrayList <ArrayList <Double>> _population;
    private ArrayList <ArrayList <Double>> _newPopulation;
    private final int _countOfGeneOfChromosome;
    private final Double _elitism_ratio;
    private Double _probability_of_mutation;
    private final int _maxEpoches;
    private final String _crossoverOption;
    private final String _fitnessOption;
    
    Genetic_Algorithm(int populationSize, int geneSize,  String initializeGeneOption, String fitnessOption, String crossoverOption) {
        this._population = new ArrayList <>();
        this._newPopulation = new ArrayList <>();
        this._countOfGeneOfChromosome = geneSize;
        this._crossoverOption = crossoverOption;
        this._fitnessOption = fitnessOption;
        this._elitism_ratio = 0.1;
        this._maxEpoches = 100;
        
        this.restrictions();
        this.initializePopulation(populationSize, initializeGeneOption);
    }
    
    private boolean restrictions() {
        if (this._countOfGeneOfChromosome <= 2 && this._crossoverOption.equals("double_point")) {
            System.out.println("Cannot use double point crossover. The number of chromosomes' genes is not sufficient.");
            return false;
        }
        
        return true;
    }
    
    // Initialization can be either all genes in same specific double value
    // or in ramdom values
    private boolean initializePopulation(int populationSize, String initializeGeneOption) {
        if (initializeGeneOption.equals("0") || initializeGeneOption.equals("1")) {
            double initializationValue;
            try {
                initializationValue = Double.parseDouble(initializeGeneOption);
            } catch(NumberFormatException e) {
                 initializationValue = 0.0;
            }
            
            ArrayList <Double> chromosome = new ArrayList <>();
            for (int i = 0; i < this._countOfGeneOfChromosome; i++)
                chromosome.add(initializationValue);
            for (int i = 0; i < 3; i++)
                chromosome.add(-1.0);
            
            for (int i = 0; i < populationSize; i++)
                this._population.add(chromosome);
            
            this._population = this.mutation(this._population);
        } else {
            int min = 0;
            int max = 1;
            for (int i = 0; i < populationSize; i++) {
                ArrayList <Double> chromosome = new ArrayList <>();
                Random r = new Random();
                for (int j = 0; j < this._countOfGeneOfChromosome; j++)
                    chromosome.add((r.nextDouble((max - min) + 1) + min >= 0.5) ? 1.0 : 0.0);
                for (int k = 0; k < 3; k++)
                    chromosome.add(-1.0);
                this._population.add(chromosome);
            }
        }
        
        return true;
    }
    
    private boolean sumationOfGenesFitness() {
        for (int i = 0; i < this._population.size(); i++) {
            ArrayList <Double> temp = new ArrayList <> (this._population.get(i));
            double sum = 0.0;
            for (int j = 0; j < this._countOfGeneOfChromosome; j++) {
                sum += temp.get(j);
            }
            temp.set(this._countOfGeneOfChromosome, sum);
            this._population.set(i, temp);
        }
        
        return true;
    }
    
    private boolean sumationOfSquareGenesFitness() {
        for (int i = 0; i < this._population.size(); i++) {
            ArrayList <Double> temp = new ArrayList <> (this._population.get(i));
            double sum = 0.0;
            for (int j = 0; j < this._countOfGeneOfChromosome; j++) {
                sum += Math.pow(temp.get(j), 2);
            }
            temp.set(this._countOfGeneOfChromosome, sum);
            this._population.set(i, temp);
        }
        
        return true;
    }
    
    private boolean calculateFitness(String fitnessFunctionOption) {
        switch (fitnessFunctionOption) {
                case "sumation_of_genes" ->  {                   
                    this.sumationOfGenesFitness();
                    break;
                }
                case "sumation_of_square_genes" -> {
                    this.sumationOfSquareGenesFitness();
                    break;
                }
                default ->  { // sumation_of_genes
                    this.sumationOfGenesFitness();
                    break;
                }
            }
                       
        return true;
    }
    
    public boolean startOptimization() {
        for (int i = 0; i < this._maxEpoches; i++) {
            this.calculateFitness(this._fitnessOption);
            this.rouletteWheel();
            this._newPopulation = this.mutation(this._newPopulation);
            this._newPopulation = this.elitism(this._population, this._newPopulation);
            this._population = this._newPopulation;
            this._newPopulation = new ArrayList <>();
        }
        
        return true;
    }
    
    private boolean validateNormalizedFitnessValues() {
        double sumNormalizedFitnessValue = 0.0;
        for (ArrayList <Double> chromosome : this._population)
            sumNormalizedFitnessValue += chromosome.get(this._countOfGeneOfChromosome + 1);
        
        return (sumNormalizedFitnessValue >= 1.0);
    }
    
    private boolean fitnessScaling() {
        boolean hasNegativeFitnessValue = false;
        for (ArrayList <Double> chromosome : this._population) {
            if (chromosome.get(this._countOfGeneOfChromosome) < 0)
                hasNegativeFitnessValue = true;
        }
        
        if (hasNegativeFitnessValue) {
            double minFitnessValue = this._population.get(0).get(this._countOfGeneOfChromosome);
            for (int i = 0; i < this._population.size(); i++) {
                if (this._population.get(i).get(this._countOfGeneOfChromosome) < minFitnessValue)
                    minFitnessValue = this._population.get(i).get(this._countOfGeneOfChromosome);
            }
            
            minFitnessValue = Math.abs(minFitnessValue);
            for (int i = 0; i < this._population.size(); i++) {
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

        for (int i = 0; i < this._population.size(); i++) {
            ArrayList <Double> inner = new ArrayList <> (this._population.get(i));
            inner.set(this._countOfGeneOfChromosome + 1, inner.get(this._countOfGeneOfChromosome) / sumFitnessValue);
            this._population.set(i, inner);
        }
        
        if (this.validateNormalizedFitnessValues()) {
            Collections.sort(this._population, Collections.reverseOrder((a, b) -> Double.compare(a.get(this._countOfGeneOfChromosome), b.get(this._countOfGeneOfChromosome))));
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
            temp.set(this._countOfGeneOfChromosome + 2, sum);
            this._population.set(i, temp);
        }
        
        return (this._population.get(0).get(this._countOfGeneOfChromosome + 2) >= 1.0);
    }
    
    private boolean selectChromosomes() {
        ArrayList <ArrayList <Double>> selectedPopulation = new ArrayList <>();
        ArrayList <ArrayList <Double>> tempPopulation = new ArrayList <>(this._population);
        
        for (int i = 0; i < this._population.size(); i++) {
            Double random = Math.random(); // Random value in [0,1]
            
            for (int j = 0; j < tempPopulation.size(); j++) {
                if (random > tempPopulation.get(j).get(this._countOfGeneOfChromosome + 1)) {
                    selectedPopulation.add(tempPopulation.get(j));
                    tempPopulation.remove(j);
                    break;
                }     
            }
        }
        
        this.crossover(selectedPopulation);
        return (!selectedPopulation.isEmpty());
    }
    
    private boolean rouletteWheel() {
        this.fitnessScaling();
        this.normalizeFitnessValues();
        this.calculateCumulativeSumOfNormalizedFitnessValues();
        for (int i = 0; i < this._population.size() / 2; i++)
            this.selectChromosomes();
        
        return true;
    }

    private boolean singlePointCrossover(ArrayList <ArrayList <Double>> selectedChromosomes, int randomGenePosition) {
        ArrayList <Double> child1 = new ArrayList <>();
        ArrayList <Double> child2 = new ArrayList <>();

        for (int i = 0; i < randomGenePosition; i++) {
            child1.add(selectedChromosomes.get(0).get(i));
            child2.add(selectedChromosomes.get(1).get(i));
        }
        for (int i = randomGenePosition; i < this._countOfGeneOfChromosome; i++) {
            child1.add(selectedChromosomes.get(1).get(i));
            child2.add(selectedChromosomes.get(0).get(i));
        }
        for (int i = 0; i < 3; i++) {
            child1.add(-1.0);
            child2.add(-1.0);
        }

        this._newPopulation.add(child1);
        this._newPopulation.add(child2);
        
        return true;
    }
    
    private boolean doublePointCrossover(ArrayList <ArrayList <Double>> selectedChromosomes, int randomGenePosition, int secondRandomGenePosition) {
        ArrayList <Double> child1 = new ArrayList <>();
        ArrayList <Double> child2 = new ArrayList <>();

        for (int i = 0; i < randomGenePosition; i++) {
            child1.add(selectedChromosomes.get(0).get(i));
            child2.add(selectedChromosomes.get(1).get(i));
        }
        for (int i = randomGenePosition; i < secondRandomGenePosition; i++) {
            child1.add(selectedChromosomes.get(1).get(i));
            child2.add(selectedChromosomes.get(0).get(i));
        }
        for (int i = secondRandomGenePosition; i < this._countOfGeneOfChromosome; i++) {
            child1.add(selectedChromosomes.get(0).get(i));
            child2.add(selectedChromosomes.get(1).get(i));
        }

        for (int i = 0; i < 3; i++) {
            child1.add(-1.0);
            child2.add(-1.0);
        }
        
        this._newPopulation.add(child1);
        this._newPopulation.add(child2);
        
        return true;
    }
    
    //Produce 2 childrens from 2 parents
    private boolean crossover(ArrayList <ArrayList <Double>> selectedChromosomes) {
        Random r = new Random();
        int min = 1;
        int max = this._countOfGeneOfChromosome;
        int randomGenePosition = r.nextInt((max - min) + 1) + min;
        
        switch (this._crossoverOption) {
            case "single_point" ->  {             
                this.singlePointCrossover(selectedChromosomes, randomGenePosition);
                break;
            }
            case "double_point" -> {
                int secondRandomGenePosition;
                do {
                    secondRandomGenePosition = r.nextInt((max - min) + 1) + min;
                }while (secondRandomGenePosition == randomGenePosition);
                
                if (secondRandomGenePosition < randomGenePosition) {
                    int temp = randomGenePosition;
                    randomGenePosition = secondRandomGenePosition;
                    secondRandomGenePosition = temp;
                }
                               
                this.doublePointCrossover(selectedChromosomes, randomGenePosition, secondRandomGenePosition);         
                break;
            }
            default ->  { // single_point
                this.singlePointCrossover(selectedChromosomes, randomGenePosition);
                break;
            }
        }
        
        return true;
    }
    
    private ArrayList <ArrayList <Double>> mutation(ArrayList <ArrayList <Double>> chromosomes) {
        ArrayList <ArrayList <Double>> mutatedChromosomes = new ArrayList <>();
        Random r = new Random();
        this._probability_of_mutation = r.nextDouble();
        
        for (int i = 0; i < chromosomes.size(); i++) {
            ArrayList <Double> temp = new ArrayList<>(chromosomes.get(i));
            for (int j = 0; j < this._countOfGeneOfChromosome; j++) {
                Double random = r.nextDouble();
                if (random < this._probability_of_mutation)
                    temp.set(j, (temp.get(j) == 0.0 ? 1.0 : 0.0));
            }
            mutatedChromosomes.add(temp);
        }
        
        return mutatedChromosomes;
    }
    
    private ArrayList <ArrayList <Double>> elitism(ArrayList <ArrayList <Double>> oldPopulation,
                                                   ArrayList <ArrayList <Double>> newPopulation) {
        ArrayList <ArrayList <Double>> newGenePopulation = new ArrayList <>();
        Double x = this._population.size() * this._elitism_ratio;
        int number_of_elitism_chromosomes = x.intValue();

        for (int i = 0; i < number_of_elitism_chromosomes; i++) {
            newGenePopulation.add(oldPopulation.get(i));
        }
        for (int i = number_of_elitism_chromosomes; i < this._population.size(); i++) {
            newGenePopulation.add(newPopulation.get(i));
        }
        
        return newGenePopulation;
    }
             
}
