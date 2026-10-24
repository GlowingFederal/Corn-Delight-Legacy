package cn.mcmod.corn_delight.legacy;
import net.minecraft.item.ItemStack;
public final class WildCorn extends net.minecraft.block.BlockBush implements net.minecraftforge.common.IShearable {
 public WildCorn() { super();setHardness(0);  }
 public net.minecraft.util.BlockRenderLayer getRenderLayer() { return net.minecraft.util.BlockRenderLayer.CUTOUT; }
 public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops,net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state,int fortune) {
 java.util.Random random=world instanceof net.minecraft.world.World?((net.minecraft.world.World)world).rand:RANDOM;
 drops.add(new ItemStack(CornDelight.ITEMS.get("corn_seeds"),1+random.nextInt(2*Math.max(0,fortune)+1)));
 }
 public boolean isShearable(ItemStack item,net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos) { return true; }
 public java.util.List<ItemStack> onSheared(ItemStack item,net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos,int fortune) { return java.util.Collections.singletonList(new ItemStack(this)); }
}
