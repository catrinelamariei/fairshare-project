package server.Authentication;

import java.security.SecureRandom;

public class CodeGenerator {
    public static String generateRandomString(){
        return generateRandomString(6);
    }
    public static String generateRandomString(int length){
        if (length<0){
            throw new IllegalArgumentException();
        }
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(symbols.length());
            char randomChar = symbols.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}
