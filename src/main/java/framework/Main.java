package framework;

import framework.container.IoCContainer;
import framework.test_beans.Car;

public class Main {
    public static void main(String[] args) throws Exception {
        var container = new IoCContainer("src/main/resources/config.xml");

        var car0 = (Car) container.getBean("id1");
        var car1 = (Car) container.getBean("id1");
        var car2 = (Car) container.getBean("id2");
        var car3 = (Car) container.getBean("id2");
        var allCars = container.getAllBeansByType(Car.class);

        System.out.println("car brand: " + car1.getCarBrand());
        System.out.println("num of wheels: " + car1.getNumOfWheels());
        System.out.println("max speed: " + car1.getMaxSpeed() + "\n");

        System.out.println("car2 brand: " + car2.getCarBrand() + "\n");
        System.out.println("car3 brand: " + car3.getCarBrand() + "\n");

        System.out.println(car0 == car1);
        System.out.println(car2 != car3);
    }
}
