package pl.gdan.elsy.tool;

/**
 * @author elser
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class StrOper {


    public static String expand(String str, int length, boolean left, char c) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            strBuf.append(c);
        }
        if (left) {
            return str + strBuf.toString();
        } else {
            return strBuf.toString() + str;
        }
    }

    public static String expand(String str, int length, boolean left, String pattern) {
        StringBuffer strBuf = new StringBuffer();
        for (; strBuf.length() < length; ) {
            strBuf.append(pattern);
        }
        strBuf.setLength(length);
        if (left) {
            return str + strBuf.toString();
        } else {
            return strBuf.toString() + str;
        }
    }

    public static String longString(int length) {
        return expand("", length, true, "1234567890");
    }

    public static String bake0(String str, int length, boolean left) {
        if (str == null) {
            return expand("", length, true, '0');
        } else {
            if (str.length() > length) {
                System.out.println(">bake: (str.length()=" + str.length() + " > length=" + length + ") for: >" + str + "<");
                Exception e = new Exception();
                e.printStackTrace();
            }
            return expand(str, length - str.length(), left, '0');
        }
    }

    public static String bake(String str, int length, boolean left) {
        if (str == null) {
            return expand("", length, true, ' ');
        } else {
            if (str.length() > length) {
                System.out.println(">bake: (str.length()=" + str.length() + " > length=" + length + ") for: >" + str + "<");
                Exception e = new Exception();
                e.printStackTrace();
            }
            return expand(str, length - str.length(), left, ' ');
        }
    }

    /**
     * Method makeRandomString.
     *
     * @param i
     * @return String
     */
    public static String makeRandomString(int length, boolean bigLetters) {
        String str = "";
        char chrIndex = (bigLetters) ? 'A' : 'a';
        for (int i = 0; i < length; i++) {
            str += ((char) (Rand.i(26) + chrIndex));
        }
        return str;
    }

    /**
     * Method makeRandomDigitString.
     *
     * @param i
     * @return String
     */
    public static String makeRandomDigitString(int length) {
        String str = "";
        for (int j = 0; j < length; j++) {
            str += ((char) (Rand.i(10) + '0'));
        }
        return str;
    }


    /**
     * Method makeRandomLastName.
     *
     * @return String
     */
    public static String makeRandomLastName() {
        String[] lastNames = {"MEIJ VAN DER", "CAMPBELL", "KAMPEN VAN", "AKSE", "WILLIGENBURG", "THIJSSENS-HERK",
                "DILLEWIJN VAN", "LANG", "VAN DER SLIK", "O'KEEFE", "13 GROOMS", "LANGE DE", "GOTTGENS-VAN"};
        return lastNames[Rand.i(lastNames.length)];
    }

    public static String makeRandomFirstName() {
        String[] names = {"John", "Tom", "Mike", "Andrew", "Witold", "Jerry", "Albert",
                "Edmund", "Kathrine", "Bruce", "Caroline", "Carol", "Donald"};
        return names[Rand.i(names.length)];
    }

    /**
     * Method makeRandomChar.
     *
     * @param b
     * @return char
     */
    public static char makeRandomChar(boolean bigLetters) {
        char chrIndex = (bigLetters) ? 'A' : 'a';
        return (char) (Rand.i(26) + chrIndex);
    }

    /**
     * Method makeRandomLogin.
     *
     * @return String
     */
    public static String makeRandomLogin(int i) {
        String[] names = {"AGM", "AIC", "AKA", "AKS",
                "ARW", "ASB", "ATS", "AWE", "BAE", "BAK", "BAS", "BLT", "BND", "BOF", "BRH", "BRN", "BRR", "MUH",
                "BSA", "BTN", "BUI", "CBN", "CJA", "CLE", "COL", "CRA", "CST", "DAL", "DEB", "DEW", "DIL", "DRL",
                "DRU", "DUW", "DYK", "EEL", "EGM", "END", "ESM", "EYK", "FHB", "FOL", "GIE", "GOE", "GOV", "GRO"};
        if (i == -1) {
            return names[Rand.i(names.length)];
        } else {
            return names[i % names.length];
        }
    }

    /**
     * Method RandAcftReg.
     *
     * @return String
     */
    private static String[] acftRegs = {"PHAHI", "PHBUH", "PHMCI", "PHMCF", "PHMCL"};

    public static String[] acftRegs2 = {"PHAHF", "PHAHI", "PHBUH", "PHJCH", "PHJCT", "PHJRP", "PHKZO", "PHKZP", "PHKZR",
            "PHMCE", "PHMCF", "PHMCG", "PHMCH", "PHMCI", "PHMCL", "PHMCM", "PHMCN", "PHMCP", "PHMCR",
            "PHMCS", "PHMCT", "PHMCU", "PHMCV", "PHMCW", "PHMCT", "PHMPD"};


    public static String randAcftReg() {
        return randAcftReg(Rand.i(acftRegs.length));
    }

    public static String randAcftReg(int i) {
        return acftRegs[i % acftRegs.length];
    }

    public static String correspondingAcftICAO(String acftReg) {
        String[] acftICAO = {"MD11", "B737", "B744", "B742", "F70", "B763"};
        for (int i = 0; i < acftRegs.length; i++) {
            if (acftRegs[i].equals(acftReg)) {
                return acftICAO[i % acftICAO.length];
            }
        }
        System.out.println("Couldn`t find corresponding AcftICAO");
        new Exception().printStackTrace();
        System.exit(0);
        return "";
    }

    /**
     * Method cut.
     *
     * @param string
     * @param TABcontent
     */
    public static String cut(String string, int len) {
        if (string.length() < len) {
            return string;
        } else {
            return string.substring(0, len - 1);
        }
    }

    public static String extractClassName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(".") + 1, fullName.length());
    }

    public static void replace(StringBuffer sb, int i, String string) {
        sb.replace(i, i + string.length(), string);
    }

    /**
     * @param sb
     * @param i
     * @param string
     */
    public static void replaceLeft(StringBuffer sb, int i, int len, String string) {
        replace(sb, i, bake(string, len, false));
    }

    /**
     * Returns random airport IATA code, which is not equal to the airports passed by method`s arguments.
     */
    public static String randAirp(String last1, String last2) {
        String[] names = {"AMS", "MIA", "SJO", "GDN", "WAW", "KRK", "MEX"};
        int i = Rand.i(names.length);

        while (names[i].equalsIgnoreCase(last1) || names[i].equalsIgnoreCase(last2)) {
            i = Rand.i(names.length);
        }
        return names[i];
    }

    /**
     * Returns random airport IATA code, which is not equal to the airport passed by method`s argument.
     */
    public static String randAirp(String last) {
        return randAirp(last, "");
    }

    /**
     * Returns random airport IATA code.
     */
    public static String randAirp() {
        return randAirp("", "");
    }

    public static String makeRandomFltNo() {
        int fltNoLen = Rand.i(3) + 2;
        return StrOper.makeRandomDigitString(fltNoLen);
    }

}
