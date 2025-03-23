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
Got request to execute buy stock: Reliance quantity: 6 price: 450.0 <br>
Adding order in pending buy req BR2 quantity 6 price 450.0 <br>
Got request to execute buy stock: Reliance quantity: 1 price: 490.0 <br>
Adding order in pending buy req BR3 quantity 1 price 490.0 <br>
Got request to execute sell stock: Reliance quantity: 8 price: 350.0 <br>
Trade happened with following details: <br>
- Trade details : Buyer Order-Id BR2 Seller Order-Id SR1 Quantity 6 per share amount 350.0 <br>
- Trade details : Buyer Order-Id BR3 Seller Order-Id SR1 Quantity 1 per share amount 350.0 <br>

Adding order in pending sell req SR1 quantity 1 price 350.0 <br>

_Current State for "Reliance"_

Pending Buy Request for stock: Reliance <br>
Order Id: BR1 Quantity: 2 price: 300.0 <br>

Pending Sell Request for stock: Reliance <br>
Order Id: SR1 Quantity: 1 price: 350.0 <br>

Got request to execute sell stock: Reliance quantity: 8 price: 150.0  <br>
Trade happened with following details: <br>
- Trade details : Buyer Order-Id BR1 Seller Order-Id SR2 Quantity 2 per share amount 150.0 <br>

Adding order in pending sell req SR2 quantity 6 price 150.0 <br>

_Current State for "Reliance"_

Pending Buy Request for stock: Reliance <br>
No req pending.

Pending Sell Request for stock: Reliance <br>
Order Id: SR1 Quantity: 1 price: 350.0 <br>
Order Id: SR2 Quantity: 6 price: 150.0 <br>

Got request to execute buy stock: Reliance quantity: 10 price: 500.0 <br>
Trade happened with following details: <br>
- Trade details : Buyer Order-Id BR4 Seller Order-Id SR1 Quantity 1 per share amount 350.0 <br>
- Trade details : Buyer Order-Id BR4 Seller Order-Id SR2 Quantity 6 per share amount 150.0 <br>
Adding order in pending buy req BR4 quantity 3 price 500.0 <br>

_Current State for "Reliance"_

Pending Buy Request for stock: Reliance <br>
Order Id: BR4 Quantity: 3 price: 500.0 <br>

Pending Sell Request for stock: Reliance <br>
No req pending.

Got request to execute buy stock: Tata quantity: 10 price: 500.0 <br>
Adding order in pending buy req BR5 quantity 10 price 500.0 <br>


_Current State for "Tata"_

Pending Buy Request for stock: Tata <br>
Order Id: BR5 Quantity: 10 price: 500.0 <br>

Got request to execute sell stock: Tata quantity: 8 price: 150.0 <br>
Trade happened with following details: <br>
Trade details : Buyer Order-Id BR5 Seller Order-Id SR2 Quantity 8 per share amount 150.0 <br>
Adding order in pending sell req SR2 quantity 8 price 150.0 <br>

_Current State for "Tata"_

Pending Buy Request for stock: Tata <br>
Order Id: BR5 Quantity: 2 price: 500.0 <br>

Pending Sell Request for stock: Tata <br>
Order Id: SR2 Quantity: 8 price: 150.0 <br>


_Current State for "Reliance"_

Pending Buy Request for stock: Reliance <br>
Order Id: BR4 Quantity: 3 price: 500.0 <br>

Pending Sell Request for stock: Reliance <br>
No req pending.
