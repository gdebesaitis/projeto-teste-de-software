#!/usr/bin/env pwsh
<#
run-tests.ps1

Script para compilar o projeto e executar os testes (JUnit 5) a partir da pasta `POS`.

O que faz:
- Baixa e valida `junit-platform-console-standalone-1.9.4.jar` em `POS/lib` (se não existir)
- Compila as classes do projeto e das classes de teste usando `javac` com classpath apropriado
- Executa os testes com o Console Launcher

Uso:
  Abra PowerShell na pasta `POS` e execute:
    .\run-tests.ps1

Observações:
- Testes que dependem de MySQL exigem que o serviço MySQL esteja ativo e configurado.
#>

$ErrorActionPreference = 'Stop'

param(
    [switch]$NoDownload
)

function FailExit([string]$msg, [int]$code = 1) {
    Write-Host "ERROR: $msg" -ForegroundColor Red
    exit $code
}

try {
    $scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
} catch {
    $scriptDir = Get-Location
}

Set-Location $scriptDir

Write-Host "Pasta do script: $scriptDir" -ForegroundColor Cyan

# Verifica java/javac
try { & java -version > $null 2>&1 } catch { FailExit '`java` não encontrado no PATH. Instale JDK e adicione ao PATH.' }
try { & javac -version > $null 2>&1 } catch { FailExit '`javac` não encontrado no PATH. Instale JDK e adicione ao PATH.' }

# Diretórios de build
$buildClasses = Join-Path $scriptDir 'build\classes'
$buildTestClasses = Join-Path $scriptDir 'build\test\classes'
New-Item -ItemType Directory -Force -Path $buildClasses,$buildTestClasses | Out-Null

# JUnit console standalone
$libDir = Join-Path $scriptDir 'lib'
if (-not (Test-Path $libDir)) { New-Item -ItemType Directory -Force -Path $libDir | Out-Null }

$junitFileName = 'junit-platform-console-standalone-1.9.4.jar'
$junitUrl = "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.4/$junitFileName"
$junitJar = Join-Path $libDir $junitFileName

if (-not $NoDownload) {
    if (-not (Test-Path $junitJar)) {
        Write-Host "JUnit console jar não encontrado em $junitJar — baixando..." -ForegroundColor Yellow
        try {
            Invoke-WebRequest -Uri $junitUrl -OutFile $junitJar -UseBasicParsing -ErrorAction Stop
            Write-Host "Baixado JUnit para $junitJar" -ForegroundColor Green
        } catch {
            FailExit "Falha ao baixar JUnit: $($_.Exception.Message)\nURL: $junitUrl" 2
        }
    } else {
        Write-Host "JUnit já existe em $junitJar" -ForegroundColor Yellow
    }
} else {
    Write-Host "Pulando download do JUnit (--NoDownload)" -ForegroundColor Yellow
    if (-not (Test-Path $junitJar)) { FailExit "Arquivo JUnit não encontrado em $junitJar (use sem --NoDownload)." 2 }
}

# Validar JAR (tentar 'jar tf', fallback para 'java -jar --version')
Write-Host "Validando JAR do JUnit..." -ForegroundColor Cyan
try {
    & jar tf $junitJar > $null 2>&1
    if ($LASTEXITCODE -ne 0) { throw 'jar tf falhou' }
} catch {
    Write-Host "Validação com 'jar' falhou, tentando 'java -jar'..." -ForegroundColor Yellow
    try {
        & java -jar $junitJar --version > $null 2>&1
        if ($LASTEXITCODE -ne 0) { throw 'java -jar falhou' }
    } catch {
        FailExit "Arquivo JAR inválido ou corrompido em $junitJar. Remova e tente novamente." 3
    }
}
Write-Host "JAR validado: $junitJar" -ForegroundColor Green

