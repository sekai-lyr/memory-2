# ============================================================================
# Sekai 商城 → Windows 桌面版 exe 一键打包脚本
# 产物：dist\out\SekaiShop\SekaiShop.exe（绿色版，含内置 JRE + JavaFX + 种子图片）
# 要求：JDK 21+（含 jpackage）、Maven、OpenJFX SDK（路径见下方参数）
# 用法：powershell -ExecutionPolicy Bypass -File tools\build-exe.ps1
# ============================================================================
param(
    [string]$JavafxHome = "B:\Sekai\javafx\javafx-sdk-25.0.2",
    [string]$JdkHome    = $env:JAVA_HOME,
    [string]$AppName    = "SekaiShop",
    [string]$Version    = "1.0.0",
    [switch]$Debug      # 调试模式：带控制台窗口、跳过图片拷贝与 zip，加快迭代
)

$ErrorActionPreference = 'Continue'
$root  = Split-Path -Parent $PSScriptRoot
$dist  = Join-Path $root 'dist'
$work  = Join-Path $dist 'work'
$input = Join-Path $dist 'jpackage-input'
$out   = Join-Path $dist 'out'
$jar   = Join-Path $root 'target\ebusiness-0.0.1-SNAPSHOT.jar'
$fxLib = Join-Path $JavafxHome 'lib'

$javac = Join-Path $JdkHome 'bin\javac.exe'
$jarTool = Join-Path $JdkHome 'bin\jar.exe'
$jpackage = Join-Path $JdkHome 'bin\jpackage.exe'

foreach ($p in @($javac, $jarTool, $jpackage, $jar, $fxLib)) {
    if (-not (Test-Path $p)) { throw "缺少路径: $p" }
}

# 需要的 JavaFX 模块 jar（base 被其他模块依赖，自动带上）
$fxJars = @('javafx.base.jar','javafx.controls.jar','javafx.graphics.jar','javafx.media.jar','javafx.web.jar')

Write-Host "==> [1/6] mvn package"
Push-Location $root
mvn package -DskipTests -q
if ($LASTEXITCODE -ne 0) { throw 'mvn package 失败' }
Pop-Location

Write-Host "==> [2/6] 编译 FxLauncher"
New-Item -ItemType Directory -Force -Path $work\classes | Out-Null
$fxCp = (Get-ChildItem $fxLib -Filter '*.jar' | ForEach-Object { $_.FullName }) -join ';'
& $javac -encoding UTF-8 -cp $fxCp -d $work\classes (Join-Path $PSScriptRoot 'launcher\FxLauncher.java')
if ($LASTEXITCODE -ne 0) { throw 'javac 失败' }

Write-Host "==> [3/6] 制作 fx-launcher.jar（清单声明 Class-Path）"
$classPath = @('ebusiness-0.0.1-SNAPSHOT.jar') + $fxJars
$manifest = @(
    'Manifest-Version: 1.0',
    "Main-Class: com.youkeda.application.ebusiness.FxLauncher",
    "Class-Path: $($classPath -join ' ')"
) -join "`r`n"
$manifest += "`r`n`r`n"
Set-Content -Path $work\MANIFEST.MF -Value $manifest -Encoding Ascii -NoNewline
& $jarTool cfm $work\fx-launcher.jar $work\MANIFEST.MF -C $work\classes .
if ($LASTEXITCODE -ne 0) { throw 'jar 失败' }

Write-Host "==> [4/6] 准备 jpackage 输入目录"
New-Item -ItemType Directory -Force -Path $input | Out-Null
Remove-Item $input\* -Force -ErrorAction SilentlyContinue
Copy-Item $jar $input
Copy-Item $work\fx-launcher.jar $input
foreach ($j in $fxJars) {
    Copy-Item (Join-Path $fxLib $j) $input
}
Get-ChildItem $input | Select-Object Name, @{n='KB';e={[math]::Round($_.Length/1KB,0)}} | Format-Table -AutoSize
foreach ($need in (@('fx-launcher.jar', 'ebusiness-0.0.1-SNAPSHOT.jar') + $fxJars)) {
    if (-not (Test-Path (Join-Path $input $need))) { throw "输入目录缺少: $need" }
}

Write-Host "==> [5/6] jpackage 生成 app-image（内置 JRE + JavaFX）"
Remove-Item $out -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $out | Out-Null
# 枚举 JDK 全部模块（等价 ALL-MODULE-SYSTEM；jlink 不认该关键字，需真实模块名）
$javaBin = Join-Path $JdkHome 'bin\java.exe'
$moduleList = (& $javaBin --list-modules | ForEach-Object { ($_ -split '@')[0] }) -join ','
& $jpackage `
    --type app-image `
    --name $AppName `
    --app-version $Version `
    --icon (Join-Path $PSScriptRoot 'sekai.ico') `
    --input $input `
    --main-jar fx-launcher.jar `
    --main-class com.youkeda.application.ebusiness.FxLauncher `
    --java-options "-Dfile.encoding=UTF-8" `
    --add-modules $moduleList `
    $(if ($Debug) { '--win-console' }) `
    --dest $out
if ($LASTEXITCODE -ne 0) { throw 'jpackage 失败' }

Write-Host "==> [6/6] 部署资源（种子图片 / JavaFX DLL / 说明文档）"
$appDir = Join-Path $out "$AppName\app"
if (-not $Debug) {
    robocopy (Join-Path $root 'uploads') (Join-Path $appDir 'uploads') /E /NFL /NDL /NJH /NJS /NP | Out-Null
    if ($LASTEXITCODE -ge 8) { throw 'robocopy 失败' }
}
# JavaFX native DLL 在 SDK 的 bin/ 目录（不在 jar 内），classpath 模式下必须拷贝并指定 java.library.path
Copy-Item (Join-Path $JavafxHome 'bin\*') $appDir -Force
Add-Content -Path (Join-Path $appDir "$AppName.cfg") -Value 'java-options=-Djava.library.path=$APPDIR' -Encoding Ascii
# 使用说明放进绿色版根目录
Copy-Item (Join-Path $PSScriptRoot 'README-exe.md') (Join-Path $out "$AppName\使用说明.md") -Force

Write-Host "完成：$appDir\SekaiShop.exe"
if (-not $Debug) {
    Write-Host "打包 zip："
    Compress-Archive -Path (Join-Path $out $AppName) -DestinationPath (Join-Path $dist "SekaiShop-v$Version-win-x64.zip") -CompressionLevel Optimal -Force
    Get-Item (Join-Path $dist "SekaiShop-v$Version-win-x64.zip") | Select-Object Name, @{n='MB';e={[math]::Round($_.Length/1MB,1)}}
}
