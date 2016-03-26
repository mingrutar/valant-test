========== Assignment ==========

Write an Inventory Syste that provides add and delete API.

==========  Solution  ==========
The framework:
   Built the system using Spring boot framework with Java 1.8. 

To download from github:
   git clone https://github.com/prirodancoder/valant-test.git 
 
To launch:
 cd myjpa
    ./mvnw deploy

  @ I am able to launch it at my Windows git terminal. if you have trouble to launch, please let me know and please provide detailed steps.

 To verufy: 
    View in a browser with uri 'http://127.0.0.1:8080/'
      it returns a empty json array if you have not add an item.
      If you like see some recorde at start, modify src/main/resources/application.properties file 'seedDatabase = false' to 'seedDatabase = true'. You will see 3 pre seeded records.

To add an item, at terminal run:
    curl -H "Content-Type: application/json" -X POST -d '{"label":"xyz","expiration":234567,"type":"A"}' http://localhost:8080/
    
To delete an item, at terminal run:
    curl -i -H "Accept: application/json" -X DELETE http://localhost:8080/Label1

Project layout:
 src/main                           // root for production code
 |_java                            // root for java code
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
2) two tests in NotificatorTest failed due to mockito verify not accept generic class type. Need more research on that.
3) Security. this system let user modify records in the database. It should implement authorization and authentication mechanisms. Due to time constrain, I skipped security.
4) more unit tests
5) provide gradle scripts ( yet to learn)

If you have any questions, please let me know. 
Thank you for reading my code!
