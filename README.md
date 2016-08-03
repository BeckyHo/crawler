### a crawler demo

#### introduce

this crawler application depends on apache httpclient4.5.2, lucene6.1 and htmlparser2.1

the main class at com.engine.launch.EngineLaunch.java

#### framework

i specified ten threads at initConfiguration.properties, so the framework like this producer-consumer pattern.

* public queue in class CrawlerManager, which maintains a instance of Todo and VisitedMap

* Even if all the threads of producers, but also consumers
