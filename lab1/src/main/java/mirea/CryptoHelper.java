package mirea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.toUpperCase;

public class CryptoHelper {
    int shift = 3;
    Map<String, String> transMap = new HashMap<>();
    ArrayList<Map.Entry<String, Double>> decList;
    ArrayList<Map.Entry<String, Double>> encList;

    public String encrypt(String text) {
        char[] arr = text.toCharArray();
        for (int i = 0; i < text.length(); i++){
            if (isLetter(arr[i])) {
                arr[i] += shift;
                if (('z' < arr[i])&&(arr[i] <= ('z' + shift))) arr[i] -= 'z' - 'a' + 1;
                if (('Z' < arr[i])&&(arr[i] <= ('Z' + shift))) arr[i] -= 'Z' - 'A' + 1;
                if (('я' < arr[i])&&(arr[i] <= ('я' + shift))) arr[i] -= 'я' - 'а' + 1;
                if (('Я' < arr[i])&&(arr[i] <= ('Я' + shift))) arr[i] -= 'Я' - 'А' + 1;
            }
        }
        text = String.valueOf(arr);
        return text;
    }

    private boolean isLetter(char c) {
        return (('А' <= c)&&(c <= 'Я')) || (('а' <= c)&&(c <= 'я'));
    }

    public String decrypt(String text, String sample) {
        createTransMap(text, sample);
        return changeChars(text);
    }

    private String changeChars(String text) {
        char[] arr = text.toCharArray();
        for (int i = 0; i < text.length(); i++){
            if (isLetter(arr[i])) {
                arr[i] = transMap.get("" + toUpperCase(arr[i])).charAt(0);
            }
        }
        text = String.valueOf(arr);
        return text;
    }

    /* encoded -> decoded */
    private void createTransMap(String text, String sample) {
        Map <String, Double> decMap = createMap(sample);
        Map <String, Double> encMap = createMap(text);

        decList = new ArrayList<>(decMap.entrySet());
        decList.sort(Map.Entry.comparingByValue());
        encList = new ArrayList<>(encMap.entrySet());
        encList.sort(Map.Entry.comparingByValue());

        for (int i = 0; i < decList.size(); i++) {
            transMap.put(encList.get(i).getKey(), decList.get(i).getKey());
        }

        printTable(decList);
    }

    public String bgDecrypt(String text, String sample) {
        Map <String, Double> decMap = createBgMap(sample);
        Map <String, Double> encMap = createBgMap(text);

        boolean[] truth = checkTruth(this.decList);
        System.out.println(Arrays.toString(truth));
        correctTrans(truth, decMap, encMap);

        return changeChars(text);
    }

    private void correctTrans(boolean[] truth, Map<String, Double> decMap, Map<String, Double> encMap) {
        for (int i = 0; i < truth.length - 1; i++) {
            if (truth[i]) continue;
            boolean flag = false;
            String esymbol = encList.get(i).getKey();
            String dsymbol = decList.get(i).getKey();

            for (int j = 0; j < encList.size(); j++) {
                String enc2 = encList.get(j).getKey();
                String encBg = esymbol + enc2;
                //System.out.println("Checking " + dsymbol + transMap.get(enc2));
                if ((encMap.get(encBg)== null)&&(decMap.get(dsymbol + transMap.get(enc2))) == null)
                    continue;
                if (((encMap.get(encBg)== null)&&(decMap.get(dsymbol + transMap.get(enc2))) < 0.001))
                    continue;
                if ((decMap.get(dsymbol + transMap.get(enc2))== null)&&(encMap.get(esymbol + enc2)) < 0.001)
                    continue;
                if (!(encMap.get(encBg)== null) && !(decMap.get(dsymbol + transMap.get(enc2)) == null)
                        && (Math.abs(encMap.get(esymbol + enc2) -
                                decMap.get(dsymbol + transMap.get(enc2))) > 0.001)) flag = true;
            }
            if (flag){
                String nextKey = encList.get(i + 1).getKey();
                String temp = transMap.get(esymbol);
                System.out.println(transMap.get(nextKey) + " and " + transMap.get(esymbol));
                transMap.replace(esymbol,transMap.get(nextKey));
                transMap.replace(nextKey, temp);
                i++;
            }
        }
    }


    private boolean[] checkTruth(ArrayList<Map.Entry<String, Double>> map) {
        boolean[] res = new boolean[map.size()];
        for (int i = 0; i < map.size() - 1; i++) {
            double eps = 0.0008;
            res[i] = ((map.get(i + 1).getValue() - map.get(i).getValue()) > eps);
        }
        res[map.size() - 1] = true;
        return res;
    }

    /* Prints letter and frequency*/
    private void printTable(ArrayList<Map.Entry<String, Double>> map){
        for (Map.Entry<String, Double> entry : map) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }
    }

    public void checkPercentage(String description, String data1, String data2){
        int col = 0;
        int col1 = 0;
        for (int i = 0; i < data1.length(); i++){
            if (isLetter(data1.charAt(i))) {
                col++;
                if (toUpperCase(data1.charAt(i)) == toUpperCase(data2.charAt(i)))
                    col1++;
            }
        }
        System.out.println(description);
        System.out.print("Percent of truth: " + col1 + "/" + col  + " \n" + ((double)col1/col) + "\n");
    }

    Map<String, Double> createBgMap(String text){
        Map <String, Double> map = new HashMap<>();
        double num = 0;
        String bg = "";
        for (int i = 0; i < text.length(); i++){
            char c = (text.charAt(i));
            if ( isLetter(c)) {
                c = toUpperCase(c);
                bg += c;
                if (bg.length() > 2) bg = bg.substring(1);
                if (bg.length() == 2){
                    num++;
                    if (map.containsKey(bg)) {
                        double d = map.get(bg);
                        map.replace(bg, d + 1);
                    }
                    else map.put(bg, 1.0);
                }
            }
        }
        for (String s: map.keySet()){
            double d = map.get(s);
            map.replace(s, d/num);
        }
        return map;
    }

    Map<String, Double> createMap(String text){
        Map <String, Double> map = new HashMap<>();
        double num = 0;
        for (int i = 0; i < text.length(); i++){
            char c = (text.charAt(i));
            if (isLetter(c)) {
                c = toUpperCase(c);
                num++;
                if (map.containsKey(c + "")) {
                    double d = map.get(c + "");
                    map.replace(c + "", d + 1);
                }
                else map.put(c + "", 1.0);
            }
        }
        for (String s: map.keySet()){
            double d = map.get(s);
            map.replace(s, d/num);
        }
        return map;
    }
}
