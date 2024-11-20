package controller;

import model.adt.MyIHeap;
import model.state.PrgState;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GarbageCollector {
    public static void runGarbageCollector(List<PrgState> states){
        if(states.size() < 1){
            return ;
        }
        MyIHeap heap = states.get(0).getHeap();
        List<Integer> activeAddresses = states.stream().flatMap(e -> GarbageCollector.getActiveAddressesForState(e).stream()).toList();
        //SE IAU ADRESELE DIN TABELEL DE SYMTABLE SI HEAP
        //SE URMARESC REFERINTELE PANA LA CAPAT
        //SE ITEREAZA HEAP-UL SI DACA ADRESA NU E ACTIVA SE ELIBEREAZA
        System.out.println("Active addresses: " + activeAddresses);
        //System.out.println("Heap: " + heap);
        heap.toMap().keySet().stream().filter(e -> !(activeAddresses.contains(e))).forEach(e -> {
            try {
                System.out.println("SE INCEARCA : " + e);
                heap.deallocate(e);
                System.out.println("Deallocated: " + e);
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException(ex);
            }
        });
        System.out.println("DUPA TOOT");
    }

    private static boolean isInTable(PrgState state, ReferenceValue value) {
        return state.getSymTable().toMap().values().stream()
                .anyMatch(symValue -> symValue instanceof ReferenceValue &&
                        ((ReferenceValue) symValue).getAddress() == value.getAddress());
    }

    private static List<Integer> getActiveAddressesForState(PrgState state) {
        return state.getSymTable().toMap().values().stream()
                .filter(e -> e.getType() instanceof ReferenceType)
                .map(e -> (ReferenceValue) e)
                .flatMap(value -> {
                    List<Integer> addresses = new ArrayList<Integer>();
                    while (true) {
                        boolean isInSymTable = isInTable(state, value);
                        if (!isInSymTable) {
                            System.out.println("Adresa " + value.getAddress() + " nu este în SymTable. Se ignoră.");
                            break;
                        }

                        if(value.getAddress() == 0){
                            break;
                        }

                        addresses.add(value.getAddress());
                        IValue next_value;
                        try {
                            next_value = state.getHeap().read(value.getAddress());
                        } catch (IndexOutOfBoundsException e) {
                            throw new RuntimeException(e);
                        }
                        if (!(next_value.getType() instanceof ReferenceType)) {
                            //DACA NU E REFERINTA IESIM DIN WHILE
                            break;
                        }
                        value = (ReferenceValue) next_value;
                    }
                    //SE RETURNEAZA TOATE ADRESELE REFERINTA
                    return addresses.stream();
                }).collect(Collectors.toList());
    }
}