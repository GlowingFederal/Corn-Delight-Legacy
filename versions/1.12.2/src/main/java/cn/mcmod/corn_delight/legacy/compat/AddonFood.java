package cn.mcmod.corn_delight.legacy.compat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.EnumAction;
import net.minecraft.world.World;
/** Native consumption preserves the addon's nutrition, timing, effects and containers. */
public class AddonFood extends ItemFood {
 private final JsonObject food;
 public AddonFood(JsonObject food) {
  super(food.get("nutrition").getAsInt(),food.get("saturation").getAsFloat(),food.has("meat") && food.get("meat").getAsBoolean());
  this.food=food;
  setMaxStackSize(food.has("stackSize") ? food.get("stackSize").getAsInt() : 64);
  if(food.has("alwaysEat") && food.get("alwaysEat").getAsBoolean()) setAlwaysEdible();
  if(food.has("container")) setContainerItem(RecipeCatalog.stack(food.get("container").getAsString()).getItem());
 }
 public int getMaxItemUseDuration(ItemStack stack) { return food.has("fast") && food.get("fast").getAsBoolean() ? 16 : 32; }
 public EnumAction getItemUseAction(ItemStack stack) { return food.has("action") ? EnumAction.DRINK : EnumAction.EAT; }
 protected void onFoodEaten(ItemStack stack,World world,EntityPlayer player) {
  if(world.isRemote) return;
  effect(food,world,player);
  if(food.has("effects")) for(JsonElement effect:food.getAsJsonArray("effects")) effect(effect.getAsJsonObject(),world,player);
 }
 private void effect(JsonObject value,World world,EntityPlayer player) {
  if(!value.has("effect")) return;
  String name=value.get("effect").getAsString();
  String id=name.indexOf(':')>=0 ? name : (name.equals("comfort") || name.equals("nourishment") ? "farmersdelight:" : "minecraft:")+name;
  Potion potion=Potion.getPotionFromResourceLocation(id);
  if(potion!=null && world.rand.nextFloat()<(value.has("chance") ? value.get("chance").getAsFloat() : 1F))
   player.addPotionEffect(new PotionEffect(potion,value.get("duration").getAsInt(),value.has("amplifier") ? value.get("amplifier").getAsInt() : 0));
 }
 public ItemStack onItemUseFinish(ItemStack stack,World world,EntityLivingBase consumer) {
  if(!(consumer instanceof EntityPlayer)) return stack;
  EntityPlayer player=(EntityPlayer)consumer;
  boolean creative=player.capabilities.isCreativeMode;
  ItemStack result=super.onItemUseFinish(stack,world,consumer);
  if(creative) { stack.grow(1); return stack; }
  ItemStack remainder=hasContainerItem(stack) ? getContainerItem(stack) : ItemStack.EMPTY;
  if(result.isEmpty()) return remainder;
  if(!world.isRemote && !remainder.isEmpty() && !player.inventory.addItemStackToInventory(remainder)) player.dropItem(remainder,false);
  return result;
 }
}
