package me.intel.os.utils;

import me.intel.os.OS;
import me.intel.os.core.Security;

import java.util.regex.Pattern;
import java.util.Scanner;

public class Prompts {
    public static boolean YNPrompt() {
        System.out.print(OS.getLanguage().get("continue") + "? (Y/N): ");
        Scanner scan = new Scanner(System.in);
        while(true) {
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("Y")) {
                return true;
            } else if(input.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }
    public static String getPassword(String prompt, String req, boolean hash) {
        Pattern regex = Pattern.compile(req);
        while(true) {
            System.out.print(prompt + ": ");
            String input = new String(System.console().readPassword());
            if(regex.matcher(input).matches()) {
                if(hash) { return Security.hashPassword(input, 14); }
                return input;
            } else {
                System.out.println("Password is invalid!");
            }
        }
    }
    public static String getPassword(String prompt) {
        System.out.print(prompt + ": ");
        Scanner scan = new Scanner(System.in);
        while(true) {
            String input = scan.nextLine();
            return Security.hashPassword(input, 14);
        }
    }

    public static boolean YNPrompt(String msg) {
        System.out.print(msg + " (Y/N): ");
        Scanner scan = new Scanner(System.in);
        while(true) {
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("Y")) {
                return true;
            } else if(input.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }
}
