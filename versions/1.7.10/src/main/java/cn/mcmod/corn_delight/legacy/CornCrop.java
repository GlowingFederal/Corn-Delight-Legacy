package cn.mcmod.corn_delight.legacy;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
/** Eight growth stages per half. Bit 3 distinguishes the independently growing upper half. */
public final class CornCrop extends BlockCrops {
 @SideOnly(Side.CLIENT) private net.minecraft.util.IIcon[] textures;
 public CornCrop() { super(); }
 protected Item func_149866_i() { return CornDelight.ITEMS.get("corn_seeds"); }
 protected Item func_149865_P() { return CornDelight.ITEMS.get("corn"); }
 protected boolean canPlaceBlockOn(Block block) { return block==net.minecraft.init.Blocks.farmland || block==vectorwing.farmersdelight.legacy.Content.richFarmland; }
 public boolean canBlockStay(World world,int x,int y,int z) {
  int meta=world.getBlockMetadata(x,y,z);
  if((meta&8)!=0) return world.getBlock(x,y-1,z)==this && world.getBlockMetadata(x,y-1,z)>=4 && world.getBlockMetadata(x,y-1,z)<8;
  return super.canBlockStay(world,x,y,z);
 }
 public int getRenderType() { return 6; }
 public void setBlockBoundsBasedOnState(IBlockAccess world,int x,int y,int z) {
  int meta=world.getBlockMetadata(x,y,z),age=meta&7;
  float h=(meta&8)!=0 ? new int[]{4,6,8,12,14,14,14,14}[age]/16F : Math.min(16,4*(age+1))/16F;
  setBlockBounds(0,0,0,1,h,1);
 }
 public void updateTick(World world,int x,int y,int z,Random random) {
  if(world.isRemote || !world.checkChunksExist(x-1,y,z-1,x+1,y+1,z+1)) return;
  if(!canBlockStay(world,x,y,z)) { dropBlockAsItem(world,x,y,z,world.getBlockMetadata(x,y,z),0);world.setBlockToAir(x,y,z);return; }
  if(world.getBlockLightValue(x,y+1,z)<9) return;
  int meta=world.getBlockMetadata(x,y,z),age=meta&7;
  float speed=growth(world,x,y,z);
  if(age<7 && random.nextInt((int)(25F/speed)+1)==0) world.setBlockMetadataWithNotify(x,y,z,(meta&8)|(age+1),2);
  if((meta&8)==0 && age>=4 && y<254 && world.isAirBlock(x,y+1,z) && random.nextInt((int)(25F/speed)+1)==0)
   world.setBlock(x,y+1,z,this,8,3);
 }
 private float growth(World world,int x,int y,int z) {
  float speed=1F;
  for(int dx=-1;dx<=1;dx++) for(int dz=-1;dz<=1;dz++) {
   Block soil=world.getBlock(x+dx,y-1,z+dz);float bonus=0;
   if(soil.canSustainPlant(world,x+dx,y-1,z+dz,net.minecraftforge.common.util.ForgeDirection.UP,this)) bonus=soil.isFertile(world,x+dx,y-1,z+dz)?3:1;
   speed+=dx==0&&dz==0?bonus:bonus/4F;
  }
  boolean row=world.getBlock(x-1,y,z)==this || world.getBlock(x+1,y,z)==this;
  boolean column=world.getBlock(x,y,z-1)==this || world.getBlock(x,y,z+1)==this;
  if(row&&column || world.getBlock(x-1,y,z-1)==this || world.getBlock(x+1,y,z-1)==this || world.getBlock(x-1,y,z+1)==this || world.getBlock(x+1,y,z+1)==this) speed/=2;
  return speed;
 }
 public boolean func_149851_a(World world,int x,int y,int z,boolean client) {
  int meta=world.getBlockMetadata(x,y,z);
  if((meta&8)!=0) return (meta&7)<7;
  return meta<7 || world.getBlock(x,y+1,z)==this && (world.getBlockMetadata(x,y+1,z)&7)<7 || world.isAirBlock(x,y+1,z);
 }
 public void func_149853_b(World world,Random random,int x,int y,int z) {
  if(world.isRemote) return;
  int meta=world.getBlockMetadata(x,y,z),next=(meta&7)+2+random.nextInt(4);
  world.setBlockMetadataWithNotify(x,y,z,(meta&8)|Math.min(7,next),3);
  if((meta&8)==0 && next>7 && y<254) {
   if(world.getBlock(x,y+1,z)==this) func_149853_b(world,random,x,y+1,z);
   else if(world.isAirBlock(x,y+1,z)) world.setBlock(x,y+1,z,this,8|Math.min(7,next-8),3);
  }
 }
 public ArrayList<ItemStack> getDrops(World world,int x,int y,int z,int meta,int fortune) {
  ArrayList<ItemStack> drops=new ArrayList<ItemStack>();
  drops.add(new ItemStack((meta&7)==7?func_149865_P():func_149866_i()));
  if((meta&7)==7) { int seeds=1;for(int i=0;i<3+Math.max(0,fortune);i++) if(world.rand.nextFloat()<4F/7F) seeds++;drops.add(new ItemStack(func_149866_i(),seeds)); }
  return drops;
 }
 @SideOnly(Side.CLIENT) public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister r) {
  textures=new net.minecraft.util.IIcon[16];for(int i=0;i<16;i++) textures[i]=r.registerIcon("corn_delight:"+(i<8?"corn_crop_stage":"corn_top_stage")+(i&7));
 }
 @SideOnly(Side.CLIENT) public net.minecraft.util.IIcon getIcon(int face,int meta) { return textures[meta&15]; }
}
