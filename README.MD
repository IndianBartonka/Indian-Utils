<div align="center">

# Indian Utils

Utile używane w różnych projektach przez Indiana

[![Jitpack](https://jitpack.io/v/IndianBartonka/Indian-Utils.svg)](https://jitpack.io/#IndianBartonka/Indian-Utils)
[![wakatime](https://wakatime.com/badge/user/adf40340-ba3b-43af-87af-de3920ec0c1b/project/5e41acf1-0b55-470c-be02-5b2f68bbdee0.svg)](https://wakatime.com/badge/user/adf40340-ba3b-43af-87af-de3920ec0c1b/project/5e41acf1-0b55-470c-be02-5b2f68bbdee0)
</div>

## Wymagania

**Java**: 17+

---

## Testowano na

### Windows

* Windows 11 (26100.3194)

### Linux

* FerenOs (6.8.0-57-generic)
* Debian (6.1.0-31-amd64)
* Debian ARM (Raspberry Edition 6.12.25+rpt-rpi-2712)

---

# Biblioteki użyte w projekcie

### [JetBrains Annotations 26.0.2](https://github.com/JetBrains/java-annotations)

Używane do lepszego wyglądu kodu i ewentualnych informacjach dla użytkownika.

### [Gson 2.13.1](https://github.com/google/gson)

Używane do łatwiej serializacji i deserializacji Json

### [JUnit Jupiter API 5.13.0-M3](https://junit.org/junit5/)

Używane do prostych testów jednostkowych.

---

# Używane w poleceniach cmd

Niektórych danych nie można pozyskiwać javą, a nie zamierzałem pisać natywnych plików cpp i używać JNA, zależy mi na
lekkości libki

## Windows

#### 1. Powershell

## Linux

#### 1. Pozyskiwanie informacji o połączeniu sieciowym

- nmcli
- wpa_cli

#### 2. Filtrowanie i przetwarzanie tekstu

- grep
- cut
- xargs
- awk
- sed
- basename

#### 3. Listowanie urządzeń PCI/PCIe

- lspci

#### 4. Informacje o dyskach

- lsblk
- df

#### 5. Informacje o pamięci RAM

- dmidecode

#### 6. Informacje o procesach

- ps

## Pobieranie

```shell
sudo apt update && sudo apt install -y pciutils coreutils grep gawk sed util-linux dmidecode procps
```

I ewentualnie

```shell
sudo apt update && sudo apt install -y network-manager
```

```shell
sudo apt update && sudo apt install -y wpasupplicant
```

---

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

---

# Przy projekcie pomagali

![Wykres kontrybucji](https://contrib.rocks/image?repo=IndianBartonka/Indian-Utils)

---

# Gwiazdki na przestrzeni czasu

[![Stargazers over time](https://starchart.cc/IndianBartonka/Indian-Utils.svg?variant=adaptive)]()
