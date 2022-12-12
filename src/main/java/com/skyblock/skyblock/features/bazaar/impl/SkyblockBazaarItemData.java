package com.skyblock.skyblock.features.bazaar.impl;

import com.skyblock.skyblock.features.bazaar.BazaarItemData;
import lombok.Data;

@Data
public class SkyblockBazaarItemData implements BazaarItemData {

    @Data
    static class SkyblockBazaarItemVolume implements BazaarItemVolume {
        private final double amount;
        private final double volume;
    }

    private final double productAmount;
    private final double buyPrice;
    private final double sellPrice;
    private final BazaarItemVolume buyVolume;
    private final int last7dInstantBuyVolume;
    private final BazaarItemVolume sellVolume;
    private final int last7dInstantSellVolume;

}
