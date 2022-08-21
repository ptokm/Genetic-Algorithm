# Genetic Algorithm

### Step 1:  Initialize Population

Population  -->     [Chromosome 1, Chromosome 2, ....., Chromosome M]

where Chromosome --> 
                    [ Gene 1, Gene 2, ..., ..., Gene N]


### Step 2: Calculate fitness for each Chromosome

That's for a simple example is a sum of each gene of specific chromosome


### Step 3: Use roulette wheel 

3.1 Normalize the finess value of each Chromosome

     normalization = fitness_value / sum_of_all_fitness_value_of_chromosomes

3.2 Calculate the cumulative sum for each Chromosome

     Chromosome 1 has cumulative sum = cumulative_sum_of_chromosome1 + ... + cumulative_sum_of_chromosomeN

     Chromosome 2 has cumulative sum = cumulative_sum_of_chromosome2 + ... + cumulative_sum_of_chromosomeN

     ....

     Chromosome M has cumulative sum = cumulative_sum_of_chromosomeN


3.3 Select x of Chromosomes

      ForEach (x)

        * Take  a random value

        * If that random value > cumulative_sum_of_some_chromosome Then
           Take that Chromosome
