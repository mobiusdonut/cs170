import java.util.*;

public class CarManager {
   private CarNode front;
   private int size;

   // pre: input list has at least one name in it (otherwise throw new IllegalArgumentException),
   //      no duplicate names, no empty strings as names
   // post: creates the car line, preserves the order of cars from the input list
   CarManager(List<String> names){
      if (names.size() == 0){
         throw new IllegalArgumentException("list is empty");
      }
      for (int i = 0; i < names.size(); i++) {
          addCar(names.get(i));
      }
   }

   // post: prints the current car line seperated by commas
   public void printCars(){
      CarNode current = front;
      while (current != null) {
        System.out.print(current.name + ", ");
        current = current.next;
      }
      System.out.println("");
   }

   // post: adds the inputted car to the end of the car line
   public void addCar(String car){
     CarNode carToAdd = new CarNode(car);
     if (size == 0) {
       front = carToAdd;
     }
     else {
       CarNode current = front;
       while (current.next != null) {
         current = current.next;
       }
       current.next = carToAdd;
    }
    size += 1;
   }

   // pre: must have at least one car in the car line otherwise new IllegalStateException is thrown
   // post: removes the car at the end of the car line
   public void removeCar(){
     if (size == 0) {
       throw new IllegalArgumentException("list is empty");
     }
     else {
       CarNode current = front;
       while (current.next.next != null) {
         current = current.next;
       }
       current.next = null;
       size -= 1;
    }
   }
}
