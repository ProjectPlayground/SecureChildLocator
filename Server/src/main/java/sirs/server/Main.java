package sirs.server;

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

        /*
        Cryptography cryptography = new Cryptography();
        String hash = cryptography.hash("hello");
        System.out.println(hash);
        System.out.println(cryptography.hashIsValid("hello", hash));


        /*

        String encrypted = cryptography.encryptRSA("i'm awesome!");
        String decrypted = cryptography.decryptRSA(encrypted);

        System.out.println(encrypted);
        System.out.println(decrypted);

        SecretKey secretKey = cryptography.generateKey();
        encrypted = cryptography.encryptAES("i'm awesome!", secretKey);
        decrypted = cryptography.decryptAES(encrypted, secretKey);

        System.out.println(encrypted);
        System.out.println(decrypted);

       DateTime dateTime = new DateTime();
       System.out.println(dateTime);
*/

    }

}
