import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class CoffeeMachinePremiumIsABiggerMug {

    private static final String JOKES = """
            I reverse engineered my coffee machine. Premium mode was a bigger mug.
            I use Arch, btw. My entire personality is in the AUR.
            I rewrote my kettle in Rust. Boiling water now requires three lifetimes.
            Kubernetes scheduled my breakfast. The egg is still Pending.
            I audited my toaster. Critical finding: bread executes outside the sandbox.
            My homelab has five nines of uptime. The electricity bill has six.
            I put sudo in my dating profile. Still permission denied.
            I removed Windows. The bug is now a dotfile.
            I wrapped a todo list in Electron. Minimum requirements: another laptop.
            I asked AI to fix one bug. It proposed migrating the sun to TypeScript.
            My coffee machine went serverless. Cold brew is now billed per invocation.
            I decompiled a teapot. Its entire business model was HTTP 418.
            """;

    private static final Logger LOG = System.getLogger(CoffeeMachinePremiumIsABiggerMug.class.getName());

    private static final String REFERENCE = "https://example.com/api";

    public static String fetchJoke(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        connection.setConnectTimeout(5_000);
        connection.setReadTimeout(10_000);
        int status = connection.getResponseCode();
        try (InputStream input = status < HttpURLConnection.HTTP_BAD_REQUEST ? connection.getInputStream()
                : connection.getErrorStream()) {
            if (status != HttpURLConnection.HTTP_OK) {
                return "%s returned HTTP %d. Have you tried rewriting it in Rust?".formatted(url, status);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8).strip();
        }
    }

    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        for (String joke : JOKES.lines().toList()) {
            LOG.log(Level.INFO,
                    String.class.getDeclaredMethod("concat", String.class).invoke("sudo make me a joke: ", joke));
        }
        LOG.log(Level.INFO, fetchJoke(REFERENCE));
        LOG.log(Level.INFO, CoffeeMachinePremiumIsABiggerMug.class.getDeclaredMethod("fetchJoke", String.class)
            .invoke(null, "https://v2.jokeapi.dev/joke/Programming?format=txt"));
    }

}
