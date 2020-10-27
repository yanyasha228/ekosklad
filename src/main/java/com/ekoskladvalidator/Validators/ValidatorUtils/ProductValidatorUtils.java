package com.ekoskladvalidator.Validators.ValidatorUtils;


import com.ekoskladvalidator.ParseUtils.CssQueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class ProductValidatorUtils {


    @Autowired
    private CssQueryParser cssQueryParser;

    public boolean checkIfCssQueryForPriceIsValid(String url, String cssQuery){

        String strToVal = null;
        try {
            strToVal = removeTrashCharsFromPriceString(cssQueryParser.
                    getText(url, cssQuery));
        } catch (IOException e) {
            return false;
        }

        try {
            Float.parseFloat(strToVal);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public Optional<Float> getValidPriceByCssQuery(String url, String cssQuery){


        String strToVal = null;
        try {
            strToVal = removeTrashCharsFromPriceString(cssQueryParser.
                    getText(url, cssQuery));

        } catch (IOException e) {
            return Optional.empty();
        }

        try {
            return Optional.of(Float.parseFloat(strToVal));

        } catch (NumberFormatException e) {

            return Optional.empty();
        }


    }

    private String removeTrashCharsFromPriceString(String strForCl) {

        String retString = strForCl.replaceAll("[^\\d.,]", "");

        retString = retString.replaceAll(",", ".");

        retString = retString.replaceAll(" ", "");

        if (!retString.isEmpty()) {
            if (String.valueOf(retString.charAt(retString.length() - 1)).equalsIgnoreCase(".")) {
                char[] chArr = retString.toCharArray();
                for (int i = retString.length() - 1; i >= 0; i--) {
                    if (chArr[i] == '.') {
                        retString = retString.substring(0, retString.length() - 1);
                    } else break;
                }
            }
        }
        return retString;
    }

}
