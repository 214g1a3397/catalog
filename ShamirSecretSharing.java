import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;

public class ShamirSecretSharing {

    // Helper function to decode the value in a given base
    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Lagrange interpolation to find the constant term
    private static BigInteger findConstantTerm(Map<BigInteger, BigInteger> points, int k) {
        BigInteger constantTerm = BigInteger.ZERO;

        for (Map.Entry<BigInteger, BigInteger> entryI : points.entrySet()) {
            BigInteger xi = entryI.getKey();
            BigInteger yi = entryI.getValue();

            BigInteger term = yi;
            for (Map.Entry<BigInteger, BigInteger> entryJ : points.entrySet()) {
                BigInteger xj = entryJ.getKey();
                if (!xi.equals(xj)) {
                    term = term.multiply(xj).divide(xj.subtract(xi));
                }
            }
            constantTerm = constantTerm.add(term);
        }

        return constantTerm;
    }

    public static void main(String[] args) {
        try {
            // Load JSON file
            FileReader reader = new FileReader("input.json");
            JSONObject json = new JSONObject(new JSONTokener(reader));

            // Parse keys
            JSONObject keys = json.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Parse points and decode y values
            Map<BigInteger, BigInteger> points = new HashMap<>();
            for (int i = 1; i <= n; i++) {
                JSONObject point = json.getJSONObject(String.valueOf(i));
                int base = point.getInt("base");
                String value = point.getString("value");

                BigInteger x = BigInteger.valueOf(i);
                BigInteger y = decodeValue(value, base);

                points.put(x, y);
            }

            // Calculate the constant term
            BigInteger constantTerm = findConstantTerm(points, k);

            // Print the result
            System.out.println("Constant term (c) of the polynomial: " + constantTerm);

        } catch (IOException | JSONException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }
}
