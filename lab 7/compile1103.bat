@echo off
REM  the following line can be edited to replace InfoLab with some other JavaFX source file in Desktop
set to_be_compiled="InfoLab.java"  

set path="c:\UNB\openjdk-14.0.2_windows-x64_bin\jdk-14.0.2\bin";"%onedrive%\Desktop";%PATH%
set PATH_TO_FX="C:\UNB\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib"
set PATH_TO_JDBC="%onedrive%\Desktop\mysql-connector-java-5.1.49.jar"
echo to be compiled is %to_be_compiled%
javac -p %PATH_TO_FX% --add-modules=javafx.controls,javafx.fxml,javafx.media,javafx.web,javafx.swing -cp %PATH_TO_JDBC%;. %to_be_compiled%
echo "If there were no error messages,  it succeeded at compiling"
pause
