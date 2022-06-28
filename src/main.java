import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class main {
    public static int clientNumber = 0;
    public static List<Hairdressers> hairdressersList = new ArrayList<Hairdressers>();
    public static List<Client> clientList = new ArrayList<Client>();
    public static List<Seat> seatList = new ArrayList<Seat>();
    public static Semaphore sCreate, sSeat, sHairdresser;

    public static void main(String[] args) throws InterruptedException {
        sSeat = new Semaphore(15);
        sHairdresser = new Semaphore(4);
        sCreate = new Semaphore(1);

        createAll();
        for (int i = 0; i<100; i++){
            creatClient();

        }
    }

    public static void createAll(){
        for (int i=1; i<5; i++){
            Hairdressers hairdressers = new Hairdressers(i);
            hairdressersList.add(hairdressers);
        }

        for (int i=1; i<16; i++){
            Seat seat = new Seat(i, false);
            seatList.add(seat);
        }
    }

    public static void creatClient(){

        try {
            sCreate.acquire();
            sSeat.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientNumber++;
        Client client = new Client(clientNumber);
        clientList.add(client);
        System.out.println("Le client "+clientNumber+" vient d'arriver devant le sallon de coiffure !");
        checkSeat.start();
    }

    public static Thread checkSeat = new Thread(){
        public void run(){
            for (Seat seat:seatList) {
                if (seat.occupied == false){
                    seat.occupied = true;
                    for (Client client: clientList) {
                        if (client.id == clientNumber){
                            client.isIn = true;
                            System.out.println("Le client "+clientNumber+" est installé sur le siège numéro "+seat.id);
                        }
                    }
                    break;
                }
            }
            sCreate.release();
        }
    };

    public static Thread getStyled = new Thread() {
        public void run() {

        }
    };

}
