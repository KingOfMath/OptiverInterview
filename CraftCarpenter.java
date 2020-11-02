import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.util.stream.Stream;

import static java.lang.Math.pow;

class Item {
    long key;

    public Item(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return key == item.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}

class Cabinet {
    int size;
    Queue<Item> itemList;
    HashSet<Long> itemKey;

    public Cabinet(int size) {
        this.size = size;
        itemList = new LinkedList<>();
        itemKey = new HashSet<>();
    }

    public HashSet<Long> getItemKey() {
        return itemKey;
    }

    // If the item queue is full, poll the head item
    // Return the head and store it to next cabinet
    public Item addItem(Item item) {
        itemList.add(item);
        itemKey.add(item.getKey());
        if (itemList.size() == this.size + 1) {
            Item head = itemList.remove();
            itemKey.remove(head.getKey());
            return head;
        }
        return null;
    }

    public void removeItem(Item item) {
        itemKey.remove(item.getKey());
        itemList.remove(item);
    }
}

class Controller {

    // Cabinet nums
    int N;
    // Item nums
    long K;
    // max
    int[] cabinetSize;
    // Store all the cabinets
    ArrayList<Cabinet> cabinetsArray;
    // Store every item which is removed from a specific cabinet and record the index of that cabinet
    HashMap<Long, Integer> outside;
    // Result type
    String itemType;
    // Current item
    Item curr = null;

    // Create N cabinets via its corresponding size
    public Controller(long k, int n, int[] cabinetSize) {
        K = k;
        N = n;
        this.cabinetSize = cabinetSize;
        this.cabinetsArray = new ArrayList<>();
        outside = new HashMap<>();
        for (int size : cabinetSize)
            this.cabinetsArray.add(new Cabinet(size));
    }

    // Check every item type
    public String addItemToController(Item item) {

        // Type: check if the item is already outside the cabinets
        if (outside.containsKey(item.getKey())) {
            outside.remove(item.getKey());
            for (int i = 0; i < N + 1; i++) {
                // If out of space, record the info
                if (i == N) {
                    outside.put(curr.getKey(), i);
                    break;
                } else if (i == 0)
                    curr = cabinetsArray.get(i).addItem(item);
                else
                    curr = cabinetsArray.get(i).addItem(curr);
                if (curr == null) break;
            }
            return "OUTSIDE";
        }

        // Type: check if the item is inside the cabinets
        // If the item is new, do nothing
        // Otherwise, remove it from the cabinet and add it again
        itemType = "NEW";
        for (int i = 0; i < N; i++) {
            Cabinet cabinet = cabinetsArray.get(i);

            if (cabinet.getItemKey().contains(item.getKey())) {
                itemType = Integer.toString(i + 1);
                cabinet.removeItem(item);
                break;
            }
        }
        for (int i = 0; i < N + 1; i++) {
            // If out of space, record the info
            if (i == N) {
                outside.put(curr.getKey(), i);
                break;
            } else if (i == 0)
                curr = cabinetsArray.get(i).addItem(item);
            else
                curr = cabinetsArray.get(i).addItem(curr);
            if (curr == null) break;
        }


        return itemType;
    }

}

public class CraftCarpenter {

    public static void main(String args[]) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        try {
            Scanner scanner = new Scanner(System.in);

            // Read the first line: Cabinet size
            String[] tokens = scanner.nextLine().split(" ");
            int[] cabinetSize = Stream.of(tokens).mapToInt(Integer::parseInt).toArray();
            for (int size : cabinetSize)
                if (size <= 0 || size >= pow(2, 10))
                    throw new Exception();

            // Specify the second line: N cabinets
            int N = cabinetSize.length;
            if (N <= 0 || N >= pow(2, 6)) throw new Exception();

            // Read the third line: K items
            long K = scanner.nextLong();
            if (K <= 0 || K >= pow(2, 32)) throw new Exception();

            // Read the items
            Controller controller = new Controller(K, N, cabinetSize);
            for (int i = 0; i < K; i++) {
                long key = scanner.nextLong();
                if (key <= 0 || key >= pow(2, 32)) throw new Exception();

                // Execute every item
                String res = controller.addItemToController(new Item(key));

                // If it is the final item, print the output info to STDOUT
                if (i == K - 1)
                    System.out.println(res);
            }
        } catch (Exception e) {
            System.out.println("INPUT_ERROR");
        }
    }


}