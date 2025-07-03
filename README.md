# NAV - Decompiled Nyomtatvány kitöltő program

---

## Project Motivation


This project was created due to the complexity and limited usability of the original application. Despite extensive efforts, I was unable to get it to function properly on Linux systems. As a result, I chose to decompile the application.

By doing so, this project makes the app accessible to anyone who downloads it, regardless of their platform.

I am not affiliated with any organization, have no political agenda, and have no intention of causing conflict among users. This project was developed solely to simplify my own workflow.

---

## Getting Started


```shell
mvn package
cd ./target/deploy
java -Djavax.xml.stream.XMLInputFactory=com.ctc.wstx.stax.WstxInputFactory -Djavax.xml.stream.XMLOutputFactory=com.ctc.wstx.stax.WstxOutputFactory -Djavax.xml.stream.XMLEventFactory=com.ctc.wstx.stax.WstxEventFactory --add-opens java.base/java.lang=ALL-UNNAMED -jar nav-abevjava.jar
```

## Env variables

| Env name        | Default Value                    | Description                                                                        |
|-----------------|----------------------------------|------------------------------------------------------------------------------------|
| USERPROFILE     | System.getProperty("user.home"); | User specific files                                                                |
| WRITE_DIRECTORY | The folder where the .jar is     | If this is not empty, then this is the only folder, where the app can write files. |
