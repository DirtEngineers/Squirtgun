package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ChemicalPhial extends BasePhial {
    private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    public ChemicalPhial(Chemical pChemical, CAPACITY_UPGRADE pCapacityUpgrade) {
        super(ItemRegistration.ITEM_PROPERTIES_WITH_TAB);
        chemical = pChemical;
        capacityUpgrade = pCapacityUpgrade;
        applyUpgrades();
        shotsAvailable = maxShots;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addEffectsToTooltip(pTooltipComponents);
    }

    public void addEffectsToTooltip(List<Component> pTooltips) {
        addTooltipEffects(chemical.getEffects(), pTooltips);
//        List<MobEffectInstance> list = chemical.getEffects();
//        List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
//        if (list.isEmpty()) {
//            pTooltips.add(NO_EFFECT);
//        } else {
//            for(MobEffectInstance mobeffectinstance : list) {
//                MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
//                MobEffect mobeffect = mobeffectinstance.getEffect();
//                Map<Attribute, AttributeModifier> map = mobeffect.getAttributeModifiers();
//                if (!map.isEmpty()) {
//                    for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
//                        AttributeModifier attributemodifier = entry.getValue();
//                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), mobeffect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
//                        list1.add(new Pair<>(entry.getKey(), attributemodifier1));
//                    }
//                }
//
//                if (mobeffectinstance.getAmplifier() > 0) {
//                    mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
//                }
//
//                if (mobeffectinstance.getDuration() > 20) {
//                    mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, 1.0F));
//                }
//
//                pTooltips.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
//            }
//        }
//
//        if (!list1.isEmpty()) {
//            pTooltips.add(CommonComponents.EMPTY);
//            pTooltips.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
//
//            for(Pair<Attribute, AttributeModifier> pair : list1) {
//                AttributeModifier attributemodifier2 = pair.getSecond();
//                double d0 = attributemodifier2.getAmount();
//                double d1;
//                if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
//                    d1 = attributemodifier2.getAmount();
//                } else {
//                    d1 = attributemodifier2.getAmount() * 100.0D;
//                }
//
//                if (d0 > 0.0D) {
//                    pTooltips.add(Component.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.BLUE));
//                } else if (d0 < 0.0D) {
//                    d1 *= -1.0D;
//                    pTooltips.add(Component.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.RED));
//                }
//            }
//        }
    }
}
