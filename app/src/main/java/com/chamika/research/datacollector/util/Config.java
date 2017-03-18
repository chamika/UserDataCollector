package com.chamika.research.datacollector.util;

import com.google.android.gms.awareness.fence.DetectedActivityFence;

/**
 * Created by chamika on 3/15/17.
 */

public class Config {
    public static int DATA_COLLECTION_REFRESH_INTERVAL = 5000;
    public static final int SERVICE_REFRESH_INTERVAL = 10000;

    public static final int[] ENABLED_ACTIVITIES = new int[]{DetectedActivityFence.IN_VEHICLE,
            DetectedActivityFence.ON_BICYCLE,
            DetectedActivityFence.ON_FOOT,
            DetectedActivityFence.WALKING,
            DetectedActivityFence.RUNNING};

}
