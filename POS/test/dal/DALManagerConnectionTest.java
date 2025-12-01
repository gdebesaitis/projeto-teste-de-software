package dal;

import org.junit.jupiter.api.*;
import dal.db.MySQLConnection;
import model.dto.Response;
import model.dto.UserDTO;
import model.dto.Message;
import model.dto.MessageType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de Conexão do DALManager - Gui D.
 * 
 * Objetivo: Verificar o comportamento do DALManager ao lidar com diferentes
 * cenários de conexão ao banco de dados, incluindo falhas e sucessos.
 * 
 * @author Gui D.
 */
public class DALManagerConnectionTest {
    
    private DALManager dalManager;
    private Response response;
    
    @BeforeEach
    public void setUp() {
        response = new Response();
    }
    
    /**
     * Teste 1: DALManager com Conexão Bem-Sucedida
     * 
     * Pré-condições:
     * - Serviço MySQL deve estar ativo
     * - Credenciais corretas no DALManager.java
     * - Banco de dados 'pos' deve existir com tabela 'users'
     * 
     * Passos:
     * 1. Criar instância de DALManager
     * 2. Tentar carregar lista de usuários
     * 3. Verificar se não há mensagens de erro de conexão
     * 
     * Resultado Esperado:
     * - Não deve haver mensagens de erro de conexão
     * - Sistema deve funcionar normalmente
     */
    @Test
    @DisplayName("Teste 1: DALManager funciona corretamente com MySQL ativo")
    public void testDALManagerWithSuccessfulConnection() {
        // Arrange
        dalManager = new DALManager();
        
        // Act
        dalManager.getUsers(response);
        
        // Assert
        boolean hasConnectionError = response.messagesList.stream()
            .anyMatch(msg -> msg.message.contains("Database Connection issue"));
        
        assertFalse(hasConnectionError, 
            "Não deve haver erro de conexão quando MySQL está ativo e configurado corretamente");
        System.out.println("✓ Teste 1 PASSOU: DALManager conectou ao banco com sucesso");
    }
    
    /**
     * Teste 2: DALManager com MySQL Offline
     * 
     * IMPORTANTE: Este teste requer modificação manual para simular falha
     * 
     * Pré-condições para execução manual:
     * 1. DESLIGUE o serviço MySQL
     * 2. Execute este teste
     * 3. RELIGUE o serviço MySQL após o teste
     * 
     * OU
     * 
     * Modifique temporariamente DALManager.java linha 30:
     * De: this.mySQL = new MySQLConnection("pos", "root", "Admin123$");
     * Para: this.mySQL = new MySQLConnection("pos", "root", "Admin123$") {
     *           @Override
     *           public Connection getConnection() {
     *               return null; // Simula falha
     *           }
     *       };
     * 
     * Resultado Esperado:
     * - Sistema deve adicionar mensagem de erro amigável
     * - Sistema NÃO deve travar (crashar)
     * - Mensagem deve ser: "Database Connection issue please contact customer services."
     */
    @Test
    @DisplayName("Teste 2: DALManager trata corretamente MySQL offline")
    //@Disabled("Requer MySQL offline ou modificação manual - Execute manualmente quando necessário")
    public void testDALManagerWithOfflineMySQL() {
        // INSTRUÇÕES:
        // 1. Desligue o MySQL: net stop MySQL80 (Windows) ou sudo systemctl stop mysql (Linux)
        // 2. Remova @Disabled desta anotação
        // 3. Execute o teste
        // 4. Religue o MySQL: net start MySQL80 (Windows) ou sudo systemctl start mysql (Linux)
        
        // Arrange
        dalManager = new DALManager();
        
        // Act
        dalManager.getUsers(response);
        
        // Assert
        assertFalse(response.isSuccessfull(), 
            "A resposta não deve ser bem-sucedida quando MySQL está offline");
        
        boolean hasConnectionError = response.messagesList.stream()
            .anyMatch(msg -> 
                msg.message.contains("Database Connection issue") &&
                msg.type == MessageType.Exception);
        
        assertTrue(hasConnectionError, 
            "Deve haver mensagem de erro de conexão quando MySQL está offline");
        
        assertEquals("Database Connection issue please contact customer services.", 
            response.messagesList.get(0).message,
            "Mensagem de erro deve ser amigável e informativa");
        
        System.out.println("✓ Teste 2 PASSOU: DALManager tratou MySQL offline sem travar");
    }
    
