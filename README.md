# Order-book-snapshots
Order book app for receiving Snapshots order book order by LINKUSDT via REST API every 10 seconds and 
## Project Structure
* package src/main/java/org/example contains:
  * config package:
    * Config.java, app configuration with constants where you can specify symbol & limit for get request
  * models package:
    * AbstractBook.java, abstract book(asks/bids) model representation
    * Ask.java, asks model representation (extends AbstractBook)
    * Bid.java, bids model representation (extends AbstractBook)
    * OrderBook.java, order book model representation, contains bidsList & asksList, implements methods for operations on lists
  * services package:
    * JsonParser.java, makes a request to get JSON response
    * LoggerService.java, implements application logging
    * OrderBookManagerService.java, implements interaction and calculation logic for models
  * App.java, starts the app
* order-book-snapshots-log - logs, calculation result output
* pom.xml maven configuration and info project.
```
.
├───src
│   ├───main/java/org/example
│   │   ├───config
│   │   │   └───Config.java
│   │   ├───models
│   │   │   ├───AbstractBook.java
│   │   │   ├───Ask.java
│   │   │   ├───Bid.java
│   │   │   └───OrderBook.java
│   │   ├───services
│   │   │   ├───JsonParser.java
│   │   │   ├───LoggerService.java
│   │   │   └───OrderBookManagerService.java
│   │   └───App.java
│   └───test/java/org/example
│       └───AppTest.java
├───.gitignore
├───LICENSE
├───README.md
├───order-book-snapshots-log
└───pom.xml
```
