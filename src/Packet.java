/**
 * The Packet class contains basic information about a specific packet, such as its ID, size,
 * the time it arrives at dispatcher, and the time it takes to reach destination.
 *
 * @author Zhen Wei Liao
 **/
public class Packet {

    /*
    Keep tracks of how many packets are created.
    Value use to assign an ID to a newly created packet
     */
    private static int packetCount = 0;
    int id, packetSize, timeArrived, timeToDest;

    /**
     * Constructor creates a Packet object that takes the default value of the member variables.
     * */
    public Packet() {

    }

    /**
     * Constructor creates a Packet object with specified attributes.
     *
     * @param packetSize
     * 		The size of the packet.
     *
     * @param timeArrived
     * 		The time when the packet arrives at dispatcher.
     *
     * @param timeToDest
     * 		The time it takes the packet to reach destination.
     * */
    public Packet(int packetSize, int timeArrived, int timeToDest) {
        this.packetSize = packetSize;
        this.timeArrived = timeArrived;
        this.timeToDest = timeToDest;
    }

    /**
     * Accessor. Returns the value that is used to assign an ID to a Packet.
     *
     * @return
     * 		Value used to assign Packet ID.
     * */
    public static int getPacketCount() {
        return packetCount;
    }

    /**
     * Accessor. Returns the ID of the Packet.
     *
     * @return
     * 		ID of the current Packet.
     * */
    public int getId() {
        return id;
    }

    /**
     * Accessor. Returns the size of the Packet.
     *
     * @return
     * 		Size of the current Packet.
     * */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * Accessor. Returns the time when the Packet arrives at dispatcher.
     *
     * @return
     * 		Time when the Packet arrives at dispatcher.
     * */
    public int getTimeArrived() {
        return timeArrived;
    }

    /**
     * Accessor. Returns the time it takes the Packet to reach destination.
     *
     * @return
     * 		Time it takes the Packet to reach destination.
     * */
    public int getTimeToDest() {
        return timeToDest;
    }

    /**
     * Modifier. Modifies the value that is used to assign an ID to a Packet.
     *
     * @param packetCount
     * 		Value used to assign Packet ID.
     * */
    public static void setPacketCount(int packetCount) {
        Packet.packetCount = packetCount;
    }

    /**
     * Modifier. Modifies the ID of the Packet.
     *
     * @param id
     * 		ID of Packet.
     * */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Modifier. Modifies the size of the Packet.
     *
     * @param packetSize
     * 		The size of the packet.
     * */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    /**
     * This setter modifies the time when the Packet arrives at dispatcher.
     *
     * @param timeArrived
     * 		Time when the Packet arrives at dispatcher.
     * */
    public void setTimeArrive(int timeArrived) {
        this.timeArrived = timeArrived;
    }

    /**
     * Modifier. Modifies the time it takes the Packet to reach destination.
     *
     * @param timeToDest
     * 		Time it takes the Packet to reach destination.
     * */
    public void setTimeToDest(int timeToDest) {
        this.timeToDest = timeToDest;
    }

    /**
     * This method checks if two Packet contains the same information.
     *
     * @param obj
     * 		The object that is being used to compare with the current Packet object.
     *
     * @return
     * 		True if obj is a Packet and contains the same information as the current Packet,
     * 		false otherwise.
     * */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Packet)) return false;

        Packet p = (Packet) obj;

        boolean compareId = (this.id == p.getId()),
                compareTimeArrive = (this.timeArrived == p.timeArrived),
                compareTimeToDest = (this.timeToDest == p.timeToDest),
                compareSize = (this.packetSize == p.packetSize);

        return compareId && compareTimeArrive && compareTimeToDest && compareSize;
    }

    /**
     * Returns a string representation of the Packet.
     *
     * @return
     * 		String representation of Packet.
     * */
    @Override
    public String toString() {
        return "[" + id + ", " + timeArrived + ", " + timeToDest + "]";
    }
}

