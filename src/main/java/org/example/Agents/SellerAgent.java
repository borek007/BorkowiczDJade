package org.example.Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SellerAgent extends Agent {
    private int bookstore_id;
    public SellerAgent(int bookstore_id) {
        this.bookstore_id = bookstore_id;
    }

    protected void setup() {
        System.out.println("Seller-agent "+getAID().getName()+" is ready.");

        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.addSubBehaviour(new HandleCallForProposalBehaviour(this));
        addBehaviour(sequentialBehaviour);
    }

    protected void takeDown() {
        System.out.println("Seller-agent "+getAID().getName()+" terminating.");
    }
    public class HandleCallForProposalBehaviour extends Behaviour {
        private boolean done = false;

        public HandleCallForProposalBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // Handle the call for proposal from the ManagerAgent
                // For now, we just print out a message
                System.out.println("Received a call for proposal from " + msg.getSender().getName());

                // TODO: Add logic to handle the call for proposal

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
