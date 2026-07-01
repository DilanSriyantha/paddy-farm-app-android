package com.dtechsolutions.paddyfarm.data.models;

import java.util.Date;

public class CultivationUpdateRequest extends CultivationCreateRequest{

    public CultivationUpdateRequest(Date startDate, String seedType, float sizeInAcres, String paddyVariety) {
        super(startDate, seedType, sizeInAcres, paddyVariety);
    }
}
