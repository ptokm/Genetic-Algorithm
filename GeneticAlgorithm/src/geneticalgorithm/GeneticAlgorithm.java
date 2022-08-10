package geneticalgorithm;

/**
 *
 * @author Paraskevi Tokmakidou
 */
public class GeneticAlgorithm {

    public static void main(String[] args) {
        Population population = new Population(10, 0);
        population.calculateFitness("step_function");
    }
    
}
