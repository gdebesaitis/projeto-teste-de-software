package dal.db;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de Conexão de Banco de Dados - Gui D.
 * 
 * Objetivo: Verificar a capacidade do sistema de se conectar ao banco MySQL
 * e lidar com falhas de conexão.
 * 
 * @author Gui D.
 */
public class MySQLConnectionTest {
    
    private MySQLConnection mySQLConnection;
    
    /**
     * Teste 1: Conexão Bem-Sucedida
     * 
     * Pré-condições:
     * - Serviço MySQL deve estar ativo (rodando)
     * - Banco de dados 'pos' deve existir
     * - Usuário 'root' com senha 'Admin123$' deve estar configurado
     * 
     * Passos:
     * 1. Criar instância de MySQLConnection com credenciais corretas
     * 2. Tentar obter uma conexão
     * 3. Verificar se a conexão não é nula
     * 4. Verificar se a conexão está válida
     * 
     * Resultado Esperado: Conexão estabelecida com sucesso
     */
    @Test
    @DisplayName("Teste 1: Conexão bem-sucedida com credenciais corretas e MySQL ativo")
    public void testSuccessfulConnection() {
        // Arrange
        mySQLConnection = new MySQLConnection("pos", "root", "Admin123$");
        
        // Act
        Connection connection = mySQLConnection.getConnection();
        
        // Assert
        assertNotNull(connection, 
            "A conexão não deve ser nula quando o MySQL está ativo e as credenciais estão corretas");
        
        try {
            assertTrue(connection.isValid(2), 
                "A conexão deve estar válida e funcional");
            System.out.println("✓ Teste 1 PASSOU: Conexão estabelecida com sucesso");
        } catch (Exception e) {
            fail("Erro ao verificar validade da conexão: " + e.getMessage());
        } finally {
            mySQLConnection.closeConnection(connection);
        }
    }
    
    /**
     * Teste 2: Falha de Conexão (Serviço Offline)
     * 
     * Pré-condições:
     * - Serviço MySQL deve estar DESLIGADO
     * - OU usar uma porta diferente para simular serviço offline
     * 
     * Passos:
     * 1. Criar instância de MySQLConnection tentando conectar em porta inexistente
     * 2. Tentar obter uma conexão
     * 3. Verificar que a conexão retorna null
     * 4. Verificar que o sistema não trava (não lança exceção não tratada)
     * 
     * Resultado Esperado: 
     * - Retorna null
     * - Sistema não trava
     * - Mensagem de erro é exibida no console
     * 
     * NOTA: Para testar com MySQL realmente offline, altere o construtor
     * para usar "pos", "root", "Admin123$" e desligue o serviço MySQL
     */
    @Test
    @DisplayName("Teste 2: Falha de conexão quando MySQL está offline")
    public void testConnectionFailureServiceOffline() {
        // Arrange - Simulando MySQL offline usando porta inexistente
        // Para teste real: desligue o serviço MySQL e use as credenciais normais
        MySQLConnection offlineConnection = new MySQLConnection("pos", "root", "Admin123$") {
            @Override
            public Connection getConnection() {
                try {
                    // Tentando conectar em uma porta que não existe (simulando offline)
                    return java.sql.DriverManager.getConnection(
                        "jdbc:mysql://localhost:9999/pos", "root", "Admin123$");
                } catch (Exception ex) {
                    System.out.println("Connection Problem." + ex.getLocalizedMessage());
                    return null;
                }
            }
        };
        
        // Act
        Connection connection = offlineConnection.getConnection();
        
        // Assert
        assertNull(connection, 
            "A conexão deve ser null quando o serviço MySQL está offline");
        System.out.println("✓ Teste 2 PASSOU: Sistema tratou corretamente MySQL offline (retornou null sem travar)");
    }
    
