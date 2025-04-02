package MyHashMap;

public class MyHashMapTest {
    public static void main(String[] args) {
        MyHashMap<String, String> myHashMap = new MyHashMap<String,String>();
        int count = 10000;
        for(int i=0;i<count;i++){
            myHashMap.put(String.valueOf(i), String.valueOf(i));
        }

        for(int i=0;i<count;i++){
            System.out.println(myHashMap.get(String.valueOf(i)));
        }

        myHashMap.remove("8");
        System.out.println(myHashMap.get("8"));

        System.out.println(count-1==myHashMap.size());

    }
}
