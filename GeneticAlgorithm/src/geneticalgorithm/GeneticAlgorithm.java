package geneticalgorithm;

/**
 *
 * @author Paraskevi Tokmakidou
 */
public class GeneticAlgorithm {

    public static void main(String[] args) {
        Population population = new Population(10, 2, "random");
        population.calculateFitness("sumation_of_genes");
        population.normalizeFitnessValues();  
    }
    
}
