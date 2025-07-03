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
java --add-opens java.base/java.lang=ALL-UNNAMED -jar nav-abevjava.jar
```