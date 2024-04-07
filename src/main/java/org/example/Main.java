package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import org.example.Agents.ClientAgent;
import org.example.Agents.DirectorFacilitatorAgent;


import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        Profile myProfile = new ProfileImpl();
        myProfile.setParameter(Profile.MAIN_HOST, "localhost");
        myProfile.setParameter(Profile.MAIN_PORT, "1099");
        myProfile.setParameter(Profile.GUI, "true"); // Set to true to enable the JADE GUI

        // Create the main container
        Runtime myRuntime = Runtime.instance();
        jade.wrapper.AgentContainer container = myRuntime.createMainContainer(myProfile);
        DirectorFacilitatorAgent directorFacilitatorAgent = DirectorFacilitatorAgent.getInstance();
        List<Bookstore> bookstores = initializeBookstore();
        List<ClientAgent> clients = initializeClientAgents();


        for (Bookstore bookstore : bookstores) {
            directorFacilitatorAgent.addBookstore(bookstore);
        }
        insertAgent(container, directorFacilitatorAgent, "director-facilitator");


        for (Bookstore bookstore : bookstores) {
            insertAgent(container, bookstore.getManagerAgent(), "manager- "+bookstore.getName() );
            insertAgent(container, bookstore.getSellerAgent(),"seller- "+ bookstore.getName());

        }
        for (ClientAgent client : clients) {
            insertAgent(container, client,"Client"+ client.getBook() + "-" + client.getAreaCode());
        }


    }

    private static AgentContainer createContainer() {
        // Create a new Profile
        try{
            Profile myProfile = new ProfileImpl();
            myProfile.setParameter(Profile.MAIN_HOST, "localhost");
            myProfile.setParameter(Profile.MAIN_PORT, "1099");
            myProfile.setParameter(Profile.GUI, "false");

            // Create a new non-main container on the local host
            Runtime myRuntime = Runtime.instance();
            jade.wrapper.AgentContainer myContainer = myRuntime.createAgentContainer(myProfile);
            return myContainer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<Bookstore> initializeBookstore() {

        ObjectMapper mapper = new ObjectMapper();
        List<Bookstore> bookstores = null;

        try {
            bookstores = mapper.readValue(new File("./agents/bookstores.json"), new TypeReference<List<Bookstore>>() {
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookstores;

    }

    private static List<ClientAgent> initializeClientAgents() {

        ObjectMapper mapper = new ObjectMapper();

        List<ClientAgent> clients = null;

        try {


            clients = mapper.readValue(new File("./agents/clients.json"), new TypeReference<List<ClientAgent>>() {
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;

    }

    public static void insertAgent(AgentContainer container, jade.core.Agent agent, String name) {
        try {
            AgentController agentController = container.acceptNewAgent(name, agent);
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}