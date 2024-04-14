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
- creating a concise overview for a selected participant showing their incoming or outgoing debts
- copying an invite-code which can be shared with others to join this event
### Admin-page
This page is locked behind authorization which is done through a code generated on the server-side.  
Authorization can be achieved by entering this code on the client-side.  
Once authorized, a token is generated and stored, allowing future admin access without need for a new code.  
From the page, the following is possible:
- see all events stored in the DB along with some basic data regarding them
- sort these events on creation-date, title, last-activity (asc/desc)
- join an event to get access to it
### Keyboard navigation + shortcuts
The program can be navigated using the keyboard.
- Use tab to navigate between buttons.
- Use arrow keys to navigate between tabs & dropdown options.
- Use enter to select/submit.
- Use ctrl+z to undo transactions in event-page.
### Preferred currency
The program supports multiple currencies and allows the user to select their preferred currency from the settings.
- The currency is used to display sums so a user might better understand the amounts.
- We use a third party API to convert the currency to the preferred currency.
- Currently, we support Euro, Dollar and Swiss Francs.
- The user can change the currency at any time.
- Currency exchange rates are cached both on the server and in the client.
- The proper functionality of the currency conversion is dependent on the third party API,
- so in case of loss of connection or the third party's API stops or is changed, the rates might be incorrect or outdated.
- On the client-side, the currency is stored in the local storage and is not shared with the server. (See RateUtils.java)
- On the server, the rates is stored in a Set, but also in storage,
- in the cause of loss of communication with the third party, to give a rough estimate (See CurrencyExchange.java)
- In the database everything is stored in the base currency (Euro) and is converted to the preferred currency when displayed.
- For settling debts, both the base currency and the preferred currency are used.

---
## Technical details
### WebSockets
- We use WebSockets to update the admin page in real-time.
- When an update occurs that would be relevant to the admin page,
- the server sends a message to the admin page.
- This includes: adding, updating or deleting an event, transaction, participant, and also when a JSON dump is uploaded
- For implementation see: ServerUtils.java, AdminPageCtrl.java, WebSocketConfig.java and the respective controllers.