/**
 * The Simulator class implement a main method that asks for user inputs and
 * output the process that indicates how packets are being sent through the network.
 *
 * @author Zhen Wei Liao
 **/

import java.util.Scanner;
import java.util.LinkedList;

public class Simulator{
    private final Router dispatcher = new Router();
    private final LinkedList<Router> routers = new LinkedList<>();
    private int totalServiceTime = 0, totalPacketsArrived = 0, packetsDropped = 0, numIntRouters,
            minPacketSize, maxPacketSize, bandWidth, duration;
    private double arrivalProb;

    private static Scanner input;
    private static boolean run;

    //maximum packets that can arrive at dispatcher in one simulation unit
    public static int MAX_PACKETS = 3;

    /**
     * Constructor creates a Simulator Object that takes the default value of the member variables.
     * */
    public Simulator(){
    }

    /**
     * Constructor creates a Simulator object with specified values for the member
     * variables
     *
     * @param numIntRouters
     * 		Number of intermediate routers.
     *
     * @param arrivalProb
     * 		Probability in which a packet will arrive at dispatcher.
     *
     * @param minPacketSize
     * 		Minimum packet size.
     *
     * @param maxPacketSize
     * 		Maximum packet size.
     *
     * @param bandwidth
     * 		Destination can receive a maximum of <code>bandwidth</code> Packets at a given simulation unit.
     *
     * @param duration
     * 		Number of simulation units.
     * */
    public Simulator(int numIntRouters, double arrivalProb, int minPacketSize,
                     int maxPacketSize, int bandwidth, int duration) {
        this.numIntRouters = numIntRouters;
        this.arrivalProb = arrivalProb;
        this.minPacketSize = minPacketSize;
        this.maxPacketSize = maxPacketSize;
        this.bandWidth = bandwidth;
        this.duration = duration;
    }

    /**
     * Returns the sum of the total time each packet is in the network
     *
     * @return
     * 		Sum of the total time each packet is in the network.
     * */
    public int getTotalServiceTime() {
        return totalServiceTime;
    }

    /**
     * Returns the total number of packets that has been successfully forwarded to the
     * destination.
     *
     *  @return
     *  	Total number of packets that has been successfully forwarded to the destination.
     * */
    public int getTotalPacketsArrived() {
        return totalPacketsArrived;
    }

    /**
     * Returns the number of packets that have been dropped due to a congested network.
     *
     * @return
     * 		Number of packets that have been dropped due to a congested network.
     * */
    public int getPacketsDropped() {
        return packetsDropped;
    }

    /**
     * Returns a random number in the range [minVal, maxVal]
     *
     * @param minVal
     * 		the smallest possible integer that can be generated
     *
     * @param maxVal
     * 		the largest possible integer that can be generated
     *
     * @return
     * 		random integer between minVal and maxVal, inclusively
     * */
    public static int randInt(int minVal, int maxVal) {
        return (int)(Math.random() * (maxVal - minVal + 1) + minVal);
    }

    /**
     * Generates Packets based on arrival probability and records all packets that arrive at dispatcher.
     *
     * @param timeArrived
     *      Time in which the packets arrived at the dispatcher.
     */
    private void packetArriving(int timeArrived){
        for(int i = 1; i <= MAX_PACKETS; i++){
            if(Math.random() < arrivalProb){
                int size = randInt(minPacketSize, maxPacketSize), id = Packet.getPacketCount() + 1;
                //Time it takes the packet to reach the destination = packetSize / 100
                Packet packet = new Packet(size, timeArrived, size/100);
                Packet.setPacketCount(id);
                packet.setId(id);
                dispatcher.enqueue(packet);
                System.out.println("Packet " + id + " arrives at dispatcher with size " + size + ".");
            }
        }
        if(dispatcher.isEmpty())
            System.out.println("No packets arrived.");
    }

    /**
     * Dispatcher sends all arrived packets to an available Router. If all Routers are full, then the
     * remaining packets will be dropped.
     */
    private void sendToRouters() throws EmptyBufferException {
        while(!dispatcher.isEmpty()) {
            Packet packetSent, packetDropped;
            try{
                int routerIndex = Router.sendPacketTo(routers);
                packetSent = dispatcher.dequeue();
                routers.get(routerIndex).enqueue(packetSent);
                System.out.println("Packet "+ packetSent.getId()+ " sent to Router "+
                        (routerIndex +1) +".");
            } catch(FullBufferException e) {
                packetDropped = dispatcher.dequeue();
                packetsDropped += 1;
                System.out.println("Network is congested. Packet "+ packetDropped.getId() +
                        " is dropped.");
            }
        }
    }

    /**
     * Records any packet that is ready to be sent to the destination.
     *
     * @param recordQueue
     *      A temporary Router that records all packets that are ready to be sent.
     */
    private void recordPackets(Router recordQueue){
        for(Router router : routers)
            if(!(router.isEmpty()) && router.peek().getTimeToDest() == 0)
                recordQueue.enqueue(router.peek());
    }

    /**
     * Sent all packets that are ready to destination.
     * If the number of packets being sent has reached the size of bandWidth,
     * any remaining packets will stay in the router and wait for the next simulation unit.
     *
     * @param packetsToBeSent
     *      A temporary Router that keeps tracks of which packets should be sent to destination.
     *
     * @param simulationUnit
     *      Current simulation unit.
     */
    private void sendPacketToDestination(Router packetsToBeSent, int simulationUnit) throws EmptyBufferException {
        int count = 0;
        while(!(packetsToBeSent.isEmpty()) && count < bandWidth){
            Packet packetSent = packetsToBeSent.dequeue(), arrivedPacket;
            for(Router router : routers){
                if(!(router.isEmpty()) && router.peek().equals(packetSent)){
                    arrivedPacket = router.dequeue();
                    totalPacketsArrived++;
                    int serviceTime = simulationUnit - arrivedPacket.getTimeArrived();
                    totalServiceTime += serviceTime;
                    System.out.println("Packet " + arrivedPacket.getId() +
                            " has successfully reached its destination: +" + serviceTime);
                    count++;
                }
            }
        }
    }

