package sirs.server;

import javax.crypto.SecretKey;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        /*
        // Json example

        AddUserRequest addUserRequest = new AddUserRequest("h@g.com", "96", "qwerty");
        Gson gson = new Gson();
        String json = gson.toJson(addUserRequest);
        System.out.println(json);
        AddUserRequest addUserRequest2 = gson.fromJson(json, AddUserRequest.class);
        System.out.println(addUserRequest2);
        */

        // Server example

        new Server(9000);

        Cryptography cryptography = new Cryptography();

        SecretKey key = cryptography.generateKey();

        String encrypted = cryptography.encryptAES("I'm awesome!!!", key);
        String decrypted = cryptography.decryptAES(encrypted, key);

        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);
    }

}
