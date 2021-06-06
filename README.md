<img src="https://i.pinimg.com/originals/2c/f3/0f/2cf30ffdbfa3db621d303e9575ff9e47.gif" width="250px" align="right" alt="Computer">
<h1 align="center">UzmCommon<img width="50px" align="center" src="https://www.fuyu.red/images/icons/atom.gif"></h1>
<h3 align="center">A set of features based on SpigotAPI with support for update and multi-version</h3>


<li align="left"><img align="center" width="40px" src="https://theminecrafthosting.com/img/JAR-Icons/papermc_logomark_500.png"> This common at the moment supports: <strong>1.8x-1.16x versions</strong></li>
<li align="left"><img align="center" width="40px" src="https://media2.giphy.com/media/6IAzxmKVaYDLFMe1Aw/giphy.gif">Developed with ❤️ by <strong>UzmStudio Inc. (JotaMPê)</strong></li>

<h1 align="center">UzmStudio Inc. official plugins that use:</h1>

 <div align="center" position="absolute">
    <ul><strong>luth.randomspawn (Private)</strong></ul>
    <ul><strong>uzm.durability (Private)</strong></ul>
    <ul><strong>flowcraft.anticheat (Private)</strong></ul>
    <ul><strong>luth.randomspawn (Private)</strong></ul>
    <ul><strong>faithmc.machine (Private)</strong></ul>
    <ul><strong>flowcraft.farming (Private)</strong></ul></div>


Some usages
------

The `Handler.Cache` class is how you build an AnvilGUI. 
The following methods allow you to modify various parts of the displayed GUI. Javadocs are available [here](http://docs.wesjd.net/AnvilGUI/).

#### `onClose(Consumer<Player>)` 
Takes a `Consumer<Player>` argument that is called when a player closes the anvil gui.
```java                                             
builder.onClose(player -> {                         
    player.sendMessage("You closed the inventory.");
});   
```
Maven Information
------

* Repository
```xml
<repository>
    <id>codemc-snapshots</id>
    <url>https://repo.codemc.io/repository/maven-snapshots/</url>
</repository>
```
 * Artifact Information:
```xml
<dependency>
    <groupId>com.uzm</groupId>
    <artifactId>uzm-common</artifactId>
    <version>2.0.0</version>
    <scope>provided</scope>
</dependency>
 ```

Gradle Information
------

 * Repository:
```groovy
repositories {
    maven {
        url 'https://repo.codemc.io/repository/maven-snapshots/'
    }
}
```
 * Artifact:
```groovy
dependencies {
    compileOnly 'com.uzm:uzm-common:2.0.0'
}
```
