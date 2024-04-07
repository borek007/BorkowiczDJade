package org.example.Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.Bookstore;

public class ManagerAgent extends Agent {
    Bookstore bookstore;
    public ManagerAgent(Bookstore bookstore) {
        this.bookstore = bookstore;
    }
    public void setup() {

        //         Add behaviours
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.addSubBehaviour(new HandleBookAvailabilityCheckBehaviour(this));
        addBehaviour(sequentialBehaviour);

        System.out.println("Manager-agent "+getAID().getName()+" is ready.");


    }

    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Manager-agent "+getAID().getName()+" terminating.");
    }


    private class HandleBookAvailabilityCheckBehaviour extends Behaviour {
        private boolean done = false;

        public HandleBookAvailabilityCheckBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // Handle the request from the SellerAgent to check the availability and price of the book
                // For now, we just print out a message
                System.out.println("Received a request to check book availability and price from " + msg.getSender().getName());
                String BookTitle = msg.getContent();
                if (bookstore.getBooks().contains(BookTitle)) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.AGREE);
                    reply.setContent(String.valueOf(bookstore.getPrice(BookTitle)));
                    myAgent.send(reply);
                } else {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("-1");
                    myAgent.send(reply);
                }


                done = true;
            } else {
                block();
            }
        }

        public boolean done() {
            return done;
        }
    }
}