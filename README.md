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


**Overall Output**  <br>

Got request to execute buy stock: Reliance quantity: 2 price: 300.0 <br>
Adding order in pending buy req BR1 quantity 2 price 300.0 <br>
Got request to execute buy stock: Reliance quantity: 6 price: 450.0<br>
Adding order in pending buy req BR2 quantity 6 price 450.0<br>
Got request to execute buy stock: Reliance quantity: 1 price: 490.0<br>
Adding order in pending buy req BR3 quantity 1 price 490.0<br>
Got request to execute sell stock: Reliance quantity: 8 price: 350.0<br>
Trade happened with following details:<br>
- Trade details : Buyer Order-Id BR2 Seller Order-Id SR1 Quantity 6 per share amount 350.0
- Trade details : Buyer Order-Id BR3 Seller Order-Id SR1 Quantity 1 per share amount 350.0

Adding order in pending sell req SR1 quantity 1 price 350.0

Current State for Reliance <br>
Pending Buy Request for stock: Reliance<br>

Pending Sell Request for stock: Reliance <br>
Order Id: SR1 Quantity: 1 price: 350.0<br>

Got request to execute sell stock: Reliance quantity: 8 price: 150.0<br>
Adding order in pending req SR2 quantity 8 price 150.0 <br>
Current State for Reliance<br>
Pending Buy Request for stock: Reliance<br>

Pending Sell Request for stock: Reliance<br>
Order Id: SR1 Quantity: 1 price: 350.0<br>
Order Id: SR2 Quantity: 8 price: 150.0<br>

Got request to execute buy stock: Reliance quantity: 10 price: 500.0<br>
Trade happened with following details:<br>
- Trade details : Buyer Order-Id BR4 Seller Order-Id SR1 Quantity 1 per share amount 350.0
- Trade details : Buyer Order-Id BR4 Seller Order-Id SR2 Quantity 8 per share amount 150.0


Adding order in pending buy req BR4 quantity 1 price 500.0<br>

_Current State for Reliance_ <br>
Pending Buy Request for stock: Reliance<br>
Order Id: BR4 Quantity: 1 price: 500.0<br>

Pending Sell Request for stock: Reliance<br>
No req pending.<br>
Got request to execute buy stock: Tata quantity: 10 price: 500.0<br>
Adding order in pending buy req BR5 quantity 10 price 500.0<br>

_Current State for Tata<br>_ <br>
Pending Buy Request for stock: Tata<br>
Order Id: BR5 Quantity: 10 price: 500.0<br>

Pending Sell Request for stock: Tata <br>
No req pending. <br>
Got request to execute sell stock: Tata quantity: 8 price: 150.0<br>
Trade happened with following details: <br>
- Trade details : Buyer Order-Id BR5 Seller Order-Id SR2 Quantity 8 per share amount 150.0<br>

_Current State for Tata_<br>
Pending Buy Request for stock: Tata <br>
Order Id: BR5 Quantity: 2 price: 500.0<br>

Pending Sell Request for stock: Tata<br>
No req pending.<br>
Current State for Reliance<br>
Pending Buy Request for stock: Reliance<br>
Order Id: BR4 Quantity: 1 price: 500.0<br>

Pending Sell Request for stock: Reliance <br>
No req pending.<br>

Flow Design.

1. Request is landing to TradeService.
2. Then tradeService valides the requests.
3. Then it extracts list of available old requests with which requests will be fulfilled.
4. Then it tries to match it with available old orders.
5. After generating matching order mapping, it creates a trades between orders.
6. Updates the old orders with left quantity requests.
7. In case current requests contains more full-filled quantities, it pushes this also in pending lists.

TODO:
Request model and entities should be different.