package optimizationAlgorithms;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Paraskevi Tokmakidou
 */
public class OptimizationAlgorithms {

    public static void main(String[] args) { 
        System.out.println("WELCOME TO GENETIC ALGOTITHM");
        OptimizationAlgorithms.interactionWithUser();
        System.out.println("THANK'S USING OUR GENETIC ALGORITHM");
    }
    
    private static boolean interactionWithUser () {
        Scanner scanner = new Scanner(System.in);
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

            System.out.println("Enter the appropriate number from the list to select the fitness function to use:");
            System.out.println("1 - sumation of genes");
            System.out.println("2 - sumation of square genes");
            String fitnessFunctionOption;
            switch (scanner.nextInt()) {
                case 1  -> fitnessFunctionOption = "sumation_of_genes";
                case 2  -> fitnessFunctionOption = "sumation_of_square_genes";
                default -> fitnessFunctionOption = "sumation_of_genes";
            }
            
            System.out.println("Enter the appropriate number from the list to select the crossover function to use:");
            System.out.println("1 - single point");
            System.out.println("2 - double point");
            String crossoverFunctionOption;
            switch (scanner.nextInt()) {
                case 1  -> crossoverFunctionOption = "single_point";
                case 2  -> crossoverFunctionOption = "double_point";
                default -> crossoverFunctionOption = "single_point";
            }

            Genetic_Algorithm genAlg = new Genetic_Algorithm(chromosomesCount, genesCount, initializeGenesOption, 
                                                   fitnessFunctionOption, crossoverFunctionOption);
            double errorPercentage = genAlg.findBestChromosome();
            System.out.println("Error: " + errorPercentage + "%");
        }
        catch (InputMismatchException e)
        {
            System.out.println("Invalid option!");
            return false;
        }
        
        return true;
    }
}
