package net.yukulab.robandpeace.mixin.client.spiderwalker;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer {
    @Unique
    List<Block> RAB$DEFAULT_CLIMBABLE_BLOCKS = List.of(Blocks.LADDER, Blocks.VINE, Blocks.SCAFFOLDING, Blocks.WEEPING_VINES, Blocks.TWISTING_VINES);

    /**
     * 天井に張り付いているときにスニーク時のプレイヤーの高さを調整する
     */
    @Inject(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "HEAD")
    )
    private void adjustPlayerYPosIfPlayerClimbing(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (abstractClientPlayerEntity.isClimbing() && abstractClientPlayerEntity.isSneaking()) {
            var world = abstractClientPlayerEntity.getWorld();
            var pos = abstractClientPlayerEntity.getBlockPos().up(2);
            var blockState = world.getBlockState(pos);
            if (!blockState.isAir() && RAB$DEFAULT_CLIMBABLE_BLOCKS.stream().noneMatch(blockState::isOf)) {
                var adjustHeight = 0.3;
                matrixStack.translate(0.0, adjustHeight, 0.0);
            }
        }
    }
}
