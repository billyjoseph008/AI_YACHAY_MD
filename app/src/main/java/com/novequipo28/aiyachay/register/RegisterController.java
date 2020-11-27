package com.novequipo28.aiyachay.register;

public class RegisterController {

    private static String newUserName = "";
    private static String newUserEmail = "";
    private static String newUserGender = "";
    private static String newUserCountry = "";
    private static String newUserState = "";
    private static String newUserCity = "";
    public static String newUserPassword = "";
    public static int newUserAge;

    public static int fragmentPosition = 1;

    RegisterController(){

    }


    public static String getNewUserName() {
        return newUserName;
    }

    public static void setNewUserName(String newUserName) {
        RegisterController.newUserName = newUserName;
    }

    public static String getNewUserEmail() {
        return newUserEmail;
    }

    public static void setNewUserEmail(String newUserEmail) {
        RegisterController.newUserEmail = newUserEmail;
    }

    public static String getNewUserGender() {
        return newUserGender;
    }

    public static void setNewUserGender(String newUserGender) {
        RegisterController.newUserGender = newUserGender;
    }

    public static String getNewUserCountry() {
        return newUserCountry;
    }

    public static void setNewUserCountry(String newUserCountry) {
        RegisterController.newUserCountry = newUserCountry;
    }

    public static String getNewUserState() {
        return newUserState;
    }

    public static void setNewUserState(String newUserState) {
        RegisterController.newUserState = newUserState;
    }

    public static String getNewUserCity() {
        return newUserCity;
    }

    public static void setNewUserCity(String newUserCity) {
        RegisterController.newUserCity = newUserCity;
    }

    public static String getNewUserPassword() {
        return newUserPassword;
    }

    public static void setNewUserPassword(String newUserPassword) {
        RegisterController.newUserPassword = newUserPassword;
    }

    public static int getNewUserAge() {
        return newUserAge;
    }

    public static void setNewUserAge(int newUserAge) {
        RegisterController.newUserAge = newUserAge;
    }
}
