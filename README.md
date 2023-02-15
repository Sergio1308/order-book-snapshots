# Order-book-snapshots
App for receiving Order Book Snapshots by LINKUSDT via REST API every 10 seconds. The result is written to the log, which is located in the root directory.
## Project Structure
* package src/main/java/org/example contains:
  * config package:
    * Config.java, app configuration with constants where you can specify symbol & limit for get request
  * models package:
    * AbstractBook.java, abstract book(asks/bids) model representation
    * Ask.java, asks model representation (extends AbstractBook)
    * Bid.java, bids model representation (extends AbstractBook)
    * OrderBook.java, order book model representation, contains bidsMap & asksMap, implements methods to work with them
  * services package:
    * JsonParser.java, makes a request to get JSON response and parse it
    * InvalidUrlException.java and JsonParsingException.java, own exceptions
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
│   │   │   ├───InvalidUrlException.java
│   │   │   ├───JsonParser.java
│   │   │   ├───JsonParsingException.java
│   │   │   ├───LoggerService.java
│   │   │   └───OrderBookManagerService.java
│   │   └───App.java
│   └───test/java/org/example
│       ├───JsonParserTest.java
│       ├───OrderBookManagerServiceTest.java
│       └───OrderBookTest.java
├───.gitignore
├───LICENSE
├───README.md
├───order-book-snapshots-log
└───pom.xml
```
