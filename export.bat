javac -cp lib/jnativehook-2.1.0.jar;lib/json_simple-1.1.jar;src; -d bin src/com/rijkv/breaktimer/Main.java
cd bin
jar cfe ../export/nolib-build.jar com.rijkv.breaktimer.Main com/rijkv/breaktimer
cd ..\export
java -jar nolib-build.jar