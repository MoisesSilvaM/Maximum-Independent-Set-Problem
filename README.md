# Maximum-Independent-Set-Problem

Creating new algorithms through genetic programing to solve the Maximum Independent Set Problem

For this project a genetic programming library called ECJ was used, this allows to make the evolution of the programs, but it is necessary to program everything related to the specific problem.

Then I explain a little about the project:

The project is divided into 3 folders, functions, model and terminals. In the folder functions are the files that give the logic to the tree that you want to build. In the model folder there are the files that allow the internal operation of the program. In the Terminals folder are the terminals that are the ones that contain the actions that generate the solution.

To launch the experiments write a Scipt where the classpath is defined, other important libraries of the project, the location of java, the ec.Evolve (which is the point of entry to ECJ), and the parameters with which to work. In these parameters you must define the problem that will be evaluated in the evolution, in this case "model/MISProblemEvo".

MISProblemEvo has 3 main functions: Setup, Evaluate and Describe . In Setup the data is loaded, in Evaluate the instances are evaluated and the best individual is stored when it is found, in Describe the outputs are ordered.

The class IndependentSet is one of the main class of the project, there the parameters of the instance are written, the functions and specific terminals designed for the problem.
