package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Agents.ClientAgent;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();


        try (FileReader reader = new FileReader("./agents/bookstores.json")) {
            Type bookstoreListType = new TypeToken<List<Bookstore>>() {
            }.getType();
            List<Bookstore> bookstores = gson.fromJson(reader, bookstoreListType);
        } catch (IOException e) {
            e.printStackTrace();
        }

//            for (Bookstore bookstore : bookstores) {
////                ManagerAgent managerAgent = new ManagerAgent(bookstore);
////                bookstore.setManagerAgent(managerAgent);
//
//
//            }


            try ( FileReader reader = new FileReader("./agents/clients.json")){
                Type clientListType = new TypeToken<List<ClientAgent>>() {
                }.getType();
                List<ClientAgent> clients = gson.fromJson(reader, clientListType);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}