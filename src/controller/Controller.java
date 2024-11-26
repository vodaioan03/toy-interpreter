package controller;

import exceptions.ADTException;
import exceptions.EmptyStackException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adt.MyIStack;
import model.state.PrgState;
import model.statements.IStatement;
import model.values.IValue;
import model.values.ReferenceValue;
import repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    IRepository repo;
    boolean displayFlag = true;

    public Controller(IRepository repo){
        this.repo = repo;
    }

    public void setDisplayFlag(){
        displayFlag = !displayFlag;
    }

    public void setCurrentProgram(PrgState prg){
        repo.addPrgState(prg);
    }


    public Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap)
    {
        List<Integer> addresses = new ArrayList<>(symTableAddr);
        boolean found = true;
        while (found)
        {
            found = false;
            List<IValue> referenced = new ArrayList<>();
            for (Integer address : addresses)
            {
                IValue value = heap.get(address);
                if (value != null)
                    referenced.add(value);
            }
            List<Integer> newAddresses = getAddrFromSymTable(referenced);
            for (Integer address: newAddresses)
                if (! addresses.contains(address))
                {
                    addresses.add(address);
                    found = true;
                }
        }

        Map<Integer, IValue> result = new HashMap<>();
        for (Map.Entry<Integer, IValue> entry : heap.entrySet()) {
            if (addresses.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer,IValue>
            heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
                .filter(v-> v instanceof ReferenceValue)
                .map(v-> {ReferenceValue v1 = (ReferenceValue)v; return v1.getAddress();})
                .collect(Collectors.toList());
    }


    public PrgState oneStep(PrgState state) throws StatementException, ExpressionException, ADTException {
        //get execution stack
        MyIStack<IStatement> st = state.getExecStack();

        //check if it is empty
        if(st.isEmpty()){
            throw new EmptyStackException("Stack is empty");
        }

        //execute the top statement
        IStatement currentStatement = st.pop();
        PrgState executedState = currentStatement.execute(state);
        //GarbageCollector.runGarbageCollector(this.repo.getStates());

        //display the state after execution if the flag is set
        if(displayFlag){
            System.out.println(executedState);
        }
        return executedState;
    }

    public PrgState allStep() throws StatementException, ExpressionException, ADTException {

        //get the current program
        PrgState prg = repo.getCurrentProgram();
        repo.logPrgStateExec();

        while(!prg.getExecStack().isEmpty()){
            prg = oneStep(prg);
            repo.logPrgStateExec();
            prg.getHeap().setContent(safeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().getValues()),
                    prg.getHeap().toMap()));
            System.out.println("== !! ==");
            repo.logPrgStateExec();
        }
        //display the initial state
        if(displayFlag){
            System.out.println(prg);
        }
        //return final state
        return prg;
    }
}
