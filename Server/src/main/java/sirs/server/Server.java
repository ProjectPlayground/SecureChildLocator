package sirs.server;

import com.google.gson.Gson;
import sirs.communication.request.AddUserRequest;

public class Server
{
    public static void main(String[] args)
    {
        // Json example

        AddUserRequest addUserRequest = new AddUserRequest("h@g.com", "96", "qwerty");
        Gson gson = new Gson();
        String json = gson.toJson(addUserRequest);
        System.out.println(json);
        AddUserRequest addUserRequest2 = gson.fromJson(json, AddUserRequest.class);
        System.out.println(addUserRequest2);
    }
}
