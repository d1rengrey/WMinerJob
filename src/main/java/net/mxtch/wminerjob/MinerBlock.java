package net.mxtch.wminerjob;

import org.bukkit.Material;

public class MinerBlock {
    private Material material;
    private double cost;
    private int delay;

    public Material getMaterial() {
        return material;
    }

    public double getCost() {
        return cost;
    }

    public long getDelay() {
        return delay;
    }

    public static class Builder {
        private MinerBlock minerBlock;

        public Builder() {
            this.minerBlock = new MinerBlock();
        }

        public Builder setCost(double cost){
            this.minerBlock.cost = cost;
            return this;
        }

        public Builder setMaterial(Material material) {
            this.minerBlock.material = material;
            return this;
        }

        public Builder setDelay(int delay) {
            this.minerBlock.delay = delay;
            return this;
        }

        public MinerBlock build() {
            return minerBlock;
        }
    }
}
