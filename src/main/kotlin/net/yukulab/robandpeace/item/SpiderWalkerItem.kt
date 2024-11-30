package net.yukulab.robandpeace.item

import gravity_changer.api.GravityChangerAPI
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class SpiderWalkerItem : Item(Settings()) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        if (context != null) {
            GravityChangerAPI.setBaseGravityDirection(context.player, context.side)
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}
