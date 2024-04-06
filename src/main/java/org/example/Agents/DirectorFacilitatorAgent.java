package org.example.Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class DirectorFacilitatorAgent extends jade.core.Agent{
    protected void setup() {
        System.out.println("DirectorFacilitator-agent "+getAID().getName()+" is ready.");
    }

    protected void takeDown() {
        System.out.println("DirectorFacilitator-agent "+getAID().getName()+" terminating.");
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
