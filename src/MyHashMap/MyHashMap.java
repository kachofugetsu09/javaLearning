package MyHashMap;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K,V>{

    Node<K, V>[] table = new Node[16];
    private int size = 0;
    public V put(K key,V value){
        int keyIndex = indexOf(key);
        Node<K, V> kvNode = table[keyIndex];
        if(kvNode == null){
            table[keyIndex] = new Node<>(key,value);
            size++;
            resizeIfNecessary();
            return null;
        }
        while(true){
            if(kvNode.key.equals(key)){
                V oldValue = kvNode.value;
                kvNode.value = value;
                return oldValue;
            }
            if(kvNode.next == null){
                kvNode.next = new Node<>(key,value);
                size++;
                resizeIfNecessary();
                return null;
            }
            kvNode = kvNode.next;
        }
    }
    public V get(K key){
        int keyIndex = indexOf(key);
        Node<K, V> kvNode = table[keyIndex];
        while(kvNode != null){
            if(kvNode.key.equals(key)){
                return kvNode.value;
            }
            kvNode = kvNode.next;
        }
        return null;
    }
    public V remove(K key){
        int keyIndex = indexOf(key);
        Node<K, V> head = table[keyIndex];
        if(head == null){
            return null;
        }
        if(head.key.equals(key)){
            table[keyIndex] = head.next;
            size--;
            return head.value;
        }
        Node<K, V> pre = head;
        Node<K,V> cur = head.next;
        while(cur!=null){
            if(cur.key.equals(key)){
                pre.next = cur.next;
                size--;
                return cur.value;
            }
            pre = cur;
            cur = cur.next;
        }
        return null;

    }

    public int size(){
        return size;
    }

    private void resizeIfNecessary() {
        if(this.size<table.length*0.75){
            return;
        }
        Node<K, V>[] newTable = new Node[table.length*2];
        for(Node<K, V> node:table){
            if(node==null){
                continue;
            }
            Node<K,V> cur = node;
            while(cur!=null){
                int newIndex = cur.key.hashCode()&(newTable.length-1) ;
                if (newTable[newIndex]==null) {
                    newTable[newIndex] = cur;
                    Node<K, V> next = cur.next;
                    cur.next =null;
                    cur = next;
                }else{
                    Node<K, V> next = cur.next;
                    cur.next = newTable[newIndex];
                    cur = next;
                }
            }
        }
        this.table = newTable;
        System.out.println("扩容了，扩容到"+table.length);
    }


    private int indexOf(Object key){
        return key.hashCode()& (table.length-1);
    }

    class Node<K, V> {
        Node<K, V> next;
        K key;
        V value;
        public Node(K key,V value){
            this.key = key;
            this.value = value;
        }
    }
}
