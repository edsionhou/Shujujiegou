package P.优先级队列;

import java.util.Comparator;

public class Main
{
    public static void main(String[] args) {

        Person p1 = new Person("1",1);
        Person p2 = new Person("2",2);
        Person p3 = new Person("3",3);
        Person p4 = new Person("4",4);
        Person p5 = new Person("5",5);
        Person p6 = new Person("6",6);

        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.boneBreak - o2.boneBreak;
            }
        };
        PriorityQueue<Person> queue = new PriorityQueue<>(comparator);
        queue.enQueue(p1);
        queue.enQueue(p2);
        queue.enQueue(p3);
        queue.enQueue(p4);
        queue.enQueue(p5);
        queue.enQueue(p6);

       // queue.print();
        while(!queue.isEmpty()){
            //queue.print();
            System.out.println(queue.deQueue());
        }
    }
}
