package edu.hm.sam.location;

/**
 * Enum class for SAM REST Interface to MissonPlanner. To know which module/task
 * is sending right now data to MP we identify those through their SAM_TYPE.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public enum SamType {
    /**
     * UNKNOWN
     */
    UNKNOWN(0),
    /**
     * ZONE_NO_FLIGHT
     */
    ZONE_NO_FLIGHT(1),
    /**
     * ZONE_SEARCH_AREA
     */
    ZONE_SEARCH_AREA(2),
    /**
     * ZONE_EMERGENT
     */
    ZONE_EMERGENT(3),
    /**
     * TARGET_SRIC
     */
    TARGET_SRIC(11),
    /**
     * TARGET_AIRDROP
     */
    TARGET_AIRDROP(12),
    /**
     * TARGET_OFFAXIS
     */
    TARGET_OFFAXIS(13),
    /**
     * TARGET_IR_STATIC
     */
    TARGET_IR_STATIC(14),
    /**
     * TARGET_IR_DYNAMIC
     */
    TARGET_IR_DYNAMIC(15),
    /**
     * TARGET_EMERGENT
     */
    TARGET_EMERGENT(16),
    /**
     * TASK_SEARCH_AREA
     */
    TASK_SEARCH_AREA(21),
    /**
     * TASK_EMERGENT
     */
    TASK_EMERGENT(22),
    /**
     * TASK_AIRDROP
     */
    TASK_AIRDROP(23),
    /**
     * TASK_OFFAXIS
     */
    TASK_OFFAXIS(24),
    /**
     * TASK_IR_STATIC
     */
    TASK_IR_STATIC(25),
    /**
     * TASK_IR_DYNAMIC
     */
    TASK_IR_DYNAMIC(26),
    /**
     * TASK_WAYPOINTS
     */
    TASK_WAYPOINTS(27),
    /**
     * TASK_SRIC
     */
    TASK_SRIC(28),
    /**
     * MANUAL, manually generated waypoint
     */
    MANUAL(91),
    /**
     * HOME
     */
    HOME(92),
    /**
     * ENUM_END
     */
    ENUM_END(99);

    private final int value;

    SamType(final int value) {
        this.value = value;
    }

    /**
     * getter for value.
     *
     * @return value.
     */
    public int getValue() {
        return value;
    }
    
    
    public static SamType fromValue(int value) {  
        for (SamType my: SamType.values()) {  
            if (my.value == value) {  
                return my;  
            }  
        }  
        return null;  
    }  
  
    int value() {  
        return value;  
    } 
}
