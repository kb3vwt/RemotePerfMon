package com.rpmc.server;

import com.rpmc.model.CPUState;
import com.rpmc.model.ComputerState;
import com.rpmc.model.MemoryState;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.Arrays;
import java.util.List;

public class StateGetter {
    Sigar sigar;
    public StateGetter() {
        sigar = new Sigar();

    }
    public ComputerState getCurrentState() {
        ComputerState state = new ComputerState();
        try {
            setProcessorStates(state);
            setMemoryState(state);
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return state;
    }

    private void setProcessorStates(ComputerState state) throws SigarException{
        List<CpuPerc> percs = Arrays.asList(sigar.getCpuPercList());
        int cpuId = 0;
        for(CpuPerc p: percs) {
            state.addProcessorState(new CPUState(cpuId,p.getCombined()));
            cpuId++;
        }
    }

    private void setMemoryState(ComputerState state) throws SigarException {
        long total = sigar.getMem().getTotal();
        long used = sigar.getMem().getUsed();
        MemoryState mem = new MemoryState(used,total);

        state.setMemoryState(mem);
    }

}
