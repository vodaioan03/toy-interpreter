package model.adt;

import exceptions.AppException;
import exceptions.InvalidAddressException;
import exceptions.KeyNotFoundException;
import model.values.IValue;

import java.util.Map;

public class MyHeap implements MyIHeap {

    MyDictionary<Integer, IValue> heap;
    Integer nextFreeAddress;

    public MyHeap() {
        this.heap = new MyDictionary<>();
        this.nextFreeAddress = 1;
        // 0 is reserved for null, which is not a valid address
    }
    @Override
    public int allocate(IValue value) {
        heap.insert(nextFreeAddress, value);
        nextFreeAddress += 1;
        return nextFreeAddress - 1;
    }

    @Override
    public void deallocate(int address) throws InvalidAddressException {
        try {
            heap.remove(address);
        } catch (KeyNotFoundException e) {
            throw new InvalidAddressException("Address " + Integer.toString(address) + " out of bounds.");
        }
    }

    @Override
    public void write(int address, IValue value) throws InvalidAddressException {
        if(!heap.getKeys().contains(address)) {
            throw new InvalidAddressException("Address "
                    + Integer.toString(address)
                    + " out of bounds.");
        }
        heap.insert(address, value);

    }

    @Override
    public IValue read(int address) throws InvalidAddressException {
        try {
            return heap.get(address);
        } catch (KeyNotFoundException e) {
            throw new InvalidAddressException("[READ HEAP]Address "
                    + Integer.toString(address)
                    + " out of bounds.");
        }
    }

    @Override
    public Map<Integer, IValue> toMap() {
        return heap.toMap();
    }

    @Override
    public void setContent(Map<Integer, IValue> content) {
        MyDictionary<Integer, IValue> newHeap = new MyDictionary<>();
        for (Map.Entry<Integer, IValue> entry : content.entrySet()) {
            newHeap.insert(entry.getKey(), entry.getValue());
        }
        this.heap = newHeap;
    }

    @Override
    public MyIDictionary<Integer, IValue> getContent() {
        return this.heap;
    }

    @Override
    public String toString(){
        StringBuilder answer = new StringBuilder("Heap:\n");
        try{
            for(int key: heap.getKeys()){
                answer.append(key).append("(").append(heap.get(key).getType().toString())
                        .append(")").append(":-> ").
                        append(heap.get(key).toString()).append("\n");
            }
        }catch(AppException exception){
            throw new RuntimeException(exception.getMessage());
        }
        return answer.toString();
    }
}