    /**
     * Teste 3: DALManager com Credenciais Incorretas
     * 
     * IMPORTANTE: Este teste requer modificação manual
     * 
     * Pré-condições para execução manual:
     * 1. Modifique DALManager.java linha 30
     * 2. Altere a senha para uma incorreta: "SenhaErrada123"
     * 3. Execute este teste
     * 4. RESTAURE a senha correta após o teste
     * 
     * Resultado Esperado:
     * - Sistema deve adicionar mensagem de erro
     * - Sistema NÃO deve travar
     * - Mensagem deve indicar problema de conexão
     */
    @Test
    @DisplayName("Teste 3: DALManager trata corretamente falha de autenticação")
    //@Disabled("Requer modificação manual das credenciais - Execute manualmente quando necessário")
    public void testDALManagerWithWrongCredentials() {
        // INSTRUÇÕES:
        // 1. Abra DALManager.java
        // 2. Linha 30: Altere "Admin123$" para "SenhaErrada123"
        // 3. Remova @Disabled desta anotação
        // 4. Execute o teste
        // 5. IMPORTANTE: Restaure a senha correta "Admin123$" no DALManager.java
        
        // Arrange
        dalManager = new DALManager();
        
        // Act
        dalManager.getUsers(response);
        
        // Assert
        assertFalse(response.isSuccessfull(), 
            "A resposta não deve ser bem-sucedida com credenciais incorretas");
        
        boolean hasConnectionError = response.messagesList.stream()
            .anyMatch(msg -> 
                msg.message.contains("Database Connection issue") &&
                msg.type == MessageType.Exception);
        
        assertTrue(hasConnectionError, 
            "Deve haver mensagem de erro de conexão com credenciais incorretas");
        
        System.out.println("✓ Teste 3 PASSOU: DALManager tratou falha de autenticação sem travar");
    }
    
    /**
     * Teste 4: Verificar Tratamento de Erro em Operações de Escrita
     * 
     * Objetivo: Garantir que operações de inserção também tratam erros de conexão
     * 
     * Pré-condições:
     * - Este teste pode ser executado com MySQL offline para teste completo
     * - Ou modificar DALManager para retornar conexão null
     */
    @Test
    @DisplayName("Teste 4: Operações de escrita tratam erro de conexão")
    //@Disabled("Requer MySQL offline - Execute manualmente quando necessário")
    public void testDALManagerWriteOperationsWithConnectionError() {
        // Arrange
        dalManager = new DALManager();
        UserDTO newUser = new UserDTO();
        newUser.setUsername("teste");
        newUser.setPassword("senha123");
        
        // Act
        dalManager.addUser(newUser, response);
        
        // Assert
        assertFalse(response.isSuccessfull(), 
            "Operação de escrita não deve ser bem-sucedida sem conexão");
        
        boolean hasConnectionError = response.messagesList.stream()
            .anyMatch(msg -> msg.message.contains("Database Connection issue"));
        
        assertTrue(hasConnectionError, 
            "Operação de escrita deve reportar erro de conexão");
        
        System.out.println("✓ Teste 4 PASSOU: Operações de escrita tratam erro de conexão corretamente");
    }
    
    /**
     * Teste 5: Verificar que Sistema Não Trava com Múltiplas Operações Falhas
     * 
     * Objetivo: Garantir robustez ao lidar com múltiplas falhas consecutivas
     */
    @Test
    @DisplayName("Teste 5: Sistema permanece estável após múltiplas falhas")
   // @Disabled("Requer MySQL offline - Execute manualmente quando necessário")
    public void testMultipleFailedOperations() {
        // Arrange
        dalManager = new DALManager();
        Response response1 = new Response();
        Response response2 = new Response();
        Response response3 = new Response();
        
        // Act - Executar múltiplas operações que devem falhar
        assertDoesNotThrow(() -> {
            dalManager.getUsers(response1);
            dalManager.getCustomers(response2);
            dalManager.getSuppliers(response3);
        }, "Sistema não deve lançar exceções não tratadas");
        
        // Assert
        assertTrue(response1.messagesList.size() > 0, "Deve haver mensagens de erro");
        assertTrue(response2.messagesList.size() > 0, "Deve haver mensagens de erro");
        assertTrue(response3.messagesList.size() > 0, "Deve haver mensagens de erro");
        
        System.out.println("✓ Teste 5 PASSOU: Sistema permaneceu estável após múltiplas falhas");
    }
    
    /**
     * Teste 6: Verificar Mensagem de Erro Amigável
     * 
     * Objetivo: Garantir que as mensagens de erro são apropriadas para o usuário
     */
    @Test
    @DisplayName("Teste 6: Mensagens de erro são amigáveis e informativas")
    //@Disabled("Requer MySQL offline - Execute manualmente quando necessário")
    public void testErrorMessageQuality() {
        // Arrange
        dalManager = new DALManager();
        
        // Act
        dalManager.getUsers(response);
        
        // Assert
        if (response.messagesList.size() > 0) {
            Message errorMessage = response.messagesList.get(0);
            
            // Verificar que mensagem não é técnica demais
            assertFalse(errorMessage.message.contains("SQLException"),
                "Mensagem não deve expor detalhes técnicos de exceção");
            
            assertFalse(errorMessage.message.toLowerCase().contains("null"),
                "Mensagem não deve mencionar 'null'");
            
            // Verificar que mensagem é informativa
            assertTrue(errorMessage.message.contains("Database Connection"),
                "Mensagem deve mencionar problema de conexão");
            
            assertTrue(errorMessage.message.contains("customer services"),
                "Mensagem deve orientar o usuário a buscar suporte");
            
            // Verificar tipo de mensagem
            assertEquals(MessageType.Exception, errorMessage.type,
                "Tipo de mensagem deve ser Exception");
            
            System.out.println("✓ Teste 6 PASSOU: Mensagens de erro são apropriadas");
        }
    }
}
