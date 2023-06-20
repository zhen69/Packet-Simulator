/**
 * The Router class represent a router in the network, which is ultimately a queue.
 * This class inherits all properties and behaviors from the LinkedList class and
 * contains all necessary methods to modify a queue.
 *
 * @author Zhen Wei Liao
 **/
import java.util.LinkedList;

public class Router extends LinkedList<Packet> {

    //maximum size of the Router
    private static int maxBufferSize;

    public Router() {
        super();
    }

    /**
     * Modifier. Modifies the maximum size of the Router.
     *
     * @param maxBufferSize
     * 		Maximum size of the Router.
     * */
    public static void setMaxBufferSize(int maxBufferSize) {
        Router.maxBufferSize = maxBufferSize;
    }

    /**
     * Adds a packet to the end of the Router queue.
     *
     * @param p
     * 		Packet being added to the queue.
     * */
    public void enqueue(Packet p) {
        this.add(p);
    }

    /**
     * Removes a packet from the front of the Router queue.
     *
     * @throws EmptyBufferException
     * 		when router is empty.
     *
     * @return
     * 		Packet removed from the queue.
     * */
    public Packet dequeue() throws EmptyBufferException{
        if(isEmpty())
            throw new EmptyBufferException("Invalid. The router is empty");

        return remove();
    }

    /**
     * Determines if the queue is empty.
     *
     * @return
     * 		true if queue is empty, false otherwise
     * */
    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    /**
     * Returns the string representation of the Router.
     *
     * @return
     * 		String representation of Router.
     * */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        if(!isEmpty()) {
            for(int i = 0; i < this.size() - 1; i++)
                str.append(this.get(i).toString()).append(", ");
            str.append(this.get(this.size() - 1));
        }
        str.append("}");
        return str.toString();
    }

    /**
     * Determines which Router in the list of intermediate Routers is available to receive a packet.
     *
     * @param routers
     * 		List of intermediate Routers.
     *
     * @throws FullBufferException
     * 		when all Routers in the given list reached maxBufferSize.
     *
     * @return
     * 		Index of the available router.
     * */
    public static int sendPacketTo(LinkedList<Router> routers) throws FullBufferException {
        int index = 0;
        boolean isFull = true, execute = true;
        for(int i = 0; i < routers.size(); i++) {
            if(routers.get(i).size() < routers.get(index).size())
                index = i;

            // execute is used to determine if the following if-statement should run or not.
            if(execute && routers.get(i).size() < maxBufferSize) {
                isFull = false;
                execute = false;
            }
        }

        if(isFull)
            throw new FullBufferException();

        return index;
    }
}