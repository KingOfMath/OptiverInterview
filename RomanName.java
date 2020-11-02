import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class RomanName {

    // Complete the getSortedList function below.
    static List<String> getSortedList(List<String> names) {
        int listSize = names.size();
        for (int i = 0; i < listSize; i++) {
            for (int j = i + 1; j < listSize; j++) {
                String firstFullName = names.get(i);
                String secondFullName = names.get(j);
                String[] firstName = firstFullName.split(" ");
                String[] secondName = secondFullName.split(" ");
                String f1 = firstName[0];
                String f2 = secondName[0];
                int o1 = getRomanNumerals(firstName[1]);
                int o2 = getRomanNumerals(secondName[1]);

                // compare
                if ((f1.compareTo(f2) > 0) || (f1.equals(f2) && o1 > o2)) {
                    String temp = secondFullName;
                    names.set(j, firstFullName);
                    names.set(i, temp);
                }
            }
        }
        return names;
    }

    static int getRomanNumerals(String name) {
        HashMap<Character, Integer> hash = new HashMap<>();
        char[] nameArr = name.toCharArray();
        int nameSize = nameArr.length, sum = 0, prev = 0, curr = 0;

        // key pairs
        hash.put('I', 1);
        hash.put('V', 5);
        hash.put('X', 10);
        hash.put('L', 50);

        // calculate
        for (int i = 0; i < nameSize; i++) {
            curr = hash.get(nameArr[i]);
            if (curr < prev) {
                sum -= curr;
            } else {
                sum += curr;
            }
            prev = curr;
        }

        return sum;
    }


    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int namesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> names = new ArrayList<>();

        for (int i = 0; i < namesCount; i++) {
            String namesItem = bufferedReader.readLine();
            names.add(namesItem);
        }

        List<String> res = getSortedList(names);

        for (int i = 0; i < res.size(); i++) {
            bufferedWriter.write(res.get(i));

            if (i != res.size() - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}