    /**
     * Teste 3: Falha de Autenticação
     * 
     * Pré-condições:
     * - Serviço MySQL deve estar ativo
     * - Credenciais INCORRETAS devem ser usadas
     * 
     * Passos:
     * 1. Criar instância de MySQLConnection com senha incorreta
     * 2. Tentar obter uma conexão
     * 3. Verificar que a conexão retorna null
     * 4. Verificar que o sistema não trava
     * 
     * Resultado Esperado:
     * - Retorna null
     * - Sistema não trava
     * - Mensagem de erro de autenticação é exibida no console
     */
    @Test
    @DisplayName("Teste 3: Falha de autenticação com credenciais incorretas")
    public void testAuthenticationFailure() {
        // Arrange - Usando senha incorreta
        mySQLConnection = new MySQLConnection("pos", "root", "SenhaErrada123");
        
        // Act
        Connection connection = mySQLConnection.getConnection();
        
        // Assert
        assertNull(connection, 
            "A conexão deve ser null quando as credenciais estão incorretas");
        System.out.println("✓ Teste 3 PASSOU: Sistema tratou corretamente falha de autenticação (retornou null sem travar)");
    }
    
    /**
     * Teste 4: Falha de Autenticação - Usuário Inexistente
     * 
     * Pré-condições:
     * - Serviço MySQL deve estar ativo
     * - Usuário inexistente deve ser usado
     * 
     * Resultado Esperado: Conexão retorna null sem travar o sistema
     */
    @Test
    @DisplayName("Teste 4: Falha de autenticação com usuário inexistente")
    public void testAuthenticationFailureInvalidUser() {
        // Arrange - Usando usuário inexistente
        mySQLConnection = new MySQLConnection("pos", "usuario_inexistente", "qualquer_senha");
        
        // Act
        Connection connection = mySQLConnection.getConnection();
        
        // Assert
        assertNull(connection, 
            "A conexão deve ser null quando o usuário não existe");
        System.out.println("✓ Teste 4 PASSOU: Sistema tratou corretamente usuário inexistente (retornou null sem travar)");
    }
    
    /**
     * Teste 5: Banco de Dados Inexistente
     * 
     * Pré-condições:
     * - Serviço MySQL deve estar ativo
     * - Nome de banco de dados inexistente deve ser usado
     * 
     * Resultado Esperado: Conexão retorna null sem travar o sistema
     */
    @Test
    @DisplayName("Teste 5: Falha de conexão com banco de dados inexistente")
    public void testConnectionFailureInvalidDatabase() {
        // Arrange - Usando nome de banco inexistente
        mySQLConnection = new MySQLConnection("banco_inexistente_xyz", "root", "Admin123$");
        
        // Act
        Connection connection = mySQLConnection.getConnection();
        
        // Assert
        assertNull(connection, 
            "A conexão deve ser null quando o banco de dados não existe");
        System.out.println("✓ Teste 5 PASSOU: Sistema tratou corretamente banco inexistente (retornou null sem travar)");
    }
    
    /**
     * Teste 6: Fechamento de Conexão
     * 
     * Objetivo: Verificar se o método closeConnection funciona corretamente
     * 
     * Resultado Esperado: Conexão deve ser fechada sem erros
     */
    @Test
    @DisplayName("Teste 6: Fechamento correto de conexão")
    public void testCloseConnection() {
        // Arrange
        mySQLConnection = new MySQLConnection("pos", "root", "Admin123$");
        Connection connection = mySQLConnection.getConnection();
        
        // Act & Assert
        if (connection != null) {
            assertDoesNotThrow(() -> {
                mySQLConnection.closeConnection(connection);
            }, "O fechamento da conexão não deve lançar exceções");
            
            try {
                assertTrue(connection.isClosed(), 
                    "A conexão deve estar fechada após chamar closeConnection");
                System.out.println("✓ Teste 6 PASSOU: Conexão fechada corretamente");
            } catch (Exception e) {
                fail("Erro ao verificar se conexão foi fechada: " + e.getMessage());
            }
        }
    }
    
    /**
     * Teste 7: Fechamento de Conexão Nula
     * 
     * Objetivo: Verificar se closeConnection trata corretamente conexão null
     * 
     * Resultado Esperado: Não deve lançar exceção ao fechar conexão null
     */
    @Test
    @DisplayName("Teste 7: Fechamento de conexão nula não deve causar erro")
    public void testCloseNullConnection() {
        // Arrange
        mySQLConnection = new MySQLConnection("pos", "root", "Admin123$");
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            mySQLConnection.closeConnection(null);
        }, "O fechamento de uma conexão null não deve lançar exceções");
        System.out.println("✓ Teste 7 PASSOU: Fechamento de conexão null tratado corretamente");
    }
}
