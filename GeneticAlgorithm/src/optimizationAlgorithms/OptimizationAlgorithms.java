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
    
    private static void interactionWithUser () {
        System.out.println("Hint: Type -1 if you want to exit");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try
            {
                System.out.println("How many chromosomes do you want to use?");
                int chromosomesCount = scanner.nextInt();
                if (chromosomesCount == -1)
                    break;

                System.out.println("How many genes do you want for each chromosome?");
                int genesCount = scanner.nextInt();
                if (genesCount == -1)
                    break;

                String initializeGenesOption = "";
                while ( ! (initializeGenesOption.equalsIgnoreCase("0") || 
                        initializeGenesOption.equalsIgnoreCase("1") ||
                        initializeGenesOption.equalsIgnoreCase("random"))) {
                    System.out.println("Enter the appropriate number from the list to select the initialization value of each chromosomes genes:");
                    System.out.println("1 - Initialize all to value: 0");
                    System.out.println("2 - Initialize all to value: 1");
                    System.out.println("3 - Initialize all to random values 0 or 1");

                    switch (scanner.nextInt()) {
                        case 1  -> initializeGenesOption = "0";
                        case 2  -> initializeGenesOption = "1";
                        case 3  -> initializeGenesOption = "random";
                        case -1 -> { break; }
                    }
                }

                String fitnessFunctionOption = "";
                while ( ! (fitnessFunctionOption.equalsIgnoreCase("sumation_of_genes") || 
                          fitnessFunctionOption.equalsIgnoreCase("sumation_of_square_genes"))) {
                    System.out.println("Enter the appropriate number from the list to select the fitness function to use:");
                    System.out.println("1 - sumation of genes");
                    System.out.println("2 - sumation of square genes");

                    switch (scanner.nextInt()) {
                        case 1  -> fitnessFunctionOption = "sumation_of_genes";
                        case 2  -> fitnessFunctionOption = "sumation_of_square_genes";
                        case -1 -> { break; }
                    }
                }

                String crossoverFunctionOption = "";
                while ( ! (crossoverFunctionOption.equalsIgnoreCase("single_point") || 
                        crossoverFunctionOption.equalsIgnoreCase("double_point"))) {
                    System.out.println("Enter the appropriate number from the list to select the crossover function to use:");
                    System.out.println("1 - single point");
                    System.out.println("2 - double point");

                    switch (scanner.nextInt()) {
                        case 1  -> crossoverFunctionOption = "single_point";
                        case 2  -> crossoverFunctionOption = "double_point";
                        case -1 -> { break; }
                    }
                }

                Genetic_Algorithm genAlg = new Genetic_Algorithm(chromosomesCount, genesCount, initializeGenesOption, 
                                                       fitnessFunctionOption, crossoverFunctionOption);
                double errorPercentage = genAlg.findBestChromosome();
                System.out.println("Error: " + errorPercentage + "%");
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid option!");
            }
        }
    }
}
