@echo off
echo Compiling AutoLib...
javac -encoding UTF-8 -d out -sourcepath . ^
  autolib\AutoLibApp.java ^
  autolib\UIConstants.java ^
  autolib\UIComponents.java ^
  autolib\DataStore.java ^
  autolib\PortalScreen.java ^
  autolib\LoginDialog.java ^
  autolib\LibrarianPortal.java ^
  autolib\models\Book.java ^
  autolib\models\Student.java ^
  autolib\models\Transaction.java ^
  autolib\panels\DashboardPanel.java ^
  autolib\panels\BookCatalogPanel.java ^
  autolib\panels\StudentsPanel.java ^
  autolib\panels\TransactionsPanel.java ^
  autolib\panels\ReportsPanel.java ^
  autolib\panels\SettingsPanel.java ^
  autolib\dialogs\AddBookDialog.java ^
  autolib\dialogs\RegisterStudentDialog.java

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation FAILED. Check errors above.
    pause
    exit /b 1
)

echo.
echo Compilation successful! Starting AutoLib...
java -cp out autolib.AutoLibApp
