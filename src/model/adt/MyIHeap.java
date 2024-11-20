package model.adt;

import exceptions.InvalidAddressException;
import model.values.IValue;

import java.util.Map;

public interface MyIHeap {
    int allocate(IValue value);

    void deallocate(int address) throws InvalidAddressException;

    void write(int address, IValue value) throws InvalidAddressException;

    IValue read(int address) throws InvalidAddressException;

    public Map<Integer, IValue> toMap();

    void setContent(Map<Integer, IValue> content);
    MyIDictionary<Integer,IValue> getContent();

}
