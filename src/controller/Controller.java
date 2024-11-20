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

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
            prg.getHeap().setContent(unsafeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().getValues()),
                    prg.getHeap().toMap()));
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
