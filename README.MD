<div align="center">

# Indian Utils

Utile używane w różnych projektach przez Indiana

[![](https://jitpack.io/v/IndianBartonka/Indian-Utils.svg)](https://jitpack.io/#IndianBartonka/Indian-Utils)
[![wakatime](https://wakatime.com/badge/user/adf40340-ba3b-43af-87af-de3920ec0c1b/project/5e41acf1-0b55-470c-be02-5b2f68bbdee0.svg)](https://wakatime.com/badge/user/adf40340-ba3b-43af-87af-de3920ec0c1b/project/5e41acf1-0b55-470c-be02-5b2f68bbdee0)
</div>

## Wymagania

**Java**: 17+

### Przykłady użycja

Przykłady użycja znajdziecie w klasie [Test.java](src%2Fmain%2Fjava%2Fpl%2Findianbartonka%2Futil%2FTest.java)
# Przy projekcie pomagali

![Wykres kontrybucji](https://contrib.rocks/image?repo=IndianBartonka/Indian-Utils)

# Zależności

Zależnosci [Maven](#maven-pomxml), [Gradle](#gradle-buildgradle), [SBT](#simple-build-tool-buildsbt)

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

## Simple Build Tool `build.sbt`

```sbt
resolvers += "jitpack" at "https://jitpack.io"
```

```sbt
libraryDependencies += "com.github.IndianBartonka" % "Indian-Utils" % "Tag"    
```
