/**************************************************************/
/* Elijah Rosal                                               */
/* Login ID: 017203992                                        */
/* CS 3310, Spring 2025                                       */
/* Programming Assignment 3                                   */
/* Prog3 class: Determines the optimal cost and sequence of   */
/* canoe rentals using dynamic programming.                   */
/**************************************************************/

import java.io.*;
import java.util.*;

public class Prog3 {
    public static void main(String[] args) throws IOException {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter input filename: ");
            String fileName = sc.nextLine();

            int n;
            int[][] cost;

            // Read input file
            try (Scanner fileScanner = new Scanner(new File(fileName))) {
                n = Integer.parseInt(fileScanner.nextLine().trim());
                cost = new int[n][n];

                // Initialize cost matrix with "infinity" values
                for (int[] row : cost)
                    Arrays.fill(row, Integer.MAX_VALUE);

                // Read cost matrix from file (upper triangular entries only)
                for (int i = 0; i < n - 1; i++) {
                    String[] tokens = fileScanner.nextLine().trim().split("\\s+");
                    for (int j = i + 1; j < n; j++) {
                        cost[i][j] = Integer.parseInt(tokens[j - i - 1]);
                    }
                }
            }

            // Compute all-pairs optimal cost matrix
            int[][] optimalCost = computeAllPairOptimalCosts(cost);

            // Print optimal cost matrix
            System.out.println("\nOptimal Cost Matrix:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i < j) {
                        String out = (optimalCost[i][j] == Integer.MAX_VALUE) ? "-" : String.valueOf(optimalCost[i][j]);
                        System.out.printf("%4s", out);
                    } else {
                        System.out.printf("%4s", "-");
                    }
                }
                System.out.println();
            }

            // Get path from 0 to n-1
            List<Integer> path = getOptimalPath(cost, n);

            // Print optimal rental path from 0 to n-1
            System.out.println("\nOptimal rental path from post 0 to post " + (n - 1) + ":");
            for (int i = 0; i < path.size() - 1; i++) {
                System.out.println("Rent canoe from post " + path.get(i) + " to post " + path.get(i + 1));
            }
            System.out.println("Total cost: " + optimalCost[0][n - 1]);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**************************************************************/
    /* Method: computeAllPairOptimalCosts                         */
    /* Purpose: Compute the minimum cost between all (i, j)       */
    /* Parameters:                                                */
    /* int[][] cost: direct rental cost matrix                    */
    /* Returns: int[][] - optimal cost matrix                     */
    /**************************************************************/
    private static int[][] computeAllPairOptimalCosts(int[][] cost) {
        int n = cost.length;
        int[][] dp = new int[n][n];

        // Initialize dp matrix with "infinity"
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
            dp[i][i] = 0;
        }

        // Compute all-pairs optimal costs
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = i; k < j; k++) {
                    if (cost[k][j] != Integer.MAX_VALUE && dp[i][k] != Integer.MAX_VALUE) {
                        dp[i][j] = Math.min(dp[i][j], dp[i][k] + cost[k][j]);
                    }
                }
                // Direct cost if no better path is found
                if (cost[i][j] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], cost[i][j]);
                }
            }
        }

        return dp;
    }

    /**************************************************************/
    /* Method: getOptimalPath                                     */
    /* Purpose: Reconstruct optimal path from post 0 to n-1       */
    /* Parameters:                                                */
    /* int[][] cost: original cost matrix                         */
    /* int n: number of posts                                     */
    /* Returns: List<Integer>: list of post indices in path       */
    /**************************************************************/
    private static List<Integer> getOptimalPath(int[][] cost, int n) {
        int[] minCost = new int[n];
        int[] prev = new int[n];

        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[0] = 0;
        prev[0] = -1;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (cost[i][j] != Integer.MAX_VALUE && minCost[i] + cost[i][j] < minCost[j]) {
                    minCost[j] = minCost[i] + cost[i][j];
                    prev[j] = i;
                }
            }
        }

        // Backtrack to construct path
        List<Integer> path = new ArrayList<>();
        for (int at = n - 1; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
}