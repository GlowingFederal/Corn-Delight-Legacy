package cn.mcmod.corn_delight.legacy;
import net.minecraft.item.ItemStack;
public final class WildCorn extends net.minecraft.block.BlockBush implements net.minecraftforge.common.IShearable {
 public WildCorn() { super();setHardness(0); setBlockTextureName("corn_delight:wild_corn"); }
 public java.util.ArrayList<ItemStack> getDrops(net.minecraft.world.World world,int x,int y,int z,int meta,int fortune) {
 java.util.ArrayList<ItemStack> drops=new java.util.ArrayList<ItemStack>();drops.add(new ItemStack(CornDelight.ITEMS.get("corn_seeds"),1+world.rand.nextInt(2*Math.max(0,fortune)+1)));return drops;
 }
 public boolean isShearable(ItemStack item,net.minecraft.world.IBlockAccess world,int x,int y,int z) { return true; }
 public java.util.ArrayList<ItemStack> onSheared(ItemStack item,net.minecraft.world.IBlockAccess world,int x,int y,int z,int fortune) { java.util.ArrayList<ItemStack> drops=new java.util.ArrayList<ItemStack>();drops.add(new ItemStack(this));return drops; }
}
