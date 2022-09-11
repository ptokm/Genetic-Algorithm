package geneticalgorithm;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Paraskevi Tokmakidou
 */
public class GeneticAlgorithm {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        
        System.out.println("WELCOME TO GENETIC ALGOTITHM");
        
        try
        {
            System.out.println("How many chromosomes do you want to use?");
            int chromosomesCount = scanner.nextInt();

            System.out.println("How many genes do you want for each chromosome?");
            int genesCount = scanner.nextInt();

            System.out.println("Enter the appropriate number from the list to select the initialization value of each chromosomes' genes:");
            System.out.println("1 - Initialize all to value: 0");
            System.out.println("2 - Initialize all to value: 1");
            System.out.println("3 - Initialize all to random values 0 or 1");
            String initializeGenesOption;
            switch (scanner.nextInt()) {
                case 1  -> initializeGenesOption = "0";
                case 2  -> initializeGenesOption = "1";
                case 3  -> initializeGenesOption = "random";
                default -> initializeGenesOption = "random";
            }

            System.out.println("Enter the appropriate number from the list to select the crossover function to use:");
            System.out.println("1 - single point");
            System.out.println("2 - double point");
            String crossoverFunction;
            switch (scanner.nextInt()) {
                case 1  -> crossoverFunction = "single_point";
                case 2  -> crossoverFunction = "double_point";
                default -> crossoverFunction = "single_point";
            }

            Population population = new Population(chromosomesCount, genesCount, initializeGenesOption, "sumation_of_genes");
            population.startOptimization(crossoverFunction);
        }
        catch (InputMismatchException e)
        {
            System.out.println("Invalid option!");
        }
        
        System.out.println("THANK'S USING OUR GENETIC ALGORITHM");
    }
    
}
