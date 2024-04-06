package org.example.Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.Bookstore;

public class ManagerAgent extends Agent {
    Bookstore bookstore;
    protected void setup(Bookstore bookstore) {
        this.bookstore = bookstore;
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

                // TODO: Add logic to check the availability and price of the book

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