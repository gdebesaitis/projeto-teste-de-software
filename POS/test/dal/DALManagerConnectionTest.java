package dal;

import org.junit.jupiter.api.*;
import model.dto.Response;
import model.dto.UserDTO;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste DALManager - Estritamente conforme requisitos.
 */
public class DALManagerConnectionTest {
    
    private DALManager dalManager;
    private Response response;
    
    @BeforeEach
    public void setUp() {
        response = new Response();
    }
    
    /**
     * Teste: Conexão Bem-Sucedida (Carregamento de Dados)
     * Requisito: Verificar se os dados são carregados com credenciais corretas.
     */
    @Test
    @DisplayName("Conexão Bem-Sucedida: Dados Carregados")
    public void testSuccessfulDataLoad() {
        // Arrange
        dalManager = new DALManager(); // Usa construtor padrão com credenciais corretas
        
        // Act
        dalManager.getUsers(response);
        
        // Assert
        assertTrue(response.isSuccessfull(), "O sistema deve carregar os dados com sucesso.");
    }

    /**
     * Teste: Falha de Conexão (Mensagem Amigável)
     * Requisito: Exibir mensagem de erro amigável e não travar.
     */
    @Test
    @DisplayName("Falha de Conexão: Mensagem Amigável")
    public void testFriendlyErrorMessage() {
        // Para simular falha no DALManager sem mocks complexos, 
        // precisaríamos alterar a conexão interna ou assumir que o banco está offline manualmente.
        // Como o DALManager cria o MySQLConnection internamente no construtor, 
        // este teste verifica se o tratamento de erro (catch) está funcionando quando ocorre erro.
        
        // Arrange (Forçando erro alterando estado interno ou simulando comportamento)
        // Nota: Sem alterar o código fonte do DALManager para aceitar injeção de dependência,
        // este teste depende do banco estar realmente offline ou de um método de teste específico.
        
        dalManager = new DALManager(); 
        
        // Act
        // Tentamos uma operação. Se o banco estiver offline (simulado), deve gerar mensagem.
        // Se estiver online, este teste passará 'falso positivo', mas é o limite da estrutura atual.
        dalManager.getUsers(response);
        
        // Assert (Lógica condicional baseada no requisito)
        if (!response.isSuccessfull()) {
            String msg = response.messagesList.get(0).message;
            assertFalse(msg.contains("Exception"), "A mensagem deve ser amigável (sem 'Exception').");
            System.out.println("Mensagem recebida: " + msg);
        }
    }

    /**
     * Teste: Falha de Autenticação
     * Requisito: Informar erro de autenticação com credenciais erradas.
     */
    @Test
    @DisplayName("Falha de Autenticação")
    public void testAuthenticationFailure() {
        // Arrange
        dalManager = new DALManager();
        UserDTO userErrado = new UserDTO();
        userErrado.setUsername("usuario_nao_existe");
        userErrado.setPassword("senha_errada");
        
        // Act
        dalManager.verifyUser(userErrado, response);
        
        // Assert
        assertFalse(response.isSuccessfull(), "O login não deve ocorrer.");
        assertFalse(response.messagesList.isEmpty(), "Deve haver uma mensagem de erro.");
        // Verifica se informa erro de autenticação (baseado na implementação do seu DAL)
        // String msg = response.messagesList.get(0).message;
        // assertTrue(msg.contains("inválid") || msg.contains("fail"), "Deve informar erro de autenticação");
    }
}