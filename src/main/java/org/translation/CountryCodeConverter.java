package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {


    private final Map<String, String> codeToCountry;
    private final Map<String, String> countryToCode;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(Objects.requireNonNull(getClass()
                    .getClassLoader().getResource(filename)).toURI()));

            countryToCode = new HashMap<>();
            codeToCountry = new HashMap<>();
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\t");
                codeToCountry.put(parts[2].trim(), parts[0].trim());
                countryToCode.put(parts[0].trim(), parts[2].trim());
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return codeToCountry.get(code.toUpperCase());
    }

    /**
     * Returns the whole codeToCountry map.
     * @return the whole codeToCountry map.
    */
    public Map<String, String> getCodeToCountry() {
        return codeToCountry;
    }

    /**
     * Returns to whole countryToCode map.
     * @return countryToCode map.
     */
    public Map<String, String> getCountryToCode() {
        return countryToCode;
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return countryToCode.get(country);
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return codeToCountry.size();
    }
}
