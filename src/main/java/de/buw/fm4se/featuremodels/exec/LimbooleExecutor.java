package de.buw.fm4se.featuremodels.exec;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Simple utility to execute the limboole executable
 * 
 * @author janri
 *
 */
public class LimbooleExecutor {

  // TODO change this according to your OS
  // windows
  private static final String LIMBOOLE_EXE = "lib/limboole.exe";
  // Linux x86 binary
  //private static final String LIMBOOLE_EXE = "lib/limboole-linux-x86.exe";
  // Linux amd64 binary
  //private static final String LIMBOOLE_EXE = "lib/limboole-linux-amd64.exe";
  // Mach-O 64-bit executable x86_64
  //private static final String LIMBOOLE_EXE = "lib/limbooleOSX";

  private static final String IN_FILE = "in.txt";
  private static final String OUT_FILE = "out.txt";
  private static final String ERROR_FILE = "error.txt";

  /**
   * A basic example of how to run Limboole
   * @param args
   */
  public static void main(String[] args) {
    String formula = "(car -> motor) | (comfort -> car)";
    try {
      System.out.println(runLimboole(formula, true));
    } catch (Exception e) {
      System.out.println("Invocation of Limboole on the following formula failed: " + formula);
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param formula  a formula in Limboole syntax
   * @param checkSAT whether to check satisfiability (set checkSAT to true) or
   *                 validity (set checkSAT to false)
   * @return output produced by Limboole
   * @throws IOException
   * @throws InterruptedException
   * @throws RuntimeException     in case Limboole produces any errors
   */
  public static String runLimboole(String formula, boolean checkSAT) throws IOException, InterruptedException {
    Files.deleteIfExists(Paths.get(IN_FILE));
    Files.deleteIfExists(Paths.get(OUT_FILE));
    Files.deleteIfExists(Paths.get(ERROR_FILE));

    Files.writeString(Paths.get(IN_FILE), formula);

    ProcessBuilder pb = new ProcessBuilder();
    if (checkSAT) {
      pb.command(LIMBOOLE_EXE, IN_FILE, "-s");
    } else {
      pb.command(LIMBOOLE_EXE, IN_FILE);
    }

    // redirect output and error to files
    pb.redirectOutput(Redirect.appendTo(new File(OUT_FILE)));
    pb.redirectError(Redirect.appendTo(new File(ERROR_FILE)));

    Process p = pb.start();
    p.waitFor();

    String errors = Files.readString(Paths.get(ERROR_FILE));
    if (errors.length() != 0) {
      throw new RuntimeException("Limboole call produced errors: " + errors);
    }
    return Files.readString(Paths.get(OUT_FILE));

  }
}
