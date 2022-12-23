package net.dirtengineers.squirtgun.common.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public abstract class BasePhial extends Item {

    public enum CAPACITY_UPGRADE {
        BASE,
        DOUBLESHOTS,
        TRIPLESHOTS
    }

    private static final Component NO_EFFECT = new TranslatableComponent("effect.none").withStyle(ChatFormatting.GRAY);
    Chemical chemical = null;
    String potionLocation = null;
    CAPACITY_UPGRADE capacityUpgrade;
    int shotsAvailable;
    int maxShots;
    int baseMaxShots = 10;

    public BasePhial(Properties pProperties) {
        super(pProperties);
        capacityUpgrade = CAPACITY_UPGRADE.BASE;
    }

    protected void applyUpgrades() {
        switch (capacityUpgrade) {
            case BASE -> maxShots = baseMaxShots;
            case DOUBLESHOTS -> maxShots = baseMaxShots * 2;
            case TRIPLESHOTS -> maxShots = baseMaxShots * 3;
        }
    }

    public void setCapacityUpgrade(CAPACITY_UPGRADE pUpgrade) {
        capacityUpgrade = pUpgrade;
        applyUpgrades();
    }

    public CAPACITY_UPGRADE getCapacityUpgrade() {
        return capacityUpgrade;
    }

    public int getShotsAvailable() {
        return shotsAvailable;
    }

    public int getFluidCapacityInMb() {
        return maxShots * Constants.SLUG_SHOT_SIZE_MB;
    }

    public Chemical getChemical() {
        return chemical;
    }

    public String getPotionLocation() {
        return potionLocation;
    }

    public void addEffectsToTooltip(List<Component> pTooltips) {}

    protected void addTooltipEffects(List<MobEffectInstance> pEffects, List<Component> pTooltips) {
        List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
        if (pEffects.isEmpty()) {
            pTooltips.add(NO_EFFECT);
        } else {
            for(MobEffectInstance mobeffectinstance : pEffects) {
                MutableComponent mutablecomponent = new TranslatableComponent(mobeffectinstance.getDescriptionId());
                MobEffect mobeffect = mobeffectinstance.getEffect();
                Map<Attribute, AttributeModifier> map = mobeffect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), mobeffect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list1.add(new Pair<>(entry.getKey(), attributemodifier1));
                    }
                }
                if (mobeffectinstance.getAmplifier() > 0) {
                    mutablecomponent = new TranslatableComponent("potion.withAmplifier", mutablecomponent, new TranslatableComponent("potion.potency." + mobeffectinstance.getAmplifier()));
                }
                if (mobeffectinstance.getDuration() > 20) {
                    mutablecomponent = new TranslatableComponent("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, 1.0F));
                }
                pTooltips.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
            }
        }

        if (!list1.isEmpty()) {
            for(Pair<Attribute, AttributeModifier> pair : list1) {
                AttributeModifier attributemodifier2 = pair.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1;
                if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    d1 = attributemodifier2.getAmount();
                } else {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }
                if (d0 > 0.0D) {
                    pTooltips.add(new TranslatableComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    pTooltips.add(new TranslatableComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }
}
