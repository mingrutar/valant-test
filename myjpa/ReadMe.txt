---- 4/3/2016 fixed unit tests ----
Mockito mock does not seem working with generic types. Mofidied the 
event notification unit tests to verify the output written by the notification 
receiver, since this receiver just write a message to the log when it is notified.

---- 4/3/2016 add gradle ----
./gradlew build			//for build
./gradlew bootRun       //to run the rest server

---- 4/2/2016 fix github issues  ----
1) change the behavior of adding item: 
 a) if item not exist, add it and return HTTP status CREATED. The following is the command and output in log:
	curl -H "Content-Type: application/json" -X POST -d '{"label":"label20","expiration":234567,"type":"A"}' http://localhost:8080/
	in log: 2016-04-02 17:06:08.189  INFO 5812 --- [nio-8080-exec-3] c.m.controller.ValantController          : Added item: Item [id=7, label=label20, expiration=Wed Dec 31 16:03:54 PST 1969, type=A]
 
 b) if the item exist, ignore the item and return HTTP status UNPROCESSABLE_ENTITY. The following is the command and output in log:
	curl -H "Content-Type: application/json" -X POST -d '{"label":"label2","expiration":234567,"type":"A"}' http://localhost:8080/
	2016-04-02 17:07:16.753  INFO 5812 --- [nio-8080-exec-5] c.m.controller.ValantController          : Item already exist. Ignored the item for saving.

2) the notification is built on Spring EventBus. Currently, the receiver (see SimpleReceiver.java) writes a message to the log. 
   Next item illustrates the details of how to verify expiration notifications.

3) for testing expiration without waiting, checkout branch 'fixIssues' or 
   in src/main/resources/application.properties: change 'seedDatabase = false' => 'seedDatabase = true'. 
   this will enable seeding.
    This will seed 4 items to the inventory db by executing MyjpaApplication::seedDB(InventoryRepository ir):
		a) Item("Label1", getExpirationDate(-1), "Type_1");		//yesterday
		b) Item("Label2", getExpirationDate(1), "Type_2");		//tomorrow
		c) Item("Label4", getExpirationDate(-2), "Type_4");		//2 days ago
		d) Item("Label3", getExpirationDate(10), "Type_3");		//10 days later
    look the log:
		2016-04-02 18:01:17.348  INFO 9516 --- [pool-2-thread-1] com.mingvalant.services.Notificator      : <=Notificator sent an expiration EVENT for item Label1 that has expired 1 hours.
		2016-04-02 18:01:17.348  INFO 9516 --- [pool-2-thread-1] com.mingvalant.services.Notificator      : <=Notificator sent an expiration EVENT for item Label4 that has expired 2 hours.
		2016-04-02 18:01:17.371  INFO 9516 --- [dPoolExecutor-1] com.mingvalant.services.SimpleReceiver   : =>SimpleReceiver received an EVENT expired: Item [id=1, label=Label1, expiration=2016-04-01 18:01:16.18, type=Type_1]
		2016-04-02 18:01:17.374  INFO 9516 --- [dPoolExecutor-2] com.mingvalant.services.SimpleReceiver   : =>SimpleReceiver received an EVENT expired: Item [id=3, label=Label4, expiration=2016-03-31 18:01:16.262, type=Type_4]
    * note: because the requirement does not ask to remove the expired items, so the messages continue. 
    * For illustration, the schedule task duration is very short.

4) Add unit test for adding duplicated item. 
5) Removed unit test for expirated item - it is wrong since the expired item is not removed.

========== Assignment ==========
Write an Inventory System that provides add and delete API.

==========  Solution  ==========
The framework:
   Built the system using Spring boot framework with Java 1.8.

To download from github:
   git clone https://github.com/prirodancoder/valant-test.git
 
To launch:
 cd myjpa
    ./mvnw deploy

  @ I am able to launch it at my Windows git terminal. if you have trouble to launch, please let me know and please provide detailed steps.

 To verify:
    View in a browser with uri 'http://127.0.0.1:8080/' 
      it returns a empty json array if you have not add an item. 
      If you like see some recorde at start, modify src/main/resources/application.properties file 'seedDatabase = false' to 'seedDatabase = true'. You will see 3 pre seeded records.

To add an item, at terminal run:
    curl -H "Content-Type: application/json" -X POST -d '{"label":"xyz","expiration":234567,"type":"A"}' http://localhost:8080/
    
To delete an item, at terminal run:
    curl -i -H "Accept: application/json" -X DELETE http://localhost:8080/Label1

Project layout: 
 src/main                           // root for production code
 |_java                             // root for java code
      com.mingvalant                // package name
       | - MyjpaApplication.jave    // spring Application. 
                                       It registeres event receiver and shut it when app is shutdown
                                       Optionnally, it seed the database 
        _controller
         | - ValantController.java  // spring RestController, rest APIs:
                                       add(Item) - HTTP request method POST
                                       delete(label) - HTTP request method DELETE
                                       allItems() - HTTP request method GET
         |_model
         | - InventoryRepository    // spring CrudRepository. here uses in-memory h2 databse
         | - Item                   // a JPA entity
         |_service
           - Notificator            // a spring publisher service
                                       it sends messages when an item is deleted
                                       or items have expirated
           - SimpleReceiver         // a spring receiver. It print the received message 
           - CheckExpirationTask    // a spring scheduled task. It checks for expired items 
                                       in every minute. If any, it asks Notificator to publish every expired item.
      |_resources                   // root for resource files
        | - application.properties  // for now only contains property 'seedDatabase'
        | - log4j.properties        // define log4j properties
  |test                             // root for unit test code
    | - com.mingvalant.MyjpaApplicationTests  // test the RestCobtroller
      |_
         CheckExpirationTaskTest    // uses mockito framework
         NotificatorTest            // uses mockito framework.

    @ The design follows spring paradiagm. 

TODO and discussion:
1) currently the JSON for Expiration is in epoch format. Would be nice if it is in a more user friendly format. But on the other hands, this is a backend server, the front app should easily convert user-friendly date format to epoch.
2) Done: two tests in NotificatorTest failed due to mockito verify not accept generic class type. Need more research on that.
3) Security. this system let user modify records in the database. It should implement authorization and authentication mechanisms. Due to time constrain, I skipped security.
4) more unit tests
5) Done: provide gradle scripts ( yet to learn)

If you have any questions, please let me know. 
Thank you for reading my code!
