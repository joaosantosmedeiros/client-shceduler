package god.joaopedro.client_scheduler.utils;

import java.util.List;

public class CrmValidator {

    private final static List<String> ESTADOS = List.of(
            "AC", "AL", "AP", "AM",
            "BA",
            "CE",
            "DF",
            "ES",
            "GO",
            "MA", "MS", "MT", "MG",
            "PA","PB","PE", "PI", "PR",
            "RJ", "RN", "RO", "RR", "RS",
            "SC", "SE", "SP",
            "TO"
    );

    public static boolean validate(String crm) {
        if(crm == null)
            return false;
        crm = crm.trim().toUpperCase();

        if(crm.length() > 15)
            return false;
        if(!crm.contains("/") || crm.replaceAll("[^/]", "").length() > 1)
            return false;

        int indexOfBackSlash = crm.indexOf("/");
        if(indexOfBackSlash < crm.length() - 3)
            return false;

        String digits = crm.substring(0, indexOfBackSlash);
        String state = crm.substring(indexOfBackSlash + 1);

        if(!ESTADOS.contains(state))
            return false;

        return digits.replaceAll("\\d", "").isEmpty();
    }
}
