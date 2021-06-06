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

The `CacheHandler` class is a handler to facilitate the creation of cache containers.
Below is a practical use of this handler:

#### [`CacheHandler`](https://github.com/JaelysonM/uzm-common/blob/5d11deb5b6999460beab13c5550be96d888fff40/Common/src/main/java/com/uzm/common/containers/cache/CacheHandler.java)
```java                                             
public class ExampleContainer extends CacheHandler {
    private String exampleString = "Example";

    public ExampleContainer(String exampleString) {
        this.exampleString = exampleString;
    }

    @Override
    public void gc() {
        // Here you will "clear" the variables from the cache to help the Garbage Collector.

        this.exampleString = null; // Declare as null...
    }

    @Override
    public void save(boolean async) {
     /*
     Here you will put your business rule to save something in a database,
     it can be async or not, but the use of this method is optional.
     */
        // database.update(key,this); hypothetical example.
    }


    @Override
    public void load() {
     /*
     Here you will put your business rule to load something from database,
     but the use of this method is optional.
     */
        // database.load(key); hypothetical example.
    }
}   
```

The `PlayerCacheHandler` class is extension from `CacheHandler`, which facilitate the creation of player cache containers.
This handler already contains the `getPlayer()` and `getUuid()` methods.
Below is a practical use of this handler:

#### [`PlayerCacheHandler`](https://github.com/JaelysonM/uzm-common/blob/5d11deb5b6999460beab13c5550be96d888fff40/Common/src/main/java/com/uzm/common/containers/cache/PlayerCacheHandler.java)
```java                                             
public class PlayerExampleContainer extends PlayerCacheHandler {
    private String exampleString = "Example";

    public PlayerExampleContainer(Player player, String uuid) {
        super(player, uuid);
    }


    @Override
    public void gc() {
        // Here you will "clear" the variables from the cache to help the Garbage Collector.

        this.exampleString = null; // Declare as null...
    }

    @Override
    public void save(boolean async) {
     /*
     Here you will put your business rule to save something in a database,
     it can be async or not, but the use of this method is optional.
     */
        // database.update(key,this); hypothetical example.
    }


    @Override
    public void load() {
     /*
     Here you will put your business rule to load something from database,
     but the use of this method is optional.
     */
        // database.load(key); hypothetical example.
    }
}
```

The `PlayerCacheHandler` and `CacheHandler` classes have built-in methods, which are `getCache()`, `collections()` and `destroy()`

Now let's see the use of each one

#### `getCache(String, Class<T>, Object...)`

This method has two bears, if there was a record in the cache with the `String`, it will return a `CompletebleFuture<T>` from the container of `Class<T>`.

```java

   ExampleContainer container = CacheHandler.getCache("exampleUniqueKey", ExampleContainer.class).join();
   
   System.out.println(container.getSomething());
   
   // or...
   
   PlayerExampleContainer container = PlayerCacheHandler.getCache("exampleUniqueKey", PlayerExampleContainer.class).join();
   
   container.getPlayer().sendMessage("Hello");

```

But if there isn't something registered in the `Class<T>` cache with the `String`, it will create a new one from the `Object...`


```java

   ExampleContainer container = CacheHandler.getCache("exampleUniqueKey", ExampleContainer.class, "something").join();
   
   System.out.println(container.getSomething());
   
   // or...
   
   Player player;
   String uuid;
   
   PlayerExampleContainer container = PlayerCacheHandler.getCache("exampleUniqueKey", PlayerExampleContainer.class, player, uuid).join();
   
   container.getPlayer().sendMessage("Hello");

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
    <version>2.0.1</version>
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
