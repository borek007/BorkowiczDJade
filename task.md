nstructions
We assume that there is a Client in the system, 
represented by ClientAgent, who wants to order a book in a bookstore.
There are several (at least 5) bookstores in the system that offer their services (sale, or printing and sale) and advertise them in the Directory Facilitator agent (DF).
Each bookstore is represented (for the "outside communication") by a SellerAgent. In addition, in every bookstore, there is a ManagerAgent,
which is known only to the SellerAgent and is responsible for books, their prices and printing availability in the bookstore (it knows which books are in stock and which ones can be ordered, 
and which can be printed and when). Bookstores inform (within the DF) what types of books they offer (e.g. IT, science, fiction, horror, ...).



In the first step, ClientAgent asks the DF agent for a list of bookstores that sell books in a specific area.
If there is no bookstore with the selected profile, ClientAgent receives a negative answer. 
If there are such bookstores, it receives (from DF) a list of SellerAgents with whom it can communicate, to discuss purchase. 
ClientAgent sends a CallForProposal to these agents, requesting a specific book (or books) for a specific date (possibly including price).
SellerAgent asks ManagerAgent about the possibility of selling a book (or books) or accepting an order for its/their printing.
ManagerAgent answers YES (and gives the cost of performing such an order and/or date of its completion) or NO (according to the rule invented by you).
As part of the "happy path" scenario, we assume that more than one bookstore will offer the needed book(s).
Then, ClientAgent selects the bookstore (according to the rule you invented) and “complete the transaction",
i.e. (a) confirmation of the order is issued and (b) transfer of data necessary to complete the financial transaction takes place.  
