Features to add
---------------
* Bank commands, topN command
* Selectable location translator method through class
* Deployment pom.xml configuration
* Conversion from BOSEconomy utility

Commands to implement
---------------------

/econ bank create <bank> [owners]
  Creates a new bank account

/econ bank remove <bank>
  Removes a bank account

/econ bank rename <bank> <name>
  Renames a bank account

/econ bank bank <bank> [page]
  Displays bank account balance 

/econ bank listmembers <bank> [page]
  Lists the members of a bank account

/econ bank withdraw <bank> <amount>
  Withdraws money from a bank account

/econ bank deposit <bank> <amount>
  Deposits money into a bank account

/econ bank addmember <bank> <player>
  Adds a member to a bank account

/econ bank removemember <bank> <player>
  Removes a member or owner from a bank account

/econ top
  Displays the top wealthiest players
  Where number of players displayed comes from the config

admin:
/econ bank list [page]
  Lists all bank accounts

/econ player list
  List all player accounts
