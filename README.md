# SaveMe
SaveMe Android Application repository.

SaveMe is an application that aims to be an alternative to the contacts application that is on all modern phones. SaveMe differs from the regular contacts application by including a QR code generator and QR code scanner. The inclusion of these features was decided up when one of our members of our development team was complaining about how he kept losing friends he had made when he went to concerts. This was due to concerts usually being held in an area with poor moblie service. We wanted a way for them to exchange information without dealing with typing it on each others phones, being unable to hear each others information due the background noise of the concert, and avoid dealing with poor internet service.

The main features of SaveMe are,
          
    1. Store any contact information that the user sets up
    2. The ability to obtain information from another user
    3. The use of a QR code generator to create a code that another user can scan to get the users information
    4. The use of a QR code scanner that allows the app to obtain information from other users QR codes
    
For storing our contact information we were originally going to work with Firebase but we have opted out on that endevor due to the hurdle of integrating Firebase into SaveMe, instead we decided to use SQLite. In SaveMe we plan to have 3 tables; the first table deals with the User's own information stored on the application, the second table deals with all the information the user collects from other users, and the third table is used to store all previous information collected.  

For the QR code generator and QR code scanner we have decided to use ZXing, an open source multi-format 1D/2D barcode image processing library implemented in Java. Using ZXing, short for Zebra Crossing, we were able to code the fragments that handle the QR code generation and the QR code scanning.

