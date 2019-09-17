import java.util.*;

public class CarMain{
   public static void main(String[] args){
      List<String> cars = new ArrayList<String>();
      cars.add("Audi");
      cars.add("Toyota");
      cars.add("Infiniti");
      cars.add("Honda");
      cars.add("BMW");
      CarManager garage = new CarManager(cars);
      garage.printCars();
      garage.removeCar();
      garage.printCars();
      garage.addCar("Mercedes");
      garage.printCars();
   }
}

/*
Testing program first sets up a list and passes as a parameter to constructor of CarManager.
printCars is called to check that the CarManager contains the cars in the correct order and to test printCars.
removeCar called to test removeCar.
addCar called to test addCar.
*/
