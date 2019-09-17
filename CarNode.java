// The CarNode is used to store the name of the car and the link to the next car in line

public class CarNode {
    public String name;   // this car's name
    public CarNode next;  // next node in the list

    // constructs a node with the given name and a null link
    public CarNode(String name) {
        this(name, null);
    }

    // constructs a node with the given name and link
    public CarNode(String name, CarNode next) {
        this.name = name;
        this.next = next;
    }
}
