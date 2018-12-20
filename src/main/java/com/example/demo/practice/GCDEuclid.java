package com.example.demo.practice;

import java.util.Scanner;

/**
 * 使用欧几里得算法求最大公约数
 */
public class GCDEuclid {
    public static int gcd (int m, int n) {
        if (m % n ==0) {
            return n;
        }else {
            return gcd(n, m % n);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter first integer: ");
        int m = input.nextInt();
        System.out.print("Enter second integer: ");
        int n = input.nextInt();
        System.out.println("The greatest common divisor for " + m + " and " + n + " is " + gcd(m, n));
    }
}
