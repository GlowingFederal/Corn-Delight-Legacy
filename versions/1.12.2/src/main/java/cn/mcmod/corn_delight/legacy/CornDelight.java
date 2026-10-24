package cn.mcmod.corn_delight.legacy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import vectorwing.farmersdelight.legacy.Content;
import vectorwing.farmersdelight.legacy.SourceTags;
import vectorwing.farmersdelight.legacy.LegacyFood;
import vectorwing.farmersdelight.legacy.api.FoodDefinition;
import vectorwing.farmersdelight.legacy.api.SourceRecipes;
@Mod(modid="corn_delight",name="Corn Delight Legacy",version=BuildVersion.VERSION,acceptedMinecraftVersions="[1.12.2]",dependencies="required-after:farmersdelight@[1.0.2,)")
public final class CornDelight {
 public static final java.util.Map<String,Item> ITEMS=new java.util.LinkedHashMap<String,Item>();
 public static CornCrop crop;
 public static Block wild;
 public static boolean generate;
 public static int chance;
 @SidedProxy(clientSide="cn.mcmod.corn_delight.legacy.ClientProxy",serverSide="cn.mcmod.corn_delight.legacy.CommonProxy") public static CommonProxy proxy;
 @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) {
  Configuration config=new Configuration(event.getSuggestedConfigurationFile());config.load();
  generate=config.getBoolean("genWildCorn","general",true,"Generate wild corn in temperate overworld biomes.");
  chance=config.getInt("chance","general",30,1,Integer.MAX_VALUE,"One cluster attempt per this many chunks.");
  if(config.hasChanged()) config.save();
  crop=new CornCrop();block("corn_crop",crop,false);
  wild=block("wild_corn",new WildCorn(),true);
  block("corn_crate",new PackedCorn(false),true);block("corn_kernel_bag",new PackedCorn(true),true);
  FoodDefinition[] foods;
  try(java.io.InputStreamReader reader=new java.io.InputStreamReader(CornDelight.class.getResourceAsStream("/corn_delight/foods.json"),java.nio.charset.StandardCharsets.UTF_8)) {
   foods=new com.google.gson.Gson().fromJson(reader,FoodDefinition[].class);
  } catch(java.io.IOException e) { throw new IllegalStateException("Cannot load Corn food contract",e); }
  for(FoodDefinition food:foods) add(food.id,food.id.equals("corn_seeds") ? new CornSeeds(food) : new LegacyFood(food));
  OreDictionary.registerOre("cropCorn",new net.minecraft.item.ItemStack(ITEMS.get("corn")));
  OreDictionary.registerOre("seedCorn",new net.minecraft.item.ItemStack(ITEMS.get("corn_seeds")));
  GameRegistry.registerWorldGenerator(new WildGeneration(),0);
  proxy.preInit();
 }
 @Mod.EventHandler public void init(FMLInitializationEvent event) {
  SourceRecipes.addCatalog("corn_delight",CornDelight.class,"/corn_delight/source_recipes.json");
  SourceTags.addCatalog(CornDelight.class,"/corn_delight/source_tags.json");
 }
 private static void add(String id,Item item) { item.setTranslationKey("corn_delight."+id).setCreativeTab(Content.TAB).setRegistryName("corn_delight",id);GameRegistry.findRegistry(Item.class).register(item);ITEMS.put(id,item); }
 private static Block block(String id,Block block,boolean item) { block.setTranslationKey("corn_delight."+id).setCreativeTab(Content.TAB).setRegistryName("corn_delight",id);GameRegistry.findRegistry(Block.class).register(block);if(item) add(id,new ItemBlock(block)); return block; }
}
