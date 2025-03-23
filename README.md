I have taken certain assumptions :

Details : 

Sample1 : 

Timeline of events: 

Got request to execute buy stock: Reliance quantity: 2 price: 300.0 - User1

Got request to execute buy stock: Reliance quantity: 6 price: 450.0 - User2

Got request to execute buy stock: Reliance quantity: 1 price: 490.0 - User3

Got request to execute sell stock: Reliance quantity: 8 price: 350.0 - User4

Then trade would happen at : <br>
	&nbsp;&nbsp; a. Between User2 & User4 - quantity 6 price 350<br>
	&nbsp;&nbsp; b. Between User3 & User4 - quantity 1 price 350

 
Sample2 : 


Got request to execute sell stock: Reliance quantity: 8 price: 350.0 - User1

Got request to execute sell stock: Reliance quantity: 1 price: 450.0 - User2

Got request to execute buy stock: Reliance quantity: 9 price: 500.0 - User3

Then trade would happen at : <br>
	&nbsp;&nbsp; a. Between User1 & User3 - quantity 6 price 350<br>
	&nbsp;&nbsp; b. Between User2 & User3 - quantity 1 price 450
