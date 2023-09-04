import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] argss) {

        String[] testStrings = {
                "ticks(Sick.vacinated)",
                "environment(infection-proximity)",
                "percent(evaluate(ent-2.p1),environment(e1))",
                "evaluate(ent-1.p1)",
                "environment(e2)",
                "ticks(ent-1.p1)",
                "environment(e1)",
                "random(2)",
                "55",
                "true",
                "tenaesh"
        };

        for (String test : testStrings) {
            if (test.matches("^[a-zA-Z_][a-zA-Z_0-9]*\\((.*)\\)$")) {
                String[] parts = test.split("\\(", 2);  // Limit the split to only happen at the first "("
                String methodName = parts[0];
                String argument = parts[1].substring(0, parts[1].length() - 1);
                System.out.println("\n---------\nExpression: " + test + "\nMethod Name: " + methodName + "\nArguments: " + argument);
            } else {
                System.out.println("\n-----\n" + test + " is a free parse value");
            }
        }
    }
}