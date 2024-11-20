package model.types;

import exceptions.AppException;
import model.values.IValue;
import model.values.ReferenceValue;

public class ReferenceType implements IType {
    IType innerType;

    public ReferenceType(IType innerType) {
        this.innerType = innerType;
    }

    @Override
    public boolean equals(IType other) {
        return (other instanceof ReferenceType)
                && ((ReferenceType)other).innerType != null
                && ((ReferenceType) other).innerType.equals(this.innerType);
    }

    @Override
    public IValue defaultValue() {
        return new ReferenceValue(0,innerType);
    }

    @Override
    public String toString(){
        return "Ref(" + innerType.toString() + ")";
    }

    public IType getInner() {
        return innerType;
    }
}