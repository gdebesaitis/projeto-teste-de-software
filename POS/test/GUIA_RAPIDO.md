# 🚀 Guia Rápido - Executar Testes de Conexão

## ⚡ Execução Rápida (3 minutos)

### Passo 1: Verificar MySQL
```bash
# Windows - Verificar se MySQL está rodando
net start | findstr MySQL

# Se não estiver rodando, iniciar:
net start MySQL80
```

### Passo 2: Executar Testes no NetBeans
1. Abrir projeto no NetBeans
2. Expandir `Test Packages` > `dal` > `db`
3. Botão direito em `MySQLConnectionTest.java` > **Test File**
4. Ver resultados na aba "Test Results"

**Resultado esperado:** 7 testes passando ✅

---

## 📋 Checklist Rápido

Antes de executar os testes:

- [ ] MySQL está rodando? → `net start MySQL80`
- [ ] Banco `pos` existe? → Verificar no MySQL Workbench
- [ ] Credenciais corretas? → `root / Admin123$`
- [ ] JUnit está instalado? → Verificar em Libraries no NetBeans

---

## ⚠️ Testes Manuais (Opcionais)

### Teste: MySQL Offline

**Quando executar:** Para verificar que o sistema não trava sem MySQL

1. Parar MySQL:
   ```bash
   net stop MySQL80
   ```

2. No NetBeans:
   - Abrir `DALManagerConnectionTest.java`
   - Localizar `testDALManagerWithOfflineMySQL()`
   - Remover linha `@Disabled`
   - Executar o teste

3. Reiniciar MySQL:
   ```bash
   net start MySQL80
   ```

**Resultado esperado:** Teste passa ✅ Sistema não trava ✅

---

## 🎯 Testes Principais

| Teste | O que verifica | Status |
|-------|----------------|--------|
| testSuccessfulConnection | Conexão funciona | ✅ Automático |
| testConnectionFailureServiceOffline | Trata MySQL offline | ⚙️ Manual |
| testAuthenticationFailure | Trata senha errada | ✅ Automático |
| testConnectionFailureInvalidDatabase | Trata banco inexistente | ✅ Automático |

---

## ❌ Troubleshooting Rápido

### Teste falha: "Connection is null"
**Solução:** MySQL não está rodando → `net start MySQL80`

### Teste falha: "Access denied"
**Solução:** Verificar senha em `DALManager.java` linha 30 → deve ser `Admin123$`

### Teste falha: "Unknown database 'pos'"
**Solução:** Criar banco de dados:
```sql
CREATE DATABASE pos;
```

### Erro: "JUnit not found"
**Solução:** 
1. Botão direito no projeto > Properties
2. Libraries > Test Libraries
3. Add Library > JUnit 5

---

## 📊 Interpretar Resultados

### ✅ Sucesso
```
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESSFUL
```
**Significado:** Tudo funcionando corretamente!

### ❌ Falha
```
Tests run: 7, Failures: 1, Errors: 0, Skipped: 0
testSuccessfulConnection FAILED
```
**Significado:** Verificar MySQL e credenciais

---

## 🔄 Workflow Recomendado

1. **Antes de começar o dia:**
   - Executar todos os testes automáticos
   - Confirmar que MySQL está funcionando

2. **Após modificar código de conexão:**
   - Executar `MySQLConnectionTest` completo
   - Executar teste manual de MySQL offline

3. **Antes de commit/push:**
   - Todos os testes automáticos devem passar
   - Documentar qualquer teste manual realizado

---

**Para documentação completa:** Veja `README_TESTES.md`
