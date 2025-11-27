# 🖥️ Executar Testes via Terminal (PowerShell)

Este guia mostra como executar os testes de conexão diretamente pelo terminal PowerShell, sem usar o NetBeans.

---

## ⚡ Execução Rápida

### Passo 1: Navegar até o projeto
```powershell
cd "d:\Meus_Documentos\Desktop\teste\projeto-teste-de-software\POS"
```

### Passo 2: Verificar MySQL
```powershell
# Verificar se MySQL está rodando
Get-Service MySQL80

# Se não estiver rodando, iniciar
Start-Service MySQL80
```

### Passo 3: Compilar e Executar no NetBeans
Como o projeto usa NetBeans, a forma mais fácil é:

```powershell
# Opção 1: Usar o Ant (build tool do NetBeans)
ant test

# Opção 2: Abrir NetBeans via terminal
start netbeans
```

---

## 🛠️ Configuração Manual (Avançado)

Se você quiser executar sem NetBeans:

### 1. Verificar Java
```powershell
java -version
# Deve mostrar Java 21 ou superior
```

### 2. Definir Variáveis
```powershell
$PROJECT_DIR = "d:\Meus_Documentos\Desktop\teste\projeto-teste-de-software\POS"
$SRC_DIR = "$PROJECT_DIR\src"
$TEST_DIR = "$PROJECT_DIR\test"
$BUILD_DIR = "$PROJECT_DIR\build"
$LIB_DIR = "d:\Meus_Documentos\Desktop\teste\projeto-teste-de-software\external-lib"
```

### 3. Compilar Código Fonte
```powershell
# Criar diretório de build
New-Item -ItemType Directory -Force -Path "$BUILD_DIR\classes"
New-Item -ItemType Directory -Force -Path "$BUILD_DIR\test\classes"

# Compilar código fonte
javac -cp "$LIB_DIR\*" -d "$BUILD_DIR\classes" -sourcepath "$SRC_DIR" `
    (Get-ChildItem -Path "$SRC_DIR" -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName)
```

### 4. Compilar Testes
```powershell
# Baixar JUnit 5 (se não tiver)
# Você pode baixar de: https://junit.org/junit5/

# Compilar testes (assumindo JUnit em C:\junit)
javac -cp "$BUILD_DIR\classes;C:\junit\*;$LIB_DIR\*" `
    -d "$BUILD_DIR\test\classes" `
    -sourcepath "$TEST_DIR" `
    (Get-ChildItem -Path "$TEST_DIR" -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName)
```

### 5. Executar Testes
```powershell
# Executar com JUnit Console Launcher
java -jar C:\junit\junit-platform-console-standalone.jar `
    --classpath "$BUILD_DIR\classes;$BUILD_DIR\test\classes;$LIB_DIR\*" `
    --scan-classpath
```

---

## 📦 Usar Ant (Recomendado)

O projeto já está configurado com Ant. Use os comandos do NetBeans:

### Compilar
```powershell
ant compile
```

### Executar Testes
```powershell
ant test
```

### Limpar Build
```powershell
ant clean
```

### Compilar Tudo
```powershell
ant clean compile test
```

---

## 🔍 Verificar Resultados

### Ver Logs de Teste
```powershell
# Navegar para resultados
cd "$PROJECT_DIR\build\test\results"

# Listar arquivos de resultado
Get-ChildItem -Filter "*.xml"

# Ver conteúdo de um resultado
Get-Content "TEST-dal.db.MySQLConnectionTest.xml"
```

---

## ⚠️ Troubleshooting

### Erro: "MySQL80 service not found"
```powershell
# Listar serviços MySQL
Get-Service | Where-Object {$_.Name -like "*mysql*"}

# Usar o nome correto do serviço
Start-Service <nome-do-servico>
```

### Erro: "javac não é reconhecido"
```powershell
# Adicionar Java ao PATH
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

### Erro: "Cannot find JUnit"
**Solução:** Use o NetBeans que já tem JUnit configurado:
```powershell
# Abrir projeto no NetBeans
start netbeans

# Ou executar via Ant
ant test
```

---

## 📋 Script Completo

Salve como `executar-testes.ps1`:

```powershell
# executar-testes.ps1
# Script para executar testes de conexão do POS

Write-Host "=== Testes de Conexão POS ===" -ForegroundColor Cyan

# 1. Verificar MySQL
Write-Host "`n[1/3] Verificando MySQL..." -ForegroundColor Yellow
$mysql = Get-Service MySQL80 -ErrorAction SilentlyContinue
if ($mysql -and $mysql.Status -eq 'Running') {
    Write-Host "✓ MySQL está rodando" -ForegroundColor Green
} else {
    Write-Host "✗ MySQL não está rodando. Iniciando..." -ForegroundColor Red
    Start-Service MySQL80
    Write-Host "✓ MySQL iniciado" -ForegroundColor Green
}

# 2. Navegar para projeto
Write-Host "`n[2/3] Navegando para projeto..." -ForegroundColor Yellow
Set-Location "d:\Meus_Documentos\Desktop\teste\projeto-teste-de-software\POS"
Write-Host "✓ Diretório: $(Get-Location)" -ForegroundColor Green

# 3. Executar testes com Ant
Write-Host "`n[3/3] Executando testes..." -ForegroundColor Yellow
ant test

Write-Host "`n=== Testes Concluídos ===" -ForegroundColor Cyan
```

### Executar o script:
```powershell
.\executar-testes.ps1
```

---

## 🎯 Comandos Úteis

### Status do MySQL
```powershell
Get-Service MySQL80 | Select-Object Name, Status, DisplayName
```

### Iniciar/Parar MySQL
```powershell
Start-Service MySQL80    # Iniciar
Stop-Service MySQL80     # Parar
Restart-Service MySQL80  # Reiniciar
```

### Ver Processos Java
```powershell
Get-Process java | Select-Object Id, ProcessName, CPU, WS
```

### Limpar Build
```powershell
Remove-Item -Recurse -Force "$PROJECT_DIR\build"
```

---

## 📖 Referências

- **NetBeans:** https://netbeans.apache.org/
- **Ant:** https://ant.apache.org/
- **JUnit 5:** https://junit.org/junit5/
- **MySQL:** https://dev.mysql.com/doc/

---

## ✅ Recomendação

**Para desenvolvimento diário, recomendamos usar o NetBeans**, pois:
- ✅ JUnit já está configurado
- ✅ Interface gráfica para ver resultados
- ✅ Execução mais rápida
- ✅ Debug integrado

**Use o terminal apenas para:**
- CI/CD pipelines
- Automação de build
- Testes em servidores sem GUI

---

**Nota:** Este arquivo é um complemento. Para a maioria dos usuários, executar pelo NetBeans é mais simples e eficiente.
