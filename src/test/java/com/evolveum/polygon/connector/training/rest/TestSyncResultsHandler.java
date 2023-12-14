package com.evolveum.polygon.connector.training.rest;

import org.identityconnectors.framework.common.objects.SyncDelta;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.spi.SyncTokenResultsHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazyman on 26/04/2017.
 */
public class TestSyncResultsHandler implements SyncTokenResultsHandler {

    private List<SyncDelta> deltas = new ArrayList<>();
    private SyncToken result;


    @Override
    public void handleResult(SyncToken syncToken) {
        result = syncToken;
    }

    @Override
    public boolean handle(SyncDelta syncDelta) {
        deltas.add(syncDelta);
        return true;
    }

    public List<SyncDelta> getDeltas() {
        return deltas;
    }

    public SyncToken getResult() {
        return result;
    }
}
