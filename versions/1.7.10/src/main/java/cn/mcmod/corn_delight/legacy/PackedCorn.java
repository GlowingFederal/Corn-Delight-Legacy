package cn.mcmod.corn_delight.legacy;
public final class PackedCorn extends net.minecraft.block.Block {
 private final boolean bag;
 public PackedCorn(boolean bag) { super(net.minecraft.block.material.Material.wood);this.bag=bag;setHardness(2F); }
 @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT) private net.minecraft.util.IIcon top,side,bottom;
 @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT) public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister r) {
 top=r.registerIcon("corn_delight:"+(bag?"corn_kernal_bag_top":"corn_crate_top"));side=r.registerIcon(bag?"farmersdelight:rice_bag_side":"corn_delight:corn_crate_side");bottom=bag?r.registerIcon("farmersdelight:rice_bag_bottom"):top;
 }
 @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT) public net.minecraft.util.IIcon getIcon(int face,int meta) { return face==0?bottom:face==1?top:side; }
}
