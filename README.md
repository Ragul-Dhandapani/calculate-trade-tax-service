**CALCULATE TRADE TAX SERVICE**

It has two endpoints <br>
    • POST createTradeDetails => Stores the Trade information into Database <br>
    • GET calculateTaxDue/{trade_id} => Find the trade_id in DB and calculate the tax due

<br>

Calculating the Tax Due formula : No.of shares * shares per price * tax rate <br>


To start the server Run the "CalculateTradeTaxServiceApplication.java" <br>
