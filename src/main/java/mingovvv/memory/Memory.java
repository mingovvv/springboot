package mingovvv.memory;

import lombok.Data;

@Data
public class Memory {

    private long used;
    private long max;

    public Memory(long used, long max) {
        this.used = used;
        this.max = max;
    }

}
