package test.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;

public class StaticAnalysisTest {

    @Test
    public void testViewsDoNotAccessDALOrJDBCOrContainBusinessLogic() throws IOException {
        Path uiDir = Paths.get("src", "ui");
        try (Stream<Path> files = Files.walk(uiDir)) {
            files.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    String content = Files.readString(p);
                    // Detect direct DB access patterns
                    boolean hasDalImport = content.contains("import dal.");
                    boolean hasSqlImport = content.contains("import java.sql") || content.contains("java.sql.");
                    boolean hasMySQLConnection = content.contains("MySQLConnection") || content.contains("new MySQLConnection");
                               // detect static fields that are not final (mutable statics)
                               boolean containsStaticField = content.matches("(?s).*static\\s+(?!final).*;.*");
                    // business logic heuristics: methods that declare calculateTotal or arithmetic in method bodies
                    boolean containsCalculateTotalMethod = content.contains("void calculateTotal(");
                    boolean containsArithmetic = content.matches("(?s).*\\*\\s*Integer.parseInt\(.*|.*\\*.*quantity.*|.*unitPrice.*\\*.*quantity.*");

                    String name = p.getFileName().toString();
                    // Fail the test if any UI file contains DB access or severe business logic patterns
                    if (hasDalImport || hasSqlImport || hasMySQLConnection || containsCalculateTotalMethod || containsStaticField || containsArithmetic) {
                        fail("UI class " + name + " contains suspicious code for MVC separation: dal/sql/mySQL or found business logic or static usage.");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

}
