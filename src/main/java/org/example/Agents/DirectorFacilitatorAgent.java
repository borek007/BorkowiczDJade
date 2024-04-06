package org.example.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.Bookstore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DirectorFacilitatorAgent extends jade.core.Agent{
    private static DirectorFacilitatorAgent instance = null;
    private DirectorFacilitatorAgent() {}
    public static DirectorFacilitatorAgent getInstance() {
        if (instance == null) {
            instance = new DirectorFacilitatorAgent();
        }
        return instance;
    }
    private List<Bookstore> bookstores = new ArrayList<>();
    public void addBookstore(Bookstore bookstore) {
        bookstores.add(bookstore);
        System.out.println("Adding bookstore "+bookstore.getName());
    }

    protected void setup() {
        addBehaviour(new HandleBookstoresRequestBehaviour(this));
        System.out.println("DirectorFacilitator-agent "+getAID().getName()+" is ready.");
    }

    protected void takeDown() {
        System.out.println("DirectorFacilitator-agent "+getAID().getName()+" terminating.");
    }


    private class HandleBookstoresRequestBehaviour extends Behaviour {
        private boolean done = false;

        public HandleBookstoresRequestBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String areaCode = msg.getContent();
                List<AID> sellerAgents = bookstores.stream()
                        .filter(b -> b.getAreaCode().equals(areaCode))
                        .map(bookstore -> bookstore.getSellerAgent().getAID())
                        .collect(Collectors.toList());

                ACLMessage reply = msg.createReply();
                if (sellerAgents.isEmpty()) {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("No bookstores found in the specified area.");
                } else {
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(sellerAgents.toString());
                }
                myAgent.send(reply);

                done = true;
            } else {
                block();
            }
        }

        public boolean done() {
            return done;
        }
    }
    private class DiscoverSellerAgentsBehaviour extends Behaviour {
        private boolean done = false;

        public DiscoverSellerAgentsBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            // Discover SellerAgents
            // For now, we just print out a message
            System.out.println("Discovering SellerAgents");
        }

        @Override
        public boolean done() {
            return done;
        }
    }
    private class ReturnSellerAgentsBehaviour extends Behaviour {
        private boolean done = false;
        private String areaCode;


        public ReturnSellerAgentsBehaviour(Agent a) {
            super(a);
        }
        public void setAreaCode(String areaCode){
            this.areaCode = areaCode;
        }

        public void action() {
            // Return SellerAgents
            // For now, we just print out a message
            System.out.println("Returning SellerAgents");
        }


        @Override
        public boolean done() {
            return done;
        }
    }

}
