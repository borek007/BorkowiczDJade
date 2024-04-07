package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Agents.ClientAgent;

import java.io.File;
import java.io.IOException;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Bookstore> bookstores = mapper.readValue(new File("./agents/bookstores.json"), new TypeReference<List<Bookstore>>(){});
            // Use the bookstores list...

            List<ClientAgent> clients = mapper.readValue(new File("./agents/clients.json"), new TypeReference<List<ClientAgent>>(){});
//            // Use the clients list...
            System.out.println("dupa");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}