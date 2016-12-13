# Secure Child Locator

## MEIC SIRS 2016

-------------------------------------------------------------------------------

## Instructions


[1] Get source code

```
git clone https://github.com/helenacruz/securechildlocator
```

[2] Install modules

```
cd Communication
mvn clean install
```

-------------------------------------------------------------------------------

### Server

```
cd Server
mvn clean install
mvn compile exec:java
```

-------------------------------------------------------------------------------

### Application

Open build.gradle with Android Studio and run in a virtual device running 
Android 5.1 (with Google APIs).
