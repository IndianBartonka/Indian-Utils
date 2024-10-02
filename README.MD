<div align="center">

# Indian Utils
 Utile używane w różnych projektach przez Indiana

</div>

[![](https://jitpack.io/v/IndianBartonka/Indian-Utils.svg)](https://jitpack.io/#IndianBartonka/Indian-Utils)

### Przykłady użycja

Przykłady użycja znajdziecie w klasie [Main.java](src%2Fmain%2Fjava%2Fme%2Findian%2Futil%2FMain.java)

## Maven `pom.xml`
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.IndianBartonka</groupId>
    <artifactId>Indian-Utils</artifactId>
    <version>TAG</version>
</dependency>
```

## Gradle `build.gradle`

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.IndianBartonka:Indian-Utils:TAG'
}
```

## Sinple Build Tool `build.sbt`

```sbt
resolvers += "jitpack" at "https://jitpack.io"
```

```sbt
libraryDependencies += "com.github.IndianBartonka" % "Indian-Utils" % "Tag"	
```
