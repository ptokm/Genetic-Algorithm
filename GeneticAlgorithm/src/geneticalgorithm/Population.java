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
    private ArrayList <ArrayList <Double>> _population;
    private ArrayList <ArrayList <Double>> _selectedPopulation;
    private ArrayList <ArrayList <Double>> _childrens;
    private final int _countOfGeneOfChromosome;
    private final Double _elitism_ratio;
    
    Population(int populationSize, int geneSize,  String initializeGeneOption, String fitnessFunctionOption) {
        this._population = new ArrayList <>();
        this._countOfGeneOfChromosome = geneSize;
        this._elitism_ratio = 0.1;
        
        this.initializePopulation(populationSize, initializeGeneOption);
        this.calculateFitness(fitnessFunctionOption);
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
            for (short i = 0; i < this._countOfGeneOfChromosome; i++)
                chromosome.add(initializationValue);
            
            for (short i = 0; i < populationSize; i++)
                this._population.add(chromosome);
            
            this._population = this.mutation(this._population);
        } else {
            int min = 0;
            int max = 1;
            for (short i = 0; i < populationSize; i++) {
                ArrayList <Double> chromosome = new ArrayList <>();
                Random r = new Random();
                for (short j = 0; j < this._countOfGeneOfChromosome; j++)
                    chromosome.add((r.nextDouble((max - min) + 1) + min >= 0.5) ? 1.0 : 0.0);
                this._population.add(chromosome);
            }
        }
        
        return true;
    }
    
    private boolean sumationOfGenesFitness() {
        for (short i = 0; i < this._population.size(); i++) {
            ArrayList <Double> temp = new ArrayList <> (this._population.get(i));
            double sum = 0.0;
            for (short j = 0; j < temp.size(); j++) {
                sum += temp.get(j);
            }
            temp.add(sum);
            this._population.set(i, temp);
        }
        
        System.out.println("--> "+this._population);
        return true;
    }
    
    private boolean sumationOfSquareGenesFitness() {
        for (short i = 0; i < this._population.size(); i++) {
            ArrayList <Double> temp = new ArrayList <> (this._population.get(i));
            double sum = 0.0;
            for (short j = 0; j < temp.size(); j++) {
                sum += Math.pow(temp.get(j), 2);
            }
            temp.add(sum);
            this._population.set(i, temp);
        }
        
        System.out.println("--> "+this._population);
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
    
    public boolean startOptimization(String crossoverFunction) {
        this.rouletteWheel(2);
        this.crossover(crossoverFunction);
        this.mutation();
        this.elitism();
        
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
            temp.add(sum);
            this._population.set(i, temp);
        }
        
        return (this._population.get(0).get(this._countOfGeneOfChromosome + 2) >= 1.0);
    }
    
    private boolean selectChromosomes(int numberOfSelection ) {
        this._selectedPopulation = new ArrayList <>();
        ArrayList <ArrayList <Double>> tempPopulation = new ArrayList <>(this._population);
        
        for (short i = 0; i < numberOfSelection; i++) {
            Double random = Math.random(); // Random value in [0,1]
            
            for (short j = 0; j < tempPopulation.size(); j++) {
                if (random > tempPopulation.get(j).get(this._countOfGeneOfChromosome + 1)) {
                    this._selectedPopulation.add(tempPopulation.get(j));
                    tempPopulation.remove(j);
                    break;
                }     
            }
        }
        
        return (!this._selectedPopulation.isEmpty());
    }
    
    private boolean rouletteWheel(int numberOfChromosomesToBeSelected) {
        this.fitnessScaling();
        this.normalizeFitnessValues();
        this.calculateCumulativeSumOfNormalizedFitnessValues();
        this.selectChromosomes(numberOfChromosomesToBeSelected);
        return true;
    }

    private boolean singlePointCrossover(int randomGenePosition) {
        ArrayList <Double> child1 = new ArrayList <>();
        ArrayList <Double> child2 = new ArrayList <>();

        for (short i = 0; i < randomGenePosition; i++) {
            child1.add(this._selectedPopulation.get(0).get(i));
            child2.add(this._selectedPopulation.get(1).get(i));
        }
        for (int i = randomGenePosition; i < this._countOfGeneOfChromosome; i++) {
            child1.add(this._selectedPopulation.get(1).get(i));
            child2.add(this._selectedPopulation.get(0).get(i));
        }

        this._childrens.add(child1);
        this._childrens.add(child2);
        
        return true;
    }
    
    private boolean doublePointCrossover(int randomGenePosition, int secondRandomGenePosition) {
        ArrayList <Double> child1 = new ArrayList <>();
        ArrayList <Double> child2 = new ArrayList <>();

        for (short i = 0; i < randomGenePosition; i++) {
            child1.add(this._selectedPopulation.get(0).get(i));
            child2.add(this._selectedPopulation.get(1).get(i));
        }
        for (int i = randomGenePosition; i < secondRandomGenePosition; i++) {
            child1.add(this._selectedPopulation.get(1).get(i));
            child2.add(this._selectedPopulation.get(0).get(i));
        }
        for (int i = secondRandomGenePosition; i < this._countOfGeneOfChromosome; i++) {
            child1.add(this._selectedPopulation.get(0).get(i));
            child2.add(this._selectedPopulation.get(1).get(i));
        }

        this._childrens.add(child1);
        this._childrens.add(child2);
        
        return true;
    }
    
    //Produce 2 childrens from 2 parents
    private boolean crossover(String crossoverOption) {
        if (this._countOfGeneOfChromosome <= 2 && crossoverOption.equals("double_point")) {
            System.out.println("Cannot use double point crossover. The number of chromosomes' genes is not sufficient.");
            return false;
        }
        
        this._childrens = new ArrayList<>();
        Random r = new Random();
        int min = 1;
        int max = this._countOfGeneOfChromosome - 1;
        int randomGenePosition = r.nextInt((max - min) + 1) + min;
        
        switch (crossoverOption) {
            case "single_point" ->  {             
                this.singlePointCrossover(randomGenePosition);
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
                               
                this.doublePointCrossover(randomGenePosition, secondRandomGenePosition);         
                break;
            }
            default ->  { // single_point
                this.singlePointCrossover(randomGenePosition);
                break;
            }
        }
        
        return true;
    }
    
    private boolean mutation() {
        Random r = new Random();
        Double probability_of_mutation = r.nextDouble();
        
        ArrayList <ArrayList <Double>> mutationChildrens = new ArrayList <>();
        for (short i = 0; i < this._childrens.size(); i++) {
            ArrayList <Double> temp = new ArrayList <>(this._childrens.get(i));
            for (short j = 0; j < temp.size(); j++) {
                Double random = r.nextDouble();
                if (random < probability_of_mutation)
                    temp.set(j, (temp.get(j) == 0.0 ? 1.0 : 0.0));
            }
            mutationChildrens.add(temp);
        }
        
        return true;
    }
    
    private ArrayList <ArrayList <Double>> mutation(ArrayList <ArrayList <Double>> chromosomes) {
        Random r = new Random();
        Double probability_of_mutation = r.nextDouble();
        
        ArrayList <ArrayList <Double>> mutationChildrens = new ArrayList <>();
        for (short i = 0; i < chromosomes.size(); i++) {
            ArrayList <Double> temp = new ArrayList <>(chromosomes.get(i));
            for (short j = 0; j < temp.size(); j++) {
                Double random = r.nextDouble();
                if (random < probability_of_mutation)
                    temp.set(j, (temp.get(j) == 0.0 ? 1.0 : 0.0));
            }
            
            mutationChildrens.add(temp);
        }
        
        return mutationChildrens;
    }
    
    private boolean elitism() {
        Double x = this._population.size() * this._elitism_ratio;
        int number_of_elitism_chromosomes = x.intValue();

        ArrayList <ArrayList <Double>> elitismPopulation = new ArrayList <>();
        for (short i = 0; i < number_of_elitism_chromosomes; i++) {
            elitismPopulation.add(this._population.get(i));
        }
        
        return true;
    }
    
}
