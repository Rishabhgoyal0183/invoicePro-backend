package com.invoicePro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Locale;

public enum State {
    ANDHRA_PRADESH,
    ARUNACHAL_PRADESH,
    ASSAM,
    BIHAR,
    CHHATTISGARH,
    GOA,
    GUJARAT,
    HARYANA,
    HIMACHAL_PRADESH,
    JHARKHAND,
    KARNATAKA,
    KERALA,
    MADHYA_PRADESH,
    MAHARASHTRA,
    MANIPUR,
    MEGHALAYA,
    MIZORAM,
    NAGALAND,
    ODISHA,
    PUNJAB,
    RAJASTHAN,
    SIKKIM,
    TAMIL_NADU,
    TELANGANA,
    TRIPURA,
    UTTAR_PRADESH,
    UTTARAKHAND,
    WEST_BENGAL;


    // for serialization: write enum name or a nicer label if you want
    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static State fromString(String raw) {
        if (raw == null) return null;
        String normalized = normalize(raw);
        try {
            return State.valueOf(normalized);
        } catch (Exception e) {
            // Throwing IllegalArgumentException causes Jackson to produce a 400 (HttpMessageNotReadableException)
            throw new HttpMessageNotReadableException("Invalid state value: '" + raw + "'. Allowed states: " + String.join(", ", State.names()));
        }
    }

    private static String normalize(String s) {
        String up = s.trim().toUpperCase(Locale.ROOT);
        up = up.replaceAll("[^A-Z0-9]+", "_"); // non-alnum -> underscore
        up = up.replaceAll("_+", "_");
        if (up.startsWith("_")) up = up.substring(1);
        if (up.endsWith("_")) up = up.substring(0, up.length() - 1);
        return up;
    }

    // helper to list allowed values (optional)
    private static String[] names() {
        State[] vals = State.values();
        String[] out = new String[vals.length];
        for (int i = 0; i < vals.length; i++) out[i] = vals[i].name();
        return out;
    }

}
