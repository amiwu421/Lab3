package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */

public class JSONTranslator implements Translator {

    private Map<String, Map<String, String>> translations = new HashMap<>();
    private Map<String, List<String>> codeTolanguages = new HashMap<>();
    private List<String> countryCodes = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);
            // in place to shut CheckStyle up
            String a3 = "alpha3";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmpJsonObject = jsonArray.getJSONObject(i);
                countryCodes.add(tmpJsonObject.getString(a3));

                List<String> languages = new ArrayList<>();
                Map<String, String> languageMap = new HashMap<>();

                for (String key: tmpJsonObject.keySet()) {
                    if (!"alpha3".equals(key) && !"id".equals(key) && !"alpha2".equals(key)) {
                        languages.add(key);
                        languageMap.put(key, tmpJsonObject.getString(key));
                    }

                }
                translations.put(tmpJsonObject.getString(a3).toUpperCase(), languageMap);
                codeTolanguages.put(tmpJsonObject.getString(a3).toUpperCase(), languages);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        return new ArrayList<>(codeTolanguages.get(country));
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String country, String language) {

        Map<String, String> languageMap = translations.get(country);
        if (languageMap != null) {
            return languageMap.get(language);
        }
        return null;
    }
}