    /**
     * Display information of the intermediate Routers and decrement the time to destination of all packets
     * that are in the front.
     */
    private void packetsInRouters() {
        for(int i = 1; i <= routers.size(); i++) {
            System.out.println("R" + i + ": " + routers.get(i - 1));
        }

        for(Router router : routers) {
            if(router != null && !(router.isEmpty()) && router.peek().getTimeToDest() != 0) {
                assert router.peek() != null;
                router.peek().setTimeToDest(router.get(0).getTimeToDest() - 1);
            }
        }
    }

    /**
     * Run and output the process that indicates how the packets are being sent through the network.
     *
     * @return
     * 		Average time each packet is in the network.
     * */
    public double simulate() throws EmptyBufferException {
        for(int i = 0; i < numIntRouters; i++)
            routers.add(new Router());

        for(int i = 1; i <= duration; i++){
            System.out.println("\nTime: " + i);
            packetArriving(i);
            sendToRouters();
            /*
            Creates a temp Router or queue that records all packets that are ready
            to be sent to destination. This is created for the purpose of fairness.
             */
            Router packetsToBeSent = new Router();
            recordPackets(packetsToBeSent);
            sendPacketToDestination(packetsToBeSent, i);
            packetsInRouters();
        }
        Packet.setPacketCount(0);
        if(totalPacketsArrived != 0)
            return ((double) totalServiceTime) / totalPacketsArrived;

        System.out.println("\nNo packets have arrived at destination during the simulation.");
        return 0;
    }

    /**
     * Checks if the user is entering anything but an integer and if the entered integer is positive.
     *
     * @param prompt
     * 		Prompt for user input.
     *
     * @throws IllegalArgumentException
     * 		when input is a negative integer.
     *
     * @return
     * 		User input as an integer
     * */
    public static int inputInt(String prompt) throws IllegalArgumentException {
        System.out.print(prompt);
        int intInput = Integer.parseInt(input.nextLine());
        if(intInput < 0)
            throw new IllegalArgumentException("Please enter a positive integer.\n");

        return intInput;
    }

    /**
     * Checks if the user enters a number or not and if the entered number is in the range [0, 1]
     *
     * @return User input as a double.
     * @throws IllegalArgumentException when user input is out of range.
     */
    private static double inputProbability() throws IllegalArgumentException {
        System.out.print("\nEnter the arrival probability of a packet: ");
        double prob = Double.parseDouble(input.nextLine());
        if(prob < 0 || prob > 1)
            throw new IllegalArgumentException("Invalid input. Please only enter a " +
                    "probability in range of [0,1].\n");

        return prob;
    }

    /**
     * Returns a Simulator based on user input.
     *
     * @return
     *      A Simulator based on user input.
     */
    private static Simulator generateSimulator(){
        int numIntRouters, minPacketSize, maxPacketSize, bandwidth, duration;
        double probability;
        numIntRouters = inputInt("Enter the number of intermediate routers: ");
        probability = inputProbability();
        Router.setMaxBufferSize(inputInt("\nEnter the maximum buffer size of a router: "));
        MAX_PACKETS = inputInt("\nEnter the maximum number of packets that can arrive at dispatcher: ");
        minPacketSize = inputInt("\nEnter the minimum size of a packet (Recommend size of 100): ");
        maxPacketSize = inputInt("\nEnter the maximum size of a packet (Recommend size > 100): ");
        bandwidth = inputInt("\nEnter the bandwidth size: ");
        duration = inputInt("\nEnter the simulation duration: ");

        return new Simulator(numIntRouters, probability, minPacketSize, maxPacketSize, bandwidth, duration);
    }

    /**
     * Activates the Simulator and display appropriate statistics.
     *
     * @param simulator
     *      Simulator being activate.
     *
     */
    private static void simulate(Simulator simulator) throws EmptyBufferException {
        double average = simulator.simulate();
        System.out.println("\n\nSimulation ending...");
        System.out.println("Total service time: " + simulator.getTotalServiceTime());
        System.out.println("Total packets served: " + simulator.getTotalPacketsArrived());
        System.out.println("Average service time per packet: " + average);
        System.out.println("Total packets dropped: " + simulator.getPacketsDropped());
    }

    /**
     * Ask the user if he/she want to create another simulation. If not, terminate the program.
     */
    private static void exit(){
        System.out.print("\nDo you want to try another simulation? [y|n]: ");
        String cont = input.nextLine().toLowerCase().trim();

        while(!(cont.equals("y") || cont.equals("n"))) {
            System.out.print("\nPlease enter only y or n: ");
            cont = input.nextLine().toLowerCase().trim();
        }

        if(cont.equals("n")){
            run = false;
            System.out.println("\nProgram terminating successfully...");
            input.close();
        }
    }

    /**
     * The main method will ask for user input and create a Simulator object based on the given information.
     * Later it calls the simulate() method on the Simulator object
     * and prints out the total service time, total packets served, average service time
     * per packet, and total packets that are dropped during the simulation.
     * */
    public static void main(String[] args) {
        input = new Scanner(System.in);
        run = true;
        Simulator simulator;
        while(run){
            System.out.println("Starting simulator...\n");
            try{
                simulator = generateSimulator();
                simulate(simulator);
                exit();
                System.out.println();
            } catch(NumberFormatException e){
                System.out.println("Error: Please enter a number.\n");
            } catch(EmptyBufferException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }


}

