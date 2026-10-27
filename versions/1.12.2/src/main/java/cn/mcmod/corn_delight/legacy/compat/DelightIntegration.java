package cn.mcmod.corn_delight.legacy.compat;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
/** No implementation-specific types cross this boundary. */
public interface DelightIntegration {
 CreativeTabs tab();
 Item food(JsonObject definition);
 boolean knife(ItemStack stack);
 Block richFarmland();
 void init();
 void postInit();
}
