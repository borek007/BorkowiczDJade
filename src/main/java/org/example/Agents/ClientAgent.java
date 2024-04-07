package org.example.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.Utills.Offer;
import org.example.Utils.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientAgent extends Agent  {
    private String areaCode;
    private String book;

    //datastore did not work so i put this data into object as a workaround

    public List<AID> sellerAgents;

    public List<Offer> responses;
    public List<AID> getSellerAgents() {
        return sellerAgents;
    }

    public void setSellerAgents(List<AID> sellerAgents) {
        this.sellerAgents = sellerAgents;
    }

    public List<Offer> getResponses() {
        return responses;
    }

    public void setResponses(List<Offer> responses) {
        this.responses = responses;
    }

    public ClientAgent(String book, String areaCode) {
        this.book = book;
        this.areaCode = areaCode;

    }
    public void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        SequentialBehaviour buyBookBehaviour = new SequentialBehaviour();
        buyBookBehaviour.addSubBehaviour(new FindBookstoresBehaviour(this));
        buyBookBehaviour.addSubBehaviour(new RequestBookBehaviour(this));
        buyBookBehaviour.addSubBehaviour(new FinishTransactionBehaviour(this));
        addBehaviour(buyBookBehaviour);
        System.out.println("Client-agent " + getAID() + " is ready.");

//        start1();
    }


    protected void takeDown() {
        System.out.println("Client-agent " + getAID()+ " terminating.");
    }

    private class FindBookstoresBehaviour extends Behaviour {



        public FindBookstoresBehaviour(ClientAgent a) {
            super(a);


        }

        public void action() {

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(areaCode);
            msg.addReceiver(DirectorFacilitatorAgent.getInstance().getAID());
            myAgent.send(msg);
            ACLMessage reply = myAgent.blockingReceive();
            if (reply.getPerformative() == ACLMessage.REFUSE) {
                System.out.println("NO BOOKSTORES FOUND");
                myAgent.doDelete();
                return;
            }


            setSellerAgents(utils.convertToAIDList(reply.getContent()));


            System.out.println(myAgent.getName()+": Found " + getSellerAgents().size() + " bookstores in area: " + areaCode);

        }

        public boolean done() {
            return true;
        }

    }


    private class RequestBookBehaviour extends Behaviour {
        ACLMessage msg;

        Boolean done = false;

        public RequestBookBehaviour(Agent a) {
            super(a);
            setup();

        }

        private void setup() {

            msg = new ACLMessage(ACLMessage.CFP);
            msg.setContent(book);
        }

        public void action() {
            if (getSellerAgents() == null || msg == null)
                return;


            sendRequest();
            getResponsesFromSellers();
        }

        private void sendRequest() {
            for (AID seller : getSellerAgents()) {
                msg.addReceiver(seller);
            }
            myAgent.send(msg);
        }

        private void getResponsesFromSellers() {
            List<Offer> responses = new ArrayList<>();

//            block(5000);

            // Receive the replies
            for (int i = 0; i < getSellerAgents().size(); i++) {
                ACLMessage reply = myAgent.blockingReceive();
                if (reply != null&&reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    responses.add(new Offer(reply.getSender().getName(), book, Double.parseDouble(reply.getContent())));
                }
            }
            setResponses(responses);

            done = !responses.isEmpty();

        }






        public boolean done() {
            // This behaviour is done when it has sent the request
            return done;
        }
    }


    private class FinishTransactionBehaviour extends Behaviour {
        private boolean done = false;

        public FinishTransactionBehaviour(Agent a) {
            super(a);
        }

        public void action() {

            if (getResponses() == null) {
                block();
                return;
            }

            //Our Client is a poor student so he will choose the cheapest offer
            //we filter out the offers with price -1, it means that the seller doesn't have the book
            Offer bestOffer = getResponses().stream().filter(offer->offer.getPrice() != -1).min(Comparator.comparingDouble(Offer::getPrice)).get();

            System.out.println("Buying book: " + book + " for price: " + bestOffer.getPrice()+ " from: " + bestOffer.getSeller());
            //I'm too tired to finish this, I'm sorry

//            ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
//            msg.setContent("Buying book: " + book + "for price: " + bestOffer.getPrice());
//            msg.addReceiver(new AID(bestOffer.getSeller(), AID.ISLOCALNAME));
//            myAgent.send(msg);
//            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
//            ACLMessage reply = myAgent.blockingReceive(mt);
//            System.out.println("Received a reply from " + reply.getSender()+ " with content: " + reply.getContent());
            done = true;


        }

        @Override
        public boolean done() {
            return done;
        }
    }


    //the below is  just fix for jackson to work :/
    public ClientAgent() {
        this.book = "";
        this.areaCode = "";
    }
    // Getter for areaCode
    public String getAreaCode() {
        return areaCode;
    }

    // Setter for areaCode
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getBook() {
        return book;
    }

    // Setter for book
    public void setBook(String book) {
        this.book = book;
    }

}