package org.example.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientAgent extends Agent implements Serializable {
    private String areaCode;
    private String book;

    public ClientAgent(String book, String areaCode) {
        this.book = book;
        this.areaCode = areaCode;
    }


    protected void setup() {
        System.out.println("Client-agent " + getAID().getName() + " is ready.");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            areaCode = (String) args[0];
            System.out.println("Target area code is " + areaCode);

            SequentialBehaviour buyBookBehaviour = new SequentialBehaviour();
            buyBookBehaviour.addSubBehaviour(new FindBookstoresBehaviour(this));
            buyBookBehaviour.addSubBehaviour(new RequestBookBehaviour(this));
            buyBookBehaviour.addSubBehaviour(new FinishTransactionBehaviour(this));
            addBehaviour(buyBookBehaviour);
        } else {
            System.out.println("No target area code specified");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Client-agent " + getAID().getName() + " terminating.");
    }

    private class FindBookstoresBehaviour extends Behaviour {

        private List<AID> sellerAgents;

        public FindBookstoresBehaviour(ClientAgent a) {
            super(a);

            this.sellerAgents = new ArrayList<>();
        }

        public void action() {

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(areaCode);
            msg.addReceiver(DirectorFacilitatorAgent.getInstance().getAID());
            myAgent.send(msg);
            ACLMessage reply = myAgent.blockingReceive();
            String content = reply.getContent();
            String[] AIDS = content.split(",");
            for (String aid : AIDS) {
                sellerAgents.add(new AID(aid, AID.ISLOCALNAME));
            }
            getDataStore().put("sellerAgents", sellerAgents);

        }

        public boolean done() {
            return !sellerAgents.isEmpty();
        }

    }


    private class RequestBookBehaviour extends Behaviour {
        ACLMessage msg;
        List<AID> sellerAgents;
        Boolean done = false;

        public RequestBookBehaviour(Agent a) {
            super(a);

        }

        private void setup() {
            sellerAgents = (List<AID>) getDataStore().get("sellerAgents");
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Requesting book: " + book);
        }

        public void action() {
            if (sellerAgents == null || msg == null)
                setup();


            sendRequest();
            getRespones();
        }

        private void getRespones() {
            ArrayList<Offer> responses = new ArrayList<>();

            // Receive the replies
            for (Iterator it = msg.getAllReplyTo(); it.hasNext(); ) {
                // in case of problems with getAllReplyTo switch to MessageTemplate.MatchInReplyTo
                ACLMessage reply = (ACLMessage) it.next();
                if (reply != null) {
                    // Handle the reply from the SellerAgent
                    // For now, we just print out a message
                    System.out.println("Received a reply from " + reply.getSender().getName() + " with content: " + reply.getContent());
                    responses.add(new Offer(reply.getSender().getName(), book, Double.parseDouble(reply.getContent())));

                }


            }
            getDataStore().put("responses", responses);
            done = true;

        }

        private void sendRequest() {


            for (AID sellerAgent : sellerAgents) {
                msg.addReceiver(sellerAgent);
            }


            myAgent.send(msg);
        }


        public boolean done() {
            // This behaviour is done when it has sent the request
            return true;
        }
    }


    private class FinishTransactionBehaviour extends Behaviour {
        private boolean done = false;

        public FinishTransactionBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            List<Offer> responses = (ArrayList<Offer>) getDataStore().get("responses");
            if (responses == null) {
                done = false;
                return;
            }
            Offer bestOffer = responses.stream().min(Comparator.comparingDouble(Offer::getPrice)).get();
            System.out.println("Best offer: " + bestOffer.getSeller() + " with price: " + bestOffer.getPrice());

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Buying book: " + book + "for price: " + bestOffer.getPrice());
            msg.addReceiver(new AID(bestOffer.getSeller(), AID.ISLOCALNAME));
            myAgent.send(msg);
            ACLMessage reply = myAgent.blockingReceive(mt);
            System.out.println("Received a reply from " + reply.getSender().getName() + " with content: " + reply.getContent());
            done = true;


        }

        @Override
        public boolean done() {
            return done;
        }
    }

    public class Offer {
        private String seller;
        private String book;
        private double price;

        public Offer(String seller, String book, double price) {
            this.seller = seller;
            this.book = book;
            this.price = price;
        }

        public String getSeller() {
            return seller;
        }

        public String getBook() {
            return book;
        }

        public double getPrice() {
            return price;
        }
    }


}