# Compilar fontes do projeto
Write-Host 'Compilando fontes do projeto...' -ForegroundColor Cyan
$srcDir = Join-Path $scriptDir 'src'
if (-not (Test-Path $srcDir)) { FailExit 'Diretório src não encontrado.' }
$srcFiles = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | ForEach-Object FullName
if ($srcFiles.Count -eq 0) { FailExit 'Nenhuma fonte Java encontrada em src\' }

$externalLibPattern = Join-Path $scriptDir '..\external-lib\*'
$libPattern = Join-Path $libDir '*'
$compileCp = "$externalLibPattern;$libPattern"

Write-Host "javac -cp $compileCp -d $buildClasses <sources>" -ForegroundColor DarkGray
& javac -cp $compileCp -d $buildClasses $srcFiles
if ($LASTEXITCODE -ne 0) { FailExit 'Erro na compilação das fontes' 3 }
Write-Host 'Compilação do código-fonte concluída.' -ForegroundColor Green

# Compilar testes
Write-Host 'Compilando fontes de teste...' -ForegroundColor Cyan
$testDir = Join-Path $scriptDir 'test'
if (-not (Test-Path $testDir)) { Write-Host 'Nenhum teste encontrado (diretório test ausente).'; exit 0 }
$testFiles = Get-ChildItem -Path $testDir -Recurse -Filter *.java | ForEach-Object FullName
if ($testFiles.Count -eq 0) { Write-Host 'Nenhum teste encontrado em test\ - nada a executar.'; exit 0 }

$testCp = "$buildClasses;$libPattern;$externalLibPattern"
Write-Host "javac -cp $testCp -d $buildTestClasses <test-sources>" -ForegroundColor DarkGray
& javac -cp $testCp -d $buildTestClasses $testFiles
if ($LASTEXITCODE -ne 0) { FailExit 'Erro na compilação dos testes' 4 }
Write-Host 'Compilação dos testes concluída.' -ForegroundColor Green

# Executar testes
Write-Host 'Executando testes com JUnit Console Launcher...' -ForegroundColor Cyan
$runCp = "$buildClasses;$buildTestClasses;$externalLibPattern;$libPattern"
Write-Host "java -jar $junitJar --class-path $runCp --scan-classpath" -ForegroundColor DarkGray
& java -jar $junitJar --class-path $runCp --scan-classpath
$exitCode = $LASTEXITCODE

if ($exitCode -eq 0) { Write-Host 'Execução de testes finalizada com sucesso.' -ForegroundColor Green } else { Write-Host "Execução de testes retornou código $exitCode" -ForegroundColor Yellow }
exit $exitCode
<#
.\run-tests.ps1

Script para compilar o projeto e executar os testes (JUnit 5) a partir da pasta `POS`.

O que faz:
.#<
<#
- Verifica `java` e `javac` no PATH
- Garante diretórios de build
- Baixa o JUnit Platform Console Standalone para `C:\junit` se não existir
- Compila as classes do projeto e das classes de teste
- Executa os testes com o Console Launcher

Uso:
  Abra PowerShell na pasta `POS` e execute:
    .\run-tests.ps1

Observações:
- Testes que dependem de MySQL exigem que o serviço MySQL esteja ativo e configurado.
#>

param(
    [switch]$NoDownload
)

function FailExit($msg, $code = 1) {
    Write-Host "ERROR: $msg" -ForegroundColor Red
# 1) Verificar java/javac
try {
    & java -version 2>$null | Out-Null
} catch {
    FailExit '`java` não encontrado no PATH. Instale JDK e adicione ao PATH.'
}
    & javac -version 2>$null | Out-Null
    FailExit '`javac` não encontrado no PATH. Instale JDK e adicione ao PATH.'
}

# 2) Diretórios
$projectDir = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location $projectDir
Write-Host "Projeto: $projectDir"

$buildClasses = Join-Path $projectDir 'build\classes'
$buildTestClasses = Join-Path $projectDir 'build\test\classes'
New-Item -ItemType Directory -Force -Path $buildClasses | Out-Null
New-Item -ItemType Directory -Force -Path $buildTestClasses | Out-Null

# 3) JUnit console standalone
$junitDir = Join-Path $projectDir 'lib'
$junitJar = Join-Path $junitDir 'junit-platform-console-standalone-1.9.4.jar'
if (-not $NoDownload) {
    if (-not (Test-Path $junitJar)) {
        Write-Host "JUnit console jar não encontrado em $junitJar — baixando..." -ForegroundColor Yellow
        New-Item -ItemType Directory -Force -Path $junitDir | Out-Null
        $url = 'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.4/junit-platform-console-standalone-1.9.4.jar'
        try {
            Invoke-WebRequest -Uri $url -OutFile $junitJar -UseBasicParsing -ErrorAction Stop
            Write-Host "Baixado JUnit para $junitJar" -ForegroundColor Green
        } catch {
            FailExit "Falha ao baixar JUnit: $($_.Exception.Message)" 2
        }
    } else {
        Write-Host "JUnit encontrado em $junitJar" -ForegroundColor Green
    }
} else {
    Write-Host "Pulando download do JUnit (--NoDownload)" -ForegroundColor Yellow
}

# 4) Compilar fontes do projeto
Write-Host "Compilando fontes do projeto..." -ForegroundColor Cyan
$srcFiles = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object FullName
if ($srcFiles.Count -eq 0) { FailExit 'Nenhuma fonte encontrada em src\' }

try {
    & javac -cp '..\external-lib\*' -d $buildClasses $srcFiles
    Write-Host 'Compilação do código-fonte concluída.' -ForegroundColor Green
} catch {
    FailExit "Erro na compilação das fontes: $($_.Exception.Message)" 3
}

# 5) Compilar testes
Write-Host "Compilando fontes de teste..." -ForegroundColor Cyan
$testFiles = Get-ChildItem -Path .\test -Recurse -Filter *.java | ForEach-Object FullName
if ($testFiles.Count -eq 0) { Write-Host 'Nenhum teste encontrado em test\ - nada a executar.'; exit 0 }

$testCp = "$buildClasses;$junitJar;..\external-lib\*"
try {
    & javac -cp $testCp -d $buildTestClasses $testFiles
    Write-Host 'Compilação dos testes concluída.' -ForegroundColor Green
} catch {
    FailExit "Erro na compilação dos testes: $($_.Exception.Message)" 4
}

# 6) Executar testes com JUnit Console
Write-Host "Executando testes com JUnit Console Launcher..." -ForegroundColor Cyan
try {
    & java -jar $junitJar --classpath "$buildClasses;$buildTestClasses;..\external-lib\*" --scan-classpath
} catch {
    FailExit "Erro na execução dos testes: $($_.Exception.Message)" 5
}

Write-Host "Execução de testes finalizada." -ForegroundColor Cyan
