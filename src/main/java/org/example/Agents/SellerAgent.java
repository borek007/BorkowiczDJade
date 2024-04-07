package org.example.Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.Bookstore;

public class SellerAgent extends Agent {
    Bookstore bookstore;

    public SellerAgent(Bookstore bookstore) {
        this.bookstore = bookstore;
    }

    protected void setup() {
//        DFAgentDescription dfd = new DFAgentDescription();
//        dfd.setName(getAID());

        System.out.println("Seller-agent "+getAID().getName()+" is ready.");


//        addBehaviour( new HandleCallForProposalBehaviour(this));

    }

    protected void takeDown() {
        System.out.println("Seller-agent "+getAID().getName()+" terminating.");
    }
    public class HandleCallForProposalBehaviour extends CyclicBehaviour {
        public class Pair<T, U> {
            public final T first;
            public final U second;

            public Pair(T first, U second) {
                this.first = first;
                this.second = second;
            }
        }

        public HandleCallForProposalBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);

            if (msg == null) {
                block();
            }
            // Handle the call for proposal from the ManagerAgent
            // For now, we just print out a message
            System.out.println("Received a call for proposal from " + msg.getSender().getName());
            System.out.println("The book requested is: " + msg.getContent());
            handleCall(msg, msg.getContent());







        }

        private void handleCall(ACLMessage msg, String BookTitle) {
            Pair<String,Boolean> result = askManager(BookTitle);
            if (result.second
//                     ||bookstore.getBooks().contains(BookTitle)
            ){

                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
//                reply.setContent(String.valueOf(bookstore.getPrice(BookTitle)));
                reply.setContent(result.first);
                myAgent.send(reply);
            } else {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                reply.setContent("-1");
                myAgent.send(reply);
            }
        }

       private Pair<String,Boolean> askManager(String Booktitle) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(bookstore.getManagerAgent().getAID());
            msg.setContent(Booktitle);
            myAgent.send(msg);
            ACLMessage reply = myAgent.blockingReceive();
            return new Pair<String,Boolean>( reply.getContent(),reply.getPerformative() == ACLMessage.AGREE);


        }


    }
}
