# (DRAFT) OOPP: transaction handler

---
## Running the program
### Server-side
#### The server side can be run using the following commands:   
**LINUX:** `./gradlew bootrun`  
**WINDOWS** `gradlew bootrun`  
#### It contains logic for managing the following:
- API-transaction calls
- Database interactions (H2)
- Authorization for accessing admin features
### client-side
**LINUX:** `./gradlew run`  
**WINDOWS** `gradlew run`
#### It contains logic for managing the following:
- an interface for the client to manage their events
- an interface for an admin to manage the database
- useful data regarding added transactions
- switching to a different language

---
## Utilizing the program
### Start-page
This is the main portal of the program and provides further access to specific pages.  
From this page, it is possible to:
- Create a new event
- See and access (recently) joined events
- Access the admin-page
- join an event through an invite-code
### Event-page
This page focuses on a single event and provides many ways to interact with it:
- creating, reading, updating and deleting of:
  - transactions
  - participants
  - tags
- seeing statistics regarding transactions
- *running custom queries* &rarr; **will this be implemented?**
- creating a concise overview for a selected participant showing their incoming and outgoing debts
- copying an invite-code which can be shared with others to join this event
### Admin-page
This page is locked behind authorization which is done through a code generated on the server-side.  
Authorization can be achieved by entering this code on the client-side.  
Once authorized, a token is generated and stored, allowing future admin access without need for a new code.  
From the page, the following is possible:
- see all events stored in the DB along with some basic data regarding them
- sort these events on creation-date, title, last-activity (asc/desc)
- join an event to get access to it

---
