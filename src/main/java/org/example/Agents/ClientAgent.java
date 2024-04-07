package org.example.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;
import org.example.Utills.Offer;
import org.example.Utils.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientAgent extends Agent  {
    private String areaCode;
    private String book;

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
            if (reply.getPerformative() == ACLMessage.REFUSE) {
                System.out.println("NO BOOKSTORES FOUND");
                myAgent.doDelete();
                return;
            }


            sellerAgents = utils.convertToAIDList(reply.getContent()) ;
//            for (AID aid : AIDS) {
//                sellerAgents.add(new AID(aid, AID.ISLOCALNAME));
//            }
            getDataStore().put("sellerAgents", sellerAgents);
            System.out.println(myAgent.getName()+": Found " + sellerAgents.size() + " bookstores in area: " + areaCode);

        }

        public boolean done() {
            return true;
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
            msg = new ACLMessage(ACLMessage.CFP);
            msg.setContent(book);
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
                //TODO: in case of problems with getAllReplyTo switch to MessageTemplate.MatchInReplyTo
                ACLMessage reply = (ACLMessage) it.next();
                if (reply != null) {
                    // Handle the reply from the SellerAgent
                    // For now, we just print out a message
//                    System.out.println("Received a reply from " + reply.getSender() + " with content: " + reply.getContent());
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
                block();
                return;
            }

            //Our Client is a poor student so he will choose the cheapest offer
            //we filter out the offers with price -1, it means that the seller doesn't have the book
            Offer bestOffer = responses.stream().filter(offer->offer.getPrice() != -1).min(Comparator.comparingDouble(Offer::getPrice)).get();
            System.out.println("Best offer: " + bestOffer.getSeller() + " with price: " + bestOffer.getPrice());

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Buying book: " + book + "for price: " + bestOffer.getPrice());
            msg.addReceiver(new AID(bestOffer.getSeller(), AID.ISLOCALNAME));
            myAgent.send(msg);
            ACLMessage reply = myAgent.blockingReceive(mt);
            System.out.println("Received a reply from " + reply.getSender()+ " with content: " + reply.getContent());
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