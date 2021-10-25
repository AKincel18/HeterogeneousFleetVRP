# **Vehicle Routing Problem**
## Description
Java implementation of the Heterogenous Vehicle Routing Problem using four heuristic algorithms:
- [*local search*](https://github.com/AKincel18/HeterogeneousFleetVRP#local-search)
- [*simulated annealing*](https://github.com/AKincel18/HeterogeneousFleetVRP#simulated-annealing)
- [*tabu search*](https://github.com/AKincel18/HeterogeneousFleetVRP#tabu-search)
- [*genetic*](https://github.com/AKincel18/HeterogeneousFleetVRP#genetic-algorithm)
## **Running the program**
Program can be started by downloading the file **App.jar** (in *app* folder) and run the command:

`java -jar App.jar`

Minimal required java version: **11**
## **Local search**
![localSearch](https://user-images.githubusercontent.com/22658595/134400878-1028563b-0587-417d-92d7-cfad0e72b25f.png)

**Parameters:**
- *Method*: selection of the algorithm version - greedy or steepest
## **Simulated annealing**
![simulatedAnnealing](https://user-images.githubusercontent.com/22658595/134400879-0ccf11d0-7cc1-4068-a2df-89e481661712.png)

**Parameters:**
- *Probability*: the probability of accepting the initial solutions, which is used to determine the initial temperature
- *T0 iteration number*: value specifying the number of iterations during which the neighborhood solution is found when the initial temperature is determined
- *Cooling factor:* temperature cooling coefficient
- *Iteration number*: number of iterations for a given temperature 

## **Tabu search**
![tabuSearch](https://user-images.githubusercontent.com/22658595/134400871-8d5a196f-dd7c-4a87-8f77-96bd2de3cb3d.png)

**Parameters:**
- *Iteration number*: number of iterations
- *Tabu iteration number*: the number of iterations in which the use of taboo motion is prohibited
## **Genetic algorithm**
![genetic](https://user-images.githubusercontent.com/22658595/134400877-6c86a411-1d6c-4ca9-8c52-3cc476f1dc80.png)

**Parameters:**
- *Population size*: population size
- *Iteration number*: the number of iterations
- *Crossover probability*: crossover probability
- *Mutation probability*: mutation probability
- *Repeating crossover number*: a number that specifies how many times the crossing will be repeated, in case of receiving a solution that does not meet the assumptions
- *Selection method*: method of selection, the available methods are: roulette method, rank selection, tournament selection
- *Tournament size*: the number of individuals taking part in the tournament. An important parameter when choosing a tournament selection
- *Selective pressure*: selection coefficient important during ranking selection, the coefficient value is a floating-point number in the range [1.0,2.0]

## **Input**
The input file should have an extension compatible with Excel, e.g. xlsx. The first row is a header informing about the values that are in each cell. The values in the columns are as follows:
- A. City name
- B. City amount
- C. City latitude
- D. City longitude
- E. Empty column
- F. Vehicle name
- G. Vehicle amount
- H. Empty column
- I. City name of depot
- J. Depot latitude
- K. Depot longitude

**Example input file**
![input](https://user-images.githubusercontent.com/22658595/135891025-abde086e-0709-420f-89be-e793c2e27bb4.png)
## **Output**
After successful completion of the program, the solution is saved to a file with the same extension as the input file. The first line contains information about the total distance traveled by the vehicles, and the next line shows the individual routes of vehicles.

**Example output file**

![output](https://user-images.githubusercontent.com/22658595/135891030-5ed7322f-7941-4159-9170-bac55c399e7c.png)