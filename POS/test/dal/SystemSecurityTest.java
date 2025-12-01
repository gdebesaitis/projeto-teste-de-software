package dal;

import org.junit.jupiter.api.*;
import model.dto.Response;
import model.dto.UserDTO;
import static org.junit.jupiter.api.Assertions.*;

public class SystemSecurityTest {

    private DALManager dalManager;
    private Response response;

    @BeforeEach
    public void setUp() {
        dalManager = new DALManager();
        response = new Response();
    }

    @Test
    @DisplayName("Segurança: Tentativa de SQL Injection no Login")
    public void testSQLInjectionProtection() {
        // Payload comum que tentaria fazer o login ser sempre verdadeiro
        String maliciousPayload = "' OR '1'='1";
        
        UserDTO hackerUser = new UserDTO();
        hackerUser.setUsername(maliciousPayload);
        hackerUser.setPassword("randomPass");
        
        // Act: Tenta logar com o payload
        dalManager.verifyUser(hackerUser, response);
        
        // Assert
        // Se a injeção funcionasse, o verifyUser preencheria o user com dados do banco e success=true.
        // Como usa PreparedStatement, ele vai procurar um usuário literalmente chamado "' OR '1'='1" e não vai achar.
        
        assertFalse(response.isSuccessfull(), 
            "FALHA DE SEGURANÇA: O sistema permitiu login com SQL Injection!");
            
        // Verifica se a mensagem de erro é segura (não expõe stack trace do SQL)
        if (!response.messagesList.isEmpty()) {
            String msg = response.messagesList.get(0).message;
            assertFalse(msg.contains("Syntax error"), "Não deve retornar erros de sintaxe SQL para o utilizador.");
        }
        
        System.out.println("✓ Sistema protegido contra SQL Injection.");
    }
}