import java.net.InetAddress;
import java.net.Socket;

public class WebPing {
    public static void main(String[] args) {
        try {
            InetAddress addr;
            Socket sock = new Socket("localhost", 80);
            addr = sock.getInetAddress();
            System.out.println("Connected to " + addr);
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}