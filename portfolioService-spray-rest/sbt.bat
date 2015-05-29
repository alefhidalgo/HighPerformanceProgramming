SET JAVA_HOME=C:\Users\AOHG\Documents\PSU\Java\JDK7
SET PATH=%PATH%;C:\Users\AOHG\Documents\PSU\Java\Java_64\bin
set SCRIPT_DIR=%~dp0
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M -jar "%SCRIPT_DIR%sbt-launch.jar" %*