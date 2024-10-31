<div align="center">

# Indian Utils

Utile używane w różnych projektach przez Indiana

[![Jitpack](https://jitpack.io/v/IndianBartonka/Indian-Utils.svg)](https://jitpack.io/#IndianBartonka/Indian-Utils)
[![wakatime](https://wakatime.com/badge/user/adf40340-ba3b-43af-87af-de3920ec0c1b/project/5e41acf1-0b55-470c-be02-5b2f68bbdee0.svg)](https://wakatime.com/badge/user/adf40340-ba3b-43af-87af-de3920ec0c1b/project/5e41acf1-0b55-470c-be02-5b2f68bbdee0)
</div>

## Wymagania

**Java**: 17+

# Biblioteki użyte w projekcie

### [JetBrains Annotations 26.0.1](https://github.com/JetBrains/java-annotations)

Używane do lepszego wyglądu kodu i ewentualnych informacjach dla użytkownika.

### [Gson 2.11.0](https://github.com/google/gson)

Używane do łatwiej serialziacij i deserialziacij Json 

TODO: POPRSW TO
### [JUnit Jupiter API 5.11.3](https://junit.org/junit5/)

Używane do prostych testów jednostkowych.

# Zależności

Zależnosci [Maven](#maven-pomxml), [Gradle](#gradle-buildgradle), [SBT](#simple-build-tool-buildsbt)

### Maven `pom.xml`

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

### Gradle `build.gradle`

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.IndianBartonka:Indian-Utils:TAG'
}
```

### Simple Build Tool `build.sbt`

```sbt
resolvers += "jitpack" at "https://jitpack.io"
```

```sbt
libraryDependencies += "com.github.IndianBartonka" % "Indian-Utils" % "TAG"    
```

# Przy projekcie pomagali

![Wykres kontrybucji](https://contrib.rocks/image?repo=IndianBartonka/Indian-Utils)

# Gwiazdki na przestrzeni czasu

[![Stargazers over time](https://starchart.cc/IndianBartonka/Indian-Utils.svg?variant=adaptive)]()
