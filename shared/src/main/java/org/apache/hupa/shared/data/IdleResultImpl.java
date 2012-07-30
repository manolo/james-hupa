package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.IdleResult;

public class IdleResultImpl implements IdleResult{

    private boolean supported;
    
    protected IdleResultImpl() {
        
    }
    
    public IdleResultImpl(boolean supported) {
        this.supported = supported;
    }
    
    /**
     * Return if the IDLE command is supported by the server
     * 
     * @return supported
     */
    public boolean isSupported() {
        return supported;
    }
}
