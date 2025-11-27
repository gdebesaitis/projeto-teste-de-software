# Documentação de Testes - Sistema POS

## 📋 Índice
- [Visão Geral](#visão-geral)
- [Estrutura de Testes](#estrutura-de-testes)
- [Testes de Conexão de Banco de Dados](#testes-de-conexão-de-banco-de-dados)
- [Como Executar os Testes](#como-executar-os-testes)
- [Cenários de Teste](#cenários-de-teste)
- [Interpretação dos Resultados](#interpretação-dos-resultados)

---

## 🎯 Visão Geral

Esta pasta contém os testes automatizados do sistema POS. O foco inicial é nos **Testes de Conexão de Banco de Dados**, conforme planejado por Gui D.

### Objetivo Principal
Verificar a capacidade do sistema de:
- Se conectar ao banco MySQL corretamente
- Lidar com falhas de conexão de forma elegante
- Exibir mensagens de erro amigáveis ao usuário
- **NÃO** travar (crashar) em caso de problemas de conexão

---

## 📁 Estrutura de Testes

```
POS/
├── src/                          # Código-fonte da aplicação
│   └── dal/
│       ├── DALManager.java
│       └── db/
│           └── MySQLConnection.java
│
└── test/                         # Testes (NOVA ESTRUTURA)
    ├── README_TESTES.md         # Esta documentação
    └── dal/
        ├── DALManagerConnectionTest.java    # Testes do DALManager
        └── db/
            └── MySQLConnectionTest.java     # Testes da conexão MySQL
```

---

## 🔌 Testes de Conexão de Banco de Dados

**Responsável:** Gui D.

### Classes de Teste

#### 1. `MySQLConnectionTest.java`
Testa a classe `MySQLConnection` diretamente.

**Testes Implementados:**

| # | Nome do Teste | Objetivo | Pré-condições |
|---|---------------|----------|---------------|
| 1 | `testSuccessfulConnection` | Conexão bem-sucedida | MySQL ativo, credenciais corretas |
| 2 | `testConnectionFailureServiceOffline` | MySQL offline | MySQL desligado ou porta inexistente |
| 3 | `testAuthenticationFailure` | Senha incorreta | MySQL ativo, senha errada |
| 4 | `testAuthenticationFailureInvalidUser` | Usuário inexistente | MySQL ativo, usuário inválido |
| 5 | `testConnectionFailureInvalidDatabase` | Banco inexistente | MySQL ativo, banco não existe |
| 6 | `testCloseConnection` | Fechamento de conexão | Conexão válida |
| 7 | `testCloseNullConnection` | Fechamento de conexão null | - |

#### 2. `DALManagerConnectionTest.java`
Testa o comportamento do `DALManager` em diferentes cenários de conexão.

**Testes Implementados:**

| # | Nome do Teste | Objetivo | Tipo | Pré-condições |
|---|---------------|----------|------|---------------|
| 1 | `testDALManagerWithSuccessfulConnection` | DALManager com MySQL ativo | Automático | MySQL ativo |
| 2 | `testDALManagerWithOfflineMySQL` | MySQL offline | Manual | MySQL desligado |
| 3 | `testDALManagerWithWrongCredentials` | Credenciais incorretas | Manual | Modificar DALManager.java |
| 4 | `testDALManagerWriteOperationsWithConnectionError` | Operações de escrita com erro | Manual | MySQL desligado |
| 5 | `testMultipleFailedOperations` | Múltiplas falhas consecutivas | Manual | MySQL desligado |
| 6 | `testErrorMessageQuality` | Qualidade das mensagens de erro | Manual | MySQL desligado |

---

## ▶️ Como Executar os Testes

### Pré-requisitos
1. **NetBeans IDE** (versão 12 ou superior recomendada)
2. **JUnit 5** (incluído no NetBeans)
3. **MySQL Server** instalado
4. **Banco de dados `pos`** criado

### Configuração Inicial

#### 1. Verificar JUnit no NetBeans
```
1. Abrir o projeto no NetBeans
2. Clicar com botão direito no projeto > Properties
3. Ir em Libraries > Test Libraries
4. Verificar se JUnit 5.x está presente
5. Se não estiver, clicar em "Add Library" > JUnit 5
```

#### 2. Configurar Banco de Dados
```sql
-- Certifique-se que o banco 'pos' existe
CREATE DATABASE IF NOT EXISTS pos;

-- Credenciais padrão usadas no DALManager.java:
-- Usuário: root
-- Senha: Admin123$
-- Banco: pos
-- Host: localhost
-- Porta: 3306
```

### Executando os Testes

#### Opção 1: Executar Todos os Testes (NetBeans)
```
1. No NetBeans, expandir pasta "Test Packages"
2. Clicar com botão direito em "dal"
3. Selecionar "Test"
4. Aguardar execução e ver resultados
```

#### Opção 2: Executar Teste Individual
```
1. Navegar até o arquivo de teste desejado
2. Clicar com botão direito no arquivo
3. Selecionar "Test File"
```

#### Opção 3: Executar via Linha de Comando
```bash
# No diretório do projeto POS
cd d:\Meus_Documentos\Desktop\teste\projeto-teste-de-software\POS

# Compilar testes
javac -cp "build/classes;test;libs/*" -d build/test/classes test/dal/db/MySQLConnectionTest.java

# Executar testes com JUnit
java -cp "build/classes;build/test/classes;libs/*" org.junit.platform.console.ConsoleLauncher --scan-classpath
```

---

## 🧪 Cenários de Teste

### Cenário 1: Conexão Bem-Sucedida ✅

**Objetivo:** Verificar que o sistema se conecta corretamente ao MySQL.

**Passos:**
1. Iniciar o serviço MySQL
   ```bash
   # Windows
   net start MySQL80
   
   # Linux
   sudo systemctl start mysql
   ```
2. Verificar que o banco `pos` existe
3. Confirmar credenciais em `DALManager.java` (linha 30):
   - Banco: `pos`
   - Usuário: `root`
   - Senha: `Admin123$`
4. Executar `MySQLConnectionTest.testSuccessfulConnection()`

**Resultado Esperado:**
```
✓ Teste 1 PASSOU: Conexão estabelecida com sucesso
```

---

### Cenário 2: Falha de Conexão (Serviço Offline) ❌

**Objetivo:** Verificar que o sistema não trava quando MySQL está offline.

**Passos:**
1. Desligar o serviço MySQL
   ```bash
   # Windows
   net stop MySQL80
   
   # Linux
   sudo systemctl stop mysql
   ```
2. Executar `MySQLConnectionTest.testConnectionFailureServiceOffline()`
   - **OU** `DALManagerConnectionTest.testDALManagerWithOfflineMySQL()`
3. Religar o MySQL após o teste

**Resultado Esperado:**
```
Connection Problem.[mensagem de erro]
✓ Teste 2 PASSOU: Sistema tratou corretamente MySQL offline (retornou null sem travar)
```

**Verificações:**
- ✅ Sistema retorna `null` para a conexão
- ✅ Não lança exceção não tratada
- ✅ Exibe mensagem de erro no console
- ✅ Não trava a aplicação

---

### Cenário 3: Falha de Autenticação 🔐

**Objetivo:** Verificar tratamento de credenciais incorretas.

#### Opção A: Teste com Senha Incorreta

**Passos:**
1. MySQL deve estar ativo
2. Executar `MySQLConnectionTest.testAuthenticationFailure()`

**Resultado Esperado:**
```
Connection Problem.[Access denied for user 'root'@'localhost']
✓ Teste 3 PASSOU: Sistema tratou corretamente falha de autenticação
```

#### Opção B: Teste Manual com DALManager

**Passos:**
1. Abrir `src/dal/DALManager.java`
2. Modificar linha 30:
   ```java
   // De:
   this.mySQL = new MySQLConnection("pos", "root", "Admin123$");
   
   // Para:
   this.mySQL = new MySQLConnection("pos", "root", "SenhaErrada123");
   ```
3. Remover `@Disabled` do teste `testDALManagerWithWrongCredentials()`
4. Executar o teste
5. **IMPORTANTE:** Restaurar a senha correta após o teste!

**Resultado Esperado:**
```
✓ Teste PASSOU: DALManager tratou falha de autenticação sem travar
```

**Verificações:**
- ✅ Response.isSuccessfull() retorna `false`
- ✅ Lista de mensagens contém erro de conexão
- ✅ Mensagem é amigável: "Database Connection issue please contact customer services."
- ✅ Sistema não trava

---

### Cenário 4: Banco de Dados Inexistente 🗄️

**Passos:**
1. Executar `MySQLConnectionTest.testConnectionFailureInvalidDatabase()`

**Resultado Esperado:**
```
✓ Teste 5 PASSOU: Sistema tratou corretamente banco inexistente
```

---

## 📊 Interpretação dos Resultados

### Saída de Sucesso
Quando todos os testes passam, você verá:

```
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
✓ Teste 1 PASSOU: Conexão estabelecida com sucesso
✓ Teste 2 PASSOU: Sistema tratou corretamente MySQL offline
✓ Teste 3 PASSOU: Sistema tratou corretamente falha de autenticação
✓ Teste 4 PASSOU: Sistema tratou corretamente usuário inexistente
✓ Teste 5 PASSOU: Sistema tratou corretamente banco inexistente
✓ Teste 6 PASSOU: Conexão fechada corretamente
✓ Teste 7 PASSOU: Fechamento de conexão null tratado corretamente

BUILD SUCCESSFUL
```

### Saída de Falha
Se um teste falhar:

```
Tests run: 7, Failures: 1, Errors: 0, Skipped: 0

1) testSuccessfulConnection(dal.db.MySQLConnectionTest)
   java.lang.AssertionError: A conexão não deve ser nula quando o MySQL está ativo
   
Expected: not null
Actual: null

BUILD FAILED
```

**Possíveis Causas de Falha:**
- ❌ MySQL não está rodando
- ❌ Credenciais incorretas no código
- ❌ Banco de dados `pos` não existe
- ❌ Firewall bloqueando conexão
- ❌ Porta 3306 já está em uso por outro processo

---

## 🔍 Testes Desabilitados (@Disabled)

Alguns testes em `DALManagerConnectionTest.java` estão marcados com `@Disabled` porque requerem configuração manual:

### Como Habilitar Testes Desabilitados

1. Localize o teste desejado
2. Remova a linha `@Disabled("...")`
3. Siga as instruções nos comentários do teste
4. Execute o teste
5. **IMPORTANTE:** Desfaça as alterações após o teste

**Exemplo:**
```java
// Antes (desabilitado)
@Test
@Disabled("Requer MySQL offline - Execute manualmente quando necessário")
public void testDALManagerWithOfflineMySQL() { ... }

// Depois (habilitado)
@Test
public void testDALManagerWithOfflineMySQL() { ... }
```

---

## ✅ Checklist de Verificação

Antes de considerar os testes completos, verifique:

- [ ] Teste 1 (Conexão bem-sucedida) está passando
- [ ] Teste 2 (MySQL offline) foi executado manualmente ao menos uma vez
- [ ] Teste 3 (Falha de autenticação) está passando
- [ ] Sistema **NÃO** trava em nenhum cenário de erro
- [ ] Mensagens de erro são amigáveis e informativas
- [ ] Conexões estão sendo fechadas corretamente
- [ ] DALManager trata erros de conexão em todas as operações

---

## 📝 Notas Importantes

### Sobre o Tratamento de Erros Atual

O código atual (`MySQLConnection.java` e `DALManager.java`) trata erros de conexão da seguinte forma:

1. **MySQLConnection.getConnection():** Retorna `null` em caso de erro
2. **DALManager:** Verifica se conexão é `null` e adiciona mensagem de erro
3. **Sistema:** Não trava, mas operação falha silenciosamente

**Mensagem de erro padrão:**
```
"Database Connection issue please contact customer services."
```

### Melhorias Futuras Sugeridas

- Implementar logging de erros
- Criar exceções customizadas
- Adicionar retry automático de conexão
- Implementar connection pooling
- Adicionar monitoramento de saúde do banco

---

## 🤝 Contribuindo com Testes

Para adicionar novos testes de conexão:

1. Escolha a classe apropriada:
   - `MySQLConnectionTest`: Testes diretos de conexão
   - `DALManagerConnectionTest`: Testes de integração com DALManager

2. Siga o padrão de nomenclatura:
   ```java
   @Test
   @DisplayName("Teste X: Descrição clara do teste")
   public void testNomeDescritivo() {
       // Arrange
       // Act
       // Assert
   }
   ```

3. Documente:
   - Pré-condições
   - Passos
   - Resultado esperado

4. Use mensagens claras nas assertions:
   ```java
   assertNotNull(connection, "Mensagem explicativa do que deve acontecer");
   ```

---

## 📞 Suporte

**Responsável pelos Testes de Conexão:** Gui D.

Em caso de dúvidas ou problemas:
1. Verifique se todos os pré-requisitos estão instalados
2. Confirme configurações do MySQL
3. Revise a seção de interpretação de resultados
4. Entre em contato com o responsável

---

**Última atualização:** 27 de novembro de 2025  
**Versão:** 1.0  
**Status:** Testes de Conexão Implementados ✅
