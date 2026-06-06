package god.joaopedro.client_scheduler.utils;

public class CpfValidator {

    public static boolean validate(String cpf) {
        if(cpf == null) return false;
        cpf = cpf.replaceAll("\\D", "");
        if(cpf.length() != 11) return false;

        if(cpf.chars().anyMatch(c -> !Character.isDigit(c))) return false;

        int sumFirstDigit = 0;
        for(int i = 0; i < cpf.length() - 2; i ++) {
            sumFirstDigit += (cpf.charAt(i) - '0') * (10 - i);
        }
        int restFirstDigit = sumFirstDigit % 11;
        if(restFirstDigit < 2 && (cpf.charAt(9) - '0') != 0) return false;
        if(restFirstDigit >= 2 && (cpf.charAt(9) - '0') != 11 - restFirstDigit) return false;

        int sumSecondDigit = 0;
        for(int i = 0; i < cpf.length() - 1; i ++) {
            sumSecondDigit += (cpf.charAt(i) - '0') * (11 - i);
        }
        int restSecondDigit = sumSecondDigit % 11;
        if(restSecondDigit < 2 && (cpf.charAt(10) - '0') != 0) return false;
        if(restSecondDigit >= 2 && (cpf.charAt(10) - '0') != 11 - restSecondDigit) return false;

        if(cpf.chars().distinct().count() == 1) return false;

        return true;
    }
}
