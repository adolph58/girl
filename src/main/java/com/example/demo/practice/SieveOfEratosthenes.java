package com.example.demo.practice;

import java.util.Scanner;

/**
 * 寻找素数高效算法-埃拉托色尼算法
 */
public class SieveOfEratosthenes {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Find all prime numbers <= n, enter n: ");
        int n = input.nextInt();
        boolean[] primes = new boolean[n + 1];

        for (int i = 0; i < primes.length; i++) {
            primes[i] = true;
        }

        for (int k = 2; k <= n / k; k++) {
            if (primes[k]) {
                for (int i = k; i <= n / k; i ++) {
                    primes[k * i] = false;
                }
            }
        }

        int count = 0;
        for (int i = 2; i < primes.length; i++) {
            if (primes[i]) {
                count++;
                if (count % 10 == 0) {
                    System.out.printf("%7d\n", i);
                } else {
                    System.out.printf("%7d", i);
                }
            }
        }

        System.out.println("\n" + count + " primes(s) less than or equal to " + n);
    }
}
