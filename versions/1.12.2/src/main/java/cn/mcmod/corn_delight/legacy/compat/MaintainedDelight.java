package cn.mcmod.corn_delight.legacy.compat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.legacy.*;
import vectorwing.farmersdelight.legacy.api.FoodDefinition;
import vectorwing.farmersdelight.legacy.api.SourceRecipes;
import cn.mcmod.corn_delight.legacy.CornDelight;
public final class MaintainedDelight implements DelightIntegration {
 public CreativeTabs tab() { return Content.TAB; }
 public Item food(JsonObject food) { return new LegacyFood(new Gson().fromJson(food,FoodDefinition.class)); }
 public boolean knife(ItemStack stack) { return Ingredients.isKnife(stack); }
 public Block richFarmland() { return Content.richFarmland; }
 public void init() {
  SourceRecipes.addCatalog("corn_delight",CornDelight.class,"/corn_delight/source_recipes.json");
  SourceTags.addCatalog(CornDelight.class,"/corn_delight/source_tags.json");
 }
 public void postInit() {}
}
