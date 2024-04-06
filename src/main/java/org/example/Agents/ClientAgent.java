package org.example.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

public class ClientAgent extends Agent {
    private String areaCode;

    protected void setup() {
        System.out.println("Client-agent "+getAID().getName()+" is ready.");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            areaCode = (String) args[0];
            System.out.println("Target area code is "+areaCode);

            SequentialBehaviour setupBehaviour = new SequentialBehaviour();
            setupBehaviour.addSubBehaviour(new FindBookstoresBehaviour(this, areaCode));
            setupBehaviour.addSubBehaviour(new RequestBookBehaviour(this));
            setupBehaviour.addSubBehaviour(new SelectBookstoreBehaviour(this));
            addBehaviour(setupBehaviour);
        }
        else {
            System.out.println("No target area code specified");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Client-agent "+getAID().getName()+" terminating.");
    }
    private class FindBookstoresBehaviour extends Behaviour {
        private String areaCode;
        private List<AID> sellerAgents;

        public FindBookstoresBehaviour(Agent a, String areaCode) {
            super(a);
            this.areaCode = areaCode;
            this.sellerAgents = new ArrayList<>();
        }

        public void action() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(areaCode);
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                for (int i = 0; i < result.length; ++i) {
                    sellerAgents.add(result[i].getName());
                }
            }
            catch (FIPAException fe) {
                fe.printStackTrace();
            }
        }

        public boolean done() {
            return !sellerAgents.isEmpty();
        }

        public List<AID> getSellerAgents() {
            return sellerAgents;
        }
    }
    private class RequestBookBehaviour extends Behaviour {
        private AID sellerAgent;

        public RequestBookBehaviour(Agent a, AID sellerAgent) {
            super(a);
            this.sellerAgent = sellerAgent;
        }

        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(sellerAgent);
            msg.setContent("Requesting book");
            myAgent.send(msg);
        }

        public boolean done() {
            // This behaviour is done when it has sent the request
            return true;
        }
    }
    public class SelectBookstoreBehaviour extends Behaviour {
        private List<AID> sellerAgents;

        public SelectBookstoreBehaviour(Agent a, List<AID> sellerAgents) {
            super(a);
            this.sellerAgents = sellerAgents;
        }

        public void action() {
            // For now, we just select the first sellerAgent from the list
            // TODO: Add logic to select a bookstore based on your criteria
            if (!sellerAgents.isEmpty()) {
                AID selectedSellerAgent = sellerAgents.get(0);
                System.out.println("Selected bookstore: " + selectedSellerAgent.getName());
            }
        }

        public boolean done() {
            // This behaviour is done when it has selected a bookstore
            return !sellerAgents.isEmpty();
        }
    }
}