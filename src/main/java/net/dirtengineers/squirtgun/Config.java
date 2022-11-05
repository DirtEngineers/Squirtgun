package net.dirtengineers.squirtgun;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

public class Config {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public Config(){}

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    static {
        Pair<Common, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure(Config.Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common {
        public static ForgeConfigSpec.IntValue encapsulatorEnergyCapacity;
        public static ForgeConfigSpec.IntValue encapsulatorEnergyPerTick;
        public static ForgeConfigSpec.IntValue encapsulatorTicksPerOperation;
        public static ForgeConfigSpec.IntValue encapsulatorFluidCapacity;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Fluid Encapsulator").push(Constants.categoryEncapsulator);
            encapsulatorEnergyCapacity = builder.comment(
                    "Maximum energy capacity for the Fluid Encapsulator.")
                    .comment("Default: 100000 (100k FE)")
                    .defineInRange("energyCapacity", 100000, 0, Integer.MAX_VALUE);
            encapsulatorEnergyPerTick = builder.comment(
                    "Energy consumed per tick when Fluid Encapsulator is processing.")
                    .comment("Default: 50 FE")
                    .defineInRange("energyPerTick", 50, 0, Integer.MAX_VALUE);
            encapsulatorTicksPerOperation = builder.comment(
                    "Ticks per operation when using the Fluid Encapsulator.")
                    .comment("Default: 60 ticks")
                    .defineInRange("ticksPerOperation", 60, 1, Integer.MAX_VALUE);
            encapsulatorFluidCapacity = builder.comment(
                    "Fluid capacity in Fluid Encapsulator tank.")
                    .comment("Default: 100000 (100 buckets)")
                    .defineInRange("fluidCapacity", 100000, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}
