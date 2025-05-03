/**************************************************************/
/* Elijah Rosal                                               */
/* Student ID: 017203992                                      */
/* CS 3310, Spring 2025                                       */
/* Programming Assignment 3                                   */
/* CanoeTripPlanner: This program computes the optimal canoe  */
/* rental costs and paths along the Los Angeles River using   */
/* Floyd's algorithm.                                         */
/**************************************************************/

import java.io.*;
import java.util.*;

public class Prog3 {
    static final int INF = Integer.MAX_VALUE / 2; // A constant to represent infinite cost

    /**
     * Method: main
     * Purpose: Reads the input file, computes the optimal rental costs 
     *          using Floyd's algorithm, and prints the optimal cost matrix
     *          and the optimal path from post 0 to n-1.
     * Parameters:
     *   String[] args: The command line arguments, where the first argument 
     *                  is the file name containing the cost matrix data.
     * Returns: void
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Prog3 <filename>");
            return;
        }

        try (Scanner scanner = new Scanner(new File(args[0]))) {
            int n = Integer.parseInt(scanner.nextLine().trim());
            int[][] cost = new int[n][n];
            int[][] next = new int[n][n];

            // Initialize the cost and next arrays
            for (int i = 0; i < n; i++) {
                Arrays.fill(cost[i], INF);
                cost[i][i] = 0;
                for (int j = 0; j < n; j++) {
                    next[i][j] = -1;
                }
            }

            // Read in the upper triangle of the cost matrix
            for (int i = 0; i < n - 1; i++) {
                String[] tokens = scanner.nextLine().trim().split("\\s+");
                for (int j = 0; j < tokens.length; j++) {
                    int dest = i + j + 1;
                    cost[i][dest] = Integer.parseInt(tokens[j]);
                    next[i][dest] = dest;
                }
            }

            // Apply Floyd's algorithm to find the shortest paths
            floydWarshall(n, cost, next);

            // Print the optimal cost matrix
            printCostMatrix(n, cost);

            // Print the optimal path from 0 to n-1
            System.out.println("\nOptimal Path from 0 to " + (n - 1) + ":");
            printPath(0, n - 1, next);
            System.out.println("\nTotal Cost: " + cost[0][n - 1]);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + args[0]);
        }
    }

    /**
     * Method: floydWarshall
     * Purpose: Apply Floyd's algorithm to find the shortest paths between 
     *          all pairs of posts.
     * Parameters:
     *   int n: The number of posts along the river.
     *   int[][] cost: The cost matrix representing the canoe rental costs.
     *   int[][] next: A matrix used to reconstruct the paths.
     * Returns: void
     */
    static void floydWarshall(int n, int[][] cost, int[][] next) {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Check if there is a valid path from i to k and k to j
                    if (cost[i][k] != INF && cost[k][j] != INF) {
                        int newCost = cost[i][k] + cost[k][j];
                        if (newCost < cost[i][j]) {
                            cost[i][j] = newCost;
                            next[i][j] = next[i][k]; // Update next to reflect the path via k
                        }
                    }
                }
            }
        }
    }
    

    /**
     * Method: printCostMatrix
     * Purpose: Prints the optimal cost matrix.
     * Parameters:
     *   int n: The number of posts.
     *   int[][] cost: The computed optimal cost matrix.
     * Returns: void
     */
    static void printCostMatrix(int n, int[][] cost) {
        System.out.println("Optimal Cost Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i < j)
                    System.out.print((cost[i][j] == INF ? "INF" : cost[i][j]) + "\t");
                else
                    System.out.print("-\t");
            }
            System.out.println();
        }
    }

    /**
     * Method: printPath
     * Purpose: Prints the optimal path from post u to post v.
     * Parameters:
     *   int u: The start post.
     *   int v: The destination post.
     *   int[][] next: The matrix used to reconstruct the path.
     * Returns: void
     */
    static void printPath(int u, int v, int[][] next) {
        if (next[u][v] == -1) {
            System.out.print("No path");
            return;
        }
        System.out.print(u);
        while (u != v) {
            u = next[u][v];
            System.out.print(" -> " + u);
        }
    }
}
