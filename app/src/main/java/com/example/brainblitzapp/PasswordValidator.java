package com.example.brainblitzapp;

public class PasswordValidator {
    public static int checkPasswordStrength(String password){
        //initialize score and set flags for checks
        int score = password.length() > 7 && password.length() < 16 ? 1 : 0; //assign a score for acceptable password length

        boolean upperCase = false, lowerCase = false, digit = false, symbol = false; //set flags for password characteristics

        for(int i = 0; i < password.length(); i++){
            //has at least one upper case letter
            if(Character.isUpperCase(password.charAt(i)) && !upperCase){
                score++;
                upperCase = true;
            }

            //has at least one lower case letter
            if(Character.isLowerCase(password.charAt(i)) && !lowerCase) {
                score++;
                lowerCase = true;
            }

            //has at least one digit
            if(Character.isDigit(password.charAt(i)) && !digit) {
                score++;
                digit = true;
            }

            //has at least one symbol
            if((inRange(33, 47, password.charAt(i))
                    || inRange(58, 64, password.charAt(i))
                    || inRange(91, 96, password.charAt(i))
                    || inRange(123, 126, password.charAt(i))) && !symbol) {

                score++;
                symbol = true;
            }
        }

        return score;
    }

    //checks if "value" is in between "minValue" and "maxValue"
    private static boolean inRange(int minValue, int maxValue, int value) {
        if (maxValue < minValue) {
            return false;
        }
        return (value >= minValue) && (value <= maxValue);
    }
